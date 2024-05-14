package it.polimi.ingsw.model.player;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import it.polimi.ingsw.model.map.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.player.InvalidCardException;


public class Player {
    private final String nickname;
    private final PlayerColor color;
    private final AtomicInteger score = new AtomicInteger();
    private final GameField field;
    private final List<Card> cardsInHand;
    private ObjectiveCard secretObjective;

    public Player(String nickname, PlayerColor color, int score, ObjectiveCard secretObjective, List<Card> cardsInHand, GameField field) {
        this.nickname = nickname;
        this.color = color;
        this.secretObjective = secretObjective;
        this.score.set(score);
        this.field = new GameField(field);
        this.cardsInHand = new ArrayList<>(cardsInHand);
    }

    public Player(Player other){
        this.nickname = other.nickname;
        this.color = other.color;
        this.secretObjective = other.secretObjective;
        this.score.set(other.getScore());
        this.cardsInHand = new ArrayList<>(other.cardsInHand);
        this.field = new GameField(other.field);
    }

    public Player(String nickname, PlayerColor color){
        this(nickname, color, 0, null, new ArrayList<>(), new GameField());
    }

    public String getNickname() {
        return nickname;
    }

    public PlayerColor getColor() {
        return color;
    }

    public int getScore() {
        return score.get();
    }

    public void incrementScore(int increment) {
        score.getAndAdd(increment);
    }

    public synchronized boolean hasSecretObjective(){
        return secretObjective != null;
    }

    public synchronized ObjectiveCard getSecretObjective() {
        return secretObjective;
    }
    public void setSecretObjective(ObjectiveCard secretObjective) {
        this.secretObjective = secretObjective;
    }

    public List<Card> getCardsInHand() {
        synchronized (cardsInHand) {
            return new ArrayList<>(cardsInHand);
        }
    }

    public void addCard(Card newCard) {
        synchronized (cardsInHand) {
            cardsInHand.add(newCard);
        }
    }

    public Card getCard(int cardId){
        synchronized (cardsInHand) {
            return cardsInHand.stream()
                    .filter(card -> card.getId() == cardId)
                    .findFirst().orElseThrow(NoSuchElementException::new);
        }
    }

    /**
     * Remove a card from the hand. It is called when player plays a card.
     * It Requires that there are 3 cards in player's hand.
     * @param cardId
     * @throws InvalidCardException called when the player doesn't have the card he was supposed to play
     */
    public Card removeCard(int cardId) throws InvalidCardException {
        Card removedCard = getCard(cardId);
        synchronized (cardsInHand) {
            cardsInHand.remove(removedCard);
        }
        return removedCard;
    }

    public GameField getField() {
        synchronized (field) {
            return field;
        }
    }
}
