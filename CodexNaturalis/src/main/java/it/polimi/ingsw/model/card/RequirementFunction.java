package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.map.GameField;

import java.util.function.Predicate;

/**
 * This interface represents a function that evaluates whether
 * a Card can be played on the chosen CardSide
 */
public interface RequirementFunction {
    /**
     * This function evaluates whether
     *  * a Card can be played on the chosen CardSide
     * @param field The field where the card will be played
     * @return true if and only if the card satisfies the requirements to be played
     * on the given side
     */
    default public boolean isSatisfied(GameField field){
        return true;
    }
}
