package it.polimi.ingsw.view.tuinew;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.dataobject.ControlledPlayerInfo;
import it.polimi.ingsw.view.tui.TUI;
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
            System.out.println("The game is ended - This is the leaderboard");
            leaderboard.forEach((player) -> System.out.println(player.nickname() + " " + player.score()));
            System.out.println("Press any key to go back to lobby choice");
            String command = scanner.nextLine().trim();
            controller.exitGame();
            transitionState(new NewLobbyScreen(tui, scanner, controller));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
