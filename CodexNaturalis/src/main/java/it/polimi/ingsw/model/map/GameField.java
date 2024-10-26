package it.polimi.ingsw.model.map;

import java.util.*;
import java.util.stream.Collectors;

import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidParameterException;
import it.polimi.ingsw.model.card.*;


public class GameField{
    private final Map<Point, CardCell> cards;
    private final Map<Point, AngleCell> angles;
    private final Set<Point> availableCells;
    private final Map<Symbol, Integer> symbolCounters;
    private List<Point> insertionOrder;
    public GameField(){
        this.cards = new HashMap<>();
        this.angles = new HashMap<>();
        this.symbolCounters = new HashMap<>();
        this.availableCells = new LinkedHashSet<>();
        this.insertionOrder = new ArrayList<>();
        this.availableCells.add(new Point(0,0));
        // Add the position for initial card

        // Initialize counters
        for(Symbol symbol : Symbol.values()){
            symbolCounters.put(symbol, 0);
        }
    }

    // Copy constructor
    public GameField(GameField other){
        this.cards = new HashMap<>(other.cards);
        this.angles = new HashMap<>(other.angles);
        this.availableCells = new LinkedHashSet<>(other.availableCells);
        this.symbolCounters = new HashMap<>(other.symbolCounters);
        this.insertionOrder = new ArrayList<>(other.insertionOrder);
    }

    public synchronized Map<Symbol, Integer> getSymbolCounters(){
        return Map.copyOf(symbolCounters);
    }
    /**
     *
     *
     * @return A copy of the CardCell Map
     */
    public synchronized Map<Point, CardCell> getCardCells() {
        return Map.copyOf(cards);
    }

    /**
     *
     * @return a copy of the insertion card order
     */
    public synchronized List<Point> getInsertionOrder() { return List.copyOf(insertionOrder); }

    /**
     *
     * @return A map with all the cards and their coordinates placed on the GameField
     */
    public synchronized Map<Point, Card> getCards() {
        return cards.entrySet().stream()
                .map((entry) -> Map.entry(entry.getKey(), entry.getValue().card()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     *
     * @return A map with all the symbols and their coordinates placed on the Game Field
     */
    public synchronized Map<Point, Symbol> getAnglesSymbols() {
        return angles.entrySet().stream()
                .map((entry) -> Map.entry(entry.getKey(), entry.getValue().topSymbol()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     *
     * @return A map with all the AngleCell and theis coordinates placed on the Game Field
     */
    public synchronized Map<Point, AngleCell> getAngleCells() {
        return Map.copyOf(angles);
    }

    /**
     *
     * @param symbol - enum with all the symbols on the angles and center of the Cards
     * @return the number of elements of a specif symbol in a Game Field, useful for objective Cards
     */
    public synchronized int getSymbolCounter(Symbol symbol){  //returns the number of Symbol in Gamefield
        return symbolCounters.get(symbol);
    }

    /**
     *
     * @return the available positions where it's possible to place the cards
     */
    public synchronized Set<Point> getAvailablePositions() {
        return new LinkedHashSet<>(availableCells);
    }

    /**
     * Method used to update the Game Field after a Player move
     * @param card - class of a specific card
     * @param cardOrientation - enum of the orientation of the card decided and placed by the Player
     * @param position - point of the place in which the player wants to place the card
     *
     */
    public synchronized void placeCard(Card card, CardOrientation cardOrientation, Point position)
    {
        if(!availableCells.contains(position)) {
            throw new InvalidParameterException("Position " + position + "not among available ones");
        } else if(!card.getSide(cardOrientation).isPlayable(this)){
            throw new InvalidParameterException("Cannot play card " + card.getId() + " on " + cardOrientation + " side");
        }

        registerCard(card, cardOrientation, position);
        updateAngles(card, cardOrientation, position);
        updateCounters(card, cardOrientation, position);
        updateAvailableCells(card, cardOrientation, position);
        insertionOrder.add(position);
    }

    /**
     * Used by placeCard to register the card in the Card map
     * @param card - class of a specific card
     * @param cardOrientation - enum of the orientation of the card decided and placed by the Player
     * @param cardPosition - point of the place in which the player wants to place the card
     */
    private void registerCard(Card card, CardOrientation cardOrientation, Point cardPosition){
        CardCell newCard = new CardCell(card, cardOrientation);
        cards.put(cardPosition, newCard);
    }

    /**
     * Used by placeCard to update the angles map
     * @param card - class of a specific card
     * @param cardOrientation - enum of the orientation of the card decided and placed by the Player
     * @param cardPosition - point of the place in which the player wants to place the card
     */
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

    /**
     * Used by placeCard to update symbolCounter set
     * @param card - class of a specific card
     * @param orientation - enum of the orientation of the card decided and placed by the Player
     * @param cardPosition - point of the place in which the player wants to place the card
     */
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

    /**
     * Used by placeCard to updateAvailableCells to know where to put the following Cards
     * @param card - class of a specific card
     * @param cardOrientation - enum of the orientation of the card decided and placed by the Player
     * @param cardPosition - point of the place in which the player wants to place the card
     */
    private void updateAvailableCells(Card card, CardOrientation cardOrientation, Point cardPosition){

        availableCells.remove(cardPosition);

        for(AnglePosition angle : AnglePosition.values()){
            Point updatedAnglePosition = cardPosition.sum(angle.getRelativePosition());
            AngleCell updatedAngle = getAngleCells().get(updatedAnglePosition);

            if(updatedAngle != null && updatedAngle.topSymbol() == Symbol.HIDDEN){
                availableCells.remove(updatedAnglePosition.sum(angle.getRelativePosition()));
            }
        }

        for(Point adjacentCardPosition : Point.getAdjacentPositions(cardPosition, 2)){
            if(isPlaceable(adjacentCardPosition)) {
                availableCells.add(adjacentCardPosition);
            }
        }
    }

    private boolean isPlaceable(Point placementPoint){

        if(getCardCells().containsKey(placementPoint)){
            return false;
        }

        for(Point adjacentAnglePosition : Point.getAdjacentPositions(placementPoint)){

            AngleCell adjacentAngleCell = getAngleCells().get(adjacentAnglePosition);

            if(adjacentAngleCell != null && adjacentAngleCell.topSymbol() == Symbol.HIDDEN){
                return false;
            }
        }

        return true;
    }

    private void incrementCounter(Symbol symbol){
        symbolCounters.put(symbol, symbolCounters.get(symbol) + 1);
    }

    private void decrementCounter(Symbol symbol){
        symbolCounters.put(symbol, symbolCounters.get(symbol) - 1);
    }

}




