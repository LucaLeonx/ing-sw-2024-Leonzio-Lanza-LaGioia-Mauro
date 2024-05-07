package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.controller.clientcontroller.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PlayerSetup;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.map.AngleCell;
import it.polimi.ingsw.model.map.CardCell;
import it.polimi.ingsw.model.map.GameField;
import it.polimi.ingsw.model.map.Point;
import it.polimi.ingsw.model.player.Player;

import static it.polimi.ingsw.model.card.CardOrientation.*;
import static it.polimi.ingsw.model.DrawChoice.*;

import java.util.*;
import java.util.stream.Collectors;

public abstract class InfoTranslator {
    public static DrawableCardsInfo convertToDrawableCardsInfo(Game game){
        Map<DrawChoice, CardSideInfo> convertedInfo = new HashMap<>();

        if(!game.getResourceCardDeck().isEmpty()) {
            Card resourceTopCard = game.getResourceCardDeck().getTopCard();
            convertedInfo.put(DECK_RESOURCE, convertToCardSideInfo(resourceTopCard, BACK, true));
        }

        if(!game.getResourceCardDeck().isEmpty()) {
            Card goldTopCard = game.getResourceCardDeck().getTopCard();
            convertedInfo.put(DECK_GOLD, convertToCardSideInfo(goldTopCard, BACK, true));
        }

        for(Map.Entry<DrawChoice, Card> visibleCardEntry : game.getVisibleCards().entrySet()){
            convertedInfo.put(visibleCardEntry.getKey(), convertToCardSideInfo(visibleCardEntry.getValue(), FRONT, true));
        }

        return new DrawableCardsInfo(convertedInfo);
    }

    public static ControlledPlayerInfo convertToControlledPlayerInfo(Player player) {

        ObjectiveInfo secretObjective;

        try{
            secretObjective = convertToObjectiveInfo(player.getSecretObjective());
        } catch (NullPointerException e){
            secretObjective = new ObjectiveInfo(-1);
        }

        return new ControlledPlayerInfo(player.getNickname(), player.getColor(),
                secretObjective, player.getScore(),
                player.getCardsInHand().stream().map((card) -> convertToCardInfo(card, player.getField())).collect(Collectors.toCollection(ArrayList::new)),
                convertToFieldInfo(player.getField()));
    }

    public static ObjectiveInfo convertToObjectiveInfo(ObjectiveCard secretObjective) {
        return new ObjectiveInfo(secretObjective.getId());
    }

    public static GameFieldInfo convertToFieldInfo(GameField field) {
        HashMap<Point, CardCellInfo> cardCellInfoMap = new HashMap<>();
        HashMap<Point, AngleCellInfo> angleCellInfoMap = new HashMap<>();

        for(Map.Entry<Point, CardCell> cardCell : field.getCardCells().entrySet()){
            cardCellInfoMap.put(cardCell.getKey(), convertToCardCellInfo(cardCell.getValue()));
        }

        for(Map.Entry<Point, AngleCell> angleCell : field.getAngleCells().entrySet()){
            angleCellInfoMap.put(angleCell.getKey(), convertToAngleCellInfo(angleCell.getValue()));
        }

        return new GameFieldInfo(cardCellInfoMap, angleCellInfoMap, new HashSet<>(field.getAvailablePositions()));

    }

    private static AngleCellInfo convertToAngleCellInfo(AngleCell angleCell) {
        return new AngleCellInfo(angleCell.topSymbol(), angleCell.topCardPosition());
    }

    private static CardCellInfo convertToCardCellInfo(CardCell cardCell) {
        return new CardCellInfo(convertToCardInfo(cardCell.card(), new GameField()), cardCell.orientation());
    }

    public static PlayerSetupInfo convertToPlayerSetupInfo(PlayerSetup setup){
        return new PlayerSetupInfo(convertToObjectiveInfo(setup.objective1()),
                convertToObjectiveInfo(setup.objective2()),
                convertToCardInfo(setup.initialCard(), new GameField()));
    }

    public static CardInfo convertToCardInfo(Card card, GameField field){
        CardSideInfo front = convertToCardSideInfo(card, FRONT, card.getSide(FRONT).getPlayingRequirements().isSatisfied(field));
        CardSideInfo back = convertToCardSideInfo(card, BACK, card.getSide(BACK).getPlayingRequirements().isSatisfied(field));
        return new CardInfo(card.getId(), card.getCardColor(), front, back);
    }

    public static CardSideInfo convertToCardSideInfo(Card card, CardOrientation orientation, boolean isPlayable){
        CardSide side = card.getSide(orientation);
        HashMap<AnglePosition, Symbol> angleSymbols = new HashMap<>();
        CardType type=CardType.RESOURCE;

        for(AnglePosition angle : AnglePosition.values()){
            angleSymbols.put(angle, side.getSymbolFromAngle(angle));
        }
        if(card.getId()>=1 && card.getId()<=40){
            type=CardType.RESOURCE;
        }
        else if(card.getId()<=80 && card.getId()>=41) {
            type=CardType.GOLD;
        } else if (card.getId()>=81 && card.getId()<=86) {
            type=CardType.INITIAL;
        }

        return new CardSideInfo(angleSymbols, new HashSet<>(side.getCenterSymbols()), card.getCardColor(), orientation, type, isPlayable, new ArrayList<>());
    }

    public static OpponentInfo convertToOpponentPlayerInfo(Player player) {

        ArrayList<CardSideInfo> opponentHand = new ArrayList<>();
        for(Card card : player.getCardsInHand()){
            opponentHand.add(convertToCardSideInfo(card, BACK, true));
        }

        return new OpponentInfo(player.getNickname(), player.getColor(), player.getScore(), opponentHand, convertToFieldInfo(player.getField()));
    }
}
