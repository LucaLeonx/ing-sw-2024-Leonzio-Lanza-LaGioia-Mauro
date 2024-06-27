package it.polimi.ingsw.controller.servercontroller;

public record TimeoutSettings(
    int loginTimeout, // in seconds
    int moveTimeout,
    int setupTimeout,
    int endGameTimeout
){}
