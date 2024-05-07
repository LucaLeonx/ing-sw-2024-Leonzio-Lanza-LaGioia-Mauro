package it.polimi.ingsw.dataobject;

import java.io.Serializable;

public record PlayerSetupInfo(ObjectiveInfo objective1,
                              ObjectiveInfo objective2,
                              CardInfo initialCard) implements Serializable {
}
