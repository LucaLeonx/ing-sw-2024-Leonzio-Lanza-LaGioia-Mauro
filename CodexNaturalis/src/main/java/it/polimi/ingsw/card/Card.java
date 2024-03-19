class Card{

    private CardColor color;
    private CardSide front;
    private CardSide back;

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
