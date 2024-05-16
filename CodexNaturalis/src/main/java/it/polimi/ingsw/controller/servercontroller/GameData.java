package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.model.Game;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class GameData{

    private final Integer id;
    private final Set<User> playingUsers;
    private final Game game;

    public GameData(Integer id, Collection<User> playingUsers, Game game){
        this.id = id;
        this.playingUsers = new HashSet<>(playingUsers);
        this.game = game;
    }

    public int getId() {
        return this.id;
    }
    public Game getGame(){
        return this.game;
    }

    public String getGameCurrentPlayer(){
        return this.game.getCurrentPlayerNickname();
    }

    public synchronized Set<User> getPlayingUsers() {
        return new HashSet<>(this.playingUsers);
    }

    public synchronized void removeUser(User user){
        playingUsers.remove(user);
    }

}
