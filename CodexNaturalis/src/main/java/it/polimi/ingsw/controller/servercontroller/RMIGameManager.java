package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.controller.clientcontroller.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIGameManager extends Remote {
    public String getCurrentPlayer() throws RemoteException;
    public ControlledPlayerInfo getControlledPlayerInfo() throws RemoteException;
    public OpponentInfo getOpponentInfo(String name) throws RemoteException;
    public DrawableCardsInfo getDrawableCardsInfo() throws RemoteException;
    public PlayerSetupInfo getPlayerSetup() throws RemoteException;
    public boolean isLastTurn() throws RemoteException;
    public boolean hasGameEnded() throws RemoteException;
    public void registerPlayerSetupChoice(int chosenObjectiveId, CardOrientation initialCardOrientation) throws RemoteException;
    public void registerPlayerMove(int placedCardId,  Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice) throws RemoteException;
    public String getWinnerName();
}
