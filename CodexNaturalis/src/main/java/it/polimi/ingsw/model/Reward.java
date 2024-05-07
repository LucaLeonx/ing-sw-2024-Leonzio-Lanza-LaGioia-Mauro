package it.polimi.ingsw.model;

import it.polimi.ingsw.dataobject.RewardType;
import it.polimi.ingsw.model.card.RewardFunction;

public record Reward(RewardType type, RewardFunction rewardCalculator) {}
