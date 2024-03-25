package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.map.Point;

public enum AnglePosition {
    /**
     * This enum represents the possible position of the angles of a Card.
     *
     */
    UP_LEFT(new Point(-1, 1)),
    UP_RIGHT(new Point(1, 1)),
    DOWN_LEFT(new Point(-1, -1)),
    DOWN_RIGHT(new Point(1, -1));

    private final Point relativePosition;

    AnglePosition(Point relativePosition){
        this.relativePosition = relativePosition;
    }

    /**
     * Returns
     * @return The position of the corrisponding angle on the GameField, relative to a card
     */
    public Point getRelativePosition(){
        return relativePosition;
    }

}
