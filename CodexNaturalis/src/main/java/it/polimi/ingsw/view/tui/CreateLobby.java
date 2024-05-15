package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.clientcontroller.LobbyObserver;
import it.polimi.ingsw.dataobject.LobbyInfo;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

public class CreateLobby extends TUIState implements LobbyObserver {
    Object lock=new Object();
    public CreateLobby(TUI tui, Scanner scanner, ClientController controller){
        super(tui, scanner, controller);
    }

    @Override
    public void display() {
        int numberOfPartecipants = 0;
        System.out.print("Choose name of the new lobby: ");
        String lobbyName = scanner.nextLine();
        do {
            System.out.print("Choose number of the participants (between 2 and 4): ");{
            try {
                numberOfPartecipants = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            }
            catch(Exception e) {
                System.out.println(e.getMessage());
                transitionState(new CreateNewLobbyOrJoinLobby(tui, scanner, controller));
            }

            }
            if(numberOfPartecipants < 2 || numberOfPartecipants > 4){
                System.out.println("Please, select a number of participants between 2 and 4");
            }
        } while (numberOfPartecipants < 2 || numberOfPartecipants > 4);
        try {
            controller.createLobby(lobbyName, numberOfPartecipants);
        }
         catch (Exception e) {
            System.out.println(e.getMessage());
            transitionState(new CreateNewLobbyOrJoinLobby(tui, scanner, controller));
        }
        // if I was able to come here the command create lobby was successful.

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



