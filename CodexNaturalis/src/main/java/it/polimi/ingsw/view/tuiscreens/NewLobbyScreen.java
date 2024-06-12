package it.polimi.ingsw.view.tuiscreens;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.ElementNotFoundException;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidOperationException;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidParameterException;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.WrongPhaseException;
import it.polimi.ingsw.dataobject.LobbyInfo;
import it.polimi.ingsw.view.tui.TUI;
import it.polimi.ingsw.view.tui.TUIScreen;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;


public class NewLobbyScreen extends TUIScreen {
    AtomicBoolean goBackToScreen = new AtomicBoolean(false);
    AtomicBoolean goBackToThisScreen = new AtomicBoolean(false);

    private final Runnable createLobbyExecution = () -> {
        System.out.print("Insert name of the new lobby: ");
        String lobbyName = scanner.nextLine();
        int requiredPlayersNum=0;
        System.out.print("Insert required number of players: ");
        try {
            requiredPlayersNum = Integer.parseInt(scanner.nextLine());
        }
        catch(NumberFormatException NFE) {
            throw new InvalidInputException("Number of players must be between 2 and 4");
        }
        if(requiredPlayersNum < 2 || requiredPlayersNum > 4){
            throw new InvalidInputException("Number of players must be between 2 and 4");
        }

        try {
            controller.createLobby(lobbyName, requiredPlayersNum);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Lobby created successfully");
    };

    private final Runnable joinLobbyExecution = () -> {
        goBackToThisScreen.set(true);// if CancelException arise this remains set to true and I will come back to choice of create lobby or join one.
        try {
            List<LobbyInfo> lobbyInfos = controller.getLobbyList();
            if(lobbyInfos.isEmpty()){
                System.out.println("No lobby is available. Create a new one");
            }
            LobbyInfo chosenLobby = new AssignmentDialog<LobbyInfo>("Choose lobby to join:",
                    generateLobbyDialogChoice(lobbyInfos)).askForChoice(scanner);
            goBackToThisScreen.set(false); // if cancel exception does not arise instead I set this value to false
            controller.joinLobby(chosenLobby.id());

        } catch (CancelChoiceException e) {
            throw new CancelChoiceException();
        } catch (ElementNotFoundException | InvalidParameterException e){
            System.out.println("Unable to join lobby - It might be already full");
            throw new CancelChoiceException();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    };

    private final ExecutionDialog lobbyOperationChoice = new ExecutionDialog(
            "Choose one of the following options:",
            new DialogOption<>("Join lobby", joinLobbyExecution),
            new DialogOption<>("Create lobby", createLobbyExecution));

    private final Thread availableLobbiesDisplay = new Thread(() -> {
        while(true){
            try {
                System.out.println("Lobbies available");
                List<LobbyInfo> lobbyInfos = controller.getLobbyList();

                if(lobbyInfos.isEmpty()){
                    System.out.println("No lobby is available. Create a new one");
                } else {
                    lobbyInfos.forEach(System.out::println);
                }
                System.out.println();

                controller.waitForLobbyListUpdate();
            } catch (WrongPhaseException e) {
                System.out.println("Unable to fetch available lobbies. Quitting...");
            } catch (RemoteException e) {
                e.printStackTrace();
                break;
            }
        }
    }, "LobbyDisplay");


    public NewLobbyScreen(TUI tui, Scanner scanner, ClientController controller) {
        super(tui, scanner, controller);
        goBackToScreen = new AtomicBoolean(false);
        goBackToThisScreen = new AtomicBoolean(false);
    }

    @Override
    public void display() {

        while (true) {
            try {
                lobbyOperationChoice.askAndExecuteChoice(scanner);
                break;
            } catch (CancelChoiceException cancel) {
                goBackToScreen.set(true);
                break;
            } catch (InvalidInputException e) {
                System.out.println("\n" + e.getMessage() + " try again\n");
                continue;
            }
        }
        if(goBackToThisScreen.get()) // first I check this condition because in this case the other boolean will be true.
        {
            transitionState(new NewLobbyScreen(tui, scanner, controller));
        }
        else if (goBackToScreen.get()) {
            transitionState(new NewLoginScreen(tui, scanner, controller));
        }

        else {
            transitionState(new NewLobbyWaitScreen(tui, scanner, controller));
        }
    }

    private List<DialogOption<LobbyInfo>> generateLobbyDialogChoice(List<LobbyInfo> availableLobbies){
        return availableLobbies.stream()
                .map((lobby) -> new DialogOption<LobbyInfo>(lobby.toString(), lobby))
                .toList();
    }
}


