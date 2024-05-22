package it.polimi.ingsw.dataobject;

import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import java.io.Serializable;

public record MoveInfo(CardInfo card,
                       Point placementPoint,
                       CardOrientation chosenSide,
                       DrawChoice drawChoice) implements Serializable {}
