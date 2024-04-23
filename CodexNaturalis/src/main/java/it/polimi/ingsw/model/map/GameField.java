package it.polimi.ingsw.model.map;

import java.util.*;

import it.polimi.ingsw.model.card.*;


public class GameField{
    private final Map<Point, Card> cards;
    private final Map<Point, AngleCell> angles;
    private final Map<Symbol, Integer> symbolCounters;
    private final HashSet<Point> availableCells;

    public GameField(){
        this.cards = new HashMap<>();
        this.angles= new HashMap<>();
        this.symbolCounters = new HashMap<>();
        this.availableCells = new HashSet<>();

        // Initialize counters
        for(Symbol symbol : Symbol.values()){
            symbolCounters.put(symbol, 0);
        }
    }

    public Map<Point, Card> getCards() {
        return cards;
    }

    public Map<Point, Symbol> getAngles() { //creating a new Hash up to return only position and TopSymbol
        Map <Point,Symbol> anglePositions= new HashMap<>();
        for (Point point : angles.keySet()) {
            AngleCell angleCell=angles.get(point);
            Symbol topSymbol=angleCell.topSymbol;
            anglePositions.put(point,topSymbol);
        }

        return anglePositions;
    }

    public int getCounter(Symbol symbol){  //returns the number of Symbol in Gamefield
        return symbolCounters.get(symbol);
    }

    public Set<Point> getAvailablePositions() {
        return availableCells;
    }

    public void placeCard(Card card, CardOrientation cardOrientation, Point position)
    {
        addCard(card, cardOrientation, position);
        updateCounters(position);
        updateAvailableCells(card);
    }

    private void addCard(Card card, CardOrientation orientation, Point position){

        CardSide currentSide = card.getSide(orientation);

        this.cards.put(position, card);

        for (AnglePosition angle : AnglePosition.values()) {
            if(currentSide.getSymbolFromAngle(angle) != Symbol.HIDDEN){
                AngleCell newCell = new AngleCell(position, currentSide.getSymbolFromAngle(angle));
                angles.put(position.sum(angle.getRelativePosition()), newCell);
            }
        }
    }

    private void updateCounters(Point position){
        for (AnglePosition angle : AnglePosition.values())
        {
                AngleCell angleCell=angles.get(position.sum(angle.getRelativePosition())); //angleCell represents the position in which we need to check eventually the new symbol
                Symbol topSymbol= angleCell.getTopSymbol();
                Symbol replacedSymbol = angleCell.getBottomSymbol();


                if (replacedSymbol != topSymbol) {
                    if(symbolCounters.containsKey(topSymbol)) {
                        symbolCounters.put(topSymbol, symbolCounters.get(topSymbol) + 1);
                    }
                    else {
                        symbolCounters.put(topSymbol, 1);
                    }
                    symbolCounters.put(replacedSymbol, symbolCounters.get(replacedSymbol) - 1);
                }

                //else if replaced symbol is equal to top symbol we don't need to update the number of elements

        }

        }



    private int updateAvailableCells(Card card){
        return 0;
    }



    private class AngleCell{
        private final Stack<Point> attachedCardsPosition; // there is always a maximum of 2 symbols
        private Symbol topSymbol;

        private Symbol bottomSymbol;

        AngleCell(Point topCardPosition, Symbol topSymbol) {
            this.attachedCardsPosition = new Stack<>();
            attachedCardsPosition.add(topCardPosition);
            this.topSymbol = topSymbol;
            this.bottomSymbol = topSymbol;
        }

        Symbol getTopSymbol(){
            return topSymbol;
        }

        Symbol getBottomSymbol(){
                return bottomSymbol;
        }

        Point getTopCardPosition(){
            return attachedCardsPosition.getLast();
        }

        void attachNewCard(Point position, Symbol topSymbol){
            attachedCardsPosition.add(position);
            this.bottomSymbol=this.topSymbol;   //in this way I can save the bottom symbol to update counters correctly
            this.topSymbol = topSymbol;
        }
    }



}


