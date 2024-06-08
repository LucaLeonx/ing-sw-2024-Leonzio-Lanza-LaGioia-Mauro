package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class AuthenticatedSession extends UnicastRemoteObject implements ServerController {
    private final CoreServer server;
    private final User user;

    public AuthenticatedSession(User user, CoreServer server) throws RemoteException {
        super();
        this.server = server;
        this.user = user;
    }

    @Override
    public List<LobbyInfo> getLobbies() throws RemoteException {
        return server.getLobbies(user);
    }

    @Override
    public LobbyInfo getJoinedLobbyInfo() throws RemoteException {
        return server.getJoinedLobbyInfo(user);
    }

    @Override
    public LobbyInfo addLobby(String name, int playersNumber) throws RemoteException {
        return server.createLobby(user, name, playersNumber);
    }

    @Override
    public void joinLobby(int lobbyId) throws RemoteException {
        server.joinLobby(user, lobbyId);
    }

    public void exitFromLobby() {
        server.exitFromLobby(user);
    }

    public List<String> getPlayerNames(){
        return server.getPlayerNames(user);
    }
    @Override
    public String getCurrentPlayer() throws RemoteException {
        return server.getCurrentPlayer(user);
    }

    @Override
    public ControlledPlayerInfo getControlledPlayerInfo() throws RemoteException {
        return server.getControlledPlayerInfo(user);
    }

    @Override
    public OpponentInfo getOpponentInfo(String name) throws RemoteException {
        return server.getOpponentInfo(user, name);
    }

    @Override
    public DrawableCardsInfo getDrawableCardsInfo() throws RemoteException {
        return server.getDrawableCardsInfo(user);
    }

    @Override
    public PlayerSetupInfo getPlayerSetup() throws RemoteException {
        return server.getPlayerSetupInfo(user);
    }

    @Override
    public List<ObjectiveInfo> getCommonObjectives() throws RemoteException {
        return server.getCommonObjectives(user);
    }

    @Override
    public boolean isLastTurn() throws RemoteException {
        return server.isLastTurn(user);
    }

    @Override
    public boolean hasGameEnded() throws RemoteException {
        return server.hasGameEnded(user);
    }

    @Override
    public boolean setupDone() throws RemoteException {
        return server.setupDone(user);
    }

    @Override
    public void registerPlayerSetupChoice(int chosenObjectiveId, CardOrientation initialCardOrientation) throws RemoteException {
        server.registerPlayerSetup(user, chosenObjectiveId, initialCardOrientation);
    }

    @Override
    public void registerPlayerMove(int placedCardId, Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice) throws RemoteException {
        server.registerPlayerMove(user, placedCardId, placementPoint, chosenSide, drawChoice);
    }

    @Override
    public void exitFromGame() throws RemoteException {
        server.exitFromGame(user);
    }

    @Override
    public List<ControlledPlayerInfo> getLeaderboard() {
        return server.getLeaderboard(user);
    }

    @Override
    public void logout() {
        server.logout(user);
    }

    @Override
    public String ping() throws RemoteException {
        return server.ping();
    }
}
