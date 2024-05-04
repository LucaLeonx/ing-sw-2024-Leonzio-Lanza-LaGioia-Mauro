package it.polimi.ingsw.networking;

import java.util.List;

public record LobbyInfo(int id,
                        String name,
                        String creator,
                        List<String> players,
                        int reqPlayers,
                        int currNumPlayers){
    @Override
    public String toString() {
        return (String) ("id " ) ;
    }
}

