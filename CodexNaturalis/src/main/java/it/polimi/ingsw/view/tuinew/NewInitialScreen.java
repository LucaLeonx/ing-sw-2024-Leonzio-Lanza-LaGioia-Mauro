package it.polimi.ingsw.view.tuinew;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.view.tui.TUI;
import it.polimi.ingsw.view.tui.TUIScreen;

import java.util.Scanner;

public class NewInitialScreen extends TUIScreen {

    public NewInitialScreen(TUI tui, Scanner scanner, ClientController controller) {
        super(tui, scanner, controller);
    }

    @Override
    public void display() {
        System.out.println("Welcome to Codex Naturalis \n");
        transitionState(new NewConnectionChoiceScreen(tui, scanner, controller));
    }
}