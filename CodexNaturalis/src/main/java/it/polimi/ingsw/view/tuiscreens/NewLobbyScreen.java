package it.polimi.ingsw.view.tuiscreens;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.ElementNotFoundException;
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

    private final Runnable createLobbyExecution = () -> {
        System.out.print("Insert name of the new lobby: ");
        String lobbyName = scanner.nextLine();
        System.out.print("Insert required number of players: ");

        int requiredPlayersNum = Integer.parseInt(scanner.nextLine());

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
        try {
            List<LobbyInfo> lobbyInfos = controller.getLobbyList();
            LobbyInfo chosenLobby = new AssignmentDialog<LobbyInfo>("Choose lobby to join:",
                    generateLobbyDialogChoice(lobbyInfos)).askForChoice(scanner);

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
    }

    @Override
    public void display() {

        AtomicBoolean goBackToScreen = new AtomicBoolean(false);


            while(true){
                try {
                    lobbyOperationChoice.askAndExecuteChoice(scanner);
                    break;
                } catch (CancelChoiceException cancel) {
                    goBackToScreen.set(true);
                    break;
                } catch (InvalidInputException e){
                    continue;
                }
            }

            if(goBackToScreen.get()){
                transitionState(new NewLoginScreen(tui, scanner, controller));
            } else {
                transitionState(new NewLobbyWaitScreen(tui, scanner, controller));
            }
        }

    private List<DialogOption<LobbyInfo>> generateLobbyDialogChoice(List<LobbyInfo> availableLobbies){
        return availableLobbies.stream()
                .map((lobby) -> new DialogOption<LobbyInfo>(lobby.toString(), lobby))
                .toList();
    }
}



