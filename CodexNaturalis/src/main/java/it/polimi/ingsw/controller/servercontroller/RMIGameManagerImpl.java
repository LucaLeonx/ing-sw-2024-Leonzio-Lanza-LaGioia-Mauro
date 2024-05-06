package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.controller.clientcontroller.ControlledPlayerInfo;
import it.polimi.ingsw.controller.clientcontroller.DrawableCardsInfo;
import it.polimi.ingsw.controller.clientcontroller.OpponentInfo;
import it.polimi.ingsw.controller.clientcontroller.PlayerSetupInfo;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.InvalidOperationException;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RMIGameManagerImpl extends UnicastRemoteObject implements RMIGameManager{
    private GameState state;

    public RMIGameManagerImpl(GameState previousState) throws RemoteException{
        this.state = previousState;
    }

    public RMIGameManagerImpl(String controlledPlayer, List<String> playerNames) throws RemoteException{
        this.state = new SetupState(controlledPlayer, playerNames, this);
    }

    @Override
    public String getCurrentPlayer()  {
        return state.getCurrentPlayerNickname();
    }
    @Override
    public ControlledPlayerInfo getControlledPlayerInfo()  {
        return state.getControlledPlayerInfo();
    }

    @Override
    public OpponentInfo getOpponentInfo(String name) {
        return state.getOpponentPlayerInfo(name);
    }

    @Override
    public DrawableCardsInfo getDrawableCardsInfo() {
        return state.getDrawableCardsInfo();
    }

    @Override
    public PlayerSetupInfo getPlayerSetup() {
        return state.getPlayerSetup();
    }

    @Override
    public boolean isLastTurn() {
        return state.isLastTurn();
    }

    @Override
    public boolean hasGameEnded() {
        return state.hasGameEnded();
    }

    @Override
    public void registerPlayerSetupChoice(int chosenObjectiveId, CardOrientation initialCardOrientation){
        state.registerPlayerSetupChoice(chosenObjectiveId, initialCardOrientation);
    }

    @Override
    public void registerPlayerMove(int placedCardId,  Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice){
        state.makeCurrentPlayerMove(placedCardId, chosenSide, placementPoint, drawChoice);
    }

    public synchronized void setState(GameState newState) {
        this.state = newState;
    }
}
