package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;
import java.util.List;

public class TimedServer extends CoreServer{
    public TimedServer(UserList userList, LobbyList lobbyList, GameList activeGames) throws RemoteException {
        super(userList, lobbyList, activeGames);
    }

    @Override
    public int register(String username) throws RemoteException {
        return super.register(username);
    }

    @Override
    public ServerController login(String username, int tempCode) throws RemoteException {
        return super.login(username, tempCode);
    }

    @Override
    public void logout(User user) {
        super.logout(user);
    }

    @Override
    public List<LobbyInfo> getLobbies(User user) {
        return super.getLobbies(user);
    }

    @Override
    public LobbyInfo getJoinedLobbyInfo(User user) {
        return super.getJoinedLobbyInfo(user);
    }

    @Override
    public LobbyInfo createLobby(User creator, String lobbyName, int requiredPlayersNum) {
        return super.createLobby(creator, lobbyName, requiredPlayersNum);
    }

    @Override
    public void joinLobby(User user, int lobbyId) {
        super.joinLobby(user, lobbyId);
    }

    @Override
    public void exitFromLobby(User user) {
        super.exitFromLobby(user);
    }

    @Override
    public List<String> getPlayerNames(User user) {
        return super.getPlayerNames(user);
    }

    @Override
    public String getCurrentPlayer(User user) {
        return super.getCurrentPlayer(user);
    }

    @Override
    public ControlledPlayerInfo getControlledPlayerInfo(User user) {
        return super.getControlledPlayerInfo(user);
    }

    @Override
    public OpponentInfo getOpponentInfo(User user, String opponentName) {
        return super.getOpponentInfo(user, opponentName);
    }

    @Override
    public DrawableCardsInfo getDrawableCardsInfo(User user) {
        return super.getDrawableCardsInfo(user);
    }

    @Override
    public List<ObjectiveInfo> getCommonObjectives(User user) {
        return super.getCommonObjectives(user);
    }

    @Override
    public PlayerSetupInfo getPlayerSetupInfo(User user) {
        return super.getPlayerSetupInfo(user);
    }

    @Override
    public boolean isLastTurn(User user) {
        return super.isLastTurn(user);
    }

    @Override
    public boolean hasGameEnded(User user) {
        return super.hasGameEnded(user);
    }

    @Override
    public void registerPlayerSetup(User user, int objectiveCardId, CardOrientation initialCardSide) {
        super.registerPlayerSetup(user, objectiveCardId, initialCardSide);
    }

    @Override
    public void registerPlayerMove(User user, int placedCardId, Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice) {
        super.registerPlayerMove(user, placedCardId, placementPoint, chosenSide, drawChoice);
    }

    @Override
    public List<ControlledPlayerInfo> getLeaderboard(User user) {
        return super.getLeaderboard(user);
    }

    @Override
    public void exitFromGame(User user) {
        super.exitFromGame(user);
    }

    @Override
    public boolean setupDone(User user) {
        return super.setupDone(user);
    }

    @Override
    public String ping() {
        return super.ping();
    }
}
