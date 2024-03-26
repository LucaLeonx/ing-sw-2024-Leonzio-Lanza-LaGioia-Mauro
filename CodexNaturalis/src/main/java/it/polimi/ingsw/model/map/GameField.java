package it.polimi.ingsw.model.map;

import java.util.*;
import java.awt.Point;
import java.lang.Math;


import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.card.Symbol;

import javax.swing.plaf.synth.SynthUI;


public class GameField{
    private Map<Point, Card> cards;
    private Map<Point, AngleCell> angles;
    private Map<Symbol, Integer> symbolCounters;
    private HashSet<Point> availableCells;

    public GameField(){ //constructor of the Field with initial card
        this.cards = new HashMap<>();
        this.angles= new HashMap<>();
        this.symbolCounters = new HashMap<>();
        this.availableCells = new HashSet<>();
    }

    public Map<Point, Card> getCards() {
        return cards;
    }

    public Map<Point, Symbol> getAngles() {
        return angles;
    }

    public int getCounter(Symbol symbol){  //returns the number of Symbol in Gamefield
        return symbolCounters.get(symbol);
    }

    public Set<Point> getAvailablePositions() {
        return availableCells;
    }

    public void addCard(Card card, CardOrientation cardOrientation, Point position)  //Passo card o cardside nel metodo??
    {
        cards.put(position, card);

        List<Point> surroundingPositions;
        /*
        surroundingPositions = new ArrayList.of(
                new Point((int) (position.getX() - 1), (int) (position.getY() + 1)),  // up left
                new Point((int) (position.getX() + 1), (int) (position.getY() + 1)),  // up right
                new Point((int) position.getX() - 1, (int) position.getY() - 1),  // down left
                new Point((int) position.getX() + 1, (int) position.getY() - 1)   // down right
        );

        // Verifica delle posizioni circostanti e aggiunta alle celle disponibili
        for (Point surroundingPosition : surroundingPositions) {
            if (!angles.containsKey(surroundingPosition) && card.getSide(cardOrientation).getA != HIDDEN) {
                availableCells.add(surroundingPosition);
            }
        }

        // Inserimento degli angoli alla mappa degli angoli

        angles.put(new Point((int) position.getX() - 1, (int) position.getY() + 1), cardOrientation.getClass(UP_LEFT));
        angles.put(new Point((int) position.getX() + 1, (int) position.getY() + 1), cardOrientation.getClass(UP_RIGHT));
        angles.put(new Point((int) position.getX() - 1, (int) position.getY() - 1), cardOrientation.getClass(DOWN_LEFT));
        angles.put(new Point((int) position.getX() + 1, (int) position.getY() - 1), cardOrientation.getClass(DOWN_RIGHT));
    */
    }

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


