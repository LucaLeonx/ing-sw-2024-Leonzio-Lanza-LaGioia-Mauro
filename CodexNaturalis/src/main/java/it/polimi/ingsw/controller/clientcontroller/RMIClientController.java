package it.polimi.ingsw.controller.clientcontroller;


import it.polimi.ingsw.controller.servercontroller.AuthenticatedSession;
import it.polimi.ingsw.controller.servercontroller.AuthenticationManager;
import it.polimi.ingsw.controller.servercontroller.NotificationSubscriber;
import it.polimi.ingsw.controller.servercontroller.ServerController;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.WrongPhaseException;
import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.InvalidOperationException;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RMIClientController extends UnicastRemoteObject implements ClientController, NotificationSubscriber {

    private final AuthenticationManager authenticator;
    private ServerController session = null;

    private final List<GameObserver> gameObservers;
    private final List<LobbyObserver> lobbyObservers;

    public RMIClientController(String host, int port, String serverName) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(host, port);
        this.authenticator = (AuthenticationManager) registry.lookup(serverName);
        this.gameObservers = new LinkedList<>();
        this.lobbyObservers = new LinkedList<>();
    }

    public RMIClientController() throws RemoteException, NotBoundException {
       this(ConnectionDefaultSettings.RMIRegistryHost, ConnectionDefaultSettings.RMIRegistryPort, ConnectionDefaultSettings.RMIServerName);
    }

    private boolean isAuthenticated(){
        return session != null;
    }

    private void checkLogin(){
        if(!isAuthenticated()){
            throw new InvalidOperationException("Cannot perform this operation when not logged in");
        }
    }

    @Override
    public int register(String username) throws RemoteException {
        if(isAuthenticated()){
            session.logout();
        }

        return authenticator.register(username);
    }

    @Override
    public void login(String username, int tempCode) throws RemoteException {
        session = (ServerController) authenticator.login(username, tempCode, this);
    }

    @Override
    public void logout() throws RemoteException {
        session.logout();
        session = null;
    }

    @Override
    public List<LobbyInfo> getLobbyList() throws RemoteException {
        checkLogin();
        return session.getLobbies();
    }

    @Override
    public LobbyInfo createLobby(String lobbyName, int requiredPlayers) throws RemoteException {
        checkLogin();
        return session.addLobby(lobbyName, requiredPlayers);
    }

    @Override
    public LobbyInfo getJoinedLobbyInfo() throws RemoteException {
        checkLogin();
        return session.getJoinedLobbyInfo();
    }

    @Override
    public void joinLobby(int lobbyId) throws RemoteException {
        checkLogin();
        session.joinLobby(lobbyId);
    }

    @Override
    public void exitFromLobby() throws RemoteException {
        checkLogin();
        session.exitFromLobby();
    }

    @Override
    public void subscribeToLobbyUpdates(LobbyObserver observer) {
        lobbyObservers.add(observer);
    }

    @Override
    public void subscribeToGameUpdates(GameObserver observer) {
        gameObservers.add(observer);
    }

    @Override
    public List<String> getPlayerNames() throws RemoteException {
        checkLogin();
        return session.getPlayerNames();
    }

    @Override
    public PlayerSetupInfo getPlayerSetup() throws RemoteException {
        checkLogin();
        return session.getPlayerSetup();
    }

    @Override
    public List<ObjectiveInfo> getCommonObjectives() throws RemoteException {
        checkLogin();
        return session.getCommonObjectives();
    }

    @Override
    public ControlledPlayerInfo getControlledPlayerInformation() throws RemoteException {
        checkLogin();
        return session.getControlledPlayerInfo();
    }

    @Override
    public OpponentInfo getOpponentInformation(String opponentName) throws RemoteException {
        checkLogin();
        return session.getOpponentInfo(opponentName);
    }

    @Override
    public DrawableCardsInfo getDrawableCards() throws RemoteException {
        checkLogin();
        return session.getDrawableCardsInfo();
    }

    @Override
    public String getCurrentPlayerName() throws RemoteException {
        checkLogin();
        return session.getCurrentPlayer();
    }

    @Override
    public boolean isLastTurn() throws RemoteException {
        checkLogin();
        return session.isLastTurn();
    }

    @Override
    public boolean hasGameEnded() throws RemoteException {
        checkLogin();
        return session.hasGameEnded();
    }

    @Override
    public String getWinner() {
        try {
            return getLeaderboard().getFirst().nickname();
        } catch (InvalidOperationException | RemoteException e){
            throw new InvalidOperationException("The game has not ended yet. Cannot find a winner");
        }
    }

    @Override
    public List<ControlledPlayerInfo> getLeaderboard() throws RemoteException {
        checkLogin();
        return session.getLeaderboard();
    }

    @Override
    public void setPlayerSetup(ObjectiveInfo chosenObjective, CardOrientation initialCardSide) throws RemoteException {
        checkLogin();
        session.registerPlayerSetupChoice(chosenObjective.id(), initialCardSide);
    }

    @Override
    public void makeMove(CardInfo card, Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice) throws RemoteException {
        checkLogin();
        session.registerPlayerMove(card.id(), placementPoint, chosenSide, drawChoice);
    }

    @Override
    public void exitGame() throws RemoteException {
        checkLogin();
        session.exitFromGame();
    }

    @Override
    public void onLobbyListUpdate(){
        List<LobbyInfo> updatedLobbyList;

        try {
            updatedLobbyList = getLobbyList();
        } catch (RemoteException e) {
            throw new WrongPhaseException(e.getMessage());
        }

        List<LobbyInfo> finalUpdatedLobbyList = updatedLobbyList;
        lobbyObservers.forEach((observer) ->
            observer.onLobbyListUpdate(finalUpdatedLobbyList));
    }

    @Override
    public void onJoinedLobbyUpdate(LobbyInfo joinedLobby) {
        lobbyObservers.forEach(observer -> observer.onJoinedLobbyUpdate(joinedLobby));
    }

    @Override
    public void onGameStarted() {
        lobbyObservers.forEach(LobbyObserver::onGameStarted);
        gameObservers.forEach(GameObserver::onGameStarted);
    }

    @Override
    public void onSetupPhaseFinished() {
        gameObservers.forEach(GameObserver::onSetupPhaseFinished);
    }

    @Override
    public void onCurrentPlayerChange(String newPlayerName) {
        gameObservers.forEach(observer -> observer.onCurrentPlayerChange(newPlayerName));
    }

    @Override
    public void onTurnSkipped(String skippedPlayerName) {
        gameObservers.forEach(observer -> observer.onTurnSkipped(skippedPlayerName));
    }

    @Override
    public void onLastTurnReached() {
        gameObservers.forEach(GameObserver::onLastTurnReached);
    }

    @Override
    public void onGameEnded() {
        gameObservers.forEach(GameObserver::onLastTurnReached);
    }
}
