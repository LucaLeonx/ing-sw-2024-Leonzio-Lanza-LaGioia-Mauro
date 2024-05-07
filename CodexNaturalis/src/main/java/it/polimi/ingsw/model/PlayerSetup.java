package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.ObjectiveCard;

public record PlayerSetup(ObjectiveCard objective1, ObjectiveCard objective2, Card initialCard){}
