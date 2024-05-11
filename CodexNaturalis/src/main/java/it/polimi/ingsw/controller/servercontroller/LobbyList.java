package it.polimi.ingsw.controller.servercontroller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    public Lobby getLobbyById(Integer lobbyId){
        return lobbies.get(lobbyId);
    }

    public List<Lobby> getLobbies(){
        return new ArrayList<>(lobbies.values());
    }

    public Lobby createLobby(User creator, String lobbyName, int requiredPlayersNum){
        int newId = idGenerator.getAndUpdate((value) -> (value + 1) % MAX_AVAILABLE_ID_NUM);
        Lobby createdLobby = new Lobby(newId, creator, requiredPlayersNum, lobbyName);
        lobbies.put(newId, createdLobby);
        return createdLobby;
    }

    public Lobby removeLobby(int lobbyId){
        return lobbies.remove(lobbyId);
    }
}
