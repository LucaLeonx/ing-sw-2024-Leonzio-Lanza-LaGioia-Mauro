package it.polimi.ingsw.dataobject;

import java.io.Serializable;

public enum MessageType implements Serializable {
    REGISTER_USER,
    CREATE_LOBBY,
    JOIN_LOBBY,
    TEMP_CODE,
    LOGIN,
    LOBBY_LIST,
    GET_JOINED_LOBBY_INFO,
    EXIT_FROM_LOBBY,
    LOGOUT,
    GET_CURRENT_PLAYER_NAME,
    GET_PLAYER_NAMES,
    GET_PLAYER_SETUP,
    GET_COMMON_OBJECTIVES,
    GET_CONTROLLED_PLAYER_INFO,
    GET_OPPONENT_INFORMATION,
    GET_DRAWABLE_CARDS,
    IS_LAST_TURN,
    HAS_GAME_ENDED,
    GET_LEADERBOARD,
    MAKE_MOVE,
    EXIT_GAME,
    SET_PLAYER_SETUP,
    ALL_PLAYERS_HAVE_SETUP,
    OK,
}
