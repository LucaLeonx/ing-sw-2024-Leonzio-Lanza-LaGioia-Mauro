package it.polimi.ingsw.view.tuiscreens;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.WrongPhaseException;
import it.polimi.ingsw.view.tui.TUI;
import it.polimi.ingsw.view.tui.TUIMethods;
import it.polimi.ingsw.view.tui.TUIScreen;

import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.concurrent.*;

public class NewLobbyWaitScreen extends TUIScreen {
    public NewLobbyWaitScreen(TUI tui, Scanner scanner, ClientController controller) {
        super(tui, scanner, controller);
    }

    Thread updateThread = new Thread( () -> {
        while(true){
            controller.waitForJoinedLobbyUpdate();
            try {
                System.out.println(controller.getJoinedLobbyInfo());
            } catch (RemoteException e) {
                break;
            }
        }}
    );

    private Future<Boolean> waitForGameStarting;

    Thread checkForUserInput = new Thread( () -> {
        String input = scanner.nextLine().trim().toLowerCase();
        if(input.equals("q")){
            try{
                controller.exitFromLobby();
                waitForGameStarting.cancel(true);
                updateThread.interrupt();
            } catch (WrongPhaseException | RemoteException e){
                System.out.println("Unable to quit from lobby - Game already started");
            }
        }
    });



    @Override
    public void display() {
        TUIMethods.printStylishMessage("WAITING FOR OTHER PLAYER TO JOIN...                                                ","\u001B[32m", "\u001B[34m");
        TUIMethods.printWolf();
        controller.waitForGameToStart();
        transitionState(new NewGameSetupScreen(tui, scanner, controller));
        /*updateThread.start();
        checkForUserInput.start();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        waitForGameStarting = executor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                controller.waitForGameToStart();
                return true;
            }
        });
        try {
            checkForUserInput.join();
            if(waitForGameStarting.isCancelled()){
                transitionState(new NewLobbyScreen(tui, scanner, controller));
            } else {
                transitionState(new NewGameSetupScreen(tui, scanner, controller));
            }
        } catch (InterruptedException e) {
            transitionState(new NewLobbyScreen(tui, scanner, controller));
        }*/




    }
}
