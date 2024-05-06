package it.polimi.ingsw.test.server;

import it.polimi.ingsw.controller.clientcontroller.ObjectiveInfo;
import it.polimi.ingsw.controller.clientcontroller.OpponentInfo;
import it.polimi.ingsw.controller.servercontroller.RMIGameManager;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TestGameClient {

    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry();
        System.out.println("RMI registry bindings: ");

        RMIGameManager gameManager = (RMIGameManager) registry.lookup("Game server");
        System.out.println(gameManager.getCurrentPlayer());
        System.out.println(gameManager.getControlledPlayerInfo());
        System.out.println(gameManager.getPlayerSetup());
        System.out.println(gameManager.getDrawableCardsInfo());
        gameManager.registerPlayerSetupChoice(96, CardOrientation.FRONT);
        gameManager.registerPlayerMove(3, new Point(2,2), CardOrientation.FRONT, DrawChoice.DECK_GOLD);
        System.out.println(gameManager.getCurrentPlayer());
        System.out.println(gameManager.getControlledPlayerInfo());

    }
}
