package it.polimi.ingsw.networking.RMI;


import it.polimi.ingsw.controller.servercontroller.Controller;
import it.polimi.ingsw.controller.servercontroller.Lobby;
import it.polimi.ingsw.dataobject.LobbyInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RMIController extends Remote, Controller {
/*
    public void addLobby(String creator, String name, int playersNumber) throws RemoteException;

    public  void addUserToLobby(int lobbyId, String username) throws RemoteException;

    public List<Lobby> getLobbies() throws RemoteException;

    public List<String> getLobbiesNames() throws RemoteException;

    public List<String> getUsersFromLobby(int lobbyId) throws RemoteException;

    public LobbyInfo getLobbyInfo(int lobbyId) throws RemoteException;
    public String test() throws RemoteException;
TO DELETE*/

}
