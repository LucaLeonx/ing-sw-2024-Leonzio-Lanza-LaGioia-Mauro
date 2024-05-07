package it.polimi.ingsw.dataobject;

import it.polimi.ingsw.model.player.PlayerColor;

import java.io.Serializable;
import java.util.ArrayList;

public record ControlledPlayerInfo(String nickname,
                                   PlayerColor color,
                                   ObjectiveInfo secretObjective,
                                   int score,
                                   ArrayList<CardInfo> cardsInHand,
                                   GameFieldInfo field) implements Serializable {
}
