package it.polimi.ingsw.model.map;

import java.util.*;
import java.lang.Math;


import it.polimi.ingsw.model.card.*;


public class GameField{
    private Map<Point, Card> cards;
    private Map<Point, AngleCell> angles;
    private Map<Symbol, Integer> symbolCounters;
    private HashSet<Point> availableCells;

    public GameField(){ 
        this.cards = new HashMap<>();
        this.angles= new HashMap<>();
        this.symbolCounters = new HashMap<>();
        this.availableCells = new HashSet<>();
    }

    public Map<Point, Card> getCards() {
        return cards;
    }

    public Map<Point, AngleCell> getAngles() {
        return angles;
    }

    public int getCounter(Symbol symbol){  //returns the number of Symbol in Gamefield
        return symbolCounters.get(symbol);
    }

    public Set<Point> getAvailablePositions() {
        return availableCells;
    }

    public void placeCard(Card card, CardOrientation cardOrientation, Point position)  //Passo card o cardside nel metodo??
    {
        addCard(card, cardOrientation, position);

    /*    addFreePositions(card, cardOrientation, position){
            if()
        }*/


  /*     surroundingPositions = new ArrayList.of(
                new Point( (), (position.getY() + 1)),  // up left
                new Point( (position.getX() + 1), (position.getY() + 1)),  // up right
                new Point( position.getX() - 1,  (position.getY() - 1)),  // down left
                new Point((int) position.getX() + 1, (position.getY() - 1))   // down right
        );

        // Verifica delle posizioni circostanti e aggiunta alle celle disponibili
        for (AnglePosition angle : AnglePosition.values()) {
            if(currentSide.getSymbolFromAngle(angle) != Symbol.HIDDEN){
                angles.put(position.sum(angle.getRelativePosition()), AngleCell(position))
            }
        }

        // Inserimento degli angoli alla mappa degli angoli

        angles.put(new Point((int) position.getX() - 1, (int) position.getY() + 1), cardOrientation.getClass(UP_LEFT));
        angles.put(new Point((int) position.getX() + 1, (int) position.getY() + 1), cardOrientation.getClass(UP_RIGHT));
        angles.put(new Point((int) position.getX() - 1, (int) position.getY() - 1), cardOrientation.getClass(DOWN_LEFT));
        angles.put(new Point((int) position.getX() + 1, (int) position.getY() - 1), cardOrientation.getClass(DOWN_RIGHT));
*/
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

    private void addFreePositions(Card card, CardOrientation orientation, Point position){

    }

   // private void updateCounters(CardSide currentSide, card.getSide(cardOrientation);
    //manca metodo che salva in SymbolCounters il numero di Symbol sul field (?)

    private class AngleCell{
        private final Stack<Point> attachedCardsPosition;
        private Symbol topSymbol;

        AngleCell(Point topCardPosition, Symbol topSymbol) {
            this.attachedCardsPosition = new Stack<>();
            attachedCardsPosition.add(topCardPosition);
            this.topSymbol = topSymbol;
        }

        Symbol getTopSymbol(){
            return topSymbol;
        }

        Point getTopCardPosition(){
            return attachedCardsPosition.getLast();
        }

        void attachNewCard(Point position, Symbol topSymbol){
            attachedCardsPosition.add(position);
            this.topSymbol = topSymbol;
        }
    }



}


