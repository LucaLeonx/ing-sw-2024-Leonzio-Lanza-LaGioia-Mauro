package it.polimi.ingsw.test.model.map;


import junit.framework.*;
import it.polimi.ingsw.model.map.Point;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.Assert.assertNotEquals;

public class PointTest extends TestCase {

    private Point a, b, c, d, cCopy;

    public void setUp(){
        a = new Point(0, 0);
        b = new Point(1, 1);
        c = new Point(0, 2);
        d = new Point(-1, -7);
        cCopy = new Point(0, 2);
    }

    public void testGetters(){
        assertEquals(0, a.x());
        assertEquals(0, a.y());
        assertEquals(-1, d.x());
        assertEquals(-7, d.y());
    }

    public void testEquality(){
        assertEquals(c, cCopy);
        assertNotSame(cCopy, c);
        assertNotEquals(c, b);
        assertEquals(c.hashCode(), cCopy.hashCode());
    }

    public void testSum(){
        assertEquals(new Point(0, -4), Point.sum(a,b,c,d));
        assertEquals(new Point(0,0), Point.sum());
        assertEquals(new Point(0, -4), Point.sum(Set.of(a, b, c, d)));
        assertEquals(new Point(0,0), Point.sum(new ArrayList<Point>()));
    }
}
