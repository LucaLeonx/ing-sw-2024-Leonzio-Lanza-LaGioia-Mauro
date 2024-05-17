package it.polimi.ingsw.test.server.rmi;

import it.polimi.ingsw.controller.servercontroller.*;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidCredentialsException;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidOperationException;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidParameterException;
import it.polimi.ingsw.dataobject.LobbyInfo;
import junit.framework.TestCase;

import java.rmi.RemoteException;

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
            ServerController session = server.login(username, tempCode, new DummyNotifiable());
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
            ServerController session = server.login(username, finalTempCode, new DummyNotifiable());
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
            ServerController session = server.login(username, finalTempCode, new DummyNotifiable());
        });

        String errorMessage = exception.getMessage();
        assertTrue(errorMessage.contains("Wrong username or temporary code"));
    }

    public void test_login_UserNotRegistered_RejectLogin(){
        Exception exception = assertThrows(InvalidCredentialsException.class, () -> {
            ServerController session = server.login("Tizio", 123456, new DummyNotifiable());
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

    private ServerController registerAndLogin(String username){
        try {
            int tempCode = server.register(username);
            return server.login(username, tempCode, new DummyNotifiable());
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

    // Just to avoid passing null references to the server as subscribers
    private class DummyNotifiable implements NotificationSubscriber{

        @Override
        public void onLobbyListUpdate() throws RemoteException {

        }

        @Override
        public void onJoinedLobbyUpdate(LobbyInfo joinedLobby) throws RemoteException {

        }

        @Override
        public void onSetupPhaseFinished() throws RemoteException {

        }

        @Override
        public void onGameStarted() throws RemoteException {

        }

        @Override
        public void onCurrentPlayerChange(String newPlayerName) throws RemoteException {

        }

        @Override
        public void onTurnSkipped(String skippedPlayerName) throws RemoteException {

        }

        @Override
        public void onLastTurnReached() throws RemoteException {

        }

        @Override
        public void onGameEnded() throws RemoteException {

        }
    }

}
