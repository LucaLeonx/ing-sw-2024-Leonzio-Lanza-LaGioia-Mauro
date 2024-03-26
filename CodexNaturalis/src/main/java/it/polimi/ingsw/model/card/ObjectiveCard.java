package it.polimi.ingsw.model.card;

public class ObjectiveCard {

    private final int id;
    private final RewardFunction rewardFunction;

    public ObjectiveCard(int id, RewardFunction rewardFunction) {
        this.id = id;
        this.rewardFunction = rewardFunction;
    }

    public int getId(){
        return id;
    }

    public RewardFunction getRewardFunction() {
        return this.rewardFunction;
    }
}
