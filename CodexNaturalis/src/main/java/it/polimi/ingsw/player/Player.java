import java.util.*;

public class Player {
    private final String nickname;  // readOnly attribute
    private int score;
    private GameField field;
    private PlayerColor color;
    private ArrayList<Card> cardsInHand;
    public Player(String nickname, PlayerColor color) {
        this.nickname = nickname;
        this.color = color;
        this.score = 0;  // initial score is 0
        this.field = new GameField();
        this.cardsInHand = new Arraylist<>();
    }

    public String getNickname() {
        return nickname;
    }

    public PlayerColor getColor() {
        return color;
    }

    public List<Card> getCardList() {
        return new ArrayList(cardsInHand);
    }

    public void addCard(Card newCard) {
       this.cardsInHand.add(newCard);
    }

    public void removeCard(int IdCard) throws InvalidCardException {
        int position=-1;
        for(int i=0; i<3; i++)
        {
            if(cardsInHand.get(i).getId()==IdCard)
                position=i;
        }
        if (position=-1) {
            throw new InvalidCardException("Card not in your hand");
        }
        else{
           cardsInHand.remove(position);
        }
        return;
    }

    public int getScore() {
        return score;
    }
}
