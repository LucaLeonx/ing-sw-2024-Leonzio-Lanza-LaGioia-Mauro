package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.model.card.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record CardSideInfo(
                           Map<AnglePosition, Symbol> angleSymbols,
                           Set<Symbol> centerSymbols,
                           CardColor color,
                           CardOrientation side,
                           boolean isPlayable,
                           List<Symbol> requiredSymbols) {
}
