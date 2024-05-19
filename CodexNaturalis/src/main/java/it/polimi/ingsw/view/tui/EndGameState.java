package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.dataobject.ControlledPlayerInfo;

import java.rmi.RemoteException;
import java.util.Scanner;

public class EndGameState extends TUIScreen{
    public EndGameState(TUI tui, Scanner scanner, ClientController controller) {
        super(tui, scanner, controller);
    }

    @Override
    public void display() {
        try {
            InitialScreen.printStylishMessage("Congratulation player " + controller.getWinner() + " has won", "\u001B[31m", "\u001B[33m");
            for(ControlledPlayerInfo p: controller.getLeaderboard()){
                System.out.println(p.nickname() + " had " +  p.score() + " points");
            }
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
    }
}
