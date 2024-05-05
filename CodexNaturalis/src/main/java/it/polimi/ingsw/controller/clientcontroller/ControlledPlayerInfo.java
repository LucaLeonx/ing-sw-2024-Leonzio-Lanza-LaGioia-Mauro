package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.model.player.PlayerColor;

import java.util.List;

public record ControlledPlayerInfo(String nickname,
                                   PlayerColor color,
                                   ObjectiveInfo secretObjective,
                                   int score,
                                   List<CardInfo> cardsInHand,
                                   GameFieldInfo field) {
}
