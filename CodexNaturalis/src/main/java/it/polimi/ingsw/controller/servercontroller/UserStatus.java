package it.polimi.ingsw.controller.servercontroller;

import java.io.Serializable;

public enum UserStatus implements Serializable {
    LOBBY_CHOICE,
    WAITING_TO_START,
    IN_GAME,
    DISCONNECTED,
}
