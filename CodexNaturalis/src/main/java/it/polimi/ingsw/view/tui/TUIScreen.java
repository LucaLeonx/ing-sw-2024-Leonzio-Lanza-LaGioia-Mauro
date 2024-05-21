package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.clientcontroller.ClientNotificationSubscription;
import it.polimi.ingsw.dataobject.LobbyInfo;

import java.util.List;
import java.util.Scanner;

public abstract class TUIScreen {
    protected final TUI tui;
    protected final Scanner scanner;
    protected final ClientController controller;

    public TUIScreen(TUI tui, Scanner scanner, ClientController controller){
        this.tui = tui;
        this.scanner = scanner;
        this.controller = controller;
    }

    public synchronized void transitionState(TUIScreen state){
        tui.setScreen(state);
    }
    public abstract void display();


    public void onLobbyListUpdate(List<LobbyInfo> lobbies) {
        // nothing
    }


    public void onJoinedLobbyUpdate(LobbyInfo joinedLobby) {
        // nothing
    }

    public void onGameStarted() {
        // nothing
    }


    public void onSetupPhaseFinished() {
        // nothing
    }


    public void onCurrentPlayerChange(String newPlayer) {
        // nothing
    }


    public void onTurnSkipped(String skippedPlayer) {

    }


    public void onLastTurnReached() {

    }


    public void onGameEnded() {

    }
}
