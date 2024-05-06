package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.CardOrientation;

import java.io.Serializable;

public record CardInfo(int id,
                       CardSideInfo front,
                       CardSideInfo back) implements Serializable {
    public CardSideInfo getSide(CardOrientation orientation){
        return switch(orientation) {
            case CardOrientation.FRONT -> this.front();
            case CardOrientation.BACK -> this.back();
        };
    }
}
