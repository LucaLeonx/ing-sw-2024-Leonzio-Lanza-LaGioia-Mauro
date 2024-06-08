package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerController extends Remote {

    public List<LobbyInfo> getLobbies() throws RemoteException;
    public LobbyInfo getJoinedLobbyInfo() throws RemoteException;
    public LobbyInfo addLobby(String name, int playersNumber) throws RemoteException;
    public void joinLobby(int lobbyId) throws RemoteException;
    public void exitFromLobby() throws RemoteException;
    public List<String> getPlayerNames() throws RemoteException;
    public String getCurrentPlayer() throws RemoteException;
    public ControlledPlayerInfo getControlledPlayerInfo() throws RemoteException;
    public OpponentInfo getOpponentInfo(String name) throws RemoteException;
    public DrawableCardsInfo getDrawableCardsInfo() throws RemoteException;
    public List<ObjectiveInfo> getCommonObjectives() throws RemoteException;
    public PlayerSetupInfo getPlayerSetup() throws RemoteException;
    public boolean isLastTurn() throws RemoteException;
    public boolean hasGameEnded() throws RemoteException;
    public boolean setupDone() throws RemoteException;
    public List<ControlledPlayerInfo> getLeaderboard() throws RemoteException;
    public void registerPlayerSetupChoice(int chosenObjectiveId, CardOrientation initialCardOrientation) throws RemoteException;
    public void registerPlayerMove(int placedCardId, Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice) throws RemoteException;
    public void exitFromGame() throws RemoteException;
    public void logout() throws RemoteException;
    public String ping() throws RemoteException;
}
