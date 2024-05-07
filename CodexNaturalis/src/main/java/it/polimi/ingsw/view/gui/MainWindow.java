package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private static CardLayout cardLayout;
    private static JPanel cardPanel;

    public MainWindow()
    {
        setTitle("Codex Naturalis");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setVisible(true);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        ChooseConnectionPanel chooseConnectionPanel = new ChooseConnectionPanel();
        ChooseLoginPanel chooseLoginPanel= new ChooseLoginPanel();
        LoginPanel loginPanel = new LoginPanel();
        RegisterPanel registerPanel = new RegisterPanel();
        cardPanel.add("chooseConnectionPanel",chooseConnectionPanel);
        cardPanel.add("chooseLoginPanel",chooseLoginPanel);
        cardPanel.add("loginPanel",loginPanel);
        cardPanel.add("registerPanel",registerPanel);
        add(cardPanel);

    }

    public static CardLayout getCardLayout(){
        return cardLayout;
    }

    public static JPanel getJPanel(){
        return cardPanel;
    }

    public static void goToNextWindow(){
        cardLayout.next(cardPanel);
    }

    public static void goToPreviousWindow(){
        cardLayout.previous(cardPanel);
    }

    public static void goToWindow(String name) {
        cardLayout.show(cardPanel, name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}
