package it.polimi.ingsw.dataobject;

import it.polimi.ingsw.model.map.Point;

import java.io.Serializable;
import java.util.*;

/**
 * Data transfer object for fields
 * @param placedCards The cards placed on the field, with their position.
 *                    They are sorted based on their insertion order
 * @param availablePositions Positions available to put cards
 */
public record GameFieldInfo(HashMap<Point, CardCellInfo> placedCards,
                            HashMap<Point, AngleCellInfo> placedAngles,
                            ArrayList<Point> availablePositions) implements Serializable {}
