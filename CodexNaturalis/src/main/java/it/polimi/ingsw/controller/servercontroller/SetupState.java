package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.InvalidOperationException;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.map.Point;

import java.util.List;

public class SetupState extends GameState{

    public SetupState(String controlledPlayer, List<String> playerNames, RMIGameManager gameManager){
        super(new Game(playerNames), controlledPlayer, gameManager);
    }

    @Override
    public void makeCurrentPlayerMove(int cardId, CardOrientation orientation, Point placement, DrawChoice choice) throws InvalidOperationException {
        throw new InvalidOperationException("Cannot perform move during setup phase");
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
