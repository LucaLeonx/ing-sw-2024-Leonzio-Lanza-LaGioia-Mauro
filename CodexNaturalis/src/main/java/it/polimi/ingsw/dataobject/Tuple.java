package it.polimi.ingsw.dataobject;

import java.io.Serializable;

public record Tuple(Object first, Object second) implements Serializable {}
