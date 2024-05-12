package it.polimi.ingsw.dataobject;

import java.io.Serializable;
import java.util.ArrayList;

public record LobbyInfo (int id,
                        String name,
                        String creator,
                        ArrayList<String> players,
                        int reqPlayers,
                        int currNumPlayers) implements Serializable {
    @Override
    public String toString() {
        return id + " Lobby: " + name + " Created by: " + creator + " " + currNumPlayers + "/" + reqPlayers + "Players";
    }
}
