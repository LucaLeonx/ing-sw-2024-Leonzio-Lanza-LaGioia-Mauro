package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidCredentialsException;
import it.polimi.ingsw.controller.socket.SocketClient;
import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidOperationException;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class SocketClientController implements ClientController {

    //private ServerController session = null;
    private String username = null;
    private int tempCode = 0;
    private SocketClient client ;

    public SocketClientController() throws IOException {
        this.client = new SocketClient(ConnectionDefaultSettings.SocketServerPort);
        this.client.startClientConnection();
    }

    private boolean isRegistered(){
        return (username != null && tempCode != 0);
    }

    private void checkLogin(){
        if(!isRegistered()){
            throw new InvalidOperationException("Cannot perform this operation when not logged in");
        }
    }

    private void checkExceptionOnMessage(Message message){
        if(message.getObj() instanceof InvalidCredentialsException){
            throw (InvalidCredentialsException) message.getObj();
        }
        if(message.getObj() instanceof InvalidOperationException){
            throw (InvalidOperationException) message.getObj();
        }
    }

    private AbstractMap.SimpleEntry<String,Integer> getCredentials(){
        return new AbstractMap.SimpleEntry<String,Integer>(username, tempCode);
    }

    @Override
    public int register(String user) throws RemoteException {
        if(!isRegistered()){ this.logout();}
        Message msg = new Message(MessageType.REGISTER_USER,null,user);
        client.sendMessage(msg);

        Message receivedMsg = client.receiveMessage();
        checkExceptionOnMessage(receivedMsg);
        this.username = user;
        this.tempCode = (Integer) receivedMsg.getObj();
        return tempCode;
    }

    @Override
    public void login(String username, int tempCode) throws RemoteException {
        AbstractMap.SimpleEntry<String,Integer> tuple = new AbstractMap.SimpleEntry<>(username,tempCode);
        client.sendMessage( new Message(MessageType.LOGIN,tuple,null));

        Message receivedMsg = client.receiveMessage();
        checkExceptionOnMessage(receivedMsg);//throw exception if wrong data
        this.username = username;
        this.tempCode = tempCode;
    }

    @Override
    public void logout() throws RemoteException {
        checkLogin();
        client.sendMessage(new Message(MessageType.LOGOUT,getCredentials(),null));
    }

    @Override
    public List<LobbyInfo> getLobbyList() throws RemoteException {
        client.sendMessage(new Message(MessageType.LOBBY_LIST, getCredentials(),null));

        return List.of((LobbyInfo) client.receiveMessage().getObj());
    }

    @Override
    public LobbyInfo createLobby(String lobbyName, int requiredPlayers) throws RemoteException {
        LobbyInfo tuple = new LobbyInfo(-1,lobbyName,null,null,requiredPlayers,0);
        client.sendMessage(new Message(MessageType.CREATE_LOBBY, getCredentials(),tuple));

        return (LobbyInfo) client.receiveMessage().getObj();
    }

    @Override
    public void joinLobby(int lobbyId) throws RemoteException {
        client.sendMessage(new Message(MessageType.JOIN_LOBBY, getCredentials(),lobbyId));
    }

    @Override
    public LobbyInfo getJoinedLobbyInfo() throws RemoteException {
        client.sendMessage(new Message(MessageType.JOINED_LOBBY_INFO, getCredentials(),null));
        return (LobbyInfo) client.receiveMessage().getObj();
    }

    @Override
    public void exitFromLobby() throws RemoteException {
        client.sendMessage(new Message(MessageType.EXIT_FROM_LOBBY, getCredentials(),null));
    }

    @Override
    public void subscribeToNotifications(ClientNotificationSubscription observer) {

    }

    @Override
    public void waitForLobbyListUpdate() {

    }

    @Override
    public void waitForGameToStart() {

    }

    @Override
    public void waitForJoinedLobbyUpdate() {

    }

    @Override
    public void waitForSetupFinished() {

    }

    @Override
    public void waitForTurnChange() {

    }

    @Override
    public void waitForGameEnded() {

    }

    @Override
    public String getCurrentPlayerName() throws RemoteException {
        return "";
    }

    @Override
    public List<String> getPlayerNames() throws RemoteException {


        return List.of();
    }

    @Override
    public PlayerSetupInfo getPlayerSetup() throws RemoteException {
        return null;
    }

    @Override
    public List<ObjectiveInfo> getCommonObjectives() throws RemoteException {
        return List.of();
    }

    @Override
    public ControlledPlayerInfo getControlledPlayerInformation() throws RemoteException {
        return null;
    }

    @Override
    public OpponentInfo getOpponentInformation(String opponentName) throws RemoteException {
        return null;
    }

    @Override
    public DrawableCardsInfo getDrawableCards() throws RemoteException {
        return null;
    }

    @Override
    public boolean isLastTurn() throws RemoteException {
        return false;
    }

    @Override
    public boolean hasGameEnded() throws RemoteException {
        return false;
    }

    @Override
    public String getWinner() throws RemoteException {
        return "";
    }

    @Override
    public List<ControlledPlayerInfo> getLeaderboard() throws RemoteException {
        return List.of();
    }

    @Override
    public void setPlayerSetup(ObjectiveInfo chosenObjective, CardOrientation initialCardSide) throws RemoteException {

    }

    @Override
    public void makeMove(CardInfo card, Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice) throws RemoteException {

    }

    @Override
    public void exitGame() throws InvalidOperationException {

    }

    @Override
    public boolean isWaitingInLobby() {
        return false;
    }

    public void closeSocket() throws IOException {
        client.closeConnection();
    }

    @Override
    public boolean isInGame() throws RemoteException{
        return false;
    }
}
