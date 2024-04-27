package it.polimi.ingsw.test.model.map;

import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.map.AngleCell;
import it.polimi.ingsw.model.map.Point;
import junit.framework.TestCase;

public class AngleCellTest extends TestCase {

    private AngleCell angleA;
    private AngleCell angleB;
    public void setUp(){
        angleA = new AngleCell(new Point(1,1), new Point(2,2), new Point(0,0), Symbol.BLANK, Symbol.ANIMAL);
        angleB = new AngleCell(new Point(1, 1), new Point(0,0), Symbol.ANIMAL);
    }

    public void testGetters(){
        assertEquals(angleA.position(), new Point(1,1));
        assertEquals(angleA.topCardPosition(), new Point(2,2));
        assertEquals(angleA.bottomCardPosition(), new Point(0,0));
        assertEquals(angleA.topSymbol(), Symbol.BLANK);
        assertEquals(angleA.bottomSymbol(), Symbol.ANIMAL);

        assertEquals(angleB.topCardPosition(), angleB.bottomCardPosition());
        assertEquals(angleB.topSymbol(), angleB.bottomSymbol());
    }

    public void testAngleUpdate(){
        angleB = angleB.withNewTopCard(new Point(2,2), Symbol.BLANK);
        assertEquals(angleA, angleB);
    }
}
