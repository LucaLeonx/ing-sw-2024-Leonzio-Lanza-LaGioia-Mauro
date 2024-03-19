class Card{

    private Color CardColor;
    private CardSide front;
    private CardSide back;

    public Card(Color c,CardSide f,CardColor b){
        this.CardColor = c;
        this.CardSide = b;
        this.CardSide = f;
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
