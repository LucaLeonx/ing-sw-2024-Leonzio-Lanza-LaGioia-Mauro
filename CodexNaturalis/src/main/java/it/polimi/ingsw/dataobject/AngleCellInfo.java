package it.polimi.ingsw.dataobject;

import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.map.Point;

import java.io.Serializable;

public record AngleCellInfo(Symbol topSymbol, Point topCardPosition) implements Serializable {}
