package it.polimi.ingsw.dataobject;

import it.polimi.ingsw.model.player.PlayerColor;

import java.io.Serializable;
import java.util.ArrayList;

public record OpponentInfo(String nickname,
                           PlayerColor color,
                           int score,
                           ArrayList<CardSideInfo> hand,
                           GameFieldInfo field) implements Serializable {}
