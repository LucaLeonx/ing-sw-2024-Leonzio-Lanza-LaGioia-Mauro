package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.model.Game;

import java.rmi.RemoteException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class User {
    private final String username;

    // TODO: remove optional: we can give it immediately after connection
    // TODO: use proper hashing for passwords (see https://www.baeldung.com/java-password-hashing)
    // We can create a class for password
    private Optional<Integer> tempPassword;
    private final AtomicReference<UserStatus> status;
    private Optional<Lobby> joinedLobby;
    private Optional<Game> joinedGame;

    public User(String nickname){
        this.username = nickname;
        status = new AtomicReference<>(UserStatus.LOBBY_CHOICE);
        joinedLobby = Optional.empty();
        joinedGame = Optional.empty();
    }

    public void givePass(Integer pass){ this.tempPassword = Optional.of(pass); }
    public UserStatus getStatus(){
        return status.get();
    }

    public void setStatus(UserStatus status){
        this.status.set(status);
    }
    public String getUsername(){ return this.username; }
    public boolean checkPass(Integer pass){
        return tempPassword.orElse(-1).equals(pass);
    }

    public synchronized Lobby getJoinedLobby(){
        return joinedLobby.get();
    }

    public synchronized void setJoinedLobby(Lobby joinedLobby){
        this.joinedLobby = Optional.of(joinedLobby);
    }

    public synchronized Game getJoinedGame(){
        return joinedGame.get();
    }

    public synchronized void setJoinedGame(Game joinedGame){
        this.joinedGame = Optional.of(joinedGame);
    }
}
