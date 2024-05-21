package it.polimi.ingsw.test.server.rmi;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.servercontroller.*;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.*;
import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;
import it.polimi.ingsw.view.tui.TUIMethods;
import junit.framework.TestCase;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static it.polimi.ingsw.model.card.CardOrientation.BACK;
import static it.polimi.ingsw.model.card.CardOrientation.FRONT;
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
        Thread creator= new Thread(() -> {
            NotificationRegistrator registrar = new NotificationRegistrator();
            ServerController controller = registerAndLogin("Luca", registrar);
            try {
                controller.addLobby("Gaming lobby", 3);
            } catch (RemoteException e) {
                fail(e.getMessage());
            }

            registrar.waitForUpdate();

            Map<String, Integer> notificationCount = registrar.getNotificationsCount();

            assertEquals(Integer.valueOf(2), notificationCount.get("LobbyList"));
            assertEquals(Integer.valueOf(1), notificationCount.get("JoinedLobby"));

            registrar.waitForUpdate();

            notificationCount = registrar.getNotificationsCount();

            assertEquals(Integer.valueOf(1), notificationCount.get("JoinedLobby"));
            assertEquals(Integer.valueOf(1), notificationCount.get("GameStarted"));
        });

        Thread joiner1 = new Thread(() ->{
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
            registrar.waitForUpdate();

            Map<String, Integer> notificationCount = registrar.getNotificationsCount();

            assertEquals(Integer.valueOf(1), notificationCount.get("GameStarted"));
        });

        Thread joiner2 = new Thread(() ->{
            NotificationRegistrator registrar = new NotificationRegistrator();
            ServerController controller = registerAndLogin("Giovanni", registrar);

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

            assertEquals(Integer.valueOf(1), notificationCount.get("GameStarted"));

        });

        creator.start();
        joiner1.start();
        joiner2.start();

        try{
            creator.join(10000);
            joiner1.join(10000);
            joiner2.join(10000);
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }
    }

    public void test_gameSetupPhase(){
        Thread player1 = new Thread(() ->{
            NotificationRegistrator registrar = new NotificationRegistrator();
            ServerController controller = registerAndLogin("Simone", registrar);

            try {
                controller.addLobby("Example lobby", 3);
            } catch (RemoteException e) {
                fail(e.getMessage());
            }

            while(registrar.getNotificationsCount().get("GameStarted") == 0){
                System.out.println("Player 1 - Waiting for game to start");
                registrar.waitForUpdate();
            }

            try {
                PlayerSetupInfo setup = controller.getPlayerSetup();
                controller.registerPlayerSetupChoice(setup.objective1().id(), FRONT);
            } catch (RemoteException e) {
                fail(e.getMessage());
            }

            while(registrar.getNotificationsCount().get("SetupFinished") == 0){
                registrar.waitForUpdate();
            }

            Map<String, Integer> notificationCount = registrar.getNotificationsCount();

            assertEquals(Integer.valueOf(1), notificationCount.get("SetupFinished"));
        });

        Thread player2 = new Thread(() ->{
            NotificationRegistrator registrar = new NotificationRegistrator();
            ServerController controller = registerAndLogin("Giovanni", registrar);

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

            while(registrar.getNotificationsCount().get("GameStarted") == 0){
                System.out.println("Player 1 - Waiting for game to start");
                registrar.waitForUpdate();
            }

            try {
                PlayerSetupInfo setup = controller.getPlayerSetup();
                controller.registerPlayerSetupChoice(setup.objective1().id(), BACK);
            } catch (RemoteException e) {
                fail(e.getMessage());
            }

            while(registrar.getNotificationsCount().get("SetupFinished") == 0){
                registrar.waitForUpdate();
            }

            Map<String, Integer> notificationCount = registrar.getNotificationsCount();

            assertEquals(Integer.valueOf(1), notificationCount.get("SetupFinished"));
        });

        Thread player3 = new Thread(() ->{
            NotificationRegistrator registrar = new NotificationRegistrator();
            ServerController controller = registerAndLogin("Luca", registrar);

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

            while(registrar.getNotificationsCount().get("GameStarted") == 0){
                registrar.waitForUpdate();
            }

            try {
                PlayerSetupInfo setup = controller.getPlayerSetup();
                controller.registerPlayerSetupChoice(setup.objective2().id(), FRONT);
            } catch (RemoteException e) {
                fail(e.getMessage());
            }

            while(registrar.getNotificationsCount().get("SetupFinished") == 0){
                registrar.waitForUpdate();
            }

            Map<String, Integer> notificationCount = registrar.getNotificationsCount();

            assertEquals(Integer.valueOf(1), notificationCount.get("SetupFinished"));
            System.out.println("Player3 finished");
        });

        player1.start();
        player2.start();
        player3.start();

        try {
            player1.join(10000);
            player2.join(10000);
            player3.join(10000);
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }

    }

    public void test_fullGame(){
        Thread player1 = createPlayingThread("Player 1", true, 3, 1);
        Thread player2 = createPlayingThread("Player 2", false, 3, 1);
        Thread player3 = createPlayingThread("Player 3", false, 3, 1);

        player1.start();
        player2.start();
        player3.start();

        try{
            player1.join(60000);
            player2.join(60000);
            player3.join(60000);
            server.getLeaderboard(userList.getUserByUsername("Player 1")).forEach((player) -> System.out.println(player.nickname() + ": " + player.score()));
        } catch (InterruptedException e){
            fail(e.getMessage());
        }
    }

    private Thread createPlayingThread(String username, boolean isCreator, int requiredPlayersNum, int lobbyId){
        return new Thread(() -> {

            NotificationRegistrator registrar = new NotificationRegistrator();
            ServerController controller = registerAndLogin(username, registrar);

            if(isCreator){
                createLobby(controller, "Lobby", requiredPlayersNum);
            } else {
                joinLobbyWhenAvailable(controller, registrar, lobbyId);
            }
            registrar.waitForMinimumNotificationNumber("GameStarted", 1);

            Random rand = new Random();
            int objectiveNum = rand.nextInt(1) + 1;
            CardOrientation initialCardOrientation = (rand.nextInt() == 0) ? FRONT : BACK;

            chooseSetup(controller, objectiveNum, initialCardOrientation);
            registrar.waitForMinimumNotificationNumber("SetupFinished", 1);

            while(true){
                try{
                    if(controller.hasGameEnded()){
                        System.out.println(username + " finished");
                        break;
                    } else if(controller.getCurrentPlayer().equals(username)){
                        playRandomCard(controller);
                        System.out.println("LastTurn: " + controller.isLastTurn());
                        System.out.println("GameEnded: " + controller.hasGameEnded());
                    } else {
                        registrar.waitForUpdate();
                    }
                } catch (RemoteException e){
                    fail(e.getMessage());
                }
            }
        }, username);
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

    private int createLobby(ServerController controller, String lobbyName, int requiredPlayersNum){

        try {
            controller.addLobby(lobbyName, requiredPlayersNum);
        } catch (RemoteException e) {
            fail(e.getMessage());
        }

        try {
            return controller.getJoinedLobbyInfo().id();
        } catch (RemoteException e) {
            fail(e.getMessage());
        }

        return 0;
    }

    private void joinLobbyWhenAvailable(ServerController controller, NotificationRegistrator registrar, int lobbyId){

        List<LobbyInfo> lobbies = null;

        while(true){
            try {
                lobbies = controller.getLobbies();
            } catch (InvalidOperationException | RemoteException e){
                fail(e.getMessage());
            }

            if(lobbies.stream().anyMatch((lobby) -> lobby.id() == lobbyId)) {
                try {
                    controller.joinLobby(1);
                    break;
                } catch (ElementNotFoundException ignored) {
                } catch (RemoteException ex) {
                    fail(ex.getMessage());
                }
            } else {
                registrar.waitForUpdate();
            }
        }
    }

    private void chooseSetup(ServerController controller, int objectiveNumber, CardOrientation initialCardSide){
        try {
            PlayerSetupInfo setup = controller.getPlayerSetup();
            ObjectiveInfo chosenObjective = (objectiveNumber == 1) ? setup.objective1() : setup.objective2();
            controller.registerPlayerSetupChoice(chosenObjective.id(), initialCardSide);
        } catch (RemoteException e) {
            fail(e.getMessage());
        }
    }

    private void playRandomCard(ServerController controller){

        Random rand = new Random();
        ControlledPlayerInfo playerInfo = null;
        DrawableCardsInfo drawableCards = null;

        try {
            playerInfo = controller.getControlledPlayerInfo();
            drawableCards = controller.getDrawableCardsInfo();
        } catch (RemoteException e){
            fail(e.getMessage());
        }
        CardInfo cardToPlay = playerInfo.cardsInHand().get(rand.nextInt(playerInfo.cardsInHand().size()));

        CardOrientation orientation = BACK;

        // An higher probability of playing cards on the front
        // Increases the odds of gaining points when making a move and ending
        // a game faster
        if(rand.nextFloat() < 0.8 && cardToPlay.getSide(FRONT).isPlayable()) {
            orientation = FRONT;
        }

        GameFieldInfo field = playerInfo.field();
        Point positionToPlay = field.availablePositions().get(rand.nextInt(field.availablePositions().size()));
        DrawChoice drawChoice = drawableCards.drawableCards().keySet().stream().findAny().orElse(DrawChoice.RESOURCE_CARD_1);
        // System.out.println(drawChoice);
        // drawableCards.drawableCards().forEach((key, value) -> System.out.println(key + " " + value));
        try {

            controller.registerPlayerMove(cardToPlay.id(), positionToPlay, orientation, drawChoice);
            playerInfo = controller.getControlledPlayerInfo();
            List<ObjectiveInfo> commonObjectives = controller.getCommonObjectives();

            System.out.println("Move of " + playerInfo.nickname() + " Score: + " + playerInfo.score() + "\n");
            TUIMethods.drawMap(playerInfo.color(), playerInfo.field(), true);
            TUIMethods.showHand(playerInfo);
            TUIMethods.showCardsOnTable(commonObjectives.getFirst(), commonObjectives.getLast(), drawableCards);
        } catch (RemoteException e){
            fail(e.getMessage());
        }

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
                    entry("GameEnded", 0),
                    entry("GameAvailable", 0)
            ));
        }

        @Override
        public void onStartedGameAvailable(GamePhase phase) {
            incrementNotificationCount("GameAvailable");
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

        public void waitForMinimumNotificationNumber(String notificationType, int minNotificationNum){
            while(getNotificationsCount().get(notificationType) < minNotificationNum){
                waitForUpdate();
            }
        }
    }

}
