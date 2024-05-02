package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.controller.clientcontroller.CardSideInfo;
import it.polimi.ingsw.controller.clientcontroller.ControlledPlayerInfo;
import it.polimi.ingsw.controller.clientcontroller.OpponentInfo;
import it.polimi.ingsw.model.DrawChoice;

import java.util.List;
import java.util.Map;

public record GameSnapshot(String currentPlayer,
                           List<String> players,
                           boolean isLastTurn,
                           Map<DrawChoice, CardSideInfo> drawableCards,
                           ControlledPlayerInfo playerInfo,
                           List<OpponentInfo> opponents) {
}
