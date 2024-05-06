package it.polimi.ingsw.networking;


import it.polimi.ingsw.controller.servercontroller.RMIGameManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RMIServer extends UnicastRemoteObject implements RMIController {
    private List<Lobby> lobbies;

    public RMIServer() throws RemoteException {
        this.lobbies = new ArrayList<>();
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
                return l.getConnectedUser();
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
                    selLobby.getConnectedUser(),
                    selLobby.getRequiredNumOfPlayers(),
                    selLobby.getNumOfWaitingPlayers());
        }
        catch (NullPointerException e) {
            return null;
        }
    }

    public String test() throws RemoteException {
        return "hello";
    }
}
