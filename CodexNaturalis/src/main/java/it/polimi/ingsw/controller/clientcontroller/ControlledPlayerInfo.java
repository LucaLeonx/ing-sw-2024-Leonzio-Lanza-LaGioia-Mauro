package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.model.player.PlayerColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public record ControlledPlayerInfo(String nickname,
                                   PlayerColor color,
                                   ObjectiveInfo secretObjective,
                                   int score,
                                   ArrayList<CardInfo> cardsInHand,
                                   GameFieldInfo field) implements Serializable {
}
