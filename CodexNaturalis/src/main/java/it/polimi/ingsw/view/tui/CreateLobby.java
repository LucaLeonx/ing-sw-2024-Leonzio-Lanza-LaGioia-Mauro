package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.clientcontroller.ClientController;

import java.util.Scanner;

public class CreateLobby extends TUIScreen {
    public CreateLobby(TUI tui, Scanner scanner, ClientController controller){
        super(tui, scanner, controller);
    }

    @Override
    public void display() {
        int numberOfPartecipants;
        System.out.print("Choose name of the new lobby: ");
        String lobbyName = scanner.nextLine();
        String stringNumberOfPartecipants;
        do {
            while(true)
            {
                System.out.print("Choose number of the participants (between 2 and 4): ");
                stringNumberOfPartecipants = scanner.nextLine().trim();
                try {
                    numberOfPartecipants = Integer.parseInt(stringNumberOfPartecipants);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("please enter a number");
                }
            }
            if(numberOfPartecipants < 2 || numberOfPartecipants > 4){
                System.out.println("Please, select a number of participants between 2 and 4");
            }
        } while (numberOfPartecipants < 2 || numberOfPartecipants > 4);

        try {
            controller.createLobby(lobbyName, numberOfPartecipants);
            transitionState(new LobbyWaiting(tui, scanner, controller));
        }
         catch (Exception e) {
            System.out.println("The name you entered is already in use, please select another one");
            System.out.println(e.getMessage());
            transitionState(new CreateNewLobbyOrJoinLobby(tui, scanner, controller));
        }

    }

}



