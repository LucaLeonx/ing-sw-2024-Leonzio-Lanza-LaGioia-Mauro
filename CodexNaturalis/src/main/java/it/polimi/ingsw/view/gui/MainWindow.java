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
        ChooseLoginPanel chooseloginPanel= new ChooseLoginPanel();
        LoginPanel loginPanel = new LoginPanel();
        RegisterPanel registerPanel = new RegisterPanel();
        cardPanel.add(chooseConnectionPanel);
        cardPanel.add(chooseloginPanel);
        cardPanel.add(loginPanel);
        cardPanel.add(registerPanel);

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}
