package it.polimi.ingsw.test.view.tui;

import it.polimi.ingsw.controller.clientcontroller.*;
import it.polimi.ingsw.dataobject.DrawableCardsInfo;
import it.polimi.ingsw.dataobject.InfoTranslator;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.map.AngleCell;
import it.polimi.ingsw.model.map.CardCell;
import it.polimi.ingsw.model.map.GameField;
import it.polimi.ingsw.model.map.Point;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.tui.TUI;
import junit.framework.TestCase;
import it.polimi.ingsw.test.model.map.GameFieldTest;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.*;

public class TUITest extends TestCase {

    private List<Card> resourceCards;
    private List<Card> goldenCards;
    private List<Card> initialCards;
    private List<ObjectiveCard> objectiveCards;
    private Player playerTestDiagonal;
    private Player M;
    private Player T;
    private Player P;

    private TUI tui=new TUI();
    public void setUp(){
        try {
            resourceCards = CardFactory.getResourceCards();
            goldenCards = CardFactory.getGoldCards();
            initialCards = CardFactory.getInitialCards();
            objectiveCards = CardFactory.getObjectiveCards();
        } catch (Exception e){
            e.printStackTrace();
            fail();
        }
        playerTestDiagonal=new Player("Diagonale", PlayerColor.RED);
        playerTestDiagonal.getField().placeCard(initialCards.get(0), CardOrientation.BACK, new Point(0,0));
        playerTestDiagonal.getField().placeCard(resourceCards.get(0), CardOrientation.FRONT, new Point(2,2));
        playerTestDiagonal.getField().placeCard(resourceCards.get(1), CardOrientation.FRONT, new Point(4,4));
        //playerTestDiagonal.getField().placeCard(resourceCards.get(2), CardOrientation.FRONT, new Point(6,6));

        T=new Player("Topolino", PlayerColor.RED);
        M=new Player("Minnie", PlayerColor.BLUE);
        P=new Player("Paperina", PlayerColor.YELLOW);

        M.getField().placeCard(initialCards.get(2), CardOrientation.FRONT, new Point(0,0));
        M.getField().placeCard(resourceCards.get(18), CardOrientation.FRONT, new Point(2, -2));
        M.getField().placeCard(resourceCards.get(11), CardOrientation.FRONT, new Point(4, -4));
        M.getField().placeCard(resourceCards.get(12), CardOrientation.FRONT, new Point(6, -6));
        M.getField().placeCard(goldenCards.get(19), CardOrientation.FRONT, new Point(4, 0));
        M.getField().placeCard(resourceCards.get(20), CardOrientation.FRONT, new Point(-2, -2));
        M.getField().placeCard(resourceCards.get(24), CardOrientation.FRONT, new Point(-4, -4));
        M.getField().placeCard(resourceCards.get(35), CardOrientation.FRONT, new Point(-2, 2));
        M.getField().placeCard(goldenCards.get(31), CardOrientation.FRONT, new Point(0, 4));
        M.getField().placeCard(resourceCards.get(39), CardOrientation.FRONT, new Point(-2, 6));
        M.getField().placeCard(goldenCards.get(36), CardOrientation.BACK, new Point(2, 2));
        M.getField().placeCard(goldenCards.get(25), CardOrientation.FRONT, new Point(-6, -6));
        M.getField().placeCard(goldenCards.get(33), CardOrientation.FRONT, new Point(-4, 4));
        M.getField().placeCard(resourceCards.get(29), CardOrientation.FRONT, new Point(-8, -8));


        P.getField().placeCard(initialCards.get(4), CardOrientation.FRONT, new Point(0,0));
        P.getField().placeCard(resourceCards.get(25), CardOrientation.FRONT, new Point(2,2));
        P.getField().placeCard(resourceCards.getFirst(), CardOrientation.BACK, new Point(4 ,0));
        P.getField().placeCard(resourceCards.get(16), CardOrientation.FRONT, new Point(-2, 2));
        P.getField().placeCard(resourceCards.get(10), CardOrientation.FRONT, new Point(-4, 4));
        P.getField().placeCard(goldenCards.get(17), CardOrientation.FRONT, new Point(-6, 6));
        P.getField().placeCard(resourceCards.get(22), CardOrientation.FRONT, new Point(6, -2));
        P.getField().placeCard(resourceCards.get(2), CardOrientation.FRONT, new Point(4, -4));
        P.getField().placeCard(goldenCards.get(12), CardOrientation.FRONT, new Point(6, -6));
        P.getField().placeCard(goldenCards.get(21), CardOrientation.FRONT, new Point(8, -4));
        P.getField().placeCard(resourceCards.get(37), CardOrientation.FRONT, new Point(10, -6));
        P.getField().placeCard(resourceCards.get(17), CardOrientation.BACK, new Point(8, -8));
        P.getField().placeCard(goldenCards.get(32), CardOrientation.FRONT, new Point(10, -10));
        P.getField().placeCard(goldenCards.get(20), CardOrientation.FRONT, new Point(-8, 8));


        T.getField().placeCard(initialCards.get(1), CardOrientation.FRONT, new Point(0,0));
        T.getField().placeCard(resourceCards.get(31), CardOrientation.FRONT, new Point( -2, -2));
        T.getField().placeCard(resourceCards.get(13), CardOrientation.FRONT, new Point(2, -2));
        T.getField().placeCard(goldenCards.get(15), CardOrientation.BACK, new Point(4, -4));
        T.getField().placeCard(resourceCards.get(9), CardOrientation.FRONT, new Point(2, 2));
        T.getField().placeCard(resourceCards.get(14), CardOrientation.FRONT, new Point(6, -6));
        T.getField().placeCard(goldenCards.get(30), CardOrientation.FRONT, new Point(4, 4));
        T.getField().placeCard(resourceCards.get(19), CardOrientation.BACK, new Point(2, -6));
        T.getField().placeCard(goldenCards.get(2), CardOrientation.FRONT, new Point(2, 6));
        T.getField().placeCard(resourceCards.get(23), CardOrientation.FRONT, new Point(-2, 2));
        T.getField().placeCard(resourceCards.get(1), CardOrientation.BACK, new Point(0, 8));
        T.getField().placeCard(goldenCards.get(1), CardOrientation.FRONT, new Point(-4, 4));
        T.getField().placeCard(goldenCards.get(27), CardOrientation.BACK, new Point(-2, 6));
        T.getField().placeCard(goldenCards.get(18), CardOrientation.FRONT, new Point(4, 0));


    }

    // To see the real image of the maps go to Notes-->ExampleMaps
    public void testTUIWithDiagonalMap(){
        new GameFieldTest().checkInvariants(playerTestDiagonal.getField());
        tui.drawMap(InfoTranslator.convertToControlledPlayerInfo(playerTestDiagonal), InfoTranslator.convertToFieldInfo(playerTestDiagonal.getField()));
    }

    public void testTUIWithMinnieMap(){
        new GameFieldTest().checkInvariants(M.getField());
        tui.drawMap(InfoTranslator.convertToControlledPlayerInfo(M), InfoTranslator.convertToFieldInfo(M.getField()));
    }

    public void testTUIWithPaperinaMap(){
        tui.drawMap(InfoTranslator.convertToControlledPlayerInfo(P), InfoTranslator.convertToFieldInfo(P.getField()));
    }


    public void testTUIWithTopolinoMap(){
        new GameFieldTest().checkInvariants(T.getField());
        tui.drawMap(InfoTranslator.convertToControlledPlayerInfo(T), InfoTranslator.convertToFieldInfo(T.getField()));
    }

    public void testShowHand(){
        T.addCard(resourceCards.get(8));
        T.addCard(resourceCards.get(8));
        T.addCard(goldenCards.get(8));
        T.setSecretObjective(objectiveCards.get(15));
        tui.showHand(InfoTranslator.convertToControlledPlayerInfo(T));

    }

    public void testShowCardsOnTable() throws FileNotFoundException {
       Game game = new Game(List.of("Pippo", "Paperino", "Pluto", "Clarabella"));
       game.setVisibleCard(DrawChoice.DECK_RESOURCE, resourceCards.get(0));
       game.setVisibleCard(DrawChoice.DECK_GOLD, goldenCards.get(1));
       game.setVisibleCard(DrawChoice.RESOURCE_CARD_1, resourceCards.get(0));
       game.setVisibleCard(DrawChoice.RESOURCE_CARD_2, resourceCards.get(2));
       game.setVisibleCard(DrawChoice.GOLD_CARD_1, goldenCards.get(1));
       game.setVisibleCard(DrawChoice.GOLD_CARD_2, goldenCards.get(0));

       DrawableCardsInfo drawableCards = InfoTranslator.convertToDrawableCardsInfo(game);
       //System.out.println(drawableCards);
       tui.showCardsOnTable(InfoTranslator.convertToObjectiveInfo(objectiveCards.get(3)),InfoTranslator.convertToObjectiveInfo(objectiveCards.get(12)),drawableCards);
    }



}
