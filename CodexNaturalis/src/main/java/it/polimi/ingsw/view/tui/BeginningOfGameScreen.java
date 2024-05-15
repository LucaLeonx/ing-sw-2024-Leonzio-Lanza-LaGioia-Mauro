package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.clientcontroller.GameObserver;
import it.polimi.ingsw.dataobject.InfoTranslator;

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
            //TUIMethods.showCardsOnTable(null, null, infoTraslator.convertToDrawableCardsInfo(controller.getDrawableCards()));
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        try {
            System.out.println("Chose your secret objective card: ");
            //TUIMethods.show2ObjectivesCards(controller.getPlayerSetup().objective1(), controller.getPlayerSetup().objective2());
            System.out.println("press 1 or 2: ");
            int choiceObjective;
            while(true) {
                try {
                    choiceObjective = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    if(choiceObjective==1 || choiceObjective==2) {
                        break;
                    }
                } catch (Exception e) {
                    scanner.nextLine(); // Consume newline
                    System.out.println(e.getMessage());
                }
            }

        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        System.out.println(" Chose your initial objective card");
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
