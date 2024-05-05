package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.controller.clientcontroller.ControlledPlayerInfo;
import it.polimi.ingsw.controller.clientcontroller.DrawableCardsInfo;
import it.polimi.ingsw.controller.clientcontroller.OpponentInfo;
import it.polimi.ingsw.controller.clientcontroller.PlayerSetupInfo;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.InvalidOperationException;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import java.util.List;

public class RMIGameManager implements GameManager{
    private GameState state;

    public RMIGameManager(GameState previousState){
        this.state = previousState;
    }

    public RMIGameManager(String controlledPlayer, List<String> playerNames){
        this.state = new SetupState(controlledPlayer, playerNames, this);
    }
    @Override
    public String getCurrentPlayer() throws InvalidOperationException {
        return state.getCurrentPlayerNickname();
    }
    @Override
    public ControlledPlayerInfo getControlledPlayerInfo() throws InvalidOperationException {
        return state.getControlledPlayerInfo();
    }

    @Override
    public OpponentInfo getOpponentInfo(String name) throws InvalidOperationException {
        return state.getOpponentPlayerInfo(name);
    }

    @Override
    public DrawableCardsInfo getDrawableCardsInfo() throws InvalidOperationException {
        return state.getDrawableCardsInfo();
    }

    @Override
    public PlayerSetupInfo getPlayerSetup() throws InvalidOperationException {
        return state.getPlayerSetup();
    }

    @Override
    public boolean isLastTurn() throws InvalidOperationException {
        return state.isLastTurn();
    }

    @Override
    public boolean hasGameEnded() {
        return state.hasGameEnded();
    }

    @Override
    public void registerPlayerSetupChoice(int chosenObjectiveId, CardOrientation initialCardOrientation) throws InvalidOperationException {
        state.registerPlayerSetupChoice(chosenObjectiveId, initialCardOrientation);
    }

    @Override
    public void registerPlayerMove(int placedCardId,  Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice) throws InvalidOperationException {
        state.makeCurrentPlayerMove(placedCardId, chosenSide, placementPoint, drawChoice);
    }

    public synchronized void setState(GameState newState) {
        this.state = newState;
    }
}

