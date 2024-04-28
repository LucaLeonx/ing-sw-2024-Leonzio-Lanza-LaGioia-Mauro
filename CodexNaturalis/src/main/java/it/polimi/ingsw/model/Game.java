package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.map.Point;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game {
    private Deck goldCardDeck;
    private Deck resourceCardDeck;
    private List<Player> players;
    private Player currentPlayer;
    private List<Card> visibleGoldCard;
    private List<Card> visibleResourceCard;
    private List<ObjectiveCard> commonObjectiveCards;
    private RandomPicker<ObjectiveCard> objCardsPicker;

    public Game(List<String> playerNames) throws FileNotFoundException {
        int i = 0;
        PlayerColor[] playerColors = {PlayerColor.RED,PlayerColor.GREEN,PlayerColor.BLUE,PlayerColor.YELLOW};
        this.goldCardDeck = new Deck(CardFactory.getGoldCards());
        this.resourceCardDeck = new Deck(CardFactory.getResourceCards());

        this.objCardsPicker = new RandomPicker<ObjectiveCard>(CardFactory.getObjectiveCards());

        this.commonObjectiveCards = new ArrayList<ObjectiveCard>(2);
        this.commonObjectiveCards.add(this.objCardsPicker.extractRandomElement().orElse(null));
        this.commonObjectiveCards.add(this.objCardsPicker.extractRandomElement().orElse(null));

        this.visibleGoldCard = new ArrayList<Card>(2);
        this.visibleGoldCard.add(this.goldCardDeck.getTopCard().orElse(null));
        this.visibleGoldCard.add(this.goldCardDeck.getTopCard().orElse(null));

        this.visibleResourceCard = new ArrayList<Card>(2);
        this.visibleResourceCard.add(this.resourceCardDeck.getTopCard().orElse(null));
        this.visibleResourceCard.add(this.resourceCardDeck.getTopCard().orElse(null));

        this.players = new ArrayList<Player>();
        for(String player : playerNames){
            Player p = new Player(player,playerColors[i]);
            setupPlayersHand(p);
            this.players.add(p);
            i++;
        }

    }
    private void setupPlayersHand(Player p){
        p.addCard(this.goldCardDeck.getTopCard().orElse(null));
        p.addCard(this.resourceCardDeck.getTopCard().orElse(null));
        p.addCard(this.resourceCardDeck.getTopCard().orElse(null));
    }

    /**
     * This method assign for each player the initial card randomly given to him,
     * the controller will take this information for send the card and give him the choice about the placement orientation
     * @return
     * @throws FileNotFoundException
     */
    public HashMap<String,Card> assignInitialCard() throws FileNotFoundException {
        RandomPicker<Card> starterCardsPicker = new RandomPicker<Card>(CardFactory.getInitialCards());
        HashMap<String,Card> cardHashMap = new HashMap<String,Card>();
        for(Player p: this.players){
            cardHashMap.put(p.getNickname(),starterCardsPicker.extractRandomElement().orElse(null));
        }
        return cardHashMap;
    }

    public ObjectiveCard[] assignObjectiveCardsToPlayer(String playerNick) throws FileNotFoundException {
        ObjectiveCard[] objCardToChoose = new ObjectiveCard[2];
        for(Player p: this.players){
            if(p.getNickname().equals(playerNick)){
                objCardToChoose[0] = this.objCardsPicker.extractRandomElement().orElse(null);
                objCardToChoose[1] = this.objCardsPicker.extractRandomElement().orElse(null);
            }
        }
        return objCardToChoose;
    }

    /**
     * This method is called by the Controller after receiving the Client's choice about the
     * placement orientation of the starter card
     * @param playerNick
     * @param initialCard
     * @param choiceOrientation
     */
    //is the initialCard parameter usefull? We surely don't to send this information back from the client to the server
    // We can just save it here or the controller can keep the information while waiting the client response
    public void setInitialCardOriented(String playerNick, Card initialCard, CardOrientation choiceOrientation){
        for(Player p : this.players){
            if(playerNick.equals(p.getNickname())){
                p.getField().placeCard(initialCard,choiceOrientation,new Point(0,0));
            }
        }
    }

}
