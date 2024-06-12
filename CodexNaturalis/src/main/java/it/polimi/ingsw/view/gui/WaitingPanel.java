package it.polimi.ingsw.view.gui;

import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;

import static java.lang.Thread.sleep;

public class WaitingPanel extends StandardPanel {

    public WaitingPanel(){
        buildPanel();
    }
    private void buildPanel() {
        JLabel waitingForOthers = new JLabel("Waiting for other players to join...");
        waitingForOthers.setHorizontalAlignment(SwingConstants.CENTER);
        waitingForOthers.setFont(new Font("Arial", Font.BOLD, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(waitingForOthers, gbc);

        while(true) {
            try {
                MainWindow.getClientController().waitForGameToStart();
                break;
            }
            catch(Exception e)
            {
                try {
                    sleep(1000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }

        }
        MainWindow.goToWindow("gameFieldPanel");

    }
}


