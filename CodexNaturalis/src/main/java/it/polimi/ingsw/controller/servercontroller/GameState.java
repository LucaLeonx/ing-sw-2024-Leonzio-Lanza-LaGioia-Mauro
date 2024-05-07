package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.InvalidOperationException;
import it.polimi.ingsw.model.PlayerSetup;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.map.GameField;
import it.polimi.ingsw.model.map.Point;
import it.polimi.ingsw.model.player.InvalidCardException;
import it.polimi.ingsw.model.player.Player;

import java.util.List;
import java.util.NoSuchElementException;

abstract class GameState  extends ServerState{

    Game game;
    String controlledPlayer;
    ServerController gameManager;

    public GameState(Game game, String controlledPlayer, ServerController gameManager){
        this.game = game;
        this.controlledPlayer = controlledPlayer;
        this.gameManager = gameManager;
    }

    public List<String> getPlayerNames() throws InvalidOperationException{
        return game.getPlayersNicknames();
    }

    public PlayerSetupInfo getPlayerSetup() throws InvalidOperationException{
        return InfoTranslator.convertToPlayerSetupInfo(game.getPlayerSetup(controlledPlayer));
    }

    public String getCurrentPlayerNickname() throws InvalidOperationException{
        return game.getCurrentPlayerNickname();
    }

    public boolean isLastPlayerOfRound() throws InvalidOperationException{
        return game.isLastPlayerOfRound();
    }

    public boolean isLastTurn(){
        return game.isLastTurn();
    }

    public void registerPlayerSetupChoice(int secretObjectiveId, CardOrientation initialCardOrientation) throws InvalidOperationException {
        Player player = game.getPlayer(controlledPlayer);
        PlayerSetup setup = game.getPlayerSetup(controlledPlayer);
        ObjectiveCard chosenObjective = (secretObjectiveId == setup.objective1().getId()) ? setup.objective1() : setup.objective2();

        player.setSecretObjective(chosenObjective);
        player.getField().placeCard(setup.initialCard(), initialCardOrientation, new Point(0,0));
    }

    public void makeCurrentPlayerMove(int cardId, CardOrientation orientation, Point placement, DrawChoice drawChoice) throws InvalidOperationException{
        if(!game.getCurrentPlayerNickname().equals(controlledPlayer)) {
            throw new InvalidOperationException("Cannot play during the turn of another player");
        }

        try{
            Player player = game.getPlayer(controlledPlayer);
            Card playedCard = player.removeCard(cardId);
            GameField field = player.getField();

            if(!field.getAvailablePositions().contains(placement)){
                throw new InvalidOperationException("The position " + placement + "is not available");
            }

            if(!playedCard.getSide(orientation).getPlayingRequirements().isSatisfied(field)){
                throw new InvalidOperationException("Cannot play card: " + cardId + "on "
                + orientation + "side: requirements not satisfied");
            }

            field.placeCard(playedCard, orientation, placement);
            player.incrementScore(playedCard.getSide(orientation).getPlayingReward().getPoints(field));

            Deck selectedDeck = switch(drawChoice){
                case DECK_RESOURCE, RESOURCE_CARD_1, RESOURCE_CARD_2 -> game.getResourceCardDeck();
                case DECK_GOLD, GOLD_CARD_1, GOLD_CARD_2 -> game.getGoldenCardDeck();
            };

            switch(drawChoice){
                case DECK_RESOURCE, DECK_GOLD: {
                    player.addCard(selectedDeck.draw());
                    break;
                }
                case RESOURCE_CARD_1, RESOURCE_CARD_2, GOLD_CARD_1, GOLD_CARD_2:
                    player.addCard(game.getVisibleCard(drawChoice));
                    game.getVisibleCards().remove(drawChoice);
                    game.getVisibleCards().put(drawChoice, selectedDeck.draw());
                    break;
            }
            if(isLastPlayerOfRound()) {
                game.setLastTurn(
                        (game.getResourceCardDeck().isEmpty() && game.getGoldenCardDeck().isEmpty())
                                || game.getPlayers().stream().anyMatch((participant) -> participant.getScore() >= 20));
            }
            game.changeCurrentPlayer();

        } catch(InvalidCardException | NoSuchElementException e){
            throw new InvalidOperationException(e.getMessage());
        }
    }

    public void skipTurn() throws InvalidOperationException {
        if(!game.getCurrentPlayerNickname().equals(controlledPlayer)) {
            throw new InvalidOperationException("Cannot skip turn during when another player is playing");
        }

        game.changeCurrentPlayer();
    }

    public boolean hasGameEnded() {
        return false;
    }



    public ControlledPlayerInfo getControlledPlayerInfo() throws InvalidOperationException{
        return InfoTranslator.convertToControlledPlayerInfo(game.getPlayer(controlledPlayer));
    }

    public OpponentInfo getOpponentPlayerInfo(String name) throws InvalidOperationException {
        if(!getPlayerNames().contains(name)){
            throw new InvalidOperationException("Information unavailable: the opponent is not existent");
        }
        return InfoTranslator.convertToOpponentPlayerInfo(game.getPlayer(name));
    }

    public DrawableCardsInfo getDrawableCardsInfo() {
        return InfoTranslator.convertToDrawableCardsInfo(game);
    }

    public String getWinnerName(){
        List<Player> leaderboard = game.getPlayers();
        leaderboard.sort((p1, p2) -> (p1.getScore() > p2.getScore()) ? 1 : -1);
        return leaderboard.getFirst().getNickname();
    }

    public abstract void transition();
}
