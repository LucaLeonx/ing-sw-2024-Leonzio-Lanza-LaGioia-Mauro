package it.polimi.ingsw.dataobject;

import it.polimi.ingsw.controller.clientcontroller.*;
import it.polimi.ingsw.controller.servercontroller.Lobby;
import it.polimi.ingsw.controller.servercontroller.User;
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

    public static DrawableCardsInfo convertToDrawableCardsInfo(Map<DrawChoice, Card> drawableCards){
        Map<DrawChoice, CardSideInfo> convertedInfo = new HashMap<>();

        for(Map.Entry<DrawChoice, Card> entry : drawableCards.entrySet()){

            CardOrientation chosenOrientation = switch(entry.getKey()){
                case DECK_RESOURCE, DECK_GOLD -> BACK;
                default -> FRONT;
            };

            convertedInfo.put(entry.getKey(), convertToCardSideInfo(entry.getValue(), chosenOrientation, true));
        }

        return new DrawableCardsInfo(convertedInfo);
    }

    public static ControlledPlayerInfo convertToControlledPlayerInfo(Player player) {

        ObjectiveInfo secretObjective;

        try{
            secretObjective = convertToObjectiveInfo(player.getSecretObjective());
        } catch (NullPointerException e){
            secretObjective = null;
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
        HashMap<Symbol, Integer> symbolCountersInfoMap = new HashMap<>();
        List<Point> insertionOrder = new ArrayList<>(field.getInsertionOrder());

        for(Map.Entry<Point, CardCell> cardCell : field.getCardCells().entrySet()){
            cardCellInfoMap.put(cardCell.getKey(), convertToCardCellInfo(cardCell.getValue()));
        }

        for(Map.Entry<Point, AngleCell> angleCell : field.getAngleCells().entrySet()){
            angleCellInfoMap.put(angleCell.getKey(), convertToAngleCellInfo(angleCell.getValue()));
        }

        for(Map.Entry<Symbol, Integer> symbolCounter  : field.getSymbolCounters().entrySet()){
            symbolCountersInfoMap.put(symbolCounter.getKey() , symbolCounter.getValue());
        }

        return new GameFieldInfo(cardCellInfoMap, angleCellInfoMap, new ArrayList<>(field.getAvailablePositions()), symbolCountersInfoMap,insertionOrder);

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
        CardSideInfo front = convertToCardSideInfo(card, FRONT, card.getSide(FRONT).isPlayable(field));
        CardSideInfo back = convertToCardSideInfo(card, BACK, card.getSide(BACK).isPlayable(field));
        return new CardInfo(card.getId(), card.getCardColor(), front, back);
    }

    public static CardSideInfo convertToCardSideInfo(Card card, CardOrientation orientation, boolean isPlayable){
        CardSide side = card.getSide(orientation);
        HashMap<AnglePosition, Symbol> angleSymbols = new HashMap<>();

        for(AnglePosition angle : AnglePosition.values()){
            angleSymbols.put(angle, side.getSymbolFromAngle(angle));
        }

        CardType type = CardType.RESOURCE;

        if(card.getId()>=1 && card.getId()<=40){
            type = CardType.RESOURCE;
        }
        else if(card.getId()<=80 && card.getId()>=41) {
            type = CardType.GOLD;
        } else if (card.getId()>=81 && card.getId()<=86) {
            type = CardType.INITIAL;
        }

        return new CardSideInfo(
                card.getId(),
                angleSymbols,
                new HashSet<>(side.getCenterSymbols()),
                card.getCardColor(), orientation,
                type,
                isPlayable,
                new ArrayList<>(side.getPlayingRequirements().getRequiredSymbols()),
                side.getPlayingReward().type());
    }

    public static OpponentInfo convertToOpponentPlayerInfo(Player player) {

        ArrayList<CardSideInfo> opponentHand = new ArrayList<>();
        for(Card card : player.getCardsInHand()){
            opponentHand.add(convertToCardSideInfo(card, BACK, true));
        }

        return new OpponentInfo(player.getNickname(), player.getColor(), player.getScore(), opponentHand, convertToFieldInfo(player.getField()));
    }

    public static LobbyInfo convertToLobbyInfo(Lobby lobby) {
        return new LobbyInfo(lobby.getId(),
                lobby.getName(),
                lobby.getCreatorUsername(),
                new ArrayList<>(lobby.getConnectedUsers().stream().map(User::getUsername).toList()),
                lobby.getRequiredNumOfPlayers(),
                lobby.getNumOfWaitingPlayers());
    }
}
