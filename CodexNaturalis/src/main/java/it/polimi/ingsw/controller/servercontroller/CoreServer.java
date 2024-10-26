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

    protected final UserList userList;
    protected final LobbyList lobbyList;
    protected final GameList activeGames;

    public CoreServer(UserList userList, LobbyList lobbyList, GameList activeGames) throws RemoteException {
        super();
        this.userList = userList;
        this.lobbyList = lobbyList;
        this.activeGames = activeGames;
    }

    public User checkLoggedIn(String username, int password) throws RemoteException {
        for (User user : userList.getUsers()) {
            if (user.getUsername().equals(username)) {
                if(user.checkPass(password)){return user;}
            }
        }
        return null;
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
    public ServerController login(String username, int tempCode) throws RemoteException {
        User loginUser;

        synchronized (userList) {
            if (!userList.isUserRegistered(username)) {
                throw new InvalidCredentialsException();
            }

            loginUser = userList.getUserByUsername(username);
        }

        if(loginUser.checkPass(tempCode)){
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
            throw new WrongPhaseException("The user is not in any lobby");
        }

        return lobbyList.getLobbyById(user.getJoinedLobbyId()).getLobbyInfo();
    }

    public LobbyInfo createLobby(User creator, String lobbyName, int requiredPlayersNum) {
        Lobby newLobby = lobbyList.createLobby(creator, lobbyName, requiredPlayersNum);
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
        joinedGame.registerPlayerSetup(user.getUsername(), objectiveCardId, initialCardSide);
    }

    public void registerPlayerMove(User user, int placedCardId, Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice){
        Game joinedGame = activeGames.getJoinedGame(user);

        if(!Objects.equals(joinedGame.getCurrentPlayerNickname(), user.getUsername())){
            throw new WrongPhaseException("Cannot perform a move when it is not your turn");
        }

        if(joinedGame.canPlayerPlay(user.getUsername())){
            joinedGame.makePlayerPlaceCard(user.getUsername(), placedCardId, placementPoint, chosenSide);
            if(!joinedGame.getVisibleCards().isEmpty()) {
                joinedGame.makePlayerDraw(user.getUsername(), drawChoice);
            }
            joinedGame.changeCurrentPlayer();
        } else {

            // Skip until you don't find a player who can play
           do {
               String currentPlayer = joinedGame.getCurrentPlayerNickname();
               joinedGame.skipTurn();

                // All players tried, no one can play
                if(currentPlayer.equals(user.getUsername())){
                    joinedGame.endGame();
                    break;
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

    public boolean setupDone(User user){
        Game joinedGame = activeGames.getJoinedGame(user);
        return joinedGame.allPlayersHaveSetup();
    }

    public String ping() {
        return "Server ready";
    }
}
