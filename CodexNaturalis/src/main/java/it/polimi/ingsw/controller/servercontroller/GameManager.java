package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.controller.clientcontroller.PlayerSetupInfo;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.InvalidOperationException;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

public interface GameManager {
    public String getCurrentPlayer() throws InvalidOperationException;
    public GameInfo getGameInfo() throws InvalidOperationException;
    public PlayerSetupInfo getPlayerSetup() throws InvalidOperationException;
    public boolean isLastTurn() throws InvalidOperationException;
    public boolean hasGameEnded() throws InvalidOperationException;
    public void registerPlayerSetupChoice(int chosenObjectiveId, CardOrientation initialCardOrientation) throws InvalidOperationException;
    public void registerPlayerMove(int placedCardId, Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice) throws InvalidOperationException;
}
