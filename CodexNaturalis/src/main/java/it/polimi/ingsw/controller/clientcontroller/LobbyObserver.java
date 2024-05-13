package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.dataobject.LobbyInfo;

import java.util.List;

public interface LobbyObserver {
    public void onLobbyListUpdate(List<LobbyInfo> lobbies);
    public void onJoinedLobbyUpdate(LobbyInfo joinedLobby);
}

