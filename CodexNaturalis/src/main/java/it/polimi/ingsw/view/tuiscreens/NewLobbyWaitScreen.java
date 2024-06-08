package it.polimi.ingsw.view.tuiscreens;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.WrongPhaseException;
import it.polimi.ingsw.dataobject.LobbyInfo;
import it.polimi.ingsw.view.tui.TUI;
import it.polimi.ingsw.view.tui.TUIMethods;
import it.polimi.ingsw.view.tui.TUIScreen;

import java.io.FilterInputStream;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.concurrent.*;




public class NewLobbyWaitScreen extends TUIScreen{
    private Future <Boolean> waitForGameStarting;
    private ExecutorService executor;
    private volatile boolean isRunning;
    Scanner sc;

    public NewLobbyWaitScreen (TUI tui, Scanner scanner ,ClientController controller){
        super(tui, scanner, controller);
        this.executor=Executors.newFixedThreadPool(3);
        this.isRunning=true;
        sc = new Scanner(new FilterInputStream(System.in){public void close(){}});
    }

    @Override
            public void display(){
        TUIMethods.printStylishMessage("WAITING FOR OTHER PLAYER TO JOIN...                                                ","\u001B[32m","\u001B[34m");
        TUIMethods.printWolf();
        System.out.println("press q to quit:");

        executor.submit(()->{
            while(isRunning){
                controller.waitForJoinedLobbyUpdate();
                try{
                    System.out.println("A new player joined the lobby or a player left your lobby,new lobby status:");
                    System.out.println(controller.getJoinedLobbyInfo());
                }catch(RemoteException e){
                    break;
                }
            }
        });

        executor.submit(()->{

            while(isRunning){
            //if (sc.hasNextLine()) {
                String input = sc.nextLine().trim().toLowerCase();
                if (input.equals("q")) {
                    try {
                        controller.exitFromLobby();
                        waitForGameStarting.cancel(true);
                        isRunning = false;
                    } catch (WrongPhaseException | RemoteException e) {
                        System.out.println("Unable to quit from lobby-Game already started");
                    }
                }
            //}
            }
        });

        waitForGameStarting=executor.submit(()->{
            controller.waitForGameToStart();
            return true;
        });

        try{
            boolean gameStarted=waitForGameStarting.get();
            if(gameStarted){
                isRunning=false;
                System.out.println("Correct number of player reached, press enter to continue: ");
                sc.close();
                executor.shutdownNow();
                transitionState(new NewGameSetupScreen(tui,scanner,controller));
            }else{
                isRunning=false;
                sc.close();
                executor.shutdownNow();
                transitionState(new NewLobbyScreen(tui,scanner,controller));
            }
        }catch(CancellationException e){
            isRunning=false;
            sc.close();
            executor.shutdownNow();
            System.out.println("You exited from the lobby");
            transitionState(new NewLobbyScreen(tui,scanner,controller));
        }catch(InterruptedException|ExecutionException e){
            isRunning=false;
            sc.close();
            executor.shutdownNow();
            transitionState(new NewLobbyScreen(tui,scanner,controller));
        }
    }
}