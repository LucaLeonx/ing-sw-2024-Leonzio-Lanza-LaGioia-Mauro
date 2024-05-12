package it.polimi.ingsw.test.server.rmi;

import java.io.Serializable;

public record Payload(String information) implements Serializable {}
