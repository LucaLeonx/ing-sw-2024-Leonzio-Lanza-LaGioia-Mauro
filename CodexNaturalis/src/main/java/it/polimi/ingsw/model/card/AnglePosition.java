package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.map.Point;

import java.io.Serializable;

/**
 * This enum represents the possible position of the angles of a Card.
 *
 */
public enum AnglePosition implements Serializable {
    UP_LEFT(new Point(-1, 1)),
    UP_RIGHT(new Point(1, 1)),
    DOWN_LEFT(new Point(-1, -1)),
    DOWN_RIGHT(new Point(1, -1));

    private final Point relativePosition;

    /**
     * Create a new AnglePosition
     * @param relativePosition Position of the angle relative to the center of the card
     */
    AnglePosition(Point relativePosition){
        this.relativePosition = relativePosition;
    }

    /**
     * Returns the position of the corresponding angle on the GameField, relative to a card
     * @return The position of the corresponding angle on the GameField, relative to a card
     */
    public Point getRelativePosition(){
        return relativePosition;
    }

}
