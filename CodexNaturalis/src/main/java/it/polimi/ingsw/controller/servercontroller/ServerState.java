package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;
import java.util.List;

public abstract class ServerState implements Controller{
    private List<Lobby> lobbies;
    private List<User> users;
    private List<Game> games;
    private User propetaryUser;
}
