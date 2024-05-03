package it.polimi.ingsw.networking;

import java.util.ArrayList;
import java.util.List;

public class Lobby {
    private final int id;
    private String name;
    private final String creatorUsername;
    private List<String> waitingPlayers;
    private final int requiredNumOfPlayers;

    public Lobby(int id,String creatorUser,int numOfPlayers,String lobbyName){
        this.id = id;
        this.name = lobbyName;
        this.creatorUsername = creatorUser;
        this.waitingPlayers = new ArrayList<>();
        this.waitingPlayers.add(creatorUser);
        this.requiredNumOfPlayers = numOfPlayers;
    }

    public synchronized void addUser(String username) {
        if(waitingPlayers.size() < requiredNumOfPlayers){
            waitingPlayers.add(username);
        }
    }

    public synchronized void removeUser(String username){
        waitingPlayers.remove(username);
    }

    public boolean readyToStart(){ return waitingPlayers.size() == requiredNumOfPlayers; }

    public List<String> getConnectedUser(){ return new ArrayList<>(waitingPlayers); }

    public int getRequiredNumOfPlayers(){ return this.requiredNumOfPlayers; }

    public int getNumOfWaitingPlayers(){ return this.waitingPlayers.size(); }

    public int getId(){ return this.id; }

    public String getName(){ return this.name; }

    public String getCreatorUsername(){ return this.creatorUsername; }


}
