package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.clientcontroller.ClientNotificationSubscription;
import it.polimi.ingsw.dataobject.ObjectiveInfo;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.card.CardOrientation;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SetUpGame extends TUIScreen{

    Object lockIsSetUpFinished;
    boolean isSetUpFinished;
    public SetUpGame(TUI tui, Scanner scanner, ClientController controller) {
        super(tui, scanner, controller);
    }

    @Override
    public void display() {
        InitialScreen.printStylishMessage("THE GAME IS STARTING...                                                            ","\u001B[32m", "\u001B[34m");
        printWolf();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // setup part:
        try {
            TUIMethods.showHand(controller.getControlledPlayerInformation());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        try {
            TUIMethods.showCardsOnTable(controller.getCommonObjectives().get(0), controller.getCommonObjectives().get(1), controller.getDrawableCards());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        try {
            String choiceObjective;
            String choiceFrontOrBack;
            ObjectiveInfo chosenCard=controller.getPlayerSetup().objective1(); // initialization to a random one because if not compiler complains.
            CardOrientation chosenOrientation=CardOrientation.FRONT;
            choiceObjective=scanner.nextLine().trim();
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
                    e.printStackTrace();
                    System.out.println(e.getMessage() + " the game ended"); // si blocca qui
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
                    e.printStackTrace();
                    scanner.nextLine(); // Consume newline
                    System.out.println(e.getMessage());
                }
            }
            controller.setPlayerSetup(chosenCard,chosenOrientation);

        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        System.out.println("Please wait for other player to make their move");
        synchronized (lockIsSetUpFinished){
            while(!isSetUpFinished) {
                try {
                    lockIsSetUpFinished.wait();
                } catch (InterruptedException IE) {
                    try{
                        System.out.println("there was a problem in the SetUpGame ");
                    }
                    catch(Exception e){
                        System.out.println(IE.getMessage());
                    }
                }
            }
        }
        transitionState(new PlayingGameState(tui, scanner, controller));
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
    public synchronized void onSetupPhaseFinished() {
        isSetUpFinished=true;
        lockIsSetUpFinished.notifyAll();
    }



}
