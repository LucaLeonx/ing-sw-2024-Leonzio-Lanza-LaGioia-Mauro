package it.polimi.ingsw.networking;

import it.polimi.ingsw.controller.servercontroller.RMIGameManager;
import it.polimi.ingsw.controller.servercontroller.RMIGameManagerImpl;
import it.polimi.ingsw.model.Game;

import java.rmi.RemoteException;
import java.util.Optional;

public class User {
    private final String username;
    private boolean isConnected;
    private Optional<Integer> tempPassword;

    private Optional<RMIGameManagerImpl> gameManager;

    public User(String nickname){
        this.username = nickname;
        this.isConnected = false;

    }

    public void connectUser(){ this.isConnected = true; }

    public void givePass(Integer pass){ this.tempPassword = Optional.of(pass); }
    public boolean isConnected(){ return this.isConnected; }
    public String getUsername(){ return this.username; }
    public boolean checkPass(Integer pass){
        return tempPassword.orElse(-1).equals(pass);
    }

    public RMIGameManager assignToGame(Game game) throws RemoteException {
        gameManager = Optional.of(new RMIGameManagerImpl(username, game));
        return gameManager.get();
    }
}
