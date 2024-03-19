public class Card{

    private final CardColor color;
    private final Side front;
    private final Side back;
    private final int id;

    public Card(CardColor color,CardSide front,CardColor back, int id){
        this.id = id;   
        this.color = color;
        this.front = back;
        this.back = front;
    }

    public Color getCardColor() {
        return CardColor;
    }

    public CardSide getBack() {
        return back;
    }

    public CardSide getFront() {
        return front;
    }
}
