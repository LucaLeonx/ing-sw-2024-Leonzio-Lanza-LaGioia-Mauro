package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.controller.servercontroller.operationexceptions.ElementNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class LobbyList {

    private static final int MAX_AVAILABLE_ID_NUM = 1_000_000;
    private final ConcurrentMap<Integer, Lobby> lobbies;
    private final AtomicInteger idGenerator;

    public LobbyList(ConcurrentMap<Integer, Lobby> lobbies) {
        this.lobbies = lobbies;
        this.idGenerator = new AtomicInteger(lobbies.keySet().stream().max(Integer::compareTo).orElse(0));
    }

    public LobbyList(){
        this(new ConcurrentHashMap<>());
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
        int newId = idGenerator.updateAndGet((value) -> (value + 1) % MAX_AVAILABLE_ID_NUM);
        Lobby createdLobby = new Lobby(newId, creator, requiredPlayersNum, lobbyName);
        lobbies.put(newId, createdLobby);
        return createdLobby;
    }

    public synchronized Lobby removeLobby(int lobbyId){
        return lobbies.remove(lobbyId);
    }
}
