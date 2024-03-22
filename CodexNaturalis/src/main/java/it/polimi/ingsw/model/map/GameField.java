import java.util.Map;
import java.util.HashMap;
import java.awt.Point;

public class GameField{
    private Map<Point,Card> cards;
    private Map<Point,Symbol> angles;
    private Map<Symbol,int> symbolCounters;
    private HashSet<Point> availableCells;

    public GameField(initialCard Card){ //constructor of the Field with initial card
        this.cards = new HashMap<>;
        this.angles= new HashMap<>;
        this.symbolCounters=new HashMap<>;
        this.availableCells= new HashSet<>;
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

    public Point[] getAvailablePositions() {
        return availableCells;
    }

    public addCard(Card card, CardOrientation cardOrientation, Point position)  //Passo card o cardside nel metodo??
    {
        cards.put(position, card);

        Point[] surroundingPositions = {
                new Point(position.getX() - 1, position.getY() + 1),  // up left
                new Point(position.getX() + 1, position.getY() + 1),  // up right
                new Point(position.getX() - 1, position.getY() - 1),  // down left
                new Point(position.getX() + 1, position.getY() - 1)   // down right
        };

        // Verifica delle posizioni circostanti e aggiunta alle celle disponibili
        for (Point surroundingPosition : surroundingPositions) {
            if (!angles.containsKey(surroundingPosition) && cardOrientation.getSymbolFromAngle() != HIDDEN) {
                availableCells.add(surroundingPosition);
            }
        }

        // Inserimento degli angoli alla mappa degli angoli

        angles.put(new Point(position.getX() - 1, position.getY() + 1), cardOrientation.getSymbolFromAngle(UP_LEFT));
        angles.put(new Point(position.getX() + 1, position.getY() + 1), cardOrientation.getSymbolFromAngle(UP_RIGHT));
        angles.put(new Point(position.getX() - 1, position.getY() - 1), cardOrientation.getSymbolFromAngle(DOWN_LEFT));
        angles.put(new Point(position.getX() + 1, position.getY() - 1), cardOrientation.getSymbolFromAngle(DOWN_RIGHT));
    }

    //manca metodo che salva in SymbolCounters il numero di Symbol sul field (?)

}