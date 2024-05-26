package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.ElementNotFoundException;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidCredentialsException;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.WrongPhaseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class CreateNewLobbyPanel extends StandardPanel  {
    public CreateNewLobbyPanel() {
        buildPanel();
    }

    private void buildPanel() {
        this.setLayout(new GridBagLayout());
        JButton createLobby = new JButton("Create Lobby");
        JTextField lobbyName = new JTextField(15);

        Integer[] players = {2, 3, 4};
        JComboBox<Integer> numberOption = new JComboBox<>(players);

        JButton goBack = new JButton("Go Back");
        Dimension buttonSize = new Dimension(300, 25); // Imposta le dimensioni desiderate (larghezza x altezza)
        goBack.setPreferredSize(buttonSize);

        createLobby.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(lobbyName.getText().isEmpty())
                    return;

                try {
                    Integer selected= (Integer) numberOption.getSelectedItem();
                    MainWindow.getClientController().createLobby(lobbyName.getText(),selected);
                } catch (ElementNotFoundException | WrongPhaseException | RemoteException | NullPointerException ex) {
                    System.out.println(ex.getMessage());
                }

                try {
                    MainWindow.getClientController().getLobbyList();
                } catch (RemoteException ex) {
                    return;
                }
                //implement number of players

                MainWindow.goToWindow("gameFieldPanel");
                resetPanel();
                buildPanel();
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
        add(numberOption, gbc);

        gbc.gridx=2;
        add(createLobby, gbc);

        gbc.gridx=0;
        gbc.gridy=1;
        gbc.gridwidth=3;
        add(goBack, gbc);



    }
}
