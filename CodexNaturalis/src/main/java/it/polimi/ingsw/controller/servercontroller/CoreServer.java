package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.controller.servercontroller.operationexceptions.ElementNotFoundException;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidCommandException;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.WrongPhaseException;
import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.InvalidOperationException;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;
import it.polimi.ingsw.model.player.Player;

import java.util.*;
import java.util.function.Consumer;

import static it.polimi.ingsw.controller.servercontroller.UserStatus.*;
import static it.polimi.ingsw.model.DrawChoice.DECK_GOLD;
import static it.polimi.ingsw.model.DrawChoice.DECK_RESOURCE;

public class CoreServer {

    private final UserList userList;
    private final LobbyList lobbyList;
    private final GameList activeGames;

    public CoreServer(UserList userList, LobbyList lobbyList, GameList activeGames) {
        this.userList = userList;
        this.lobbyList = lobbyList;
        this.activeGames = activeGames;
    }

    public void logout(User user){

        UserStatus status = user.getStatus();

        if(status != LOBBY_CHOICE) {
            throw switch (status) {
                case WAITING_TO_START -> new WrongPhaseException("Cannot logout when in a lobby");
                case IN_GAME -> new WrongPhaseException("Cannot logout when in game");
                default -> new WrongPhaseException();
            };
        }

        userList.removeUser(user);
    }

    public void addNotificationSubscriber(User user, NotificationSubscriber subscriber){
        user.setNotificationSubscriber(subscriber);
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

    public LobbyInfo createLobby(User creator, String lobbyName, int requiredPlayersNum){

        if (requiredPlayersNum < 2 || requiredPlayersNum > 4){
            throw new InvalidCommandException("Invalid number of required players");
        } else if(creator.getStatus() != LOBBY_CHOICE){
            throw new WrongPhaseException("Cannot create Lobby when not choosing one");
        }

        Lobby newLobby = lobbyList.createLobby(creator, lobbyName, requiredPlayersNum);
        creator.setStatus(WAITING_TO_START);

        notifyToAllExcept(userList.getUsers(), creator, NotificationSubscriber::onLobbyListUpdate);

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
            if(lobbyList.containsLobby(lobbyId)){
                lobbyToJoin = lobbyList.getLobbyById(lobbyId);
                lobbyToJoin.addUser(user);

                if(lobbyToJoin.readyToStart()) {
                    lobbyToJoin = lobbyList.removeLobby(lobbyId);
                }
            }
        }

        if(lobbyToJoin.readyToStart()){
            activeGames.addGameFromLobby(lobbyToJoin);
            notifyToAll(lobbyToJoin.getConnectedUsers(), NotificationSubscriber::onGameStarted);
        } else {
            LobbyInfo lobbyInfo = lobbyToJoin.getLobbyInfo();
            notifyToAllExcept(lobbyToJoin.getConnectedUsers(), user, (subscriber) -> subscriber.onJoinedLobbyUpdate(lobbyInfo));
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

        notifyToAll(userList.getUsers(), NotificationSubscriber::onLobbyListUpdate);
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
            notifyToAll(connectedUsers, NotificationSubscriber::onSetupPhaseFinished);
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
                notifyToAll(connectedUsers, NotificationSubscriber::onGameEnded);
            } else if(joinedGame.isLastTurn()){
                notifyToAll(connectedUsers, NotificationSubscriber::onLastTurnReached);
            }
        } else {

            // Skip until you don't find a player who can play
           do {
                String currentPlayer = joinedGame.getCurrentPlayerNickname();
                joinedGame.skipTurn();
                notifyToAll(connectedUsers, (subscriber) -> subscriber.onTurnSkipped(currentPlayer));

                // All players tried, no one can play
                if(currentPlayer.equals(user.getUsername())){
                    joinedGame.endGame();
                    notifyToAll(connectedUsers, NotificationSubscriber::onGameEnded);
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

    private void notifyToAllExcept(Collection<User> users, User excluded, Consumer<NotificationSubscriber> notificationToSend) {
        users.stream()
                .filter((other) -> !other.equals(excluded))
                .map(User::getNotificationSubscriber)
                .forEach(notificationToSend);
    }

    private void notifyToAll(Collection<User> users, Consumer<NotificationSubscriber> notificationToSend){
        users.stream()
                .map(User::getNotificationSubscriber)
                .parallel()
                .forEach(notificationToSend);
    }

    public String ping() {
        return "Server ready";
    }
}
