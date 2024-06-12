package it.polimi.ingsw.view.gui;

import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;

import static java.lang.Thread.sleep;

import javax.swing.*;
import java.awt.*;

public class WaitingPanel extends StandardPanel {
    public WaitingPanel() {
    }

    public void buildPanel() {
        JLabel waitingForOthers = new JLabel("Waiting for other players to join...");
        waitingForOthers.setHorizontalAlignment(SwingConstants.CENTER);
        waitingForOthers.setFont(new Font("Arial", Font.BOLD, 20));

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(waitingForOthers, gbc);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                MainWindow.getClientController().waitForGameToStart();
                return null;
            }

            @Override
            protected void done() {
                MainWindow.goToWindow("gameFieldPanel");
                MainWindow.gameFieldPanel.buildPanel();
            }
        };

        worker.execute(); // Start the background task
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image image = null;
        try {
            //  image = new ImageIcon(Objects.requireNonNull(getClass().getResource("codex_game.jpg"))).getImage();
            image = new ImageIcon("src/main/resources/other_images/lobbyScreen.jpg").getImage();
        }
        catch (Exception e){
            System.out.println("Path non rilevato");
        }
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }
}

