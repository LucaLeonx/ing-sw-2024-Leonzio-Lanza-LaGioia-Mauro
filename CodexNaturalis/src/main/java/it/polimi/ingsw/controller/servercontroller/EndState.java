package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.dataobject.PlayerSetupInfo;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.InvalidOperationException;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

public class EndState extends GameState{

    public EndState(Game game, String controlledPlayer, ServerController gameManager){
        super(game, controlledPlayer, gameManager);
        for(Player player : game.getPlayers()){
            player.incrementScore(player.getSecretObjective().getRewardFunction().getPoints(player.getField()));
            player.incrementScore(game.getCommonObjectiveCards().getFirst().getRewardFunction().getPoints(player.getField()));
            player.incrementScore(game.getCommonObjectiveCards().getLast().getRewardFunction().getPoints(player.getField()));
        }
    }
    @Override
    public void transition() {
        gameManager.setState(new EndState(game, controlledPlayer, gameManager));
    }

    @Override
    public void makeCurrentPlayerMove(int cardId, CardOrientation orientation, Point placement, DrawChoice choice) throws InvalidOperationException {
        throw new InvalidOperationException("Cannot perform move after game ended");
    }

    @Override
    public void registerPlayerSetupChoice(int secretObjectiveId, CardOrientation initialCardOrientation) throws InvalidOperationException {
        throw new InvalidOperationException("Cannot change player setup after game ended");
    }

    @Override
    public PlayerSetupInfo getPlayerSetup() throws InvalidOperationException {
        throw new InvalidOperationException("Cannot obtain setup info after game ended");
    }

    @Override
    public void skipTurn() throws InvalidOperationException {
        throw new InvalidOperationException("Cannot skip turn after game ended");
    }

    @Override
    public String getWinnerName(){
        throw new InvalidOperationException("Cannot decide the winner while playing");
    }

    @Override
    public boolean hasGameEnded(){
        return true;
    }
}
