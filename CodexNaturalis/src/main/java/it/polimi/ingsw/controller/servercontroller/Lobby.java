package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.dataobject.LobbyInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Lobby implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int id;
    private String name;
    private final String creatorUsername;
    private List<User> waitingPlayers;
    private final int requiredNumOfPlayers;

    public Lobby(int id, User creatorUser, int numOfPlayers, String lobbyName){
        this.id = id;
        this.name = lobbyName;
        this.creatorUsername = creatorUser.getUsername();
        this.waitingPlayers = new ArrayList<>();
        this.waitingPlayers.add(creatorUser);
        this.requiredNumOfPlayers = numOfPlayers;
    }

    public synchronized void addUser(User user) {
        if(waitingPlayers.size() < requiredNumOfPlayers){
            waitingPlayers.add(user);
            user.setJoinedLobby(this);
        }
    }

    public synchronized void removeUser(String username){
        User removedUser = waitingPlayers.stream().filter((user) -> user.getUsername().equals(username)).findFirst().get();
        removedUser.setJoinedLobby(null);
        waitingPlayers.remove(removedUser);
    }

    public synchronized boolean readyToStart(){ return waitingPlayers.size() == requiredNumOfPlayers; }

    public synchronized List<User> getConnectedUsers(){
        return waitingPlayers;
    }

    public synchronized List<String> getConnectedUserNames(){
        return waitingPlayers.stream().map(User::getUsername).toList();
    }
    public synchronized int getRequiredNumOfPlayers(){
        return this.requiredNumOfPlayers;
    }

    public synchronized int getNumOfWaitingPlayers(){
        return this.waitingPlayers.size();
    }

    public synchronized int getId(){
        return this.id;
    }

    public synchronized String getName(){
        return this.name;
    }

    public synchronized String getCreatorUsername(){
        return this.creatorUsername;
    }

    public synchronized LobbyInfo getLobbyInfo(){
        return new LobbyInfo(id,
                name,
                creatorUsername,
                new ArrayList<>(getConnectedUserNames()),
                requiredNumOfPlayers,
                waitingPlayers.size());
    }

    @Override
    public synchronized String toString() {
        return "id :"+ id + " Lobby: " + name + " || Created by: " + creatorUsername + " || " + waitingPlayers.size() + "/" + requiredNumOfPlayers + " Players";
    }
}
