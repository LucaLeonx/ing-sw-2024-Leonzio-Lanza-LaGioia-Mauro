package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.clientcontroller.CardSideInfo;
import it.polimi.ingsw.controller.clientcontroller.ControlledPlayerInfo;
import it.polimi.ingsw.controller.clientcontroller.OpponentInfo;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.player.Player;

import java.util.List;
import java.util.Map;

public record GameInfo(String currentPlayer,
                       List<String> players,
                       boolean isLastTurn,
                       Map<DrawChoice, CardSideInfo> drawableCards,
                       ControlledPlayerInfo playerInfo,
                       List<OpponentInfo> opponents) {

}
