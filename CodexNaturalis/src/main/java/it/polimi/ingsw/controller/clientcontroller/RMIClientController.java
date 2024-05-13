package it.polimi.ingsw.controller.clientcontroller;


import it.polimi.ingsw.controller.servercontroller.AuthenticatedSession;
import it.polimi.ingsw.controller.servercontroller.AuthenticationManager;
import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.InvalidOperationException;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;

public class RMIClientController implements ClientController{

    private final Registry registry;
    private AuthenticationManager authenticator;
    private AuthenticatedSession session = null;

    public RMIClientController(String host, int port, String serverName) throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry(host, port);
        this.authenticator = (AuthenticationManager) registry.lookup(serverName);
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
        session = authenticator.login(username, tempCode);
    }

    @Override
    public void logout() {
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
    public void exitFromLobby() {
        checkLogin();
        session.exitFromLobby();
    }

    @Override
    public List<String> getPlayerNames() {
        checkLogin();
        return session.getPlayerNames();
    }

    @Override
    public PlayerSetupInfo getPlayerSetup() throws RemoteException {
        checkLogin();
        return session.getPlayerSetup();
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
    public Map<DrawChoice, CardSideInfo> getDrawableCards() throws RemoteException {
        checkLogin();
        return session.getDrawableCardsInfo().drawableCards();
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
        } catch (InvalidOperationException e){
            throw new InvalidOperationException("The game has not ended yet. Cannot find a winner");
        }
    }

    @Override
    public List<ControlledPlayerInfo> getLeaderboard() {
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
    public void exitGame() throws InvalidOperationException {
        checkLogin();
        session.exitGame();
    }
}
