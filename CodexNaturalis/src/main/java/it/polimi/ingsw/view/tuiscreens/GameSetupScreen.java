package it.polimi.ingsw.view.tuiscreens;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidParameterException;
import it.polimi.ingsw.dataobject.ControlledPlayerInfo;
import it.polimi.ingsw.dataobject.ObjectiveInfo;
import it.polimi.ingsw.dataobject.PlayerSetupInfo;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.view.tui.TUI;
import it.polimi.ingsw.view.tui.TUIMethods;
import it.polimi.ingsw.view.tui.TUIScreen;

import java.rmi.RemoteException;
import java.util.Scanner;

public class GameSetupScreen extends TUIScreen {
    public GameSetupScreen(TUI tui, Scanner scanner, ClientController controller) {
        super(tui, scanner, controller);
    }

    @Override
    public void display() {
        System.out.println("\n");
        TUIMethods.printStylishMessage("                              THE GAME IS STARTED                              ","\u001B[33m", "\u001B[31m");

        try {
            System.out.println("Players: " + controller.getPlayerNames());
            PlayerSetupInfo playerSetup = controller.getPlayerSetup();
            ControlledPlayerInfo controlledPlayer = controller.getControlledPlayerInformation();
            TUIMethods.showCardsOnTable(controller.getCommonObjectives().get(0), controller.getCommonObjectives().get(1),controller.getDrawableCards());
            TUIMethods.showHand(controlledPlayer);
            System.out.println("The secret objectives to chose from");
            TUIMethods.show2Objectives(playerSetup.objective1(), playerSetup.objective2());

            ObjectiveInfo chosenObjective = null;
            CardOrientation initialCardOrientation = null;

            boolean confirmed = true;

            do {
                confirmed = true;
                try {
                    chosenObjective = new AssignmentDialog<ObjectiveInfo>(
                            "Choose your secret objective: ",
                            new DialogOption<>("First objective", playerSetup.objective1()),
                            new DialogOption<>("Second objective", playerSetup.objective2()))
                            .askForChoice(scanner);

                    TUIMethods.showInitialCard(playerSetup.initialCard());
                    initialCardOrientation = new AssignmentDialog<CardOrientation>(
                            "Choose initial card orientation:",
                            new DialogOption<>("Front", CardOrientation.FRONT),
                            new DialogOption<>("Back", CardOrientation.BACK
                            )).askForChoice(scanner);
                } catch (CancelChoiceException e){
                    confirmed = false;
                }
            } while(!confirmed);
            try {
                controller.setPlayerSetup(chosenObjective, initialCardOrientation);
            } catch (InvalidParameterException e){
                System.out.println("Setup phase skipped due to inactivity");
            }
            System.out.println("Waiting for other players to finish their setup");
            controller.waitForSetupFinished();
            transitionState(new GamePlayScreen(tui, scanner, controller));

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
