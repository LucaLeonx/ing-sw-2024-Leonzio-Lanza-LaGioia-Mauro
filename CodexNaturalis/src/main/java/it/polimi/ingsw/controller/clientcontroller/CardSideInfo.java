package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.model.card.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record CardSideInfo(
                           Map<AnglePosition, Symbol> angleSymbols,
                           List<Symbol> centerSymbols,
                           CardOrientation side,
                           boolean isPlayable) {
}
