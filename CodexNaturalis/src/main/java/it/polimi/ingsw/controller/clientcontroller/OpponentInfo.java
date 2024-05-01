package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.model.player.PlayerColor;

import java.util.List;

public record OpponentInfo(String nickname,
                           PlayerColor color,
                           int score,
                           List<CardSideInfo> hand,
                           GameFieldInfo field) {}
