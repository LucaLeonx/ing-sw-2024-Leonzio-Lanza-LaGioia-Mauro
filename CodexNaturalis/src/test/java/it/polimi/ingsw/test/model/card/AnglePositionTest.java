package it.polimi.ingsw.test.model.card;

import it.polimi.ingsw.model.card.AnglePosition;
import it.polimi.ingsw.model.map.Point;
import junit.framework.*;

public class AnglePositionTest extends TestCase{

    public void testReturnedValues(){
        assertEquals(AnglePosition.UP_LEFT.getRelativePosition(), new Point(-1,1));
        assertEquals(AnglePosition.UP_RIGHT.getRelativePosition(), new Point(1,1));
        assertEquals(AnglePosition.DOWN_LEFT.getRelativePosition(), new Point(-1,-1));
        assertEquals(AnglePosition.DOWN_RIGHT.getRelativePosition(), new Point(1,-1));
    }
}
