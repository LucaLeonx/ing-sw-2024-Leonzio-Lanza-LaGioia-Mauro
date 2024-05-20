package it.polimi.ingsw.view.tui;
//TODO: try to make people exit from LobbyWaiting if needed.
import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.clientcontroller.ClientNotificationSubscription;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.WrongPhaseException;
import it.polimi.ingsw.dataobject.LobbyInfo;

import java.rmi.RemoteException;
import java.util.Scanner;

class LobbyWaiting extends TUIScreen implements ClientNotificationSubscription {
    boolean isGameStarted =false;
    public LobbyWaiting(TUI tui, Scanner scanner, ClientController controller) {
        super(tui, scanner, controller);
    }

    @Override
    public void display() {

        System.out.println("Waiting for players to join...");
        // Asking for input from scanner is already a blocking call
        // No need to create an additional thread
        // System.out.println("Waiting for players to join... Press 'q' to quit and go back to the menu");
        /*while(! controller.isGameStarted()) {
            String input = scanner.nextLine().trim();
            if (input.equals("q")) {
                try {
                    System.out.println("Exiting...");
                    controller.exitFromLobby();
                    transitionState(new JoinLobby(tui, scanner, controller));
                } catch (WrongPhaseException e) {
                    System.out.println("Unable to exit - game already started");
                } catch (RemoteException re) {
                    System.out.println(re.getMessage());
                }
            } else {
                transitionState(this);
            }
        }*/
        /*try {
            while (!controller.isGameStarted()) {
                try {
                    wait(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        transitionState(new SetUpGame(tui,scanner,controller));*/
        /*try {
            while (!controller.isGameStarted()) {
                try {
                    wait(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }*/

        while(!isGameStarted){
            try {
                this.wait();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public synchronized void onJoinedLobbyUpdate(LobbyInfo joinedLobby) {
        System.out.println(joinedLobby);
    }

    @Override
    public synchronized void onGameStarted() {
        System.out.println("Number of players reached - Game starting...");
        transitionState(new SetUpGame(tui, scanner, controller));
        isGameStarted=true;
        this.notifyAll();
    }

}
