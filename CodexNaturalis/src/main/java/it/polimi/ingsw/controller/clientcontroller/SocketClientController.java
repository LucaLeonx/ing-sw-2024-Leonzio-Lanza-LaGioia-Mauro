package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.controller.servercontroller.ServerController;
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
    private SocketClient client ;

    public SocketClientController() throws IOException {
        this.client = new SocketClient(ConnectionDefaultSettings.SocketServerPort);
        this.client.startClientConnection();
    }

    @Override
    public int register(String username) throws RemoteException {
        Message msg = new Message(MessageType.REGISTER_USER,username);
        client.sendMessage(msg);

        Message receivedMsg = client.receiveMessage();
        if(receivedMsg.getObj() instanceof Integer){
            return (Integer) receivedMsg.getObj();
        }
        else{
            throw new InvalidOperationException("Username " + username + " is already in use");
        }
    }

    @Override
    public void login(String username, int tempCode) throws RemoteException {
        List<Object> tuple = new ArrayList<>();
        tuple.add(username);
        tuple.add(tempCode);
        client.sendMessage( new Message(MessageType.LOGIN,tuple));
    }

    @Override
    public void logout() throws RemoteException {

    }

    @Override
    public List<LobbyInfo> getLobbyList() throws RemoteException {
        client.sendMessage(new Message(MessageType.LOBBY_LIST,new Object()));

        return List.of((LobbyInfo) client.receiveMessage().getObj());
    }

    @Override
    public LobbyInfo createLobby(String lobbyName, int requiredPlayers) throws RemoteException {
        client.sendMessage(new Message(MessageType.CREATE_LOBBY, new AbstractMap.SimpleEntry<>(lobbyName, requiredPlayers) ));

        return (LobbyInfo) client.receiveMessage().getObj();
    }

    @Override
    public void joinLobby(int lobbyId) throws RemoteException {
        client.sendMessage(new Message(MessageType.JOIN_LOBBY,lobbyId));
    }

    @Override
    public LobbyInfo getJoinedLobbyInfo() throws RemoteException {
        client.sendMessage(new Message(MessageType.JOINED_LOBBY_INFO,null));
        return (LobbyInfo) client.receiveMessage().getObj();
    }

    @Override
    public void exitFromLobby() throws RemoteException {
        client.sendMessage(new Message(MessageType.EXIT_FROM_LOBBY,null));
    }

    @Override
    public void subscribeToNotifications(ClientNotificationSubscription observer) {

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

    public void closeSocket() throws IOException {
        client.closeConnection();
    }

    @Override
    public boolean isGameStarted() throws RemoteException{
        return false;
    }
}
