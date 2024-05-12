package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import java.rmi.RemoteException;
import java.util.List;

public class AuthenticatedSession implements Controller{

    private final FrontierServerLayer enteringServerLayer;
    private final User user;

    public AuthenticatedSession(User user, FrontierServerLayer enteringServerLayer) {
        this.enteringServerLayer = enteringServerLayer;
        this.user = user;
    }

    @Override
    public String getCurrentPlayer() throws RemoteException {
        return enteringServerLayer.getCurrentPlayer(user);
    }

    @Override
    public ControlledPlayerInfo getControlledPlayerInfo() throws RemoteException {
        return enteringServerLayer.getControlledPlayerInfo(user);
    }

    @Override
    public OpponentInfo getOpponentInfo(String name) throws RemoteException {
        return enteringServerLayer.getOpponentInfo(user, name);
    }

    @Override
    public DrawableCardsInfo getDrawableCardsInfo() throws RemoteException {
        return enteringServerLayer.getDrawableCardsInfo(user);
    }

    @Override
    public PlayerSetupInfo getPlayerSetup() throws RemoteException {
        return enteringServerLayer.getPlayerSetupInfo(user);
    }

    @Override
    public boolean isLastTurn() throws RemoteException {
        return enteringServerLayer.isLastTurn(user);
    }

    @Override
    public boolean hasGameEnded() throws RemoteException {
        return enteringServerLayer.hasGameEnded(user);
    }

    @Override
    public void registerPlayerSetupChoice(int chosenObjectiveId, CardOrientation initialCardOrientation) throws RemoteException {
        enteringServerLayer.registerPlayerSetup(user, chosenObjectiveId, initialCardOrientation);
    }

    @Override
    public void registerPlayerMove(int placedCardId, Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice) throws RemoteException {
        enteringServerLayer.registerPlayerMove(user, placedCardId, placementPoint, chosenSide, drawChoice);
    }

    @Override
    public List<ControlledPlayerInfo> getLeaderboard() {
        return enteringServerLayer.getLeaderboard(user);
    }

    @Override
    public LobbyInfo addLobby(String name, int playersNumber) throws RemoteException {
        return enteringServerLayer.createLobby(user, name, playersNumber);
    }

    @Override
    public void joinLobby(int lobbyId) throws RemoteException {
        enteringServerLayer.joinLobby(user, lobbyId);
    }

    @Override
    public List<LobbyInfo> getLobbies() throws RemoteException {
        return enteringServerLayer.getLobbies(user);
    }

    @Override
    public LobbyInfo getJoinedLobbyInfo() throws RemoteException {
        return enteringServerLayer.getJoinedLobbyInfo(user);
    }

    @Override
    public String test() throws RemoteException {
        return "Ciao";
    }

    @Override
    public void logout() {
        enteringServerLayer.logout(user);
    }

    public void exitFromLobby() {
        enteringServerLayer.exitFromLobby(user);
    }

    public List<String> getPlayerNames(){
        return enteringServerLayer.getPlayerNames(user);
    }

    public void exitGame() {
        enteringServerLayer.exitGame(user);
    }
}
