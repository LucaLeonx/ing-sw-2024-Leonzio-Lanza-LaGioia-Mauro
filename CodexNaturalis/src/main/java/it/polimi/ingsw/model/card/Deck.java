package it.polimi.ingsw.model.card;

import java.util.*;

public class Deck {
    private Optional<Card> topCard;
    private int numberOfCards;
    private final RandomPicker<Card> cardPicker;

    public Deck(Collection<Card> cards){
        numberOfCards = cards.size();
        cardPicker = new RandomPicker<>(cards);
        topCard = cardPicker.extractRandomElement();
    }

    public synchronized boolean isEmpty(){
        return (numberOfCards == 0);
    }

    public synchronized Card getTopCard(){
        if(topCard.isPresent()) {
            return topCard.get();
        } else {
            throw new EmptyDeckException();
        }
    }

    public synchronized int getNumberOfCards() {
        return numberOfCards;
    }

    public synchronized Card draw(){
        if(topCard.isPresent()) {
            Optional<Card> drawnCard = topCard;
            topCard = cardPicker.extractRandomElement();

            if (numberOfCards > 0) { //TODO: if statement can safely be removed
                numberOfCards--;
            }
            return drawnCard.get();
        } else {
            throw new EmptyDeckException();
        }
    }
}
