package it.polimi.ingsw.view.gui;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidOperationException;
import it.polimi.ingsw.dataobject.LobbyInfo;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.LINE_END;
import static java.lang.Thread.sleep;

/**
 * This Panel show all the existing lobbies and permits user to access them
 */
public class RegisterPanel extends StandardPanel {
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final Map<String, Integer> lobbyById = new HashMap<String, Integer>();
    private Timer timer;
    public RegisterPanel() {
        removeAll();
        buildPanel();
    }

    private void buildPanel() {
        this.setLayout(new GridBagLayout());
        JButton goBack = new JButton("Go Back");

        JLabel listOfLobbies = new JLabel("List of Lobbies: ");
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/other_images/UpdateArrow.jpg"));
        JButton updateLobbylist = new JButton(icon);
        updateLobbylist.setBackground(Color.WHITE);

        JList<String> jList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(jList);

        JTextField lobbyName = new JTextField(15);
        JButton createLobby = new JButton("Create a new Lobby");

        JButton joinLobby = new JButton("Join Lobby");

        createLobby.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println(lobbyName.getText());
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
                    MainWindow.waitingPanel.buildPanel();
                    MainWindow.goToWindow("waitingPanel");
                } catch (RemoteException ex) {
                    //System.out.println(ex.getMessage());
                } /*catch (InvalidOperationException ex){
                    JOptionPane.showMessageDialog(new JFrame(),
                            "Error, the user has been removed from the game due to inactivity." +
                                    "Close the application and login again","Inactivity Error",
                            JOptionPane.ERROR_MESSAGE
                            );
                    JFrame mainWindow = (JFrame) SwingUtilities.getWindowAncestor(jList);
                    mainWindow.dispose();
                }*/
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

        //timer.start();
    }

    public void showLobbies() {
            try {
                List<LobbyInfo> lobbyList = MainWindow.getClientController().getLobbyList();
                listModel.clear();
                lobbyById.clear();
                for (LobbyInfo lobby : lobbyList) {
                    this.listModel.addElement(lobby.toString());
                    lobbyById.put(lobby.toString(), lobby.id());
                }

            } catch (RemoteException ex) {
                //System.out.println(ex);
            }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image image = null;
        try {
            image = new ImageIcon(this.getClass().getResource("/other_images/codex_game.jpg")).getImage();
        }
        catch (Exception e){
            System.out.println("Path non rilevato");
        }
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        //showLobbies();
    }
}
