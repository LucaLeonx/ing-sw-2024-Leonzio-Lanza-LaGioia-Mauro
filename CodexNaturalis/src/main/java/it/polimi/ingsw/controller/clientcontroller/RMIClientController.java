package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.controller.servercontroller.RMIGameManager;
import it.polimi.ingsw.controller.servercontroller.RMIGameManagerImpl;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;
import it.polimi.ingsw.networking.RMIController;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;
/*
public class RMIClientController implements ClientController{

    private RMIController rmiController;
    private RMIGameManagerImpl gameManager;

    public RMIClientController() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry();
        rmiController = (RMIController) registry.lookup("Codex_naturalis_server");
    }

    @Override
    public List<String> getPlayerNames() {

    }

    @Override
    public PlayerSetupInfo getPlayerSetup() {
        return null;
    }

    @Override
    public ControlledPlayerInfo getControlledPlayerInformation() {
        return null;
    }

    @Override
    public OpponentInfo getOpponentInformation(String opponentName) {
        return null;
    }

    @Override
    public Map<DrawChoice, CardSideInfo> getDrawableCards() {
        return null;
    }

    @Override
    public String getCurrentPlayerName() {
        return null;
    }

    @Override
    public boolean isLastTurn() {
        return false;
    }

    @Override
    public boolean hasGameEnded() {
        return false;
    }

    @Override
    public String getWinner() {
        return null;
    }

    @Override
    public void setPlayerSetup(ObjectiveInfo chosenObjective, CardOrientation initialCardSide) {

    }

    @Override
    public void makeMove(CardInfo card, Point placementPoint, DrawChoice drawchoice) {

    }

    @Override
    public void start() {

    }

    @Override
    public void close() {

    }

}*/
