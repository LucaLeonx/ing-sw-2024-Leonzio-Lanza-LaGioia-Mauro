package it.polimi.ingsw.dataobject;

import java.io.Serializable;

public enum MessageType implements Serializable {
    REGISTER_USER,
    CREATE_LOBBY,
    JOIN_LOBBY,
    TEMP_CODE,
    LOGIN,
    LOBBY_LIST,
    JOINED_LOBBY_INFO,
    EXIT_FROM_LOBBY,
}
