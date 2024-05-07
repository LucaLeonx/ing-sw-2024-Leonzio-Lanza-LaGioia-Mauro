package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.RewardFunction;

public record Reward(String type, RewardFunction rewardCalculator) {}
