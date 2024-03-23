package it.polimi.ingsw.model.card;

public class Card{

    private final int id;
    private final CardColor color;
    private final CardSide front;
    private final CardSide back;


    public Card(int id, CardColor color, CardSide front, CardSide back){
        this.id = id;   
        this.color = color;
        this.front = front;
        this.back = back;
    }

    public int getId(){
        return id;
    }

    public CardColor getCardColor() {
        return color;
    }

    public CardSide getSide(CardOrientation side) {
        return switch(side){
            case FRONT -> front;
            case BACK ->  back;
        };
    }


}
