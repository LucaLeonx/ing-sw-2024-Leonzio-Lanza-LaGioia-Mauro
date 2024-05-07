package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.dataobject.LobbyInfo;
import it.polimi.ingsw.dataobject.OpponentInfo;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.InvalidOperationException;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.map.Point;

import java.rmi.RemoteException;
import java.util.List;

public class SetupState extends GameState{

    public SetupState(String controlledPlayer, Game game, ServerController gameManager){
        super(game, controlledPlayer, gameManager);
    }

    @Override
    public void makeCurrentPlayerMove(int cardId, CardOrientation orientation, Point placement, DrawChoice choice) throws InvalidOperationException {
        throw new InvalidOperationException("Cannot perform move during setup phase");
    }

    @Override
    public String getCurrentPlayer() throws RemoteException {
        return null;
    }

    @Override
    public OpponentInfo getOpponentInfo(String name) throws RemoteException {
        return null;
    }

    @Override
    public void registerPlayerSetupChoice(int secretObjectiveId, CardOrientation initialCardOrientation) throws InvalidOperationException{
        super.registerPlayerSetupChoice(secretObjectiveId, initialCardOrientation);
        transition();
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
        return null;
    }

    @Override
    public List<String> getLobbiesNames() throws RemoteException {
        return null;
    }

    @Override
    public List<String> getUsersFromLobby(int lobbyId) throws RemoteException {
        return null;
    }

    @Override
    public LobbyInfo getLobbyInfo(int lobbyId) throws RemoteException {
        return null;
    }

    @Override
    public String test() throws RemoteException {
        return null;
    }

    @Override
    public void skipTurn() throws InvalidOperationException {
        throw new InvalidOperationException("Cannot skip turn during setup phase");
    }

    @Override
    public void transition() {
        gameManager.setState(new PlayState(game, controlledPlayer, gameManager));
    }
}
