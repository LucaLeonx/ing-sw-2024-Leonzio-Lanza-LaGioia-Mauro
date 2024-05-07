package it.polimi.ingsw.networking;


import it.polimi.ingsw.controller.clientcontroller.ControlledPlayerInfo;
import it.polimi.ingsw.controller.clientcontroller.DrawableCardsInfo;
import it.polimi.ingsw.controller.clientcontroller.OpponentInfo;
import it.polimi.ingsw.controller.clientcontroller.PlayerSetupInfo;
import it.polimi.ingsw.controller.servercontroller.RMIGameManager;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.rmi.*;

public interface RMIController extends Controller,Remote {
}
