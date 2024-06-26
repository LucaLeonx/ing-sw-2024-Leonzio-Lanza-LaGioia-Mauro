package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.controller.servercontroller.AuthenticationManager;
import it.polimi.ingsw.controller.servercontroller.ServerController;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.WrongPhaseException;
import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidOperationException;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RMIClientController extends UnicastRemoteObject implements ClientController {

    private final AuthenticationManager authenticator;
    private ServerController session = null;

    public RMIClientController(ConnectionSettings connectionSettings) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(connectionSettings.getServerHost(), connectionSettings.getRMIPort());
        this.authenticator = (AuthenticationManager) registry.lookup(connectionSettings.getRMIServerName());
    }

    public RMIClientController() throws RemoteException, NotBoundException {
       this(new ConnectionSettings());
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

        int tempCode = authenticator.register(username);
        login(username, tempCode);
        return tempCode;
    }

    @Override
    public void login(String username, int tempCode) throws RemoteException {
        session = authenticator.login(username, tempCode);
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
    public void waitForLobbyListUpdate() {

        try {
            while(!session.hasGameEnded()){
                Thread.sleep(1000);
            }
        } catch (RemoteException | InterruptedException e) {
            return;
        }
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
    public void waitForGameToStart(){
        checkLogin();
        int currPlayer, reqPlayer;
        LobbyInfo currentLobby;
        try{
            currentLobby = getJoinedLobbyInfo();
            while(currentLobby != null && currentLobby.currNumPlayers()< currentLobby.reqPlayers() ){
                Thread.sleep(1000);
                currentLobby = getJoinedLobbyInfo();
            }
        } catch(InterruptedException | RemoteException | InvalidOperationException e){
            return;
        }
    }

    @Override
    public void waitForJoinedLobbyUpdate(){
        checkLogin();
        int oldNumPlayers = 0;
        try {
            oldNumPlayers = session.getJoinedLobbyInfo().currNumPlayers();
            while(oldNumPlayers == session.getJoinedLobbyInfo().currNumPlayers()){
                Thread.sleep(1000);
            }
        } catch (RemoteException | InterruptedException e) {
            return;
        }
    }

    @Override
    public void waitForSetupFinished(){
        checkLogin();
        try {
            while(!session.setupDone()){
                Thread.sleep(1000);
            }
        } catch (RemoteException | InterruptedException e) {
            return;
        }
    }

    @Override
    public void waitForTurnChange(){
        checkLogin();
        String oldCurrentPlayer = null;
        try {
            oldCurrentPlayer = session.getCurrentPlayer();
            while(oldCurrentPlayer.equals(session.getCurrentPlayer())){
                Thread.sleep(1000);
            }
        } catch (RemoteException | InterruptedException e) {
            return;
        }
    }

    @Override
    public void waitForGameEnded(){
        checkLogin();
        try {
            while(!session.hasGameEnded()){
                Thread.sleep(1000);
            }
        } catch (RemoteException | InterruptedException e) {
            return;
        }
    }

    @Override
    public boolean isInGame(){
        try {
            session.getCurrentPlayer();
            return true;
        } catch (WrongPhaseException | RemoteException e){
            return false;
        }
    }

     @Override
     public boolean isWaitingInLobby(){
        try {
            session.getJoinedLobbyInfo();
            return true;
        } catch (WrongPhaseException | RemoteException e){
            return false;
        }
     }
}
