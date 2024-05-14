package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.clientcontroller.ClientController;

import java.util.Scanner;

public abstract class TUIState {
    protected final TUI tui;
    protected final Scanner scanner;
    protected final ClientController controller;

    public TUIState(TUI tui, Scanner scanner, ClientController controller){
        this.tui = tui;
        this.scanner = scanner;
        this.controller = controller;
    }

    public void transitionState(TUIState state){
        tui.setState(state);
    }
    public abstract void display();
}
