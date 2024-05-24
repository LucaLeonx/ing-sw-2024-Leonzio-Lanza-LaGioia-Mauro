package it.polimi.ingsw.view.tuiscreens;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.dataobject.ControlledPlayerInfo;
import it.polimi.ingsw.view.tui.TUI;
import it.polimi.ingsw.view.tui.TUIMethods;
import it.polimi.ingsw.view.tui.TUIScreen;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

public class NewGameEndState extends TUIScreen {
    public NewGameEndState(TUI tui, Scanner scanner, ClientController controller) {
        super(tui, scanner, controller);
    }

    @Override
    public void display() {
        try {
            List<ControlledPlayerInfo> leaderboard = controller.getLeaderboard();
            TUIMethods.printStylishMessage("Congratulation player " + controller.getWinner() + " has won", "\u001B[31m", "\u001B[33m");
            System.out.println("The game is ended - This is the leaderboard");
            leaderboard.forEach((player) -> System.out.println(player.nickname() + " " + player.score()));
            System.out.println("\n FINAL MAPS: \n");
            for(ControlledPlayerInfo p: leaderboard) {
                System.out.println("The final map of player" + p.nickname());
                TUIMethods.drawMap(p.color(), p.field(), false);
            }
            System.out.println("Press any key to go back to lobby choice");
            String command = scanner.nextLine().trim();
            controller.exitGame();
            transitionState(new NewLobbyScreen(tui, scanner, controller));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


}
