package it.polimi.ingsw.networking;


import it.polimi.ingsw.controller.servercontroller.RMIGameManager;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.rmi.*;

public interface RMIController extends Remote {

    public void addLobby(String creator, String name, int playersNumber) throws RemoteException;

    public  void addUserToLobby(int lobbyId, String username) throws RemoteException;

    public List<Lobby> getLobbies() throws RemoteException;

    public List<String> getLobbiesNames() throws RemoteException;

    public List<String> getUsersFromLobby(int lobbyId) throws RemoteException;

    public LobbyInfo getLobbyInfo(int lobbyId) throws RemoteException;
    public String test() throws RemoteException;
}
