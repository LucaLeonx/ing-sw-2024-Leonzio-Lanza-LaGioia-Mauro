package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.clientcontroller.LobbyObserver;
import it.polimi.ingsw.dataobject.LobbyInfo;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

public class LobbyWaiting extends TUIState implements LobbyObserver {
    Object lock=new Object();
    public LobbyWaiting(TUI tui, Scanner scanner, ClientController controller) {
        super(tui, scanner, controller);
        controller.subscribeToLobbyUpdates(this);
    }
    @Override
    public void display() {

        Thread waiterThread = new Thread(() -> {
            synchronized (lock) {
                try {
                    while (!ReachedCorrectNumberOfPlayer()) {
                        lock.wait(); // Wait until condition is met or interrupted
                    }
                    // Condition met: transition to game screen
                    transitionState(new GameScreen(tui, scanner, controller));
                } catch (InterruptedException e) {
                    // Thread interrupted, handle clean exit
                    Thread.currentThread().interrupt(); // Reset interrupt status
                    System.out.println("Thread interrupted, returning to menu.");
                    try{
                        controller.exitFromLobby();
                    }
                    catch (RemoteException RE) {
                        // Handle RemoteException from controller.exitFromLobby()
                        System.out.println("Remote exception occurred: " + RE.getMessage());
                        transitionState(new CreateNewLobbyOrJoinLobby(tui, scanner, controller));
                    }
                    transitionState(new CreateNewLobbyOrJoinLobby(tui, scanner, controller));
                }
            }
        });

        waiterThread.start();

        // Thread for handling user input
        Thread userInputThread = new Thread(() -> {
            System.out.println("Waiting for players. Press 'q' to return to menu...");
            while (true) {
                String userInput = scanner.nextLine().trim();
                if ("q".equalsIgnoreCase(userInput)) {
                    // User wants to return to menu, interrupt the waiting thread
                    waiterThread.interrupt();
                    break; // Exit the loop
                }
            }
        });

        userInputThread.start();
    }

    public boolean ReachedCorrectNumberOfPlayer(){
        int desiredNumber;
        int currentNumber;
        try {
            desiredNumber = controller.getJoinedLobbyInfo().reqPlayers();
            currentNumber = controller.getJoinedLobbyInfo().currNumPlayers();
            if(desiredNumber==currentNumber){
                return true;
            }
            else{
                return false;
            }
        }
        catch(RemoteException RE){
            System.out.println("It seems like the lobby you joined doesn't exists anymore");
            System.out.println(RE.getMessage());
            return false;
        }
    }

    @Override
    public void onLobbyListUpdate(List<LobbyInfo> lobbies) {
        //nothing
    }

    @Override
    public void onJoinedLobbyUpdate(LobbyInfo joinedLobby) {
        System.out.println(joinedLobby);
        lock.notifyAll();
    }

    @Override
    public void onGameStarted() {
        // nothing
    }


}
