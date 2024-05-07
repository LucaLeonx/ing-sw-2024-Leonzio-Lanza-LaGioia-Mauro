package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.dataobject.ControlledPlayerInfo;
import it.polimi.ingsw.dataobject.DrawableCardsInfo;
import it.polimi.ingsw.dataobject.OpponentInfo;
import it.polimi.ingsw.dataobject.PlayerSetupInfo;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;
import it.polimi.ingsw.dataobject.LobbyInfo;

import java.rmi.RemoteException;
import java.util.List;

//Class used by both Socket and RMI
public class ServerController implements Controller {

    private ServerState state;





    @Override
    public String getCurrentPlayer() throws RemoteException {
        return state.getCurrentPlayer();
    }

    @Override
    public ControlledPlayerInfo getControlledPlayerInfo() throws RemoteException {
        return null;
    }

    @Override
    public OpponentInfo getOpponentInfo(String name) throws RemoteException {
        return null;
    }

    @Override
    public DrawableCardsInfo getDrawableCardsInfo() throws RemoteException {
        return null;
    }

    @Override
    public PlayerSetupInfo getPlayerSetup() throws RemoteException {
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
    public void registerPlayerSetupChoice(int chosenObjectiveId, CardOrientation initialCardOrientation) throws RemoteException {

    }

    @Override
    public void registerPlayerMove(int placedCardId, Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice) throws RemoteException {

    }

    @Override
    public void addLobby(String creator, String name, int playersNumber) throws RemoteException {

    }

    @Override
    public void addUserToLobby(int lobbyId, String username) throws RemoteException {

    }

    @Override
    public List<Lobby> getLobbies() throws RemoteException {
        return List.of();
    }

    @Override
    public List<String> getLobbiesNames() throws RemoteException {
        return List.of();
    }

    @Override
    public List<String> getUsersFromLobby(int lobbyId) throws RemoteException {
        return List.of();
    }

    @Override
    public LobbyInfo getLobbyInfo(int lobbyId) throws RemoteException {
        return null;
    }

    @Override
    public String test() throws RemoteException {
        return "";
    }

    public void setState(ServerState state) {
        this.state = state;
    }
}
