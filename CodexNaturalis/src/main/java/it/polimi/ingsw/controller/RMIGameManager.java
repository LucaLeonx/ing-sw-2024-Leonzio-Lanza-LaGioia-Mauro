package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.clientcontroller.PlayerSetupInfo;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.InvalidOperationException;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

public class RMIGameManager implements GameManager{

    private String playerName;
    private GameState state;

    public RMIGameManager(String playerName){
        this.playerName = playerName;
    }
    @Override
    public String getCurrentPlayer() {
        return state.getCurrentPlayerNickname();
    }

    @Override
    public GameInfo getGameInfo() {
        return new GameInfo(state.getCurrentPlayerNickname(), state.getPlayerNames(), );
    }

    @Override
    public PlayerSetupInfo getPlayerSetup() {
        return state.getPlayerSetup(playerName);
    }

    @Override
    public boolean hasGameEnded() {
        return state.hasGameEnded();
    }

    @Override
    public void registerPlayerSetupChoice(int chosenObjectiveId, CardOrientation initialCardOrientation) {
        state.registerPlayerSetupChoice(playerName, chosenObjectiveId, initialCardOrientation);
    }

    @Override
    public void registerPlayerMove(int placedCardId,  Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice) throws InvalidOperationException {
        if(state.getCurrentPlayerNickname().equals(playerName)) {
            state.makeCurrentPlayerMove(placedCardId, chosenSide, placementPoint, drawChoice);
        }
    }
}
