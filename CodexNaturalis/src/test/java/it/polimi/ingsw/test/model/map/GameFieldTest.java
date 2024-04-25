package it.polimi.ingsw.test.model.map;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.map.GameField;
import it.polimi.ingsw.model.map.Point;
import junit.framework.TestCase;

import java.util.List;

public class GameFieldTest extends TestCase {

    private List<Card> resourceCards;
    private List<Card> goldenCards;

    private List<Card> initialCards;

    private GameField field;
    public void setUp(){
        try {
            resourceCards = CardFactory.getResourceCards();
            goldenCards = CardFactory.getGoldCards();
            initialCards = CardFactory.getInitialCards();
        } catch (Exception e){
            assertTrue(false);
        }

        field = new GameField();
    }

    public void testDiagonalPattern(){
        field.placeCard(resourceCards.get(0), CardOrientation.BACK, new Point(2,2));
        field.placeCard(resourceCards.get(1), CardOrientation.BACK, new Point(4,4));
        field.placeCard(resourceCards.get(2), CardOrientation.BACK, new Point(6,6));

         RewardFunction patternReward = GameFunctionFactory.createDiagonalPatternMatchFunction(true, CardColor.GREEN);

         assertEquals(patternReward.getPoints(field), 2);
    }


}
