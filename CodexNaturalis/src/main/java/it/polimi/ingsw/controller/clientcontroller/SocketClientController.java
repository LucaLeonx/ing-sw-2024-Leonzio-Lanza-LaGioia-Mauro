package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.controller.servercontroller.ServerController;
import it.polimi.ingsw.controller.socket.SocketClient;
import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.InvalidOperationException;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import java.rmi.RemoteException;
import java.util.List;

public class SocketClientController implements ClientController {

    private ServerController session = null;
    private SocketClient client ;

    public SocketClientController(){
        this.client = new SocketClient(ConnectionDefaultSettings.SocketServerPort);
    }

    @Override
    public int register(String username) throws RemoteException {
        return 0;
    }

    @Override
    public void login(String username, int tempCode) throws RemoteException {

    }

    @Override
    public void logout() throws RemoteException {

    }

    @Override
    public List<LobbyInfo> getLobbyList() throws RemoteException {
        return List.of();
    }

    @Override
    public LobbyInfo createLobby(String lobbyName, int requiredPlayers) throws RemoteException {
        return null;
    }

    @Override
    public void joinLobby(int lobbyId) throws RemoteException {

    }

    @Override
    public LobbyInfo getJoinedLobbyInfo() throws RemoteException {
        return null;
    }

    @Override
    public void exitFromLobby() throws RemoteException {

    }

    @Override
    public void subscribeToLobbyUpdates(LobbyObserver observer) {

    }

    @Override
    public void subscribeToGameUpdates(GameObserver observer) {

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
}
