package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.dataobject.LobbyInfo;
import it.polimi.ingsw.model.Game;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface NotificationSubscriber extends Remote {

    // public void onStartedGameAvailable(String gamePhase)
    public void onLobbyListUpdate() throws RemoteException;
    public void onJoinedLobbyUpdate(LobbyInfo joinedLobby) throws RemoteException;
    public void onSetupPhaseFinished() throws RemoteException;
    public void onGameStarted() throws RemoteException;
    public void onCurrentPlayerChange(String newPlayerName) throws RemoteException;
    public void onTurnSkipped(String skippedPlayerName) throws RemoteException;
    public void onLastTurnReached() throws RemoteException;
    public void onGameEnded() throws RemoteException;
}