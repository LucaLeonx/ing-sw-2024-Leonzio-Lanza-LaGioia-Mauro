package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.GameInfo;
import it.polimi.ingsw.controller.clientcontroller.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.card.ObjectiveCard;
import it.polimi.ingsw.model.card.RewardFunction;
import it.polimi.ingsw.model.map.CardCell;
import it.polimi.ingsw.model.map.GameField;
import it.polimi.ingsw.model.map.Point;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

import static java.util.Map.entry;

public abstract class DataMapper {
    public static GameInfo convertToGameInfo(Game game){
        return null;
    }

    public static PlayerSetupInfo convertToInfo(ObjectiveCard firstObjective, ObjectiveCard secondObjective, Card initialCard){
        return new PlayerSetupInfo(convertToInfo(firstObjective), convertToInfo(secondObjective), convertToInfo(initialCard));
    }

    private static CardSideInfo convertToInfo(Card card, CardOrientation orientation){
        //CardSide side = card.getSide(orientation);

        return new CardSideInfo(card.getCenterSymbols(), );
    }

    private static CardInfo convertToInfo(Card card){
        return new CardInfo(card.getId(), convertToInfo(card, CardOrientation.FRONT), convertToInfo(card, CardOrientation.BACK));
    }

    private static ObjectiveInfo convertToInfo(ObjectiveCard objective){
        return new ObjectiveInfo(objective.getId(),
                Arrays.stream(objective.getClass().getMethods()).findFirst().toString());
    }

    private static RewardInfo convertToInfo(RewardFunction rewardFunction) {
        return new RewardInfo(Arrays.stream(rewardFunction.getClass().getMethods()).findFirst().toString());
    }

    private static GameFieldInfo convertToInfo(GameField field) {
        return new GameFieldInfo(null, field.getAvailablePositions());

    }

    private static CardCellInfo convertToInfo(CardCell cardCell){
        return new CardCellInfo(convertToInfo(cardCell.card()), cardCell.orientation());
    }

    /* public static JSON generateJSONInformation(Game game); */
}
