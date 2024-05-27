package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidOperationException;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import java.rmi.RemoteException;
import java.util.List;

public interface ClientController {
    // WARNING: The methods may throw unchecked exceptions, if called in the wrong place

    /**
     * Register user to the server
     * @param username
     * @return temporary code to access the user
     * @throws RemoteException If the username is already in use
     */
    public int register(String username) throws RemoteException;

    /**
     * Login to the game as user. WARNING: for now, a user cannot
     * log in from two different terminals at the same time.
     * @param username
     * @param tempCode
     * @throws RemoteException
     */
    public void login(String username, int tempCode) throws RemoteException;
    public void logout() throws RemoteException;

    /**
     *
     * @return
     * @throws RemoteException If the operation is not possible: in particular, if the user is not
     * logged in, it cannot perform any operation related to lobbies.
     */
    public List<LobbyInfo> getLobbyList() throws RemoteException;

    /**
     *
     * @param lobbyName
     * @param requiredPlayers
     * @return The information about the created lobby
     * @throws RemoteException
     */
    public LobbyInfo createLobby(String lobbyName, int requiredPlayers) throws RemoteException;
    void joinLobby(int lobbyId) throws RemoteException;
    /**
     *
     * @return
     * @throws RemoteException If the user is not in a lobby
     */
    public LobbyInfo getJoinedLobbyInfo() throws RemoteException;

    public void exitFromLobby() throws RemoteException;

    public void subscribeToNotifications(ClientNotificationSubscription observer);

    public void waitForLobbyListUpdate();

    public void waitForGameToStart();

    public void waitForJoinedLobbyUpdate();

    public void waitForSetupFinished() throws RemoteException;

    public void waitForTurnChange();

    public void waitForGameEnded();



    public boolean isInGame() throws RemoteException;
    public String getCurrentPlayerName() throws RemoteException;
    public List<String> getPlayerNames() throws RemoteException;
    public PlayerSetupInfo getPlayerSetup() throws RemoteException;
    public List<ObjectiveInfo> getCommonObjectives() throws RemoteException;
    public ControlledPlayerInfo getControlledPlayerInformation() throws RemoteException;
    public OpponentInfo getOpponentInformation(String opponentName) throws RemoteException;
    /**
     * @return The cards that can be drawn, along with their position. Usually there is
     * an entry for each value in DrawChoice enum. If it is missing, it means that no card
     * can be drawn from that position.
     */
    public DrawableCardsInfo getDrawableCards() throws RemoteException;
    public boolean isLastTurn() throws RemoteException;
    public boolean hasGameEnded() throws RemoteException;
    public String getWinner() throws RemoteException;
    public List<ControlledPlayerInfo> getLeaderboard() throws RemoteException;
    public void setPlayerSetup(ObjectiveInfo chosenObjective, CardOrientation initialCardSide) throws RemoteException;
    public void makeMove(CardInfo card, Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice) throws RemoteException;
    public void exitGame() throws InvalidOperationException, RemoteException;

    boolean isWaitingInLobby();
}

