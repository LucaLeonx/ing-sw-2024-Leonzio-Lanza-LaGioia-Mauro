package it.polimi.ingsw.controller.clientcontroller;

import java.io.Serializable;

public record RewardInfo(String name, int rewardedPoints) implements Serializable {}
