package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface ClientController {
    // WARNING: The methods may throw unchecked exceptions, if called in the wrong place
    public int register(String username) throws RemoteException;
    public void login(String username, int tempCode) throws RemoteException;
    public void logout();
    public List<LobbyInfo> getLobbyList() throws RemoteException;
    public LobbyInfo createLobby(String lobbyName, int requiredPlayers) throws RemoteException;
    public LobbyInfo getJoinedLobbyInfo() throws RemoteException;
    void joinLobby(int lobbyId) throws RemoteException;
    public void exitFromLobby();
    // public subscribeToLobbyListUpdates(ControllerObserver observer);
    // public subscribeToJoinedLobbyUpdates(ControllerObserver observer);
    // public subscribeToGameUpdates(ControllerObserver observer);
    public List<String> getPlayerNames();
    public PlayerSetupInfo getPlayerSetup() throws RemoteException;
    public ControlledPlayerInfo getControlledPlayerInformation() throws RemoteException;
    public OpponentInfo getOpponentInformation(String opponentName) throws RemoteException;
    /**
     *
     * @return The cards that can be drawn, along with their position. Usually there is
     * an entry for each value in DrawChoice enum. If it is missing, it means that no card
     * can be drawn from that position.
     */
    public Map<DrawChoice, CardSideInfo> getDrawableCards() throws RemoteException;
    public String getCurrentPlayerName() throws RemoteException;
    public boolean isLastTurn() throws RemoteException;
    public boolean hasGameEnded() throws RemoteException;
    public String getWinner();
    public List<ControlledPlayerInfo> getLeaderboard();
    public void setPlayerSetup(ObjectiveInfo chosenObjective, CardOrientation initialCardSide) throws RemoteException;
    public void makeMove(CardInfo card, Point placementPoint, CardOrientation chosenSide, DrawChoice drawchoice) throws RemoteException;
}

