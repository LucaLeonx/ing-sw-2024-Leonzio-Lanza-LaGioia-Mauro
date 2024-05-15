package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.clientcontroller.GameObserver;
import it.polimi.ingsw.dataobject.InfoTranslator;
import it.polimi.ingsw.dataobject.ObjectiveInfo;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.card.ObjectiveCard;

import java.util.Objects;
import java.util.Scanner;

public class BeginningOfGameScreen extends TUIState implements GameObserver{

    Objects lockHasGameStarted;
    boolean hasGameStarted;
    InfoTranslator infoTranslator;
    public BeginningOfGameScreen(TUI tui, Scanner scanner, ClientController controller) {
        super(tui, scanner, controller);
    }

    @Override
    public void display() {
        synchronized (lockHasGameStarted) {
            InitialScreen.printStylishMessage("THE GAME IS STARTING...                                                            ","\u001B[32m", "\u001B[34m");
            printWolf();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while(!hasGameStarted) {
                try {
                    lockHasGameStarted.wait();
                } catch (InterruptedException IE) {
                    try{
                        controller.exitGame();
                        System.out.println("Sei uscito dal gioco");
                    }
                    catch(Exception e){
                        System.out.println(IE.getMessage());
                    }
                }
            }
        }
        try {
            TUIMethods.showHand(controller.getControlledPlayerInformation());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        try {
            InfoTranslator infoTraslator = null;
            TUIMethods.showCardsOnTable(null, null, controller.getDrawableCards());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        try {
            int choiceObjective;
            int choiceFrontOrBack;
            ObjectiveInfo chosenCard=controller.getPlayerSetup().objective1(); // initialization to a random one because if not compiler complains.
            CardOrientation chosenOrientation=CardOrientation.FRONT;

            while(true) {
                System.out.println("Chose your secret objective card: ");
                TUIMethods.show2Objectives(controller.getPlayerSetup().objective1(), controller.getPlayerSetup().objective2());
                System.out.println("press 1 or 2: ");
                try {
                    choiceObjective = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    if(choiceObjective==1) {
                        chosenCard=controller.getPlayerSetup().objective1();
                        break;
                    }
                    else if(choiceObjective==2){
                        chosenCard=controller.getPlayerSetup().objective2();
                        break;
                    }
                } catch (Exception e) {
                    scanner.nextLine(); // Consume newline
                    System.out.println(e.getMessage());
                }
            }

            while(true) {
                System.out.println("Chose the orientation of your initial card:  ");
                TUIMethods.showInitialCard(controller.getPlayerSetup().initialCard());
                System.out.println("press 1 for Front 2 for Back: ");
                try {
                    choiceFrontOrBack = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    if(choiceFrontOrBack==1) {
                        chosenOrientation=CardOrientation.FRONT;
                        break;
                    }
                    else if(choiceFrontOrBack==2){
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
    public void onGameStarted() {
        synchronized (lockHasGameStarted){
            hasGameStarted=true;
            lockHasGameStarted.notifyAll();
        }
    }

    @Override
    public void onSetupPhaseFinished() {

    }

    @Override
    public void onCurrentPlayerChange(String newPlayer) {

    }

    @Override
    public void onTurnSkipped(String skippedPlayer) {

    }

    @Override
    public void onGameEnded() {

    }
}
