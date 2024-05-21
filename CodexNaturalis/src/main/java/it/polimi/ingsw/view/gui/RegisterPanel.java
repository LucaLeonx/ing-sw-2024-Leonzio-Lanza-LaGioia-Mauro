package it.polimi.ingsw.view.gui;

import com.sun.tools.javac.Main;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidOperationException;
import it.polimi.ingsw.dataobject.LobbyInfo;

import javax.print.attribute.standard.OrientationRequested;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.LINE_END;

public class RegisterPanel extends StandardPanel {
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final Map<String, Integer> lobbyById = new HashMap<String, Integer>();

    public RegisterPanel() {
        buildPanel();
    }

    private void buildPanel() {
        this.setLayout(new GridBagLayout());
        JButton goBack = new JButton("Go Back");

        JLabel listOfLobbies = new JLabel("List of Lobbies: ");

        JButton updateLobbylist = new JButton("↻");
        updateLobbylist.setFont(new Font("Arial", Font.BOLD, 20));

        JList<String> jList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(jList);

        JTextField lobbyName = new JTextField(15);
        JButton createLobby = new JButton("Create a new Lobby");

        JButton joinLobby = new JButton("Join Lobby");


        createLobby.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(lobbyName.getText());
                MainWindow.goToWindow("createNewLobbyPanel");
            }
        });

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindow.goToWindow("chooseLoginPanel");
            }
        });

        updateLobbylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLobbies();
            }
        });

        joinLobby.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String lobbyrow = jList.getSelectedValue();
                try {
                    MainWindow.getClientController().joinLobby(lobbyById.get(lobbyrow));
                } catch (RemoteException | InvalidOperationException ex) {
                    System.out.println(ex.getMessage());
                    return;
                }
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridy = 0;
        gbc.gridx = 1;
        add(listOfLobbies, gbc);

        gbc.gridx = 2;
        gbc.anchor = LINE_END;
        add(updateLobbylist, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = CENTER;
        add(scrollPane, gbc);

        gbc.gridy = 2;
        add(createLobby, gbc);

        gbc.gridy = 3;
        add(goBack, gbc);

        gbc.gridy = 4;
        add(joinLobby, gbc);

    }

    public void showLobbies() {
        if (this.isShowing()) {
            try {
                List<LobbyInfo> lobbyList = MainWindow.getClientController().getLobbyList();
                listModel.clear();
                lobbyById.clear();
                for (LobbyInfo lobby : lobbyList) {
                    this.listModel.addElement(lobby.toString());
                    lobbyById.put(lobby.toString(), lobby.id());
                }

            } catch (RemoteException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
