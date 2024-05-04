package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.controller.clientcontroller.PlayerSetupInfo;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.InvalidOperationException;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;
import it.polimi.ingsw.model.player.Player;

import java.util.List;

public class RMIGameManager implements GameManager{
    private final String controlledPlayer;
    private GameState state;

    public RMIGameManager(String controlledPlayer, GameState previousState){
        this.controlledPlayer = controlledPlayer;
        this.state = previousState;
    }

    public RMIGameManager(String controlledPlayer, List<String> playerNames){
        this(controlledPlayer, new SetupState(controlledPlayer, playerNames));
    }
    @Override
    public String getCurrentPlayer() throws InvalidOperationException {
        return state.getCurrentPlayerNickname();
    }

    @Override
    public GameInfo getGameInfo() throws InvalidOperationException {
        return state.getGameInfo();
    }

    @Override
    public PlayerSetupInfo getPlayerSetup() throws InvalidOperationException {
        return state.getPlayerSetup(controlledPlayer);
    }

    @Override
    public boolean isLastTurn() {
        return state.isLastPlayerTurn();
    }

    @Override
    public boolean hasGameEnded() {
        return state.hasGameEnded();
    }

    @Override
    public void registerPlayerSetupChoice(int chosenObjectiveId, CardOrientation initialCardOrientation) {
        state.registerPlayerSetupChoice(chosenObjectiveId, initialCardOrientation);
    }

    @Override
    public void registerPlayerMove(int placedCardId,  Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice) throws InvalidOperationException {
        state.makeCurrentPlayerMove(placedCardId, chosenSide, placementPoint, drawChoice);
    }

    public synchronized void updateState(){
        this.state = state.transition(this);
    }
}
