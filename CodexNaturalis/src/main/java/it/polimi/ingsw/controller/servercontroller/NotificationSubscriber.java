package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.dataobject.LobbyInfo;
import it.polimi.ingsw.model.Game;

import java.util.List;

public interface NotificationSubscriber {
    public void onLobbyListUpdate(List<LobbyInfo> lobbies);
    public void onJoinedLobbyUpdate(LobbyInfo joinedLobby);
    public void onSetupPhaseFinished(Game game);
    public void onCurrentPlayerChange(Game game, String newPlayerName);
    public void onTurnSkipped(Game game, String skippedPlayerName);
    public void onLastTurnReached(Game game);
    public void onGameEnded(Game game);
}