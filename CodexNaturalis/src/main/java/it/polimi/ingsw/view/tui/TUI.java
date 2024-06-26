package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.ConnectionSettings;
import it.polimi.ingsw.view.tuiscreens.InitialScreen;

import java.util.Scanner;

/**
 * A class to implement pattern state
 */
public class TUI {
    private TUIScreen screen;
    public static ConnectionSettings connectionSettings;

    public TUI() {
        Scanner scanner = new Scanner(System.in);
        setScreen(new InitialScreen(this, scanner, null));
    }

    public void display() {
        screen.display();
    }

    public void setScreen(TUIScreen state) {
        this.screen = state;
        display();
    }
}
