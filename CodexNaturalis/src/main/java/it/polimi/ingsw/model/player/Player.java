package it.polimi.ingsw.model.player;

import java.util.*;
import it.polimi.ingsw.model.map.*;
import it.polimi.ingsw.model.card.*;


public class Player {
    private final String nickname;  // readOnly attribute
    private int score;
    private final GameField field;
    private final PlayerColor color;
    private List<Card> cardsInHand;
    public Player(String nickname, PlayerColor color) {
        this.nickname = nickname;
        this.color = color;
        this.score = 0;  // initial score is 0
        this.field = new GameField();
        this.cardsInHand = new ArrayList<>();
    }

    public String getNickname() {
        return nickname;
    }

    public PlayerColor getColor() {
        return color;
    }

    public List<Card> getCardList() {
        return new ArrayList<>(cardsInHand);
    }

    public void addCard(Card newCard) {
       this.cardsInHand.add(newCard);
    }

    /**
     * Remove a card from the hand. It is called when player plays a card.
     * It Requires that there are 3 cards in player's hand.
     * @param idCard
     * @throws InvalidCardException called when the player doesn't have the card he was supposed to play
     */
    public void removeCard(int idCard) throws InvalidCardException {
        int position = -1;
        for(int i=0; i<3; i++)
        {
            if(cardsInHand.get(i).getId() == idCard)
                position = i;
        }
        if (position == -1) {
            throw new InvalidCardException("Card not in your hand");
        }
        else {
            cardsInHand.remove(position);
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public GameField getField() {
        return field;
    }
}
