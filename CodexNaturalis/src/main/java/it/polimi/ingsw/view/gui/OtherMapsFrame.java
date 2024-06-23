package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class OtherMapsFrame extends JFrame {
    private static OtherMapsPanel otherMapsPanel;


    public OtherMapsFrame(String oppName) throws RemoteException {
        otherMapsPanel = new OtherMapsPanel();
        otherMapsPanel.buildPanel(oppName);
        this.add(otherMapsPanel);
        setVisible(true);
        this.setSize(1500, 800);
    }


}
