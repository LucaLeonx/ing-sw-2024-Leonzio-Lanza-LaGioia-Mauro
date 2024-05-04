package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.controller.clientcontroller.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.map.AngleCell;
import it.polimi.ingsw.model.map.CardCell;
import it.polimi.ingsw.model.map.GameField;
import it.polimi.ingsw.model.map.Point;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class InfoTranslator {
    public static GameSnapshot convertToGameInfo(Game game){
        return null;
    }

    /*public static PlayerSetupInfo convertToInfo(ObjectiveCard firstObjective, ObjectiveCard secondObjective, Card initialCard){
        return new PlayerSetupInfo(convertToInfo(firstObjective), convertToInfo(secondObjective), convertToInfo(initialCard));
    }*/

    private static CardSideInfo convertToInfo(Card card, CardOrientation orientation, boolean isPlayable) {
        CardSide side = card.getSide(orientation);
        Map<AnglePosition, Symbol> angles = new HashMap<>();
        for(AnglePosition angle : AnglePosition.values()){
            angles.put(angle, side.getSymbolFromAngle(angle));
        }
        return new CardSideInfo(angles, List.copyOf(side.getCenterSymbols()),  orientation, isPlayable);
    }

    /*private static CardInfo convertToInfo(Card card, GameField field){
        return new CardInfo(card.getId(), convertToInfo(card, CardOrientation.FRONT, card.getSide(CardOrientation.FRONT).getPlayingRequirements().isSatisfied(field)), convertToInfo(card, CardOrientation.BACK, card.getSide(CardOrientation.BACK).getPlayingRequirements().isSatisfied(field));
    }*/

    private static ObjectiveInfo convertToInfo(ObjectiveCard objective){
        return new ObjectiveInfo(objective.getId(),
                Arrays.stream(objective.getClass().getMethods()).findFirst().toString());
    }

    private static RewardInfo convertToInfo(RewardFunction rewardFunction) {
        return new RewardInfo(Arrays.stream(rewardFunction.getClass().getMethods()).findFirst().toString());
    }

    /*private static GameFieldInfo convertToInfo(GameField field) {
        Map<Point, CardCellInfo> cardInfo = new HashMap<>();
        Map<Point, AngleCellInfo> angleInfo = new HashMap<>();

        for(Map.Entry<Point, CardCell> entry : field.getCardCells().entrySet()){
            cardInfo.put(entry.getKey(), convertToInfo(entry.getValue()));
        }

        for(Map.Entry<Point, AngleCell> entry : field.getAngleCells().entrySet()){
            angleInfo.put(entry.getKey(), convertToInfo(entry.getValue()));
        }

        return new GameFieldInfo(cardInfo, angleInfo, field.getAvailablePositions());
    }*/

    /*private static CardCellInfo convertToInfo(CardCell cardCell){
        return new CardCellInfo(new CardInfo(cardCell.card().getId(), cardCell.cardColor()), cardCell.orientation(), true), cardCell.orientation());
    }*/

    private static AngleCellInfo convertToInfo(AngleCell angleCell){
        return new AngleCellInfo(angleCell.topSymbol(), angleCell.topCardPosition());
    }

    /* public static JSON generateJSONInformation(Game game); */
}
