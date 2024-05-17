package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.clientcontroller.ClientController;

import java.util.Scanner;

public class CreateLobby extends TUIScreen {
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
            System.out.println("The name you entered is already in use");
            System.out.println(e.getMessage());
            transitionState(new CreateNewLobbyOrJoinLobby(tui, scanner, controller));
        }
        // if I was able to come here the command create lobby was successful.
        transitionState(new LobbyWaiting(tui, scanner, controller));

    }

}



