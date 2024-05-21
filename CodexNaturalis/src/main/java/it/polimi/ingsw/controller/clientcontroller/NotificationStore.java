package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.controller.servercontroller.GamePhase;
import it.polimi.ingsw.controller.servercontroller.NotificationSubscriber;
import it.polimi.ingsw.dataobject.LobbyInfo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

public class NotificationStore extends UnicastRemoteObject implements NotificationSubscriber{

    private final Map<String, Integer> notificationsReceived;

    public NotificationStore() throws RemoteException {
        super();
        notificationsReceived = new HashMap<>(Map.ofEntries(
                entry("LobbyList", 0),
                entry("JoinedLobby", 0),
                entry("GameStarted", 0),
                entry("SetupFinished", 0),
                entry("CurrentPlayerChange", 0),
                entry("TurnSkipped", 0),
                entry("LastTurn", 0),
                entry("GameEnded", 0),
                entry("GameAvailable", 0)
        ));
    }

    @Override
    public void onStartedGameAvailable(GamePhase phase) throws RemoteException {
        incrementNotificationCount("GameAvailable");
    }

    @Override
    public synchronized void onLobbyListUpdate() throws RemoteException{
        incrementNotificationCount("LobbyList");
    }

    @Override
    public synchronized void onJoinedLobbyUpdate(LobbyInfo joinedLobby) throws RemoteException {
        incrementNotificationCount("JoinedLobby");
    }

    @Override
    public synchronized void onGameStarted() throws RemoteException{
        incrementNotificationCount("GameStarted");
    }

    @Override
    public synchronized void onSetupPhaseFinished() throws RemoteException {
        incrementNotificationCount("SetupFinished");
    }

    @Override
    public synchronized void onCurrentPlayerChange(String newPlayerName) throws RemoteException {
        incrementNotificationCount("CurrentPlayerChange");
    }

    @Override
    public synchronized void onTurnSkipped(String skippedPlayerName) throws RemoteException {
        incrementNotificationCount("TurnSkipped");
    }

    @Override
    public synchronized void onLastTurnReached() throws RemoteException {
        incrementNotificationCount("LastTurn");
    }

    @Override
    public synchronized void onGameEnded() throws RemoteException {
        incrementNotificationCount("GameEnded");
    }

    public synchronized void incrementNotificationCount(String notificationType) throws RemoteException{
        notificationsReceived.compute(notificationType, (key, value) -> (value == null) ? 0 : value + 1);
    }

    public synchronized void resetNotificationCount(String notificationType){
        notificationsReceived.compute(notificationType, (key, value) -> 0);
        notifyAll();
    }

    public synchronized void resetAll(){
        for(String notificationType : notificationsReceived.keySet()) {
            resetNotificationCount(notificationType);
        }
        notifyAll();
    }

    public synchronized Map<String, Integer> getNotificationsCount() {
        return new HashMap<>(notificationsReceived);
    }

    public synchronized void waitForUpdate() {
        try {
            wait(1000); // Should be enough time to get an update
        } catch (InterruptedException e) {
            return;
        }
    }

    public void waitForMinimumNotificationNumber(String notificationType, int minNotificationNum){
        while(getNotificationsCount().get(notificationType) < minNotificationNum){
            waitForUpdate();
        }
    }

    public void waitForNotificationArrival(String notificationType){
        int oldNotificationType = getNotificationsCount().get(notificationType);
        System.out.println("OldNotificationType: " + oldNotificationType);
        while(getNotificationsCount().get(notificationType) <= oldNotificationType){
            System.out.println("Notification type: " + getNotificationsCount().get(notificationType));
            waitForUpdate();
        }
    }
}

