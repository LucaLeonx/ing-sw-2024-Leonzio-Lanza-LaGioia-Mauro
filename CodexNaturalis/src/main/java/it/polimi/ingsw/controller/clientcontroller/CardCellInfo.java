package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.card.CardSide;

public record CardCellInfo(CardInfo card, CardOrientation orientation) {
}
