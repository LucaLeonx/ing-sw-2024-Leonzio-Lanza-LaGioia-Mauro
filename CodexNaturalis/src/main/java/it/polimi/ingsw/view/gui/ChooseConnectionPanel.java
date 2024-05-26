package it.polimi.ingsw.view.gui;

import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ChooseConnectionPanel extends StandardPanel {

    public ChooseConnectionPanel() {
        buildPanel();
    }

    private void buildPanel() {
        this.setLayout(new GridBagLayout());

        JButton socketButton= new JButton("Socket");
        JButton rmiButton= new JButton("RMI");
        socketButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        rmiButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        ButtonStyle(socketButton);
        ButtonStyle(rmiButton);

        JLabel nameTextArea = new JLabel("Welcome to Codex Naturalis");
        JLabel chooseTextArea = new JLabel("Choose your connection:");
        nameTextArea.setBackground(Color.white);
        nameTextArea.setHorizontalAlignment(SwingConstants.CENTER);
        nameTextArea.setOpaque(true);
        nameTextArea.setFont(new Font("Arial", Font.BOLD, 20));  // Font enlarged

        chooseTextArea.setBackground(Color.white);
        chooseTextArea.setHorizontalAlignment(SwingConstants.CENTER);
        chooseTextArea.setOpaque(true);
        chooseTextArea.setFont(new Font("Arial", Font.BOLD, 20));  // Font enlarged

        JLabel wrongConnection = new JLabel();
        wrongConnection.setBackground(Color.white);
        wrongConnection.setHorizontalAlignment(SwingConstants.CENTER);
        wrongConnection.setOpaque(true);
        wrongConnection.setForeground(Color.RED);


        rmiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//performed method after pushing a button
                try {
                    MainWindow.setRMIController();
                } catch (IOException | NotBoundException io) {
                    System.out.println(io.getMessage());
                    wrongConnection.setText("Connection is not working properly");
                    return;
                }
                MainWindow.goToWindow("chooseLoginPanel");
            }


            }
        );
        socketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//performed method after pushing a button
                try {
                    MainWindow.setSocketController();
                } catch (IOException ex) {
                    wrongConnection.setText("Connection is not working properly");
                    return;
                }
                MainWindow.goToWindow("chooseLoginPanel");
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(nameTextArea, gbc);

        gbc.gridy= 1;
        add(chooseTextArea, gbc);

        gbc.gridy = 2;
        add(socketButton, gbc);

        gbc.gridy = 3;
        add(rmiButton, gbc);

        gbc.gridy = 4;
        add(wrongConnection, gbc);

    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image image = null;
        try {

            image = new ImageIcon("CodexNaturalis/src/main/resources/other_images/codex_game.jpg").getImage();
        }
        catch (Exception e){
            System.out.println("Path non rilevato");
        }
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }

}