package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.controller.servercontroller.GamePhase;
import it.polimi.ingsw.dataobject.LobbyInfo;

import java.util.List;

public interface ClientNotificationSubscription {

    default public void onStartedGameAvailable(GamePhase phase){
        return;
    }
    default public void onLobbyListUpdate(List<LobbyInfo> lobbies) {
        return;
    }
    default public void onJoinedLobbyUpdate(LobbyInfo joinedLobby) {
        return;
    }
    default public void onGameStarted() {
        return;
    }

    default public void onSetupPhaseFinished() {
        return;
    }

    default public void onCurrentPlayerChange(String newPlayer) {
        return;
    }

    default public void onTurnSkipped(String skippedPlayer) {
        return;
    }

    default public void onLastTurnReached(){
        return;
    }
    default public void onGameEnded(){
        return;
    }
}
