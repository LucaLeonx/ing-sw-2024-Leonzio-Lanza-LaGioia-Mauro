package it.polimi.ingsw.dataobject;

import java.io.Serializable;

public record RewardInfo(String name, int rewardedPoints) implements Serializable {}
