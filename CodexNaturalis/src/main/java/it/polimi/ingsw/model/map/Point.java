package it.polimi.ingsw.model.map;

import java.util.*;
import java.util.stream.*;

public record Point(int x, int y) {
    /**
     * This method performs the component sum of the points passed
     * as parameters.
     * @param points the points to be summed up
     * @return The sum of the points. If no point is provided, the Point(0, 0) is returned
     */
    public static Point sum(Point... points){
        return sumPointStream(Arrays.stream(points));
    }

    /**
     * This method performs the component sum of a collections.
     * @param points The collection containing the points to be summed
     * @return The sum of the points. If the colleciton is empty, the Point(0, 0) is returned
     */
    public static Point sum(Collection<Point> points){
        return sumPointStream(points.stream());
    }

    // Just to avoid code duplication
    private static Point sumPointStream(Stream<Point> pointStream){
        return pointStream.reduce(new Point(0, 0), (a, b) -> new Point(a.x + b.x, a.y + b.y));
    }

    /**
     * This method performs the component sum of 2 points.
     * @param other The Point istance to be summed
     * @return The sum of the 2 points inside a new istance of Class Point
     */
    public Point sum(Point other){
        return new Point(this.x() + other.y(), this.y() + other.y());
    }
}
