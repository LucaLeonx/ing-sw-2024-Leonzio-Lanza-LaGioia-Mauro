package it.polimi.ingsw.model.player;

import java.io.Serializable;

public class InvalidCardException extends Exception implements Serializable {
    public InvalidCardException(int invalidCardId) {
        super("Card with id " + invalidCardId + " is not present in hand");
    }
}
