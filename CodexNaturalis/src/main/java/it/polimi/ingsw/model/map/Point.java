package it.polimi.ingsw.model.map;

import it.polimi.ingsw.model.card.AnglePosition;

import java.io.Serializable;
import java.util.*;
import java.util.stream.*;

public record Point(int x, int y) implements Serializable {
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
     * @return The sum of the points. If the collection is empty, the Point(0, 0) is returned
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
     * @param other The Point instance to be summed
     * @return The sum of the 2 points inside a new instance of Class Point
     */
    public Point sum(Point other){
        return new Point(this.x() + other.x(), this.y() + other.y());
    }

    /**
     * This method returns a new Point whose coordinates are those of the caller,
     * scaled (multiplied) by a given number
     * @param factor The factor to which scale the coordinates
     * @return A new Point whose coordinates are those of the caller, scaled by the specified factor
     */
    public Point scale(int factor){
        return new Point(this.x() * factor, this.y() * factor);
    }

    public Point inverse() {
        return this.scale(-1);
    }

    public static Set<Point> getAdjacentPositions(Point position, int scaleFactor){
        return Stream.of(AnglePosition.values())
                .map((angle) -> angle.getRelativePosition().scale(scaleFactor))
                .map((relativePosition) -> Point.sum(position, relativePosition))
                .collect(Collectors.toUnmodifiableSet());
    }

    public String toString(){ return " (" + this.x() + ", " + this.y() + ")"; }

    public static Set<Point> getAdjacentPositions(Point position){
        return getAdjacentPositions(position, 1);
    }
}
