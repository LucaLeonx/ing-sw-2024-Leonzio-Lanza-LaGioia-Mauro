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

    /**
     *
     * @return The card identifier
     */
    public int getId(){
        return id;
    }

    /**
     *
     * @return The color, note that the color is the same on both faces of the card
     */
    public CardColor getCardColor() {
        return color;
    }

    /**
     *
     * @param side - enum that has only front and back as values
     * @return The side (CardSide type) of the face given in input
     */
    public CardSide getSide(CardOrientation side) {
        return switch(side){
            case FRONT -> front;
            case BACK ->  back;
        };
    }


}
