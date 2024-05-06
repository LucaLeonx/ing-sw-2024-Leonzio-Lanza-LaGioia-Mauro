package it.polimi.ingsw.controller.clientcontroller;

import java.io.Serializable;

public record PlayerSetupInfo(ObjectiveInfo objective1,
                              ObjectiveInfo objective2,
                              CardInfo initialCard) implements Serializable {
}
