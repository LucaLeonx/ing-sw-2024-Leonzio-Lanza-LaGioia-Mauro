package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.model.card.AnglePosition;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.card.Symbol;

import java.util.List;
import java.util.Map;

public record CardSideInfo(Map<AnglePosition, Symbol> angleSymbols,
                           List<Symbol> centerSymbols,
                           List<Symbol> requiredSymbols,
                           CardOrientation side,
                           CardColor color,
                           boolean isPlayable,
                           RewardInfo reward) {}
