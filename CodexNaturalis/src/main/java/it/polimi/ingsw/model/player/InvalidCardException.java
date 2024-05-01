package it.polimi.ingsw.model.player;

public class InvalidCardException extends Exception {
    public InvalidCardException(int invalidCardId) {
        super("Card with id " + invalidCardId + " is not present in hand");
    }
}