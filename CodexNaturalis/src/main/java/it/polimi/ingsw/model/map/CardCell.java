package it.polimi.ingsw.model.map;

import it.polimi.ingsw.model.card.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CardCell {
    private final Point position;
    private final Card card;
    private final CardOrientation cardOrientation;
    private final Set<AnglePosition> coveredAngles;

    public CardCell(Point position, Card card, CardOrientation cardOrientation) {
        this(position, card, cardOrientation, new HashSet<>());
    }

    public Point getPosition(){
        return this.position;
    }

    public Card getCard(){
        return card;
    }

    public CardOrientation getCardOrientation() {
        return cardOrientation;
    }

    public CardColor getCardColor() {
        return card.getCardColor();
    }

    public CardSide getVisibleCardSide() {
        return card.getSide(cardOrientation);
    }

    public Set<Point> getAdjacentCardsPosition(){

        return Stream.of(AnglePosition.values())
                .map((angle) -> angle.getRelativePosition().scale(2))
                .map((relativePosition) -> Point.sum(position, relativePosition))
                .collect(Collectors.toUnmodifiableSet());

    }

    public Set<Point> getCoveringCardsPositions(){
        return coveredAngles.stream()
                .map((angle) -> angle.getRelativePosition().scale(2))
                .map((relativePosition) -> Point.sum(position, relativePosition))
                .collect(Collectors.toUnmodifiableSet());
    }

    public CardCell withCoveredAngles(AnglePosition... newCoveredAngles){
        Set<AnglePosition> coveredAngles = new HashSet<>(this.coveredAngles);
        Collections.addAll(coveredAngles, newCoveredAngles);
        return new CardCell(this.position, this.card, this.cardOrientation, coveredAngles);
    }

    private CardCell(Point position, Card card, CardOrientation cardOrientation, Set<AnglePosition> coveredAngles) {
        this.position = position;
        this.card = card;
        this.cardOrientation = cardOrientation;
        this.coveredAngles = Set.copyOf(coveredAngles);
    }

    private Set<AnglePosition> getCoveredAngles() {
        return coveredAngles;
    }

}

