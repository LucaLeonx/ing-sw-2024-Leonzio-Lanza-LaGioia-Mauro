package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.controller.servercontroller.operationexceptions.ElementNotFoundException;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.WrongPhaseException;
import it.polimi.ingsw.model.Game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GameList {

    private final HashMap<Integer, GameData> gameList;

    public GameList() {
        this.gameList = new HashMap<>();
    }

    public synchronized Set<User> getConnectedUsers(Integer gameId){
        GameData game = gameList.get(gameId);
        return (game == null) ? new HashSet<>() : game.getPlayingUsers();
    }

    public synchronized boolean areAllUsersExited(Integer gameId){
        return gameList.get(gameId)
                .getPlayingUsers()
                .isEmpty();
    }

    public synchronized boolean areAllUsersDisconnected(Integer gameId){
        return gameList.get(gameId)
                .getPlayingUsers().stream()
                .allMatch((user) -> user.getStatus().equals(UserStatus.DISCONNECTED));
    }

    public synchronized void addGameFromLobby(Lobby lobby) {

        GameData newGame = new GameData(lobby.getId(), lobby.getConnectedUsers(), new Game(lobby.getConnectedUserNames()));
        for (User player : newGame.getPlayingUsers()) {
            player.setJoinedGameId(newGame.getId());
            player.setStatus(UserStatus.IN_GAME);
        }

        gameList.put(newGame.getId(), newGame);
    }

    public synchronized void removeGame(Integer gameId){
        gameList.remove(gameId);
    }

    public synchronized GameData getGameData(Integer gameId){
        GameData data = gameList.get(gameId);
        if(data == null){
            throw new ElementNotFoundException("The game is not present");
        }
        return data;
    }

    public synchronized Game getGameById(Integer gameId){
        return getGameData(gameId).getGame();
    }

    public synchronized Game getJoinedGame(User user){
        if(!user.hasJoinedGameId()){
            throw new WrongPhaseException("The user is not in any game");
        }

        return getGameById(user.getJoinedGameId());
    }

    public synchronized void exitFromJoinedGame(User user) {
        if(!user.hasJoinedGameId()){
            throw new WrongPhaseException("The user is not in any game");
        }

        gameList.get(user.getJoinedGameId()).removeUser(user);
        user.removeJoinedGame();
        user.setStatus(UserStatus.LOBBY_CHOICE);
    }
}
