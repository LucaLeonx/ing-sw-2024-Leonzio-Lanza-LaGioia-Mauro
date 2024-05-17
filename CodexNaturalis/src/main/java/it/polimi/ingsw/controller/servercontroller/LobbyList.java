package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.controller.servercontroller.operationexceptions.ElementNotFoundException;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidParameterException;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.WrongPhaseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static it.polimi.ingsw.controller.servercontroller.UserStatus.LOBBY_CHOICE;

public class LobbyList {

    private static final int MAX_AVAILABLE_ID_NUM = 1_000_000;
    private final Map<Integer, Lobby> lobbies;
    private final AtomicInteger idGenerator;

    public LobbyList(Map<Integer, Lobby> lobbies) {
        this.lobbies = lobbies;
        this.idGenerator = new AtomicInteger(lobbies.keySet().stream().max(Integer::compareTo).orElse(0));
    }

    public LobbyList(){
        this(new HashMap<>());
    }

    public synchronized Lobby getLobbyById(Integer lobbyId){
        Lobby lobby = lobbies.get(lobbyId);

        if(lobby == null){
            throw new ElementNotFoundException("The lobby " + lobbyId + " is not available");
        }

        return lobby;
    }

    public synchronized boolean containsLobby(Integer lobbyId){
        return lobbies.containsKey(lobbyId);
    }

    public synchronized List<Lobby> getLobbies(){
        return new ArrayList<>(lobbies.values());
    }

    public synchronized Lobby createLobby(User creator, String lobbyName, int requiredPlayersNum){
        if (requiredPlayersNum < 2 || requiredPlayersNum > 4){
            throw new InvalidParameterException("Invalid number of required players");
        } else if(creator.getStatus() != LOBBY_CHOICE){
            throw new WrongPhaseException("Cannot create Lobby when not choosing one");
        }

        System.out.println("Status before creation: " + creator.getStatus());

        int newId = idGenerator.updateAndGet((value) -> (value + 1) % MAX_AVAILABLE_ID_NUM);
        Lobby createdLobby = new Lobby(newId, creator, requiredPlayersNum, lobbyName);
        createdLobby.addUser(creator);
        System.out.println("Status after creation: " + creator.getStatus());

        lobbies.put(newId, createdLobby);
        return createdLobby;
    }

    public synchronized Lobby removeLobby(int lobbyId){
        return lobbies.remove(lobbyId);
    }
}
