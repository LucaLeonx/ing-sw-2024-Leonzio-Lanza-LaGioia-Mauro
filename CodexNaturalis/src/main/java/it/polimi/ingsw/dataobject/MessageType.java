package it.polimi.ingsw.dataobject;

import java.io.Serializable;

public enum MessageType implements Serializable {
    REGISTER_USER,
    CREATE_LOBBY,
    JOIN_LOBBY,
    TEMP_CODE,

}
