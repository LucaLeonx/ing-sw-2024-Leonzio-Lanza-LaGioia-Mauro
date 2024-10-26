package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.controller.ConnectionSettings;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidCredentialsException;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidParameterException;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.WrongPhaseException;
import it.polimi.ingsw.controller.socket.SocketClient;
import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidOperationException;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.AbstractMap;
import java.util.List;

public class SocketClientController implements ClientController {

    private String username = null;
    private int tempCode = 0;
    private SocketClient client;


    public SocketClientController(ConnectionSettings connectionSettings) throws IOException {
        this.client = new SocketClient(connectionSettings.getServerHost(), connectionSettings.getSocketPort());
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
        synchronized (this) {
            if (isRegistered()) {
                this.logout();
            }
            Message msg = new Message(MessageType.REGISTER_USER, null, user);
            client.sendMessage(msg);

            Message receivedMsg = client.receiveMessage();
            checkExceptionOnMessage(receivedMsg);
            this.username = user;
            this.tempCode = (Integer) receivedMsg.getObj();
            return tempCode;
        }
    }

    @Override
    public void login(String username, int tempCode) throws RemoteException {
        AbstractMap.SimpleEntry<String,Integer> tuple = new AbstractMap.SimpleEntry<>(username,tempCode);
        synchronized (this) {
            client.sendMessage(new Message(MessageType.LOGIN, tuple, null));

            Message receivedMsg = client.receiveMessage();
            checkExceptionOnMessage(receivedMsg);//throw exception if wrong data
            this.username = username;
            this.tempCode = tempCode;
        }
    }

    @Override
    public void logout() throws RemoteException {
        checkLogin();
        client.sendMessage(new Message(MessageType.LOGOUT,getCredentials(),null));
        client.receiveMessage();
    }

    @Override
    public List<LobbyInfo> getLobbyList() throws RemoteException {
        synchronized (this) {
            client.sendMessage(new Message(MessageType.LOBBY_LIST, getCredentials(), null));
            Message receivedMsg = client.receiveMessage();
            checkExceptionOnMessage(receivedMsg);
            return (List<LobbyInfo>) receivedMsg.getObj();
        }
    }

    @Override
    public LobbyInfo createLobby(String lobbyName, int requiredPlayers) throws RemoteException {
        synchronized (this) {
            LobbyInfo tuple = new LobbyInfo(-1, lobbyName, null, null, requiredPlayers, 0);
            client.sendMessage(new Message(MessageType.CREATE_LOBBY, getCredentials(), tuple));

            return (LobbyInfo) client.receiveMessage().getObj();
        }
    }

    @Override
        public LobbyInfo getJoinedLobbyInfo() throws RemoteException {
        synchronized (this){
            client.sendMessage(new Message(MessageType.GET_JOINED_LOBBY_INFO, getCredentials(),null));
            Message response = client.receiveMessage();
            try {
                checkExceptionOnMessage(response);
            } catch (InvalidCredentialsException e) {
                return null;
            }//I think is useless

            return (LobbyInfo) response.getObj();
        }
    }

    @Override
    public void joinLobby(int lobbyId) throws RemoteException {
        synchronized (this) {
            client.sendMessage(new Message(MessageType.JOIN_LOBBY, getCredentials(), lobbyId));
            Message received = client.receiveMessage();
            if (received == null || !received.getObj().equals("Success")) {
                throw new InvalidOperationException("Not joined the lobby");
            }
        }
    }

    @Override
    public void exitFromLobby() throws RemoteException {
        synchronized (this) {
            client.sendMessage(new Message(MessageType.EXIT_FROM_LOBBY, getCredentials(), null));
            client.receiveMessage();
        }
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
        }catch(RemoteException | InterruptedException | InvalidParameterException e){
            return;
        }
    }

    @Override
    public void waitForGameToStart() {
        int currPlayer,reqPlayer;
        LobbyInfo currentLobby;
        try{
            currentLobby = getJoinedLobbyInfo();
            while(currentLobby != null && currentLobby.currNumPlayers() < currentLobby.reqPlayers() ){
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
                currPlayers = currentLobby.currNumPlayers();
            }
        }catch(InterruptedException | RemoteException | InvalidOperationException e){
            return;
        }
    }

    private boolean allPlayersHaveSetup(){
        synchronized (this) {
            client.sendMessage(new Message(MessageType.ALL_PLAYERS_HAVE_SETUP, getCredentials(), null));
            return (boolean) client.receiveMessage().getObj();
        }
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
        synchronized (this) {
            client.sendMessage(new Message(MessageType.GET_CURRENT_PLAYER_NAME, getCredentials(), null));
            Message response = client.receiveMessage();
            checkExceptionOnMessage(response);
            return response.getObj().toString();
        }
    }

    @Override
    public List<String> getPlayerNames() throws RemoteException {
        synchronized (this) {
            client.sendMessage(new Message(MessageType.GET_PLAYER_NAMES, getCredentials(), null));
            Message response = client.receiveMessage();
            checkExceptionOnMessage(response);
            return (List<String>) response.getObj();
        }
    }

    @Override
    public PlayerSetupInfo getPlayerSetup() throws RemoteException {
        synchronized (this) {
            client.sendMessage(new Message(MessageType.GET_PLAYER_SETUP, getCredentials(), null));
            return (PlayerSetupInfo) client.receiveMessage().getObj();
        }
    }

    @Override
    public List<ObjectiveInfo> getCommonObjectives() throws RemoteException {
        synchronized (this) {
            client.sendMessage(new Message(MessageType.GET_COMMON_OBJECTIVES, getCredentials(), null));
            return (List<ObjectiveInfo>) client.receiveMessage().getObj();
        }
    }

    @Override
    public ControlledPlayerInfo getControlledPlayerInformation() throws RemoteException {
        synchronized (this) {
            client.sendMessage(new Message(MessageType.GET_CONTROLLED_PLAYER_INFO, getCredentials(), null));
            Message response = client.receiveMessage();
            checkExceptionOnMessage(response);
            return (ControlledPlayerInfo) response.getObj();
        }
    }

    @Override
    public OpponentInfo getOpponentInformation(String opponentName) throws RemoteException {
        synchronized (this) {
            client.sendMessage(new Message(MessageType.GET_OPPONENT_INFORMATION, getCredentials(), opponentName));
            return (OpponentInfo) client.receiveMessage().getObj();
        }
    }

    @Override
    public DrawableCardsInfo getDrawableCards() throws RemoteException {
        synchronized (this) {
            client.sendMessage(new Message(MessageType.GET_DRAWABLE_CARDS, getCredentials(), null));
            return (DrawableCardsInfo) client.receiveMessage().getObj();
        }
    }

    @Override
    public boolean isLastTurn() throws RemoteException {
        synchronized (this) {
            client.sendMessage(new Message(MessageType.IS_LAST_TURN, getCredentials(), null));
            return (boolean) client.receiveMessage().getObj();
        }
    }

    @Override
    public boolean hasGameEnded() throws RemoteException {
        synchronized (this) {
            client.sendMessage(new Message(MessageType.HAS_GAME_ENDED, getCredentials(), null));
            return (boolean) client.receiveMessage().getObj();
        }
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
        synchronized (this) {
            client.sendMessage(new Message(MessageType.GET_LEADERBOARD, getCredentials(), null));
            return (List<ControlledPlayerInfo>) client.receiveMessage().getObj();
        }
    }

    @Override
    public void setPlayerSetup(ObjectiveInfo chosenObjective, CardOrientation initialCardSide) throws RemoteException {
        Tuple data = new Tuple(chosenObjective, initialCardSide);
        synchronized (this) {
            client.sendMessage(new Message(MessageType.SET_PLAYER_SETUP, getCredentials(), data));
            Message response = client.receiveMessage();
            if(response.getObj() != null){
                if(response.getObj().getClass().equals(InvalidParameterException.class)) {
                    throw new InvalidParameterException();
                }
            }
        }
    }

    @Override
    public void makeMove(CardInfo card, Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice) throws RemoteException {
        synchronized (this) {
            client.sendMessage(new Message(MessageType.MAKE_MOVE, getCredentials(), new MoveInfo(card, placementPoint, chosenSide, drawChoice)));
            Message response = client.receiveMessage();
            if(response.getObj() != null){
                if(response.getObj().getClass().equals(WrongPhaseException.class)){
                    throw new WrongPhaseException();
                }
            }
        }
    }

    @Override
    public void exitGame() throws InvalidOperationException {
        synchronized (this) {
            client.sendMessage(new Message(MessageType.EXIT_GAME, getCredentials(), null));
            client.receiveMessage();
        }
    }

    @Override
    public boolean isWaitingInLobby() {
        synchronized (this) {
            try{
                return getJoinedLobbyInfo() != null;
            }catch(Exception e){
                return false;
            }
        }
    }

    public void closeSocket() throws IOException {
        client.closeConnection();
    }

    @Override
    public boolean isInGame() throws RemoteException{
        synchronized (this) {
            try {
                getCurrentPlayerName();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
