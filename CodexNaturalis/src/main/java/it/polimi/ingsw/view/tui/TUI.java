package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.clientcontroller.ClientNotificationSubscription;
import it.polimi.ingsw.dataobject.LobbyInfo;
import it.polimi.ingsw.view.tuinew.NewInitialScreen;

import java.util.List;
import java.util.Scanner;

public class TUI implements ClientNotificationSubscription {
    private TUIScreen screen;
    public TUI(){
        Scanner scanner = new Scanner(System.in);
        setScreen(new NewInitialScreen(this, scanner, null));
    }

    public void display() {
        screen.display();
    }

    public void setScreen(TUIScreen state){
        this.screen = state;
        display();
    }

    @Override
    public void onLobbyListUpdate(List<LobbyInfo> lobbies) {
        screen.onLobbyListUpdate(lobbies);
    }

    @Override
    public void onJoinedLobbyUpdate(LobbyInfo joinedLobby) {
        screen.onJoinedLobbyUpdate(joinedLobby);
    }

    @Override
    public void onGameStarted() {
        screen.onGameStarted();
    }

    @Override
    public void onSetupPhaseFinished() {
        screen.onSetupPhaseFinished();
    }

    @Override
    public void onCurrentPlayerChange(String newPlayer) {
        screen.onCurrentPlayerChange(newPlayer);
    }

    @Override
    public void onTurnSkipped(String skippedPlayer) {
        screen.onTurnSkipped(skippedPlayer);
    }

    @Override
    public void onLastTurnReached() {
        screen.onLastTurnReached();
    }

    @Override
    public void onGameEnded() {
        screen.onGameEnded();
    }
}
