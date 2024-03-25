package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.map.GameField;

/**
 * This interface represents a function that return
 * the number of points awarded when a Card is played on the
 * given CardSide
 */
public interface RewardFunction {

    /**
     * Returns
     * the number of points awarded when a Card is played on the
     * given CardSide
     * @param field The field where the Card has been played
     * @return the number of points awarded when a Card is played on the
     * given CardSide.
     */
    public int getPoints(GameField field);
}
