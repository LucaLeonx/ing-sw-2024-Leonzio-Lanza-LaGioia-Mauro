package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class OtherMapsFrame extends JFrame {
    private static OtherMapsPanel otherMapsPanel;
    private static CardLayout cardLayout;
    private static JPanel cardPanel;


    public OtherMapsFrame(String oppName) throws RemoteException {
        otherMapsPanel = new OtherMapsPanel();
        otherMapsPanel.buildPanel(oppName);
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add("otherMapsPanel", otherMapsPanel);
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
