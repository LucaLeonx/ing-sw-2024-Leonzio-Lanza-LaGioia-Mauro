package it.polimi.ingsw.test.model.map;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardFactory;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.CardCell;
import it.polimi.ingsw.model.map.Point;
import junit.framework.TestCase;

import static it.polimi.ingsw.model.card.AnglePosition.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;

public class CardCellTest extends TestCase {

    private CardCell cardCellA;
    private CardCell cardCellB;

    private Card cardA;
    private Card cardB;

    public void setUp(){
        try {
            List<Card> resourceCards = CardFactory.getResourceCards();
            cardA = resourceCards.get(1);
            cardB = resourceCards.get(2);
        } catch (FileNotFoundException e){
            e.printStackTrace();
            fail();
        }

        cardCellA = new CardCell(new Point(2, 2), cardA, CardOrientation.FRONT);
        cardCellB = new CardCell(new Point(4, -6), cardB, CardOrientation.BACK);
    }

    public void testPositionGetter(){
        assertEquals(new Point(2,2), cardCellA.getPosition());
    }
    public void testCardInfoGetters(){
        assertEquals(cardA, cardCellA.getCard());
        assertEquals(cardA.getCardColor(), cardCellA.getCardColor());
        assertEquals(cardA.getSide(CardOrientation.FRONT), cardCellA.getVisibleCardSide());
    }

    public void testAdjacentAnglesGetter(){
        assertEquals(Set.of(
                new Point(0,0),
                new Point(4,4),
                new Point(0,4),
                new Point(4,0)), cardCellA.getAdjacentCardsPosition());
        assertEquals(Set.of(
                new Point(6,-4),
                new Point(2,-8),
                new Point(6, -8),
                new Point(2,-4)), cardCellB.getAdjacentCardsPosition());
    }

    public void testAngleCovering(){
        assertEquals(Set.of(), cardCellA.getCoveringCardsPositions());
        assertEquals(Set.of(), cardCellB.getCoveringCardsPositions());
        cardCellA = cardCellA.withCoveredAngles(UP_RIGHT, DOWN_LEFT);
        cardCellB = cardCellB.withCoveredAngles(UP_RIGHT, UP_LEFT, DOWN_RIGHT, DOWN_LEFT);

        assertEquals(Set.of(new Point(4,4), new Point(0, 0)), cardCellA.getCoveringCardsPositions());
        assertEquals(Set.of(
                new Point(6,-4),
                new Point(2,-8),
                new Point(6, -8),
                new Point(2,-4)), cardCellB.getCoveringCardsPositions());
    }




}
