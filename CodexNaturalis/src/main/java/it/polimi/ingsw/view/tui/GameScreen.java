package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.dataobject.ObjectiveInfo;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.card.CardOrientation;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GameScreen extends TUIScreen{

    Object lockHasGameStarted;
    Object lockIsSetUpFinished;
    boolean hasGameStarted;
    boolean isSetUpFinished;
    public GameScreen(TUI tui, Scanner scanner, ClientController controller) {
        super(tui, scanner, controller);
    }

    @Override
    public void display() {
        synchronized (lockHasGameStarted) {
            InitialScreen.printStylishMessage("THE GAME IS STARTING...                                                            ","\u001B[32m", "\u001B[34m");
            printWolf();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while(!hasGameStarted) {
                try {
                    lockHasGameStarted.wait();
                } catch (InterruptedException IE) {
                    try{
                        controller.exitGame();
                        System.out.println("You exited from the game ");
                    }
                    catch(Exception e){
                        System.out.println(IE.getMessage());
                    }
                }
            }
        }
        // setup part:
        try {
            TUIMethods.showHand(controller.getControlledPlayerInformation());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        try {
            TUIMethods.showCardsOnTable(null, null, controller.getDrawableCards());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        try {
            String choiceObjective;
            String choiceFrontOrBack;
            ObjectiveInfo chosenCard=controller.getPlayerSetup().objective1(); // initialization to a random one because if not compiler complains.
            CardOrientation chosenOrientation=CardOrientation.FRONT;

            while(true) {
                System.out.println("Chose your secret objective card: ");
                TUIMethods.show2Objectives(controller.getPlayerSetup().objective1(), controller.getPlayerSetup().objective2());
                System.out.print("select 1st or 2nd objective: ");
                try {
                    choiceObjective=scanner.nextLine().trim();
                    if(choiceObjective.equals("1")) {
                        chosenCard=controller.getPlayerSetup().objective1();
                        break;
                    }
                    else if(choiceObjective.equals("2")){
                        chosenCard=controller.getPlayerSetup().objective2();
                        break;
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            while(true) {
                System.out.println("Chose the orientation of your initial card:  ");
                TUIMethods.showInitialCard(controller.getPlayerSetup().initialCard());
                System.out.print("select 1 for Front 2 for Back: ");
                try {
                    choiceFrontOrBack = scanner.nextLine().trim();
                    if(choiceFrontOrBack.equals("1")) {
                        chosenOrientation=CardOrientation.FRONT;
                        break;
                    }
                    else if(choiceFrontOrBack.equals("2")){
                        chosenOrientation=CardOrientation.BACK;
                        break;
                    }
                } catch (Exception e) {
                    scanner.nextLine(); // Consume newline
                    System.out.println(e.getMessage());
                }
            }
            controller.setPlayerSetup(chosenCard,chosenOrientation);

        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        synchronized (lockIsSetUpFinished){
            while(!isSetUpFinished) {
                try {
                    lockIsSetUpFinished.wait();
                } catch (InterruptedException IE) {
                    try{
                        controller.exitGame();
                        System.out.println("You exited from the game ");
                    }
                    catch(Exception e){
                        System.out.println(IE.getMessage());
                    }
                }
            }
        }

    }

    public static void printWolf(){
        System.out.println("\u001B[34m"+ "                                           ,     ,");
        System.out.println("\u001B[34m"+"                                           |\\---/|" );
        System.out.println("\u001B[34m"+"                                          /  , , |\t\t\t"+ "\u001B[35m" + "(_\\|/_)");
        System.out.println("\u001B[34m"+"                                     __.-'|  / \\ /\t\t\t "+ "\u001B[35m" + "(/|\\) ");
        System.out.println("\u001B[34m"+"                            __ ___.-'        ._O|");
        System.out.println("\u001B[34m"+"                         .-'  '        :      _/");
        System.out.println("\u001B[34m"+"                        / ,    .        .     |");
        System.out.println("\u001B[34m"+"                       :  ;    :        :   _/");
        System.out.println("\u001B[34m"+"                       |  |   .'     __:   /");
        System.out.println("\u001B[34m"+"                       |  :   /'----'| \\  |");
        System.out.println("\u001B[34m"+"                       \\  |\\  |      | /| |");
        System.out.println("\u001B[34m"+"                        '.'| /       || \\ |");
        System.out.println("\u001B[32m"+"_____________\\|/________"+"\u001B[34m"+"| /|.'"+"\u001B[32m"+"_______"+ "\u001B[34m"+ "'.l \\\\_" + "\u001B[32m"+"_______\\|/______________________\\|/____");
        System.out.println("\u001B[32m"+"__\\|/___________________"+ "\u001B[34m"+"|| ||" +"\u001B[32m"+"_____________"+"\u001B[34m"+ "'-'" + "\u001B[32m"+"_______\\|/______________________\\|/____");
        System.out.println("\u001B[32m"+"________________\\|/_____"+ "\u001B[34m" +"'-''-'" + "\u001B[32m"+"_______\\|/_____________\\|/_________________________\\|/");
        //System.out.println("\u001B[32m"+"_______\\|/_____________\\|/_________________________\\|/_________________\\|/__________");
        // Reset colors after printing
        System.out.print("\u001B[0m");
    }

    @Override
    public synchronized void onGameStarted() {
            hasGameStarted=true;
            lockHasGameStarted.notifyAll();
    }

    @Override
    public synchronized void onSetupPhaseFinished() {
        hasGameStarted=true;
        lockIsSetUpFinished.notifyAll();
    }

    @Override
    public void onCurrentPlayerChange(String newPlayer) {
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
    public void onTurnSkipped(String skippedPlayer) {
        System.out.println("Player " + skippedPlayer + " skipped the turn since player is disconnected");

    }

    @Override
    public void onLastTurnReached() {
        return;
    }

    @Override
    public void onGameEnded() {
        try {
            InitialScreen.printStylishMessage("Congratulation player " + controller.getWinner() + " has won", "\u001B[31m", "\u001B[33m");
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
    }


}
