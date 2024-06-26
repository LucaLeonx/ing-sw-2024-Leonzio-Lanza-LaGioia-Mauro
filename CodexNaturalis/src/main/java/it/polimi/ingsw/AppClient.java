package it.polimi.ingsw;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.clientcontroller.ConnectionDefaultSettings;
import it.polimi.ingsw.controller.clientcontroller.ConnectionSettings;
import it.polimi.ingsw.controller.clientcontroller.RMIClientController;
import it.polimi.ingsw.dataobject.LobbyInfo;
import it.polimi.ingsw.dataobject.Message;
import it.polimi.ingsw.dataobject.MessageType;
import it.polimi.ingsw.controller.socket.SocketClient;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidOperationException;
import it.polimi.ingsw.view.tui.TUI;

import javax.naming.*;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AppClient {

    public static void main(String[] args){
        TUI tui = new TUI();
        tui.display();
    }
}


