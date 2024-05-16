package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.dataobject.LobbyInfo;
import it.polimi.ingsw.model.Game;

import java.rmi.RemoteException;
import java.util.List;

public interface NotificationSubscriber {
    public void onLobbyListUpdate() throws RemoteException;
    public void onJoinedLobbyUpdate(LobbyInfo joinedLobby);
    public void onSetupPhaseFinished();
    public void onGameStarted();
    public void onCurrentPlayerChange(String newPlayerName);
    public void onTurnSkipped(String skippedPlayerName);
    public void onLastTurnReached();
    public void onGameEnded();
}