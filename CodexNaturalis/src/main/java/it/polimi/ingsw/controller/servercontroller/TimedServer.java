package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;
import it.polimi.ingsw.model.player.Player;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

public class TimedServer extends CoreServer {

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(2);
    private final Map<User, ScheduledFuture<?>> userTimeouts = new HashMap<>();

    private final TimeoutSettings timeouts;

    public TimedServer(UserList userList, LobbyList lobbyList, GameList activeGames, TimeoutSettings timeouts) throws RemoteException {
        super(userList, lobbyList, activeGames);
        this.timeouts = timeouts;
    }

    @Override
    public int register(String username) throws RemoteException {
        int tempCode = super.register(username);
        User newUser = this.userList.getUserByUsername(username);
        ScheduledFuture<?> timeout = scheduler.schedule(removeUser(newUser, this.userList), timeouts.loginTimeout(), SECONDS);
        userTimeouts.put(newUser, timeout);
        return tempCode;
    }

    @Override
    public ServerController login(String username, int tempCode) throws RemoteException {
        User user = this.userList.getUserByUsername(username);
        if(user != null) {
            userTimeouts.get(user).cancel(false);
        }
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
        if(user.hasJoinedGameId()){
            GameData data = activeGames.getGameData(user.getJoinedGameId());
            for(User player: data.getPlayingUsers()){
                ScheduledFuture<?> timeout = scheduler.schedule(makeUserChooseSetup(player, this), timeouts.setupTimeout(), SECONDS);
                userTimeouts.put(player, timeout);
            }
        }
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
        ScheduledFuture<?> timeout = userTimeouts.get(user);
        if (timeout != null){
            timeout.cancel(false);
        }
        super.registerPlayerSetup(user, objectiveCardId, initialCardSide);
        Game game = activeGames.getJoinedGame(user);
        if(game.allPlayersHaveSetup()){
            User firstPlayer = this.userList.getUserByUsername(game.getCurrentPlayerNickname());
            ScheduledFuture<?> gameTimeout = scheduler.schedule(makeUserSkipTurn(firstPlayer, this.activeGames, this.userTimeouts), timeouts.moveTimeout(), SECONDS);
            userTimeouts.put(firstPlayer, timeout);
        }
    }

    @Override
    public void registerPlayerMove(User user, int placedCardId, Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice) {
        userTimeouts.get(user).cancel(false);
        super.registerPlayerMove(user, placedCardId, placementPoint, chosenSide, drawChoice);
        Game joinedGame = activeGames.getJoinedGame(user);
        if(joinedGame.isEnded()){
            for(Player player : joinedGame.getPlayers()){
                User nextUser = userList.getUserByUsername(player.getNickname());
                ScheduledFuture<?> timeout = scheduler.schedule(makeUserExitGame(nextUser, this), timeouts.endGameTimeout(), SECONDS);
                userTimeouts.put(nextUser, timeout);
            }
        } else {
            User nextUser = userList.getUserByUsername(joinedGame.getCurrentPlayerNickname());
            ScheduledFuture<?> timeout = scheduler.schedule(makeUserSkipTurn(nextUser, this.activeGames, this.userTimeouts), timeouts.moveTimeout(), SECONDS);
            userTimeouts.put(nextUser, timeout);
        }
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

    private Runnable removeUser(User user, UserList userList){
        return new Runnable() {
            @Override
            public void run() {
                userList.removeUser(user);
            }
        };
    }

    private Runnable makeUserChooseSetup(User user, TimedServer timedServer) {
        return new Runnable() {
            @Override
            public void run() {
                PlayerSetupInfo setup = timedServer.getPlayerSetupInfo(user);
                timedServer.registerPlayerSetup(user, setup.objective1().id(), CardOrientation.FRONT);
            }
        };
    }

    private Runnable makeUserSkipTurn(User user, GameList gameList, Map<User, ScheduledFuture<?>> userTimeouts){
        return new Runnable() {
            @Override
            public void run(){
                Game game = gameList.getJoinedGame(user);
                game.skipTurn();
                User nextUser = userList.getUserByUsername(game.getCurrentPlayerNickname());
                ScheduledFuture<?> timeout = scheduler.schedule(makeUserSkipTurn(nextUser, gameList, userTimeouts), timeouts.moveTimeout(), SECONDS);
                userTimeouts.put(nextUser, timeout);
            }
        };
    }

    private Runnable makeUserExitGame(User user, TimedServer server){
        return new Runnable() {
            @Override
            public void run() {
                server.exitFromGame(user);
            }
        };
    }



}

