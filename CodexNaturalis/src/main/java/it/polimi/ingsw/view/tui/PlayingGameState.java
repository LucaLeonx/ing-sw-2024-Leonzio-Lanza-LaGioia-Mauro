package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.clientcontroller.ClientNotificationSubscription;
import it.polimi.ingsw.dataobject.ControlledPlayerInfo;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.player.Player;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class PlayingGameState extends TUIScreen implements ClientNotificationSubscription {
    public PlayingGameState(TUI tui, Scanner scanner, ClientController controller) {
        super(tui, scanner, controller);
    }

    @Override
    public void display() {
        System.out.println("Setup finished, now to the actual game!");
        while(true){
            try {
                if (!controller.hasGameEnded()) break;
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            InitialScreen.printStylishMessage("Congratulation player " + controller.getWinner() + " has won", "\u001B[31m", "\u001B[33m");
            for(ControlledPlayerInfo p: controller.getLeaderboard()){
                System.out.println(p.nickname() + " had " +  p.score() + " points");
            }
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
    }

    public synchronized void onCurrentPlayerChange(String newPlayer) {
        try {
            TUIMethods.showCardsOnTable(controller.getCommonObjectives().get(0), controller.getCommonObjectives().get(1), controller.getDrawableCards());
            if (controller.isLastTurn()) {
                System.out.println("ATTENTION: this is going to be the last turn");
            }
            if (newPlayer == controller.getControlledPlayerInformation().nickname()) {
                //It is the turn of me so I would need to make the choice
                int cardChoice = -1;
                String stringCardChoice;
                int chosenPoint = -1;
                String stringChosenPoint;
                String cardOrientation;
                String stringDrawChoice = "";
                List<String> options = Arrays.asList("rd", "gd", "rc1", "rc2", "gc1", "gc2");
                DrawChoice drawChoice = DrawChoice.DECK_GOLD;
                CardOrientation orientationChosen = CardOrientation.FRONT;

                TUIMethods.drawMap(controller.getControlledPlayerInformation(), controller.getControlledPlayerInformation().field(), true);
                TUIMethods.showHand(controller.getControlledPlayerInformation());
                do {
                    System.out.println("Select the card you want to play (1 for first card etc...), 0 to exit game: ");
                    try {
                        stringCardChoice = scanner.nextLine().trim();
                        cardChoice = Integer.parseInt(stringCardChoice);
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a number");
                    }
                    if (controller.getControlledPlayerInformation().cardsInHand().get(cardChoice - 1) != null) {
                        break;
                    } else if (cardChoice == 0) {
                        controller.exitGame();
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                    }
                } while (true);

                do {
                    System.out.println("Select the orientation of the card to play (f for front, b for back): ");
                    cardOrientation = scanner.nextLine().toLowerCase().trim();
                    if (cardOrientation.equals("f")) {
                        orientationChosen = CardOrientation.FRONT;
                        break;
                    } else if (cardOrientation.equals("b")) {
                        orientationChosen = CardOrientation.BACK;
                        break;
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                    }
                } while (true);

                do {
                    System.out.println("Select the position in the map where you want to place your card: ");
                    try {
                        stringChosenPoint = scanner.nextLine().trim();
                        chosenPoint = Integer.parseInt(stringChosenPoint);
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a number");
                    }
                    if (controller.getControlledPlayerInformation().field().availablePositions().get(chosenPoint) != null) {
                        break;
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                    }
                } while (true);


                do {
                    System.out.println("Select the card you want to draw:  ");
                    System.out.println("Rd: Resource Deck");
                    System.out.println("Gd: Golden Deck");
                    System.out.println("Rc1: Resource Card 1");
                    System.out.println("Rc2: Resource Card 2");
                    System.out.println("Gc1: Golden Card 1");
                    System.out.println("Gc2: Golden Card 1");
                    String choice = scanner.nextLine().trim().toLowerCase(); // Read user input and trim whitespace

                    // Validate user input against the list of options
                    if (options.contains(choice)) {
                        stringDrawChoice = choice;
                        break; // Valid choice, exit the loop
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                    }
                } while (true);
                switch (stringDrawChoice) {
                    case "rd":
                        drawChoice = DrawChoice.DECK_RESOURCE;
                        break;
                    case "gd":
                        drawChoice = DrawChoice.DECK_GOLD;
                        break;
                    case "rc1":
                        drawChoice = DrawChoice.RESOURCE_CARD_1;
                        break;
                    case "rc2":
                        drawChoice = DrawChoice.RESOURCE_CARD_2;
                        break;
                    case "gc1":
                        drawChoice = DrawChoice.GOLD_CARD_1;
                        break;
                    case "gc2":
                        drawChoice = DrawChoice.GOLD_CARD_2;
                        break;
                }

                controller.makeMove(controller.getControlledPlayerInformation().cardsInHand().get(cardChoice - 1), controller.getControlledPlayerInformation().field().availablePositions().get(chosenPoint), orientationChosen, drawChoice);
            }
            else {
                // when it is not my turn I still want to watch my map but without next position hint in order to (maybe) see it in a more clear way.
                TUIMethods.drawMap(controller.getControlledPlayerInformation(), controller.getControlledPlayerInformation().field(), false);
                TUIMethods.showHand(controller.getControlledPlayerInformation());
            }
        }
        catch(RemoteException RE1){
            System.out.println(RE1.getMessage());
        }

    }

    @Override
    public synchronized void onTurnSkipped(String skippedPlayer) {
        System.out.println("Player " + skippedPlayer + " skipped the turn since player is disconnected");

    }

    @Override
    public synchronized void onGameEnded() {
        this.notifyAll();
    }

}
