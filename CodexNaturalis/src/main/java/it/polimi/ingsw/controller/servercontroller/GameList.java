package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.model.Game;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameList {

    private final ConcurrentHashMap<Game, List<User>> gameList;

    public GameList() {
        this.gameList = new ConcurrentHashMap<>();
    }

    public List<User> getConnectedUsers(Game game){
        return gameList.get(game);
    }

    public boolean areAllUsersExited(Game game){
        return gameList.get(game).stream().allMatch((user) -> user.getStatus().equals(UserStatus.LOBBY_CHOICE));
    }

    public boolean areAllUsersDisconnected(Game game){
        return gameList.get(game).stream().allMatch((user) -> user.getStatus().equals(UserStatus.LOBBY_CHOICE));
    }

    public void addGame(Game game, List<User> users){
        gameList.put(game, new CopyOnWriteArrayList<>(users));
    }

    public void removeGame(Game game){
        gameList.remove(game);
    }
}
