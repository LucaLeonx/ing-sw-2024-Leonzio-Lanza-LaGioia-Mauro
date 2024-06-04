package it.polimi.ingsw.view.tuiscreens;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.WrongPhaseException;
import it.polimi.ingsw.dataobject.LobbyInfo;
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
    private Thread checkForGameToStart;
    private Future<Boolean> waitForGameStarting;
    private ExecutorService executor;
    private LobbyInfo lobbyInfo;
    private LobbyInfo lobbyInfoUpdated;
    private volatile boolean isRunning;
    private volatile boolean isExited;

    public NewLobbyWaitScreen(TUI tui, Scanner scanner, ClientController controller) {
        super(tui, scanner, controller);
        //this.executor = Executors.newSingleThreadExecutor();
        this.isRunning = true;
        /*try{
            lobbyInfo=controller.getJoinedLobbyInfo();
        }
        catch(RemoteException re){
            re.printStackTrace();
        }*/
    }

    @Override
    public void display() {
        TUIMethods.printStylishMessage("WAITING FOR OTHER PLAYER TO JOIN...                                                ", "\u001B[32m", "\u001B[34m");
        TUIMethods.printWolf();
        //controller.waitForGameToStart();
        //transitionState(new NewGameSetupScreen(tui, new Scanner(System.in) , controller));

        //System.out.print("Lobby current status: ");
        //System.out.println(lobbyInfo);
        //System.out.println("press q to quit lobby: ");


        updateThread = new Thread(() -> {
            while (isRunning && !Thread.currentThread().isInterrupted()) {
                /*
                controller.waitForJoinedLobbyUpdate();
                try {
                    lobbyInfoUpdated=controller.getJoinedLobbyInfo();
                    if(!lobbyInfo.equals(lobbyInfoUpdated)) {
                        if(lobbyInfo.currNumPlayers()<lobbyInfoUpdated.currNumPlayers()) {
                            for(String p: lobbyInfoUpdated.players()){
                                if(!lobbyInfo.players().contains(p))
                                {
                                    System.out.println(p +" joined the lobby");
                                }
                            }
                        }
                        else{
                            for(String p: lobbyInfo.players()){
                                if(!lobbyInfoUpdated.players().contains(p))
                                {
                                    System.out.println(p +" quit the lobby");
                                }
                            }
                        }

                 */
                try {
                    lobbyInfo = controller.getJoinedLobbyInfo();
                    System.out.print("Lobby current status: ");
                    System.out.println(controller.getJoinedLobbyInfo());
                    System.out.println("press q to quit lobby: ");
                }
                catch(RemoteException RE){
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
                            //waitForGameStarting.cancel(true);
                            isRunning = false;
                            updateThread.interrupt();
                            checkForGameToStart.interrupt();
                            transitionState(new NewGameSetupScreen(tui, new Scanner(System.in) , controller));
                        } catch (WrongPhaseException | RemoteException e) {
                            System.out.println("Unable to quit the lobby - Game already started");
                        }
                    }
                }
            }
        });

        checkForGameToStart = new Thread(() -> {
            controller.waitForGameToStart();
            isRunning = false;
            updateThread.interrupt();
            checkForUserInput.interrupt();
            scanner.close();
            executor.shutdownNow();
            transitionState(new NewGameSetupScreen(tui, new Scanner(System.in) , controller));
        });


        updateThread.start();
        checkForUserInput.start();
        checkForGameToStart.start();

    }

}
