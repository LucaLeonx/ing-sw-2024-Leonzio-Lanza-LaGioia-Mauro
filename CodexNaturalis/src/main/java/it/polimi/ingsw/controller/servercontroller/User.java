package it.polimi.ingsw.controller.servercontroller;

import java.util.Optional;

public class User {
    private final String username;
    private boolean isConnected;
    private Optional<Integer> tempPassword;

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


}
