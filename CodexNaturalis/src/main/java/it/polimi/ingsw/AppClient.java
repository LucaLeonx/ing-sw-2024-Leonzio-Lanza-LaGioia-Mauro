package it.polimi.ingsw;

import it.polimi.ingsw.controller.ConnectionSettings;
import it.polimi.ingsw.view.tui.TUI;

public class AppClient {

    public static void main(String[] args){
        TUI.connectionSettings = ConnectionSettings.parseConnectionSettings(args);
        TUI tui = new TUI();
        tui.display();
    }
}


