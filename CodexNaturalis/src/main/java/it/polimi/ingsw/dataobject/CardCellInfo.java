package it.polimi.ingsw.dataobject;

import it.polimi.ingsw.model.card.CardOrientation;

import java.io.Serializable;

public record CardCellInfo(CardInfo card, CardOrientation orientation) implements Serializable {
}
