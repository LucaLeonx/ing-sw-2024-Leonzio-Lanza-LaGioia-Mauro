package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.model.map.Point;

import java.util.Set;
import java.util.SortedMap;

/**
 * Data transfer object for fields
 * @param placedCards The cards placed on the field, with their position.
 *                    They are sorted based on their insertion order
 * @param availablePositions Positions available to put cards
 */
public record GameFieldInfo(SortedMap<Point, CardSideInfo> placedCards,
                            Set<Point> availablePositions) {}
