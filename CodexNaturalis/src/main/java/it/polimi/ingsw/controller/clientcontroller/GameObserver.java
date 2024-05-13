package it.polimi.ingsw.controller.clientcontroller;

public interface GameObserver {
    public void onGameStarted();
    public void onSetupPhaseFinished();
    public void onCurrentPlayerChange(String newPlayer);
    public void onTurnSkipped(String skippedPlayer);
    public void onGameEnded();
}
