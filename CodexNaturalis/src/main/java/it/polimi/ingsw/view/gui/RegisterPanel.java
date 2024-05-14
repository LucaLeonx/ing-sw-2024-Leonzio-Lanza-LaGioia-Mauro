package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.clientcontroller.LobbyObserver;
import it.polimi.ingsw.dataobject.LobbyInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RegisterPanel extends ButtonListPanel implements LobbyObserver {
    DefaultListModel<String> listModel= new DefaultListModel<>();

    public RegisterPanel(){
        buildPanel();
    }
    private void buildPanel(){
        this.setLayout(new GridBagLayout());
        JButton goBack = new JButton("Go Back");

        JLabel listOfLobbies = new JLabel("List of Lobbies: ");

        JList<String> list = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(list);

        JTextField lobbyName = new JTextField(15);
        JButton createLobby = new JButton("Create a new Lobby");

        createLobby.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(lobbyName.getText());
            }
        });

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindow.goToWindow("chooseLoginPanel");
            }
        });

        createLobby.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindow.goToWindow("createNewLobbyPanel");
            }
        });


        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridy=0;
        gbc.gridx=1;
        gbc.anchor = GridBagConstraints.CENTER;
        add(listOfLobbies, gbc);

        gbc.gridy=1;
        gbc.gridwidth = 2;
        add(scrollPane, gbc);

        gbc.gridy=2;
        add(createLobby, gbc);

        gbc.gridy=3;
        add(goBack, gbc);

    }


    @Override
    public void onLobbyListUpdate(List<LobbyInfo> lobbies) {
        for (LobbyInfo L: lobbies) {
            listModel.addElement(L.toString());
        }
    }

    @Override
    public void onJoinedLobbyUpdate(LobbyInfo joinedLobby) {

    }
}
