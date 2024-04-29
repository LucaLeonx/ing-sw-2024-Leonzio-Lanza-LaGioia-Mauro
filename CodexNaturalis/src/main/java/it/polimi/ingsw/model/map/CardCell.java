package it.polimi.ingsw.model.map;

import it.polimi.ingsw.model.card.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Record used by the Gamefield to represent the type of card with color and side in a specific point
 * @param card - class of a specific card
 * @param orientation - orientation of the card played by the Player
 */
public record CardCell(Card card, CardOrientation orientation) {

    public CardColor cardColor() {
        return this.card().getCardColor();
    }

    public CardSide visibleCardSide() {
        return this.card().getSide(this.orientation());
    }
}

