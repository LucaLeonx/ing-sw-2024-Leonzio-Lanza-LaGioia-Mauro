package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.dataobject.LobbyInfo;
import it.polimi.ingsw.dataobject.OpponentInfo;
import it.polimi.ingsw.dataobject.PlayerSetupInfo;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.InvalidOperationException;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import java.rmi.RemoteException;
import java.util.List;

public class PlayState extends GameState {
    public PlayState(Game game, String controlledPlayer, ServerController gameManager) {
        super(game, controlledPlayer, gameManager);
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
    public PlayerSetupInfo getPlayerSetup() throws InvalidOperationException{
        throw new InvalidOperationException("Cannot get player setup choice when playing");
    }

    @Override
    public void registerPlayerSetupChoice(int secretObjectiveId, CardOrientation initialCardOrientation) throws InvalidOperationException {
        throw new InvalidOperationException("Cannot change player setup choice while playing");
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
    public void makeCurrentPlayerMove(int cardId, CardOrientation orientation, Point placement, DrawChoice drawChoice) throws InvalidOperationException{
        if(isLastTurn()){
            super.makeCurrentPlayerMove(cardId, orientation, placement, drawChoice);
            transition();
        } else {
            super.makeCurrentPlayerMove(cardId, orientation, placement, drawChoice);
        }
    }

    @Override
    public void skipTurn() throws InvalidOperationException {
        if(isLastTurn()){
            super.skipTurn();
            transition();
        } else {
            super.skipTurn();
        }
    }

    @Override
    public String getWinnerName(){
        throw new InvalidOperationException("Cannot decide the winner while playing");
    }

    @Override
    public void transition() {
        gameManager.setState(new EndState(game, controlledPlayer, gameManager));
    }
}
