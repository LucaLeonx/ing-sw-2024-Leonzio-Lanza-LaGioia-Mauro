package it.polimi.ingsw.networking.RMI;


import it.polimi.ingsw.controller.servercontroller.Controller;
import it.polimi.ingsw.controller.servercontroller.Lobby;
import it.polimi.ingsw.controller.servercontroller.User;
import it.polimi.ingsw.dataobject.LobbyInfo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
public class RMIServerController extends ServerController implements Controller {
    private List<Lobby> lobbies;
    private List<User> users;

    public RMIServerController() throws RemoteException {
        this.lobbies = new ArrayList<>();
        this.users = new ArrayList<>();
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public void addLobby(String creator, String name, int playersNumber) throws RemoteException {
        Random rn = new Random();
        int randomID = rn.nextInt();
        while (lobbies.stream().map(Lobby::getId).toList().contains(randomID)) {
            randomID = rn.nextInt();
        }
        lobbies.add(new Lobby(randomID, creator, playersNumber, name));
    }

    @Override
    public synchronized void addUserToLobby(int lobbyId, String username) throws RemoteException {
        for (Lobby lobby : lobbies) {
            if (lobby.getId() == lobbyId && lobby.getNumOfWaitingPlayers() < lobby.getRequiredNumOfPlayers()) {
                lobby.addUser(username);
            }
        }
    }

    @Override
    public List<Lobby> getLobbies() throws RemoteException {
        return lobbies;
    }

    @Override
    public List<String> getLobbiesNames() throws RemoteException {
        return lobbies.stream().map(Lobby::getName).toList();
    }

    @Override
    public List<String> getUsersFromLobby(int lobbyId) throws RemoteException {
        for (Lobby l : lobbies) {
            if (l.getId() == lobbyId) {
                return l.getConnectedUsers();
            }
        }
        return null;
    }

    @Override
    public LobbyInfo getLobbyInfo(int lobbyId) throws RemoteException {
        Lobby selLobby = lobbies.stream().filter(lobby -> lobby.getId() == lobbyId).findFirst().orElse(null);
        try {
            return new LobbyInfo(selLobby.getId(),
                    selLobby.getName(),
                    selLobby.getCreatorUsername(),
                    new ArrayList<>(selLobby.getConnectedUsers()),
                    selLobby.getRequiredNumOfPlayers(),
                    selLobby.getNumOfWaitingPlayers());
        }
        catch (NullPointerException e) {
            return null;
        }
    }

}*/
