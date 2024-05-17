package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.clientcontroller.ClientNotificationSubscription;
import it.polimi.ingsw.dataobject.LobbyInfo;

import java.util.List;
import java.util.Scanner;

public abstract class TUIScreen implements ClientNotificationSubscription {
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

    @Override
    public void onLobbyListUpdate(List<LobbyInfo> lobbies) {
        // nothing
    }

    @Override
    public void onJoinedLobbyUpdate(LobbyInfo joinedLobby) {
        // nothing
    }

    @Override
    public void onGameStarted() {
        // nothing
    }

    @Override
    public void onSetupPhaseFinished() {
        // nothing
    }

    @Override
    public void onCurrentPlayerChange(String newPlayer) {
        // nothing
    }

    @Override
    public void onTurnSkipped(String skippedPlayer) {

    }

    @Override
    public void onLastTurnReached() {

    }

    @Override
    public void onGameEnded() {

    }
}
