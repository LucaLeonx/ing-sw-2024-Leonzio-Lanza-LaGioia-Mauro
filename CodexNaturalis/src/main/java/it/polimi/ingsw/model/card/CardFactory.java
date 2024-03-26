package it.polimi.ingsw.model.card;

import java.util.*;

public abstract class CardFactory {

    private static final Map<CardColor, CardSide> cardBack =
            Collections.singletonMap(
                    CardColor.GREEN, new CardSide(),


            );


    private static final List<Card> cardList =
            List.of(
                    new Card()


            );

    private static List<Card> getCardList(){
        return cardList;
    }

}
