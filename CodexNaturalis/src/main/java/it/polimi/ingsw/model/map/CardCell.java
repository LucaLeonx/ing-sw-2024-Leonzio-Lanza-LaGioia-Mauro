package it.polimi.ingsw.model.map;

import it.polimi.ingsw.model.card.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record CardCell(Card card, CardOrientation orientation) {

    public CardColor cardColor() {
        return this.card().getCardColor();
    }

    public CardSide visibleCardSide() {
        return this.card().getSide(this.orientation());
    }
}

