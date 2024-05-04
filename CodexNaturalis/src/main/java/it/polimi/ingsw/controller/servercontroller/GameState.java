package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.controller.clientcontroller.PlayerSetupInfo;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.InvalidOperationException;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.map.Point;
import it.polimi.ingsw.model.player.Player;

import java.util.List;
import java.util.Map;

abstract class GameState {
    private Game game;

    public List<String> getPlayerNames(){
        return game.getPlayersNicknames();
    }

    public abstract PlayerSetupInfo getPlayerSetup(String name);

    public abstract void registerPlayerSetupChoice(String name, int secretObjective, CardOrientation initialCardOrientation);

    public Player getPlayerFullInformation(String name){
        return game.getPlayerInformation(name);
    }

    /*public Player getPlayerPublicInformation(String name){
        Player fullPlayerInformation = game.getPlayerInformation(name);

        List<Card> hiddenHand = fullPlayerInformation.getCardsInHand().stream()
                .map(card -> game.generateDummyCard(card)).toList();

        return new Player(fullPlayerInformation.getNickname(), fullPlayerInformation.getColor(),
                generateDummyObjective(), fullPlayerInformation.getCardsInHand(),
                fullPlayerInformation.getField());

    }*/

    public Map<DrawChoice, Card> getDrawableCards() {
        return game.getDrawableCards();
    }

    public String getCurrentPlayerNickname(){
        return game.getCurrentPlayerNickname();
    }

    public boolean isLastTurn(){
        return game.isLastTurn();
    }

    public void updateNextPlayer(){
        game.updateNextPlayer();
    }

    public void makeCurrentPlayerMove(int cardId, CardOrientation orientation, Point placement, DrawChoice choice) throws InvalidOperationException {
        try{
            game.makeCurrentPlayerMove(cardId, orientation, placement, choice);
        } catch (Exception e){
            throw new InvalidOperationException(e.getMessage());
        }
    }

    public boolean hasGameEnded() {
        return false;
    }
}
