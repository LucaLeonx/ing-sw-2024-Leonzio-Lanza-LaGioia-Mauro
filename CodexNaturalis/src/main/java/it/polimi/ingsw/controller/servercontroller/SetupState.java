package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.InvalidOperationException;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.map.Point;

public class SetupState extends GameState{

    public SetupState(String controlledPlayer, Game game, RMIGameManagerImpl gameManager){
        super(game, controlledPlayer, gameManager);
    }

    @Override
    public void makeCurrentPlayerMove(int cardId, CardOrientation orientation, Point placement, DrawChoice choice) throws InvalidOperationException {
        throw new InvalidOperationException("Cannot perform move during setup phase");
    }

    @Override
    public void registerPlayerSetupChoice(int secretObjectiveId, CardOrientation initialCardOrientation) throws InvalidOperationException{
        super.registerPlayerSetupChoice(secretObjectiveId, initialCardOrientation);
        transition();
    }

    @Override
    public void skipTurn() throws InvalidOperationException {
        throw new InvalidOperationException("Cannot skip turn during setup phase");
    }

    @Override
    public void transition() {
        gameManager.setState(new PlayState(game, controlledPlayer, gameManager));
    }
}
