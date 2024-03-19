public class Card{

    private static CardColor color;
    private static Side front;
    private static Side back;

    public Card(CardColor color,CardSide front,CardColor back){
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
