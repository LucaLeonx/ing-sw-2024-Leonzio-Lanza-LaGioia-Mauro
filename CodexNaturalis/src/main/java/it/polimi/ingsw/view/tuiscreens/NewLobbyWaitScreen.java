package it.polimi.ingsw.view.tuiscreens;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.WrongPhaseException;
import it.polimi.ingsw.view.tui.TUI;
import it.polimi.ingsw.view.tui.TUIMethods;
import it.polimi.ingsw.view.tui.TUIScreen;

import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.concurrent.*;

import java.util.Scanner;
import java.util.concurrent.*;

import java.util.Scanner;
import java.util.concurrent.*;

import java.util.Scanner;
import java.util.concurrent.*;

public class NewLobbyWaitScreen extends TUIScreen {
    private Thread updateThread;
    private Thread checkForUserInput;
    private Future<Boolean> waitForGameStarting;
    private ExecutorService executor;
    private volatile boolean isRunning;

    public NewLobbyWaitScreen(TUI tui, Scanner scanner, ClientController controller) {
        super(tui, scanner, controller);
        this.executor = Executors.newSingleThreadExecutor();
        this.isRunning = true;
    }

    @Override
    public void display() {
        TUIMethods.printStylishMessage("WAITING FOR OTHER PLAYER TO JOIN...                                                ", "\u001B[32m", "\u001B[34m");
        TUIMethods.printWolf();
        System.out.println("press q to quit: ");

        updateThread = new Thread(() -> {
            while (isRunning && !Thread.currentThread().isInterrupted()) {
                controller.waitForJoinedLobbyUpdate();
                try {
                    System.out.println("A new player joined the lobby or a player left your lobby, new lobby status: ");
                    System.out.println(controller.getJoinedLobbyInfo());
                } catch (RemoteException e) {
                    break;
                }
            }
        });

        checkForUserInput = new Thread(() -> {
            while (isRunning && !Thread.currentThread().isInterrupted()) {
                if (scanner.hasNextLine()) {
                    String input = scanner.nextLine().trim().toLowerCase();
                    if (input.equals("q")) {
                        try {
                            controller.exitFromLobby();
                            waitForGameStarting.cancel(true);
                            isRunning = false;
                            updateThread.interrupt();
                        } catch (WrongPhaseException | RemoteException e) {
                            System.out.println("Unable to quit from lobby - Game already started");
                        }
                    }
                }
            }
        });

        updateThread.start();
        checkForUserInput.start();

        waitForGameStarting = executor.submit(() -> {
            controller.waitForGameToStart();
            return true;
        });

        try {
            boolean gameStarted = waitForGameStarting.get();
            if (gameStarted) {
                transitionState(new NewGameSetupScreen(tui, new Scanner(System.in) , controller));
            } else {
                transitionState(new NewLobbyScreen(tui, scanner, controller));
            }
        } catch (CancellationException e) {
            System.out.println("The game start waiting task was cancelled.");
            transitionState(new NewLobbyScreen(tui, scanner, controller));
        } catch (InterruptedException | ExecutionException e) {
            transitionState(new NewLobbyScreen(tui, scanner, controller));
        } finally {
            isRunning = false;
            updateThread.interrupt();
            checkForUserInput.interrupt();
            executor.shutdownNow();
        }
    }
}
