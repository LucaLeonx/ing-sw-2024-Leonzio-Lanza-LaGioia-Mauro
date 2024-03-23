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

    public boolean isEmpty(){
        return (numberOfCards == 0);
    }

    public Optional<Card> getTopCard(){
        return topCard;
    }

    public int getNumberOfCards() {
        return numberOfCards;
    }

    public Optional<Card> draw(){

        Optional<Card> returnedCard = topCard;
        topCard = cardPicker.extractRandomElement();

        if(numberOfCards > 0){
            numberOfCards -= 1;
        } else {
            numberOfCards = 0;
        }

        return  returnedCard;
    }
}
