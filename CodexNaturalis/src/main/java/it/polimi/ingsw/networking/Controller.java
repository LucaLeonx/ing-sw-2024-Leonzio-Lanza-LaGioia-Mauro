package it.polimi.ingsw.networking;

import it.polimi.ingsw.controller.servercontroller.Lobby;
import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import java.rmi.RemoteException;
import java.util.List;

public interface Controller {
    public void addLobby(String creator, String name, int playersNumber) throws RemoteException;

    public  void addUserToLobby(int lobbyId, String username) throws RemoteException;

    public List<Lobby> getLobbies() throws RemoteException;

    public List<String> getLobbiesNames() throws RemoteException;

    public List<String> getUsersFromLobby(int lobbyId) throws RemoteException;
    public LobbyInfo getLobbyInfo(int lobbyId) throws RemoteException;
    public String getCurrentPlayer() throws RemoteException;
    public ControlledPlayerInfo getControlledPlayerInfo() throws RemoteException;
    public OpponentInfo getOpponentInfo(String name) throws RemoteException;
    public DrawableCardsInfo getDrawableCardsInfo() throws RemoteException;
    public PlayerSetupInfo getPlayerSetup() throws RemoteException;
    public boolean isLastTurn() throws RemoteException;
    public boolean hasGameEnded() throws RemoteException;
    public void registerPlayerSetupChoice(int chosenObjectiveId, CardOrientation initialCardOrientation) throws RemoteException;
    public void registerPlayerMove(int placedCardId, Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice) throws RemoteException;
    public String getWinnerName();
    public String test() throws RemoteException;
}
