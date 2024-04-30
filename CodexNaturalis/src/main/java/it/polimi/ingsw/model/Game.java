package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.map.GameField;
import it.polimi.ingsw.model.map.Point;
import it.polimi.ingsw.model.player.InvalidCardException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Game {
    private Deck goldCardDeck;
    private Deck resourceCardDeck;
    private final List<Player> players;

    private boolean isLastTurn;
    private Player currentPlayer;
    // To be removed
    private List<Card> visibleResourceCard;
    private List<Card> visibleGoldCard;

    private final Map<DrawChoice, Card> visibleCards;
    private final List<ObjectiveCard> commonObjectiveCards;
    private RandomPicker<ObjectiveCard> objCardsPicker;

    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock(true);

    public Game(List<String> playerNames, Map<DrawChoice, Card> visibleCards) throws FileNotFoundException {
        this.visibleCards = visibleCards;
        int i = 0;

        PlayerColor[] playerColors = {PlayerColor.RED,PlayerColor.GREEN,PlayerColor.BLUE,PlayerColor.YELLOW};
        this.goldCardDeck = new Deck(CardFactory.getGoldCards());
        this.resourceCardDeck = new Deck(CardFactory.getResourceCards());

        this.objCardsPicker = new RandomPicker<ObjectiveCard>(CardFactory.getObjectiveCards());

        this.commonObjectiveCards = new ArrayList<ObjectiveCard>(2);
        this.commonObjectiveCards.add(this.objCardsPicker.extractRandomElement().orElse(null));
        this.commonObjectiveCards.add(this.objCardsPicker.extractRandomElement().orElse(null));

        /*
        this.visibleGoldCard.add(this.goldCardDeck.getTopCard());

        this.visibleResourceCard = new ArrayList<Card>(2);
        this.visibleResourceCard.add(this.resourceCardDeck.getTopCard());
        this.visibleResourceCard.add(this.resourceCardDeck.getTopCard());
        */
        this.players = new ArrayList<Player>();
        for(String player : playerNames){
            Player p = new Player(player,playerColors[i], new ObjectiveCard(0, GameFunctionFactory.createPointsRewardFunction(0)));
            setupPlayersHand(p);
            this.players.add(p);
            i++;
        }
        isLastTurn = false;
    }
    private void setupPlayersHand(Player p){
        /*
        p.addCard(this.goldCardDeck.getTopCard());
        p.addCard(this.resourceCardDeck.getTopCard());
        p.addCard(this.resourceCardDeck.getTopCard());
        */
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
                objCardToChoose[0] = this.objCardsPicker.extractRandomElement().orElseThrow(() -> new RuntimeException("No ObjectiveCard Available"));
                objCardToChoose[1] = this.objCardsPicker.extractRandomElement().orElseThrow(() -> new RuntimeException("No ObjectiveCard Available"));
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
    // is the initialCard parameter useful? We surely don't to send this information back from the client to the server
    // We can just save it here or the controller can keep the information while waiting the client response
    public void setInitialCardOriented(String playerNick, Card initialCard, CardOrientation choiceOrientation){
        for(Player p : this.players){
            if(playerNick.equals(p.getNickname())){
                p.getField().placeCard(initialCard,choiceOrientation,new Point(0,0));
            }
        }
    }

    public List<String> getPlayersNicknames(){
        rwl.readLock().lock();
        List<String> playerNicknames = players.stream().map(Player::getNickname).toList();
        rwl.readLock().unlock();

        return playerNicknames;
    }

    public Player getPlayerInformation(String nickname) throws NoSuchElementException {
        rwl.readLock().lock();
        Player playerInformation = players.stream()
                .filter((player) -> player.getNickname().equals(nickname))
                .findFirst().orElseThrow(NoSuchElementException::new);
        rwl.readLock().unlock();

        return playerInformation;
    }

    public String getCurrentPlayerNickname() {
        rwl.readLock().lock();

        String currentPlayerNickname = currentPlayer.getNickname();

        rwl.writeLock().unlock();
        return currentPlayerNickname;
    }

    public boolean isLastTurn(){
        rwl.readLock().lock();
        boolean returnedLastTurn = isLastTurn;
        rwl.readLock().unlock();
        return returnedLastTurn;
    }

    public boolean isLastPlayer(){
        rwl.readLock().lock();
        boolean isLastPlayer = currentPlayer.equals(players.getLast());
        rwl.readLock().unlock();
        return isLastPlayer;
    }

    public List<ObjectiveCard> getCommonObjectiveCards() {
        rwl.readLock().lock();
        List<ObjectiveCard> commonObjectives = commonObjectiveCards;
        rwl.readLock().unlock();
        return commonObjectives;
    }

    public Map<DrawChoice, Card> getDrawableCards(){

        Map<DrawChoice, Card> drawableCards = new HashMap<>(visibleCards);
        rwl.readLock().lock();
        if(!resourceCardDeck.isEmpty()){
            //drawableCards.put(DrawChoice.DECK_RESOURCE, generateDummyCard(resourceCardDeck.getTopCard()));
        }

        if(!goldCardDeck.isEmpty()){
            //drawableCards.put(DrawChoice.DECK_GOLD, generateDummyCard(resourceCardDeck.getTopCard()));
        }
        rwl.readLock().unlock();

        return drawableCards;
    }

    // Move in CardFactory
    public Card generateDummyCard(Card card){
        return new Card(0, card.getCardColor(), card.getSide(CardOrientation.BACK), card.getSide(CardOrientation.BACK));
    }

    public void makeCurrentPlayerMove(int cardId, CardOrientation orientation, Point placementPoint, DrawChoice drawChoice) throws InvalidOperationException {

        rwl.writeLock().lock();
        Card playedCard;
        CardSide playedSide;
        GameField field = currentPlayer.getField();
        Deck deckToUse = switch(drawChoice){
            case DECK_RESOURCE, RESOURCE_CARD_1, RESOURCE_CARD_2 -> resourceCardDeck;
            case DECK_GOLD, GOLD_CARD_1, GOLD_CARD_2 -> goldCardDeck;
        };

        try {
            playedCard = currentPlayer.removeCard(cardId);
            playedSide = playedCard.getSide(orientation);
        } catch (InvalidCardException e) {
            throw new InvalidOperationException(e.getMessage());
        }

        if(!field.getAvailablePositions().contains(placementPoint)){
            throw new InvalidOperationException("The position " + placementPoint + "is not among available ones");
        } else if(!playedSide.getPlayingRequirements().isSatisfied(field)){
            throw new InvalidOperationException("Playing requirements for the card on this side are not satisfied");
        }

        field.placeCard(playedCard, orientation, placementPoint);
        currentPlayer.incrementScore(playedSide.getPlayingReward().getPoints(field));

        switch(drawChoice){
            case DECK_RESOURCE, DECK_GOLD -> currentPlayer.addCard(deckToUse.draw()
                    .orElseThrow(() -> new InvalidOperationException("Cannot draw from empty golden cards deck")));
            case RESOURCE_CARD_1, RESOURCE_CARD_2, GOLD_CARD_1, GOLD_CARD_2 ->
            {
                Card drawnCard = visibleCards.get(drawChoice);

                currentPlayer.addCard(drawnCard);
                if(!deckToUse.isEmpty()){
                    visibleCards.put(drawChoice, deckToUse.draw().get());
                }
            }
        }

        updateNextPlayer();
        rwl.writeLock().unlock();
    }

    public void updateNextPlayer(){
        rwl.writeLock().lock();
        if(isLastPlayer()){
            currentPlayer = players.getFirst();
            isLastTurn = setLastTurn();
        } else {
            currentPlayer = players.get(players.indexOf(currentPlayer));
        }
        rwl.writeLock().unlock();
    }

    private boolean setLastTurn(){
        rwl.readLock().lock();
        boolean newLastTurn = (resourceCardDeck.isEmpty() && goldCardDeck.isEmpty())
                || players.stream().anyMatch(player -> player.getScore() >= 20);
        rwl.readLock().unlock();
        return newLastTurn;
    }
}
