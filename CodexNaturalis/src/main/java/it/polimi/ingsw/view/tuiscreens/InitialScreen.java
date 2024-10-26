package it.polimi.ingsw.view.tuiscreens;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.view.tui.TUI;
import it.polimi.ingsw.view.tui.TUIMethods;
import it.polimi.ingsw.view.tui.TUIScreen;

import java.util.Scanner;

public class InitialScreen extends TUIScreen {

    public InitialScreen(TUI tui, Scanner scanner, ClientController controller) {
        super(tui, scanner, controller);
    }

    @Override
    public void display() {
        TUIMethods.printStylishMessage("                          WELCOME TO CODEX NATURALIS                            ", "\u001B[32m", "\u001B[31m");
        TUIMethods.printMushroom();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        transitionState(new ConnectionChoiceScreen(tui, scanner, controller));
    }


}