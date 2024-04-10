package it.polimi.ingsw.model.card;

import java.util.*;

public abstract class CardFactory {


    private static CardSide createBackSide(Symbol centerSymbol){
        return new CardSide(new HashSet<>().add(centerSymbol), Map.of(
                AnglePosition.UP_LEFT, Symbol.BLANK,
                AnglePosition.UP_RIGHT, Symbol.BLANK,
                AnglePosition.DOWN_LEFT, Symbol.BLANK,
                AnglePosition.DOWN_RIGHT, Symbol.BLANK
        ));
    }

    private static List<Card> getCardList(){
        return List.of();
    }

}
