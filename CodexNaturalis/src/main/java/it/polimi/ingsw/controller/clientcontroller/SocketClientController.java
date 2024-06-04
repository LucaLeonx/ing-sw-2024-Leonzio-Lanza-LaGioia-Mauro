package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidCredentialsException;
import it.polimi.ingsw.controller.socket.SocketClient;
import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidOperationException;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.rmi.RemoteException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SocketClientController implements ClientController {

    //private ServerController session = null;
    private  String username = null;
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
        if(isRegistered()){ this.logout();}
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

        return (List<LobbyInfo>) client.receiveMessage().getObj();
    }

    @Override
    public LobbyInfo createLobby(String lobbyName, int requiredPlayers) throws RemoteException {
        LobbyInfo tuple = new LobbyInfo(-1,lobbyName,null,null,requiredPlayers,0);
        client.sendMessage(new Message(MessageType.CREATE_LOBBY, getCredentials(),tuple));

        return (LobbyInfo) client.receiveMessage().getObj();
    }

    @Override
        public LobbyInfo getJoinedLobbyInfo() throws RemoteException {
        client.sendMessage(new Message(MessageType.GET_JOINED_LOBBY_INFO, getCredentials(),null));
        Message response = client.receiveMessage();
        checkExceptionOnMessage(response);
        return (LobbyInfo) response.getObj();
    }

    @Override
    public void joinLobby(int lobbyId) throws RemoteException {
        client.sendMessage(new Message(MessageType.JOIN_LOBBY, getCredentials(),lobbyId));
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
        List<LobbyInfo> oldLobbies;
        try{
            oldLobbies = getLobbyList();
            int newSize,oldSize;
            newSize = oldSize = oldLobbies.size();
            while(newSize == oldSize){
                Thread.sleep(1000);
                newSize = getLobbyList().size();
            }
        }catch(RemoteException | InterruptedException e){
            return;
        }
    }

    @Override
    public void waitForGameToStart() {
        int currPlayer,reqPlayer;
        LobbyInfo currentLobby;
        try{
            currentLobby = getJoinedLobbyInfo();
            while(currentLobby != null){
                Thread.sleep(1000);
                currentLobby = getJoinedLobbyInfo();
            }
        }catch(InterruptedException | RemoteException | InvalidOperationException e){
            return;
        }
    }

    @Override
    public void waitForJoinedLobbyUpdate() {
        int currPlayers,oldPlayers;
        LobbyInfo currentLobby;
        try{
            currentLobby = getJoinedLobbyInfo();
            oldPlayers = currPlayers = currentLobby.currNumPlayers();
            while(currPlayers == oldPlayers){
                Thread.sleep(1000);
                currentLobby = getJoinedLobbyInfo();
                currPlayers = getJoinedLobbyInfo().currNumPlayers();
            }
        }catch(InterruptedException | RemoteException | InvalidOperationException e){
            return;
        }
    }

    private boolean allPlayersHaveSetup(){
        client.sendMessage(new Message(MessageType.ALL_PLAYERS_HAVE_SETUP,getCredentials(),null));
        return (boolean) client.receiveMessage().getObj();
    }

    @Override
    public void waitForSetupFinished(){
        try{
            while(!allPlayersHaveSetup()){
                Thread.sleep(1000);
            }
        }catch(InterruptedException e){
            return;
        }
    }

    @Override
    public void waitForTurnChange() {//Continuous polling
        String oldUser;
        try{
            oldUser = getCurrentPlayerName();
            while(oldUser.equals(getCurrentPlayerName())){
                Thread.sleep(1000);
            }
        }catch (InterruptedException | RemoteException e) {
            return;
        }
    }

    @Override
    public void waitForGameEnded() {
        try{
            while(!hasGameEnded()){
                Thread.sleep(1000);
            }
        }catch(InterruptedException | RemoteException e){
            return;
        }
    }

    @Override
    public String getCurrentPlayerName() throws RemoteException {
        client.sendMessage(new Message(MessageType.GET_CURRENT_PLAYER_NAME, getCredentials(),null));
        return client.receiveMessage().getObj().toString();
    }

    @Override
    public List<String> getPlayerNames() throws RemoteException {
        client.sendMessage(new Message(MessageType.GET_PLAYER_NAMES,getCredentials(),null));
        Message response = client.receiveMessage();
        checkExceptionOnMessage(response);
        return (List<String>) response.getObj();
    }

    @Override
    public PlayerSetupInfo getPlayerSetup() throws RemoteException {
        client.sendMessage(new Message(MessageType.GET_PLAYER_SETUP,getCredentials(),null));
        return (PlayerSetupInfo) client.receiveMessage().getObj();
    }

    @Override
    public List<ObjectiveInfo> getCommonObjectives() throws RemoteException {
        client.sendMessage(new Message(MessageType.GET_COMMON_OBJECTIVES,getCredentials(),null));
        return (List<ObjectiveInfo>) client.receiveMessage().getObj();
    }

    @Override
    public ControlledPlayerInfo getControlledPlayerInformation() throws RemoteException {
        client.sendMessage(new Message(MessageType.GET_CONTROLLED_PLAYER_INFO,null,null));
        return (ControlledPlayerInfo) client.receiveMessage().getObj();
    }

    @Override
    public OpponentInfo getOpponentInformation(String opponentName) throws RemoteException {
        client.sendMessage(new Message(MessageType.GET_OPPONENT_INFORMATION,getCredentials(),opponentName));
        return (OpponentInfo) client.receiveMessage().getObj();
    }

    @Override
    public DrawableCardsInfo getDrawableCards() throws RemoteException {
        client.sendMessage(new Message(MessageType.GET_DRAWABLE_CARDS,getCredentials(),null));
        return (DrawableCardsInfo) client.receiveMessage().getObj();
    }

    @Override
    public boolean isLastTurn() throws RemoteException {
        client.sendMessage(new Message(MessageType.IS_LAST_TURN,getCredentials(),null));
        return (boolean) client.receiveMessage().getObj();
    }

    @Override
    public boolean hasGameEnded() throws RemoteException {
        client.sendMessage(new Message(MessageType.HAS_GAME_ENDED,getCredentials(),null));
        return (boolean) client.receiveMessage().getObj();
    }

    @Override
    public String getWinner() throws RemoteException {
        try {
            return getLeaderboard().getFirst().nickname();
        } catch (InvalidOperationException | RemoteException e){
            throw new InvalidOperationException("The game has not ended yet. Cannot find a winner");
        }
    }

    @Override
    public List<ControlledPlayerInfo> getLeaderboard() throws RemoteException {
        client.sendMessage(new Message(MessageType.HAS_GAME_ENDED,getCredentials(),null));
        return (List<ControlledPlayerInfo>) client.receiveMessage().getObj();
    }

    @Override
    public void setPlayerSetup(ObjectiveInfo chosenObjective, CardOrientation initialCardSide) throws RemoteException {
        Tuple data = new Tuple(chosenObjective, initialCardSide);
        client.sendMessage(new Message(MessageType.SET_PLAYER_SETUP,getCredentials(),data));
    }

    @Override
    public void makeMove(CardInfo card, Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice) throws RemoteException {
        client.sendMessage(new Message(MessageType.MAKE_MOVE,getCredentials(),new MoveInfo(card,placementPoint,chosenSide,drawChoice)));
    }

    @Override
    public void exitGame() throws InvalidOperationException {
        client.sendMessage(new Message(MessageType.EXIT_GAME,getCredentials(),null));
    }

    @Override
    public boolean isWaitingInLobby() {
        client.sendMessage(new Message(MessageType.GET_JOINED_LOBBY_INFO,getCredentials(),null));
        return !(client.receiveMessage().getObj() instanceof Exception);
    }

    public void closeSocket() throws IOException {
        client.closeConnection();
    }

    @Override
    public boolean isInGame() throws RemoteException{
        client.sendMessage(new Message(MessageType.GET_JOINED_LOBBY_INFO,getCredentials(),null));
        return !(client.receiveMessage().getObj() instanceof Exception);
    }
}
