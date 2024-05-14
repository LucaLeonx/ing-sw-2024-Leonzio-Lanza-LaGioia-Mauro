package it.polimi.ingsw.view.tui;

import java.util.Scanner;

public class TUI {
    private TUIState state;
    public TUI(){
        Scanner scanner = new Scanner(System.in);
        setState(new InitialScreen(this, scanner, null));
    }

    public void display() {
        state.display();
    }

    public void setState(TUIState state){
        this.state = state;
        display();
    }
}
