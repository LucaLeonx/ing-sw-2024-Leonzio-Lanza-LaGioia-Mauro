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
                while (!ReachedCorrectNumberOfPlayer()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException IE) {
                        try {
                            controller.exitFromLobby();
                        } catch (RemoteException e) {
                            System.out.println(e.getMessage());
                        }
                        transitionState(new CreateNewLobbyOrJoinLobby(tui, scanner, controller));
                    }
                }
                if(ReachedCorrectNumberOfPlayer()){
                    transitionState(new GameScreen(tui, scanner, controller));
                }
            }
        });

        waiterThread.start();

        // Now start a separate thread for handling user input
        Thread userInputThread = new Thread(() -> {
            System.out.println("We will notify you when new players join. Wait or press 'q' to come back...");
            while (true) {
                String userInput = scanner.nextLine();
                if ("q".equalsIgnoreCase(userInput.trim())) {
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


}
