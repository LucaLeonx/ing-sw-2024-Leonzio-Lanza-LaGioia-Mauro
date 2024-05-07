package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.dataobject.PlayerSetupInfo;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.InvalidOperationException;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

public class PlayState extends GameState {
    public PlayState(Game game, String controlledPlayer, ServerController gameManager) {
        super(game, controlledPlayer, gameManager);
    }

    @Override
    public PlayerSetupInfo getPlayerSetup() throws InvalidOperationException{
        throw new InvalidOperationException("Cannot get player setup choice when playing");
    }

    @Override
    public void registerPlayerSetupChoice(int secretObjectiveId, CardOrientation initialCardOrientation) throws InvalidOperationException {
        throw new InvalidOperationException("Cannot change player setup choice while playing");
    }

    @Override
    public void makeCurrentPlayerMove(int cardId, CardOrientation orientation, Point placement, DrawChoice drawChoice) throws InvalidOperationException{
        if(isLastTurn()){
            super.makeCurrentPlayerMove(cardId, orientation, placement, drawChoice);
            transition();
        } else {
            super.makeCurrentPlayerMove(cardId, orientation, placement, drawChoice);
        }
    }

    @Override
    public void skipTurn() throws InvalidOperationException {
        if(isLastTurn()){
            super.skipTurn();
            transition();
        } else {
            super.skipTurn();
        }
    }


    @Override
    public void transition() {
        gameManager.setState(new EndState(game, controlledPlayer, gameManager));
    }
}
