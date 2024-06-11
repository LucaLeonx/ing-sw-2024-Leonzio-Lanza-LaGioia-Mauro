package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.clientcontroller.RMIClientController;
import it.polimi.ingsw.controller.clientcontroller.SocketClientController;
import it.polimi.ingsw.dataobject.LobbyInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class MainWindow extends JFrame {
    private static CardLayout cardLayout;
    private static JPanel cardPanel;
    private static ClientController clientController;
    public MainWindow()
    {
        setTitle("Codex Naturalis");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setVisible(true);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        ChooseConnectionPanel chooseConnectionPanel = new ChooseConnectionPanel();
        ChooseLoginPanel chooseLoginPanel= new ChooseLoginPanel();
        LoginPanel loginPanel = new LoginPanel();
        RegisterPanel registerPanel = new RegisterPanel();
        InsertNamePanel insertNamePanel = new InsertNamePanel();
        CreateNewLobbyPanel createNewLobbyPanel = new CreateNewLobbyPanel();
        GameFieldPanel gameFieldPanel = new GameFieldPanel();

        cardPanel.add("chooseConnectionPanel",chooseConnectionPanel);
        cardPanel.add("chooseLoginPanel",chooseLoginPanel);
        cardPanel.add("loginPanel",loginPanel);
        cardPanel.add("registerPanel",registerPanel);
        cardPanel.add("insertNamePanel", insertNamePanel);
        cardPanel.add("createNewLobbyPanel", createNewLobbyPanel);
        cardPanel.add("gameFieldPanel", gameFieldPanel);
        add(cardPanel);

    }

    public static CardLayout getCardLayout(){
        return cardLayout;
    }

    public static JPanel getJPanel(){
        return cardPanel;
    }

    public static ClientController getClientController(){
        return clientController;
    }

    public static void setRMIController() throws NotBoundException, RemoteException {
        clientController = new RMIClientController();
    }

    public static void setSocketController()throws IOException {
        clientController = new SocketClientController();
    }

    public static void goToWindow(String name) {
        cardLayout.show(cardPanel, name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}
