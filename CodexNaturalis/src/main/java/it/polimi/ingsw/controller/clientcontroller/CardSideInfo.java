package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.model.card.*;

import java.io.Serializable;
import java.util.*;

public record CardSideInfo(
                           HashMap<AnglePosition, Symbol> angleSymbols,
                           HashSet<Symbol> centerSymbols,
                           CardColor color,
                           CardOrientation side,
                           CardType Type,
                           boolean isPlayable,
                           ArrayList<Symbol> requiredSymbols) implements Serializable {
}
