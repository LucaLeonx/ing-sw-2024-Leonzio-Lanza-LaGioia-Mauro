public class Card{

    private final CardColor color;
    private final Side front;
    private final Side back;
    private final int id;

    public Card(CardColor color, CardSide front, CardColor back, int id){
        this.id = id;   
        this.color = color;
        this.front = front;
        this.back = back;
    }

    public int getId(){
        return id;
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
