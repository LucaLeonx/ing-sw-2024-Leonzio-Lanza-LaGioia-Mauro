package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.clientcontroller.ClientNotificationSubscription;
import it.polimi.ingsw.dataobject.LobbyInfo;
import it.polimi.ingsw.view.tuiscreens.NewInitialScreen;

import java.util.List;
import java.util.Scanner;

public class TUI {
    private TUIScreen screen;
    public TUI(){
        Scanner scanner = new Scanner(System.in);
        setScreen(new NewInitialScreen(this, scanner, null));
    }

    public void display() {
        screen.display();
    }

    public void setScreen(TUIScreen state) {
        this.screen = state;
        display();
    }
}
