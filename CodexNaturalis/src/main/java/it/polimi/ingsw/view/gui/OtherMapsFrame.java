package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.clientcontroller.RMIClientController;
import it.polimi.ingsw.controller.clientcontroller.SocketClientController;
import it.polimi.ingsw.dataobject.CardCellInfo;
import it.polimi.ingsw.dataobject.GameFieldInfo;
import it.polimi.ingsw.dataobject.OpponentInfo;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OtherMapsFrame extends JFrame {
    private static OtheMapsPanel otheMapsPanel;
    private static CardLayout cardLayout;
    private static JPanel cardPanel;


    public OtherMapsFrame(String oppName) throws RemoteException {
        otheMapsPanel= new OtheMapsPanel();
        otheMapsPanel.buildPanel(oppName);
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add("otherMapsPanel",otheMapsPanel);
        add(cardPanel);
        setVisible(true);

        this.setSize(1500, 800);
    }

    public static CardLayout getCardLayout(){
        return cardLayout;
    }

    public static JPanel getJPanel(){
        return cardPanel;
    }

    public static void goToWindow(String name) {
        cardLayout.show(cardPanel, name);
    }


}
