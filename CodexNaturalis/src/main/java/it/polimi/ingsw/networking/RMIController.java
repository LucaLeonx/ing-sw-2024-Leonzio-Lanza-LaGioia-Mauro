package it.polimi.ingsw.networking;

import java.rmi.Remote;
import java.util.List;

public interface RMIController extends Remote {

    public void addLobby(String creator, String name, int playersNumber);

    public void addUserToLobby(int lobbyId,String username);

    public List<Lobby> getLobbies();

    public List<String> getLobbiesNames();

    public List<String> getUsersFromLobby(int lobbyId);

}
