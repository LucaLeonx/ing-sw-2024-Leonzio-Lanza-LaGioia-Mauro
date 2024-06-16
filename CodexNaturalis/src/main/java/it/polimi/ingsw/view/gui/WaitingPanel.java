package it.polimi.ingsw.view.gui;

import com.sun.tools.javac.Main;
import it.polimi.ingsw.controller.clientcontroller.ClientController;

import javax.swing.*;
import java.awt.*;

import static java.lang.Thread.sleep;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * In this Window players wait for other players to join the lobby
 */
public class WaitingPanel extends StandardPanel {
    private ExecutorService executor;
    private volatile boolean isCancelled;
    public WaitingPanel() {

    }

    public void buildPanel() {
        isCancelled=false;
        removeAll();
        ExecutorService executor =  Executors.newSingleThreadExecutor();
        JLabel waitingForOthers = new JLabel("Waiting for other players to join...");
        waitingForOthers.setHorizontalAlignment(SwingConstants.CENTER);
        waitingForOthers.setFont(new Font("Arial", Font.BOLD, 20));

        JButton backButton = new JButton("Back to Lobby Creation");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MainWindow.getClientController().exitFromLobby();
                    isCancelled = true; // Set cancellation flag
                    executor.shutdownNow();
                    MainWindow.goToWindow("createNewLobbyPanel");
                    //MainWindow.createNewLobbyPanel.buildPanel();
                } catch (RemoteException ex) {
                    System.out.println("Action not possible, game already started");
                }
            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(waitingForOthers, gbc);

        gbc.gridy = 1; // Position the button below the label
        add(backButton, gbc);

        // Start a new thread for the background task
        executor.submit(()-> {
                // Perform the long-running task
                MainWindow.getClientController().waitForGameToStart();
                // Check cancellation flag before transitioning to game panel
                if (!isCancelled) {
                        MainWindow.goToWindow("setUpGamePanel");
                        MainWindow.setUpGamePanel.buildPanel();
                }
        });
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image image = null;
        try {
            image = new ImageIcon("src/main/resources/other_images/lobbyScreen.jpg").getImage();
        }
        catch (Exception e){
            System.out.println("Path non rilevato");
        }
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }
}

