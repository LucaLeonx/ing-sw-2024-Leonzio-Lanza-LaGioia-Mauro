package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.controller.servercontroller.operationexceptions.ElementNotFoundException;
import it.polimi.ingsw.model.Game;

import java.rmi.RemoteException;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

public class User {

    private static final int MAX_TEMPCODE_VAL = 1_000_000;
    private final String username;
    private Optional<Integer> tempPassword;
    private UserStatus status;
    private Optional<Integer> joinedLobbyId;
    private Optional<Integer> joinedGameId;

    public User(String nickname){
        this.username = nickname;
        status = UserStatus.LOBBY_CHOICE;
        joinedLobbyId = Optional.empty();
        joinedGameId = Optional.empty();
    }

    public int generateNewPass(){
        tempPassword = Optional.of(ThreadLocalRandom.current().nextInt(0, MAX_TEMPCODE_VAL));
        return tempPassword.get();
    }

    public synchronized UserStatus getStatus(){
        return status;
    }

    public synchronized void setStatus(UserStatus status){
        this.status = status;
    }
    public String getUsername(){ return this.username; }
    public synchronized boolean checkPass(Integer pass){
        return tempPassword.orElse(-1).equals(pass);
    }

    public synchronized int getJoinedLobbyId(){
        return joinedLobbyId.orElseThrow(ElementNotFoundException::new);
    }

    public synchronized void setJoinedLobbyId(int joinedLobbyId){
        this.joinedLobbyId = Optional.of(joinedLobbyId);
    }

    public synchronized int getJoinedGameId(){
        return joinedGameId.orElseThrow(ElementNotFoundException::new);
    }

    public synchronized void removeJoinedLobby(){
        this.joinedLobbyId = Optional.empty();
    }

    public synchronized boolean hasJoinedLobbyId() {
        return this.joinedLobbyId.isPresent();
    }

    public synchronized void setJoinedGameId(int joinedGameId){
        this.joinedGameId = Optional.of(joinedGameId);
    }

    public synchronized void removeJoinedGame(){
        this.joinedGameId = Optional.empty();
    }

    public synchronized boolean hasJoinedGameId() {
        return this.joinedGameId.isPresent();
    }
}
