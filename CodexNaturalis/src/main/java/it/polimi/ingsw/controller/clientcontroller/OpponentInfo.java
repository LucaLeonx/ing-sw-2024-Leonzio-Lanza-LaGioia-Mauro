package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.model.player.PlayerColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public record OpponentInfo(String nickname,
                           PlayerColor color,
                           int score,
                           ArrayList<CardSideInfo> hand,
                           GameFieldInfo field) implements Serializable {}
