package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.card.CardSide;

import java.io.Serializable;

public record CardCellInfo(CardInfo card, CardOrientation orientation) implements Serializable {
}
