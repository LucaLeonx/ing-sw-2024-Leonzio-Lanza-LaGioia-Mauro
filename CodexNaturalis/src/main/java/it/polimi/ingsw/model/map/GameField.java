package it.polimi.ingsw.model.map;

import java.util.*;
import java.util.stream.Collectors;

import it.polimi.ingsw.model.card.*;


public class GameField{
    private final Map<Point, CardCell> cards;
    private final Map<Point, AngleCell> angles;
    private final Set<Point> availableCells;
    private final Map<Symbol, Integer> symbolCounters;

    public GameField(){
        this.cards = new HashMap<>();
        this.angles = new HashMap<>();
        this.symbolCounters = new HashMap<>();
        this.availableCells = new HashSet<>();

        // Initialize counters
        for(Symbol symbol : Symbol.values()){
            symbolCounters.put(symbol, 0);
        }
    }

    public Map<Point, CardCell> getCardCells() {
        return Map.copyOf(cards);
    }
    public Map<Point, Card> getCards() {
        return cards.entrySet().stream()
                .map((entry) -> Map.entry(entry.getKey(), entry.getValue().card()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<Point, Symbol> getAnglesSymbols() {
        return angles.entrySet().stream()
                .map((entry) -> Map.entry(entry.getKey(), entry.getValue().topSymbol()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<Point, AngleCell> getAngleCells() {
        return Map.copyOf(angles);
    }

    public int getSymbolCounter(Symbol symbol){  //returns the number of Symbol in Gamefield
        return symbolCounters.get(symbol);
    }

    public Set<Point> getAvailablePositions() {
        return Set.copyOf(availableCells);
    }

    public void placeCard(Card card, CardOrientation cardOrientation, Point position)
    {
        registerCard(card, cardOrientation, position);
        updateAngles(card, cardOrientation, position);
        updateCounters(card, cardOrientation, position);
        updateAvailableCells(card, cardOrientation, position);
    }

    private void registerCard(Card card, CardOrientation cardOrientation, Point cardPosition){
        CardCell newCard = new CardCell(card, cardOrientation);
        cards.put(cardPosition, newCard);
    }

    private void updateAngles(Card card, CardOrientation cardOrientation, Point cardPosition){
        CardSide side = card.getSide(cardOrientation);

        for(AnglePosition angle : AnglePosition.values()){
            Point anglePosition = cardPosition.sum(angle.getRelativePosition());
            Symbol topSymbol = side.getSymbolFromAngle(angle);

            // Create the angle if it doesn't exist
            AngleCell previousAngle = angles.getOrDefault(anglePosition, new AngleCell(cardPosition, topSymbol));
            angles.put(anglePosition, previousAngle.withNewTopCard(cardPosition, topSymbol));
        }
    }

    private void updateCounters(Card card, CardOrientation orientation, Point cardPosition){
        for(Symbol symbol : card.getSide(orientation).getCenterSymbols()){
            incrementCounter(symbol);
        }

        for(Point point : Point.getAdjacentPositions(cardPosition)){
            AngleCell updatedAngle = angles.get(point);
            incrementCounter(updatedAngle.topSymbol());

            // An already present angle has been covered
            if(updatedAngle.isCovered()){
                decrementCounter(updatedAngle.bottomSymbol());
            }
        }
    }

    private void updateAvailableCells(Card card, CardOrientation cardOrientation, Point cardPosition){
        for(AnglePosition angle : AnglePosition.values()){
            if(card.getSide(cardOrientation).getSymbolFromAngle(angle) != Symbol.HIDDEN){
                availableCells.add(cardPosition.sum(angle.getRelativePosition().scale(2)));
            }
        }
    }

    private void incrementCounter(Symbol symbol){
        symbolCounters.put(symbol, symbolCounters.get(symbol) + 1);
    }

    private void decrementCounter(Symbol symbol){
        symbolCounters.put(symbol, symbolCounters.get(symbol) - 1);
    }
}




