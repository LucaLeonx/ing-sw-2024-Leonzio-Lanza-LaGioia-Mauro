package it.polimi.ingsw.model.card;

public class ObjectiveCard {
    private final RewardFunction rewardFunction;

    public ObjectiveCard(RewardFunction rewardFunction) {
        this.rewardFunction = rewardFunction;
    }

    public RewardFunction getRewardFunction() {
        return this.rewardFunction;
    }
}
