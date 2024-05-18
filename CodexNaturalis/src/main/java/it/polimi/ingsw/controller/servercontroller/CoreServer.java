package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.controller.servercontroller.operationexceptions.*;
import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;
import it.polimi.ingsw.model.player.Player;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import static it.polimi.ingsw.controller.servercontroller.UserStatus.*;
import static it.polimi.ingsw.model.DrawChoice.DECK_GOLD;
import static it.polimi.ingsw.model.DrawChoice.DECK_RESOURCE;

public class CoreServer extends UnicastRemoteObject implements AuthenticationManager {

    private final UserList userList;
    private final LobbyList lobbyList;
    private final GameList activeGames;

    public CoreServer(UserList userList, LobbyList lobbyList, GameList activeGames) throws RemoteException {
        super();
        this.userList = userList;
        this.lobbyList = lobbyList;
        this.activeGames = activeGames;
    }

    @Override
    public int register(String username) throws RemoteException {
        synchronized (userList){
            if(userList.isUserRegistered(username)){
                throw new InvalidOperationException("Username " + username + " is already in use");
            }
        }

        User newUser = new User(username);
        int tempCode = newUser.generateNewPass();
        userList.addUser(newUser);
        return tempCode;
    }

    @Override
    public ServerController login(String username, int tempCode, NotificationSubscriber subscriber) throws RemoteException {
        User loginUser;

        if(subscriber == null){
            throw new InvalidParameterException("Cannot provide null notification subscriber");
        }

        synchronized (userList) {
            if (!userList.isUserRegistered(username)) {
                throw new InvalidCredentialsException();
            }

            loginUser = userList.getUserByUsername(username);
        }

        if(loginUser.checkPass(tempCode)){
            loginUser.setNotificationSubscriber(subscriber);

            if(loginUser.hasJoinedGameId()){

                Game game = activeGames.getJoinedGame(loginUser);
                GamePhase phase = GamePhase.PLAY_PHASE;

                if(game.allPlayersHaveSetup()){
                    phase = GamePhase.SETUP_PHASE;
                } else if(game.isEnded()){
                    phase = GamePhase.END_PHASE;
                }

                loginUser.getNotificationSubscriber().onStartedGameAvailable(phase);
            }

            return new AuthenticatedSession(loginUser, this);
        } else {
            throw new InvalidCredentialsException();
        }
    }

    public void logout(User user){
        synchronized (userList) {
            UserStatus status = user.getStatus();

            if (status != LOBBY_CHOICE) {
                throw switch (status) {
                    case WAITING_TO_START -> new WrongPhaseException("Cannot logout when in a lobby");
                    case IN_GAME -> new WrongPhaseException("Cannot logout when in game");
                    default -> new WrongPhaseException();
                };
            }

            userList.removeUser(user);
        }
    }

    public List<LobbyInfo> getLobbies(User user){
        List<Lobby> availableLobbies = lobbyList.getLobbies();
        return availableLobbies.stream().map(InfoTranslator::convertToLobbyInfo).toList();
    }

    public LobbyInfo getJoinedLobbyInfo(User user){
        if(user.getStatus() != WAITING_TO_START) {
            throw new InvalidOperationException("The user is not in any lobby");
        }

        return lobbyList.getLobbyById(user.getJoinedLobbyId()).getLobbyInfo();
    }

    public LobbyInfo createLobby(User creator, String lobbyName, int requiredPlayersNum) {
        Lobby newLobby = lobbyList.createLobby(creator, lobbyName, requiredPlayersNum);

        for(User other : userList.getUsers()){
            if(!other.equals(creator)){
                try {
                    other.getNotificationSubscriber().onLobbyListUpdate();
                } catch (RemoteException e){
                    continue;
                }
            }
        }

        return newLobby.getLobbyInfo();
    }

    public void joinLobby(User user, int lobbyId){

        if(user.getStatus() != LOBBY_CHOICE){
            throw new WrongPhaseException();
        }

        Lobby lobbyToJoin = null;
        // It doesn't remain null;
        // If the lobby isn't available, an exception is thrown

        synchronized (lobbyList){
            lobbyToJoin = lobbyList.getLobbyById(lobbyId);
            lobbyToJoin.addUser(user);

            if(lobbyToJoin.readyToStart()) {
                lobbyToJoin = lobbyList.removeLobby(lobbyId);
            }
        }

        if(lobbyToJoin.readyToStart()){
            activeGames.addGameFromLobby(lobbyToJoin);
            List<User> connectedUsers = lobbyToJoin.getConnectedUsers();

            for(User other : connectedUsers){
                try {
                    other.getNotificationSubscriber().onGameStarted();
                } catch (RemoteException e){
                    continue;
                }
            }
        } else {
            LobbyInfo lobbyInfo = lobbyToJoin.getLobbyInfo();
            // TODO: avoid the repetition of this loop for RemoteException handling
            for(User other : lobbyToJoin.getConnectedUsers()){
                try {
                    other.getNotificationSubscriber().onJoinedLobbyUpdate(lobbyInfo);
                } catch (RemoteException e){
                    continue;
                }
            }
        }

        for(User other : userList.getUsers()){
            try {
                other.getNotificationSubscriber().onLobbyListUpdate();
            } catch (RemoteException e){
                continue;
            }
        }
    }

    public void exitFromLobby(User user){
        if(user.getStatus() != WAITING_TO_START){
            throw new WrongPhaseException("Cannot exit a lobby if not in one");
        }

        Lobby exitedLobby;

        try {
            exitedLobby = lobbyList.getLobbyById(user.getJoinedLobbyId());
        } catch (ElementNotFoundException e){
            throw new WrongPhaseException("Cannot exit from lobby after game started");
        }

        exitedLobby.removeUser(user);


        if(exitedLobby.getNumOfWaitingPlayers() == 0) {
            lobbyList.removeLobby(exitedLobby.getId());
            for(User other : userList.getUsers()){
                if(!other.equals(user)){
                    try {
                        other.getNotificationSubscriber().onLobbyListUpdate();
                    } catch (RemoteException e){
                        continue;
                    }
                }
            }
        } else {
            for(User other : exitedLobby.getConnectedUsers()){
                try {
                    other.getNotificationSubscriber().onJoinedLobbyUpdate(exitedLobby.getLobbyInfo());
                } catch (RemoteException e){
                    continue;
                }
            }
        }
    }

    public List<String> getPlayerNames(User user) {

        Game joinedGame = activeGames.getJoinedGame(user);

        return joinedGame.getPlayers().stream()
                .map(Player::getNickname)
                .toList();
    }

    public String getCurrentPlayer(User user){
        Game joinedGame = activeGames.getJoinedGame(user);
        return joinedGame.getCurrentPlayerNickname();
    }

    public ControlledPlayerInfo getControlledPlayerInfo(User user){
        Game joinedGame = activeGames.getJoinedGame(user);
        return InfoTranslator.convertToControlledPlayerInfo(joinedGame.getPlayer(user.getUsername()));
    }

    public OpponentInfo getOpponentInfo(User user, String opponentName){
        Game joinedGame = activeGames.getJoinedGame(user);
        return InfoTranslator.convertToOpponentPlayerInfo(joinedGame.getPlayer(opponentName));
    }

    public DrawableCardsInfo getDrawableCardsInfo(User user){

        Game joinedGame = activeGames.getJoinedGame(user);

        Map<DrawChoice, Card> drawableCards = joinedGame.getVisibleCards();

        if(!joinedGame.getResourceCardDeck().isEmpty()) {
            Card resourceTopCard = joinedGame.getResourceCardDeck().getTopCard();
            drawableCards.put(DECK_RESOURCE, resourceTopCard);
        }

        if(!joinedGame.getGoldenCardDeck().isEmpty()) {
            Card resourceTopCard = joinedGame.getGoldenCardDeck().getTopCard();
            drawableCards.put(DECK_GOLD, resourceTopCard);
        }

        return InfoTranslator.convertToDrawableCardsInfo(drawableCards);
    }

    public List<ObjectiveInfo> getCommonObjectives(User user) {
        Game joinedGame = activeGames.getJoinedGame(user);
        return joinedGame.getCommonObjectiveCards().stream()
                .map(InfoTranslator::convertToObjectiveInfo)
                .toList();
    }

    public PlayerSetupInfo getPlayerSetupInfo(User user){
        Game joinedGame = activeGames.getJoinedGame(user);
        return InfoTranslator.convertToPlayerSetupInfo(joinedGame.getPlayerSetup(user.getUsername()));
    }

    public boolean isLastTurn(User user){
        Game joinedGame = activeGames.getJoinedGame(user);
        return joinedGame.isLastTurn();
    }

    public boolean hasGameEnded(User user){
        Game joinedGame = activeGames.getJoinedGame(user);
        return joinedGame.isEnded();
    }

    public void registerPlayerSetup(User user, int objectiveCardId, CardOrientation initialCardSide){
        Game joinedGame = activeGames.getJoinedGame(user);
        Set<User> connectedUsers = activeGames.getConnectedUsers(user.getJoinedGameId());

        joinedGame.registerPlayerSetup(user.getUsername(), objectiveCardId, initialCardSide);

        if(joinedGame.allPlayersHaveSetup()){
            for(User player : connectedUsers){
                try {
                    player.getNotificationSubscriber().onSetupPhaseFinished();
                } catch (RemoteException e){
                    continue;
                }
            }
        }
    }

    public void registerPlayerMove(User user, int placedCardId, Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice){
        Game joinedGame = activeGames.getJoinedGame(user);
        Set<User> connectedUsers = activeGames.getConnectedUsers(user.getJoinedGameId());

        if(joinedGame.getCurrentPlayerNickname() != user.getUsername()){
            throw new WrongPhaseException("Cannot perform a move when it is not your turn");
        }

        if(joinedGame.canPlayerPlay(user.getUsername())){
            joinedGame.makePlayerPlaceCard(user.getUsername(), placedCardId, placementPoint, chosenSide);
            joinedGame.makePlayerDraw(user.getUsername(), drawChoice);
            joinedGame.changeCurrentPlayer();

            if(joinedGame.isEnded()){
                for(User other : connectedUsers){
                    try {
                        other.getNotificationSubscriber().onGameEnded();
                    } catch (RemoteException e){
                        continue;
                    }
                }
            } else if(joinedGame.isLastTurn()){
                for(User other : connectedUsers){
                    try {
                        other.getNotificationSubscriber().onLastTurnReached();
                    } catch (RemoteException e){
                        continue;
                    }
                }
            }
        } else {

            // Skip until you don't find a player who can play
           do {
               String currentPlayer = joinedGame.getCurrentPlayerNickname();
               joinedGame.skipTurn();

               for(User other : connectedUsers){
                   try {
                       other.getNotificationSubscriber().onTurnSkipped(currentPlayer);
                   } catch (RemoteException e){
                       continue;
                   }
               }

                // All players tried, no one can play
                if(currentPlayer.equals(user.getUsername())){
                    joinedGame.endGame();
                    for(User other : connectedUsers){
                        try {
                            other.getNotificationSubscriber().onGameEnded();
                        } catch (RemoteException e){
                            continue;
                        }
                    }
                }
            } while((!joinedGame.canPlayerPlay(joinedGame.getCurrentPlayerNickname())));
        }
    }

    public List<ControlledPlayerInfo> getLeaderboard(User user) {
        Game joinedGame = activeGames.getJoinedGame(user);

        if(!joinedGame.isEnded()){
            throw new WrongPhaseException("The game has not finished yet");
        }
        return joinedGame.getLeaderBoard().stream()
                .map(InfoTranslator::convertToControlledPlayerInfo)
                .toList();
    }

    public void exitFromGame(User user) {
        Integer gameId = user.getJoinedGameId();
        activeGames.exitFromJoinedGame(user);
        if(activeGames.areAllUsersDisconnected(gameId)){
            activeGames.removeGame(gameId);
        }
    }

    public String ping() {
        return "Server ready";
    }
}
