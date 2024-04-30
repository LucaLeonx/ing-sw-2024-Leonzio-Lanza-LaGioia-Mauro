package it.polimi.ingsw.model.card;

public class EmptyDeckException extends RuntimeException{
    public EmptyDeckException(){
        super("Deck is empty, cannot get top card");
    }
}
