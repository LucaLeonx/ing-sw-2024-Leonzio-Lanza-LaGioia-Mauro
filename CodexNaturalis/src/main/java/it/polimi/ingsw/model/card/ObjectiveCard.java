package it.polimi.ingsw.model.card;

/**
 * This class represents an Objective Card
 */
public class ObjectiveCard {

    private final int id;
    private final RewardFunction rewardFunction;

    /**
     * Class constructor
     * @param id The unique identifier of the card
     * @param rewardFunction The function that specifies the number of points to award when the objective is completed
     */
    public ObjectiveCard(int id, RewardFunction rewardFunction) {
        this.id = id;
        this.rewardFunction = rewardFunction;
    }

    /**
     *
     * @return The identifier of the ObjectiveCard
     */
    public int getId(){
        return id;
    }

    /**
     *
     * @return The function that calculates the number of points to award when the objective is satisfied
     */
    public RewardFunction getRewardFunction() {
        return this.rewardFunction;
    }
}
