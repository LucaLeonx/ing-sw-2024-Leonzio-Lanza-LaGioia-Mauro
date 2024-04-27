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

        cardCellA = new CardCell(cardA, CardOrientation.FRONT);
        cardCellB = new CardCell(cardB, CardOrientation.BACK);
    }

    public void testCardInfoGetters(){
        assertEquals(cardA, cardCellA.card());
        assertEquals(CardOrientation.FRONT, cardCellA.orientation());
        assertEquals(cardA.getCardColor(), cardCellA.cardColor());
        assertEquals(cardA.getSide(CardOrientation.FRONT), cardCellA.visibleCardSide());
    }
}
