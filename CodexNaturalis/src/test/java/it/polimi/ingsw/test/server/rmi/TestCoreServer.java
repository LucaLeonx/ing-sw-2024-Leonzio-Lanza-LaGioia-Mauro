package it.polimi.ingsw.test.server.rmi;

import it.polimi.ingsw.controller.servercontroller.*;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.*;
import it.polimi.ingsw.dataobject.LobbyInfo;
import junit.framework.TestCase;

import java.lang.invoke.WrongMethodTypeException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static org.junit.Assert.assertThrows;

public class TestCoreServer extends TestCase {

    private CoreServer server;
    private UserList userList;
    private GameList gameList;
    private LobbyList lobbyList;

    public void setUp() {
        userList = new UserList();
        gameList = new GameList();
        lobbyList = new LobbyList();

        try{
            server = new CoreServer(userList, lobbyList, gameList);
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public void test_register_UsernameAlreadyRegistered_BlockRegistration(){

        String repeatedUsername = "Steve";

        Exception exception = assertThrows(InvalidOperationException.class, () -> {
            int tempCode = server.register(repeatedUsername);
            int tempCodeAgain = server.register(repeatedUsername);
        });

        String errorMessage = exception.getMessage();

        assertTrue(errorMessage.contains(repeatedUsername));
    }

    public void test_login_UserRegisteredButNotLoggedYet_SuccessfulLogin(){

        String username = "Steve";
        try {
            int tempCode = server.register(username);
            ServerController session = server.login(username, tempCode, new NotificationRegistrator());
        } catch (Exception e){
            fail(e.getMessage());
        }

        // Test passed
    }

    public void test_login_UserRegisteredWrongTempcode_RejectLogin(){
        String username = "Steve";
        int tempCode = -2;

        try {
            tempCode = server.register(username);
        } catch (Exception e){
            fail(e.getMessage());
        }

        final int finalTempCode = tempCode + 1; // This is required by the assertThrows

        Exception exception = assertThrows(InvalidCredentialsException.class, () -> {
            ServerController session = server.login(username, finalTempCode, new NotificationRegistrator());
        });

        String errorMessage = exception.getMessage();
        assertTrue(errorMessage.contains("Wrong username or temporary code"));
    }

    public void test_login_UserRegisteredWrongUsername_RejectLogin(){
        String username = "Steve";
        int tempCode = -2;

        try {
            tempCode = server.register(username);
        } catch (Exception e){
            fail(e.getMessage());
        }

        final int finalTempCode = tempCode + 1;
        Exception exception = assertThrows(InvalidCredentialsException.class, () -> {
            ServerController session = server.login(username, finalTempCode, new NotificationRegistrator());
        });

        String errorMessage = exception.getMessage();
        assertTrue(errorMessage.contains("Wrong username or temporary code"));
    }

    public void test_login_UserNotRegistered_RejectLogin(){
        Exception exception = assertThrows(InvalidCredentialsException.class, () -> {
            ServerController session = server.login("Tizio", 123456, new NotificationRegistrator());
        });

        String errorMessage = exception.getMessage();
        assertTrue(errorMessage.contains("Wrong username or temporary code"));
    }

    public void test_login_UserRegisteredNullNotificationSubscriber_RejectLogin(){
        String username = "Steve";
        int tempCode = -2;

        try {
            tempCode = server.register(username);
        } catch (Exception e){
            fail(e.getMessage());
        }

        int finalTempCode = tempCode;
        Exception exception = assertThrows(InvalidParameterException.class, () -> {
            ServerController session = server.login(username, finalTempCode, null);
        });

        String errorMessage = exception.getMessage();
        assertTrue(errorMessage.contains("Cannot provide null notification subscriber"));
    }

    public void test_createAndJoinLobby_SuccessfulOperationsAndNotificationSending(){
        Thread creator= new Thread(() ->{
            NotificationRegistrator registrar = new NotificationRegistrator();
            ServerController controller = registerAndLogin("Steve", registrar);
            try {
                controller.addLobby("Example lobby", 3);
            } catch (RemoteException e) {
                fail(e.getMessage());
            }

            Exception creationException = assertThrows(WrongPhaseException.class, () -> {
                controller.addLobby("Another lobby", 2);
            });

            assertTrue(creationException.getMessage().contains("Cannot create Lobby when not choosing one"));
            registrar.waitForUpdate();

            Map<String, Integer> notificationCount = registrar.getNotificationsCount();

            assertEquals(Integer.valueOf(1), notificationCount.get("LobbyList"));
            assertEquals(Integer.valueOf(1), notificationCount.get("JoinedLobby"));
            System.out.println("Creator finished");
        });

        Thread joiner = new Thread(() ->{
            NotificationRegistrator registrar = new NotificationRegistrator();
            ServerController controller = registerAndLogin("Simone", registrar);

            while(true){
                try {
                    controller.joinLobby(1);
                    break;
                } catch (ElementNotFoundException e){
                    continue;
                } catch (RemoteException e) {
                    fail(e.getMessage());
                }
            }

            registrar.waitForUpdate();

            Map<String, Integer> notificationCount = registrar.getNotificationsCount();

            assertEquals(Integer.valueOf(2), notificationCount.get("LobbyList"));
            assertEquals(Integer.valueOf(1), notificationCount.get("JoinedLobby"));

            LobbyInfo joinedLobby = null;

            try {
                joinedLobby = controller.getJoinedLobbyInfo();
            } catch (RemoteException e) {
                fail(e.getMessage());
            }

            assertEquals(joinedLobby.creator(), "Steve");
            assertEquals(joinedLobby.name(), "Example lobby");
            assertEquals(joinedLobby.id(), 1);
            assertEquals(joinedLobby.reqPlayers(), 3);
            assertEquals(joinedLobby.currNumPlayers(), 2);
            assertEquals(joinedLobby.players(), List.of("Steve", "Simone"));
            System.out.println("Joiner finished");
        });

        creator.start();
        joiner.start();

        try{
            creator.join(10000);
            joiner.join(10000);
        } catch (InterruptedException e){
            fail(e.getMessage());
        }

        System.out.println("Test finished");
    }

    public void test_joinLobby_LobbyAlmostFull_SuccessGetGameStartedNotification(){

    }

    private ServerController registerAndLogin(String username){
        try {
            int tempCode = server.register(username);
            return server.login(username, tempCode, new NotificationRegistrator());
        } catch (Exception e){
            fail(e.getMessage());
        }

        return null;
    }



    private ServerController registerAndLogin(String username, NotificationSubscriber subscriber){
        try {
            int tempCode = server.register(username);
            return server.login(username, tempCode, subscriber);
        } catch (Exception e){
            fail(e.getMessage());
        }

        return null;
    }



    private class NotificationRegistrator implements NotificationSubscriber{

        private final Map<String, Integer> notificationsReceived;

        NotificationRegistrator(){
            notificationsReceived = new HashMap<>(Map.ofEntries(
                    entry("LobbyList", 0),
                    entry("JoinedLobby", 0),
                    entry("GameStarted", 0),
                    entry("SetupFinished", 0),
                    entry("CurrentPlayerChange", 0),
                    entry("TurnSkipped", 0),
                    entry("LastTurn", 0),
                    entry("GameEnded", 0)
            ));
        }
        @Override
        public synchronized void onLobbyListUpdate() throws RemoteException {
            incrementNotificationCount("LobbyList");
        }

        @Override
        public synchronized void onJoinedLobbyUpdate(LobbyInfo joinedLobby) throws RemoteException {
            incrementNotificationCount("JoinedLobby");
        }

        @Override
        public synchronized void onGameStarted() throws RemoteException {
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

        private synchronized void incrementNotificationCount(String notificationType){
            notificationsReceived.compute(notificationType, (key, value) -> (value == null) ? 0 : value + 1);
        }

        public synchronized void resetNotificationCount(String notificationType){
            notificationsReceived.compute(notificationType, (key, value) -> 0);
            notifyAll();
        }

        public synchronized void resetAll(){
            for(String notificationType : notificationsReceived.keySet()){
                resetNotificationCount(notificationType);
            }
            notifyAll();
        }

        public synchronized Map<String, Integer> getNotificationsCount(){
            return new HashMap<>(notificationsReceived);
        }

        public synchronized void waitForUpdate() {
            try {
                wait(1000); // Should be enough time to get an update
            } catch (InterruptedException e) {
                return;
            }
        }
    }

}
