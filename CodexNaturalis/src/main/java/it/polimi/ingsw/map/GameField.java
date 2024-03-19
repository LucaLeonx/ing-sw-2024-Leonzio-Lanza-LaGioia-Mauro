import java.util.Map;
import java.util.HashMap;
public class GameField{
    private final Map<Point,Card> cards;
    private final Map<Point,Symbol> angles;
    private final Map<Symbol,int> symbolCounters;

    public GameField(initialCard Card){
        this.cards = new HashMap<>;
        this.angles= new HashMap<>;
    }



}