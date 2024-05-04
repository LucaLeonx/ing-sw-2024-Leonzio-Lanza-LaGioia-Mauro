package it.polimi.ingsw.model.player;

import java.util.*;
import it.polimi.ingsw.model.map.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.player.InvalidCardException;


public class Player {
    private final String nickname;
    private int score;
    private final GameField field;
    private final PlayerColor color;
    private ObjectiveCard secretObjective;
    private final List<Card> cardsInHand;

    public Player(String nickname, PlayerColor color, ObjectiveCard secretObjective, List<Card> cardsInHand, GameField field) {
        this.nickname = nickname;
        this.color = color;
        this.secretObjective = secretObjective;
        this.score = 0;  // initial score is 0
        this.field = new GameField(field);
        this.cardsInHand = new ArrayList<>(cardsInHand);
    }

    public Player(Player other){
        this.nickname = other.nickname;
        this.color = other.color;
        this.secretObjective = other.secretObjective;
        this.score = other.score;
        this.cardsInHand = new ArrayList<>(other.cardsInHand);
        this.field = new GameField(other.field);
    }

    public Player(String nickname, PlayerColor color){
        this(nickname, color, null, new ArrayList<>(), new GameField());
    }

    public String getNickname() {
        return nickname;
    }

    public PlayerColor getColor() {
        return color;
    }

    public synchronized ObjectiveCard getSecretObjective() {
        if(secretObjective != null) {
            return secretObjective;
        } else {
            throw new NullPointerException("Secret Objective is null");
        }
    }
    public void setSecretObjective(ObjectiveCard secretObjective) {
        this.secretObjective = secretObjective;
    }

    public List<Card> getCardsInHand() {
        return new ArrayList<>(cardsInHand);
    }

    public void addCard(Card newCard) {
       this.cardsInHand.add(newCard);
    }

    /**
     * Remove a card from the hand. It is called when player plays a card.
     * It Requires that there are 3 cards in player's hand.
     * @param cardId
     * @throws InvalidCardException called when the player doesn't have the card he was supposed to play
     */
    public Card removeCard(int cardId) throws InvalidCardException {
        Card removedCard = null;
        synchronized (cardsInHand) {
            removedCard = cardsInHand.stream()
                    .filter(card -> card.getId() == cardId)
                    .findFirst().orElseThrow(() -> new InvalidCardException(cardId));
            cardsInHand.remove(removedCard);
        }
        return removedCard;
    }

    public synchronized int getScore() {
        return score;
    }

    public synchronized void incrementScore(int increment) {
        this.score += increment;
    }

    public GameField getField() {
        return field;
    }
}
