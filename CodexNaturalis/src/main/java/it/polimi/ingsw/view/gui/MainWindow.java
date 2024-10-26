package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.ConnectionSettings;
import it.polimi.ingsw.controller.clientcontroller.RMIClientController;
import it.polimi.ingsw.controller.clientcontroller.SocketClientController;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * MainWindow is the frame container of all the JPanels of the GUI
 */
public class MainWindow extends JFrame {
    private static CardLayout cardLayout;
    private static JPanel cardPanel;
    private static ClientController clientController;
    public static ChooseConnectionPanel chooseConnectionPanel = new ChooseConnectionPanel();
    public static ChooseLoginPanel chooseLoginPanel= new ChooseLoginPanel();
    public static LoginPanel loginPanel = new LoginPanel();
    public static RegisterPanel registerPanel = new RegisterPanel();
    public static InsertNamePanel insertNamePanel = new InsertNamePanel();
    public static CreateNewLobbyPanel createNewLobbyPanel = new CreateNewLobbyPanel();
    public static WaitingPanel waitingPanel= new WaitingPanel();
    public static SetUpGamePanel setUpGamePanel = new SetUpGamePanel();
    public static GameFieldPanel gameFieldPanel = new GameFieldPanel();
    //public static OtherMapsPanel otherMapsPanel = new OtherMapsPanel();
    public static EndGamePanel endGamePanel = new EndGamePanel();
    public static ConnectionSettings connectionSettings;

    public MainWindow(ConnectionSettings newConnectionSettings)
    {
        setTitle("Codex Naturalis");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 800);
        setMinimumSize(new Dimension(1100, 800));
        setVisible(true);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        connectionSettings = newConnectionSettings;

        cardPanel.add("chooseConnectionPanel",chooseConnectionPanel);
        cardPanel.add("chooseLoginPanel",chooseLoginPanel);
        cardPanel.add("loginPanel",loginPanel);
        cardPanel.add("registerPanel",registerPanel);
        cardPanel.add("insertNamePanel", insertNamePanel);
        cardPanel.add("createNewLobbyPanel", createNewLobbyPanel);
        cardPanel.add("waitingPanel", waitingPanel);
        cardPanel.add("setUpGamePanel", setUpGamePanel);
        cardPanel.add("gameFieldPanel", gameFieldPanel);
        cardPanel.add("endGamePanel", endGamePanel);
        //cardPanel.add("otherMapsPanel", otherMapsPanel);
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
        clientController = new RMIClientController(connectionSettings);
    }

    public static void setSocketController() throws IOException {
        clientController = new SocketClientController(connectionSettings);
    }
    public static void goToWindow(String name) {
        cardLayout.show(cardPanel, name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->{
            new MainWindow(ConnectionSettings.parseConnectionSettings(args));
        });
    }
}
