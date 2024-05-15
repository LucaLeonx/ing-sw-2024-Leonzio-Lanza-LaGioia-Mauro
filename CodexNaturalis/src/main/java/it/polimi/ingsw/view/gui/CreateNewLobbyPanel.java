package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.clientcontroller.ClientController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateNewLobbyPanel extends JPanel  {
    public CreateNewLobbyPanel() {
        buildPanel();
    }

    private void buildPanel() {
        this.setLayout(new GridBagLayout());
        JButton createLobby = new JButton("Create Lobby");
        JTextField lobbyName = new JTextField(15);

        JButton goBack = new JButton("Go Back");
        Dimension buttonSize = new Dimension(300, 25); // Imposta le dimensioni desiderate (larghezza x altezza)
        goBack.setPreferredSize(buttonSize);
     //   goBack.setSize(1000,50);


        createLobby.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindow.goToWindow("registerPanel");
            }
        });

        GridBagConstraints gbc= new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(lobbyName, gbc);

        gbc.gridx=1;
        add(createLobby, gbc);

        gbc.gridx=0;
        gbc.gridy=1;
        gbc.gridwidth=2;
        add(goBack, gbc);

    }
}