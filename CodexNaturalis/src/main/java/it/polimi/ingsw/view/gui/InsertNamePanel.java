package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidOperationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

/**
 * Login Panel in which players can Register and get their code as password
 */
public class InsertNamePanel extends StandardPanel {
    private JTextArea registerName;
    private int generatedPassword;

    public InsertNamePanel(){
        buildPanel();
    }
    private void buildPanel() {
        this.setLayout(new GridBagLayout());

        JButton goBack = new JButton("Go Back");
        JLabel nameLabel = new JLabel("Register username: ");
        registerName = new JTextArea(1,6);
        JButton registerButton = new JButton("Register NOW");

        JLabel incorrectNameMessage= new JLabel();
        incorrectNameMessage.setForeground(Color.RED);

        JLabel password = new JLabel();
        JTextArea passwordValue = new JTextArea();
        passwordValue.setVisible(false);

        JButton readyButton = new JButton(("Ready to play"));
        readyButton.setVisible(false);

        JLabel warningMessage = new JLabel ();
        warningMessage.setForeground(Color.RED);


        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(registerName.getText().isEmpty())
                    return;

                try {
                    generatedPassword=MainWindow.getClientController().register(registerName.getText());
                } catch (RemoteException | InvalidOperationException ex) {
                    incorrectNameMessage.setText(ex.getMessage());
                    return;
                }

                registerName.setEditable(false);
                registerButton.setVisible(false);
                incorrectNameMessage.setVisible(false);

                password.setText("Password given: ");
                warningMessage.setText("Be Careful! Remember your password before Playing!");
                readyButton.setVisible(true);

                passwordValue.setText(String.valueOf(generatedPassword));
                passwordValue.setVisible(true);
                passwordValue.setEditable(false);
            }
        });

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindow.goToWindow("chooseLoginPanel");
                registerName.setEditable(true);
                registerButton.setVisible(true);
                passwordValue.setVisible(false);
                password.setVisible(false);
                warningMessage.setVisible(false);
                readyButton.setVisible(false);
            }
        });

        readyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindow.registerPanel.showLobbies();
                MainWindow.goToWindow("registerPanel");
                resetPanel();
                buildPanel();
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;
        add(nameLabel, gbc);

        gbc.gridx = 1;
        add(registerName, gbc);

        gbc.gridx=0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(incorrectNameMessage, gbc);

        gbc.gridy=3;
        add(registerButton,gbc);

        gbc.gridy= 4;
        gbc.gridwidth = 1;
        add(password, gbc);

        gbc.gridx=1;
        add(passwordValue, gbc);

        gbc.gridx=0;
        gbc.gridy= 5;
        gbc.gridwidth = 2;
        add(warningMessage, gbc);

        gbc.gridy= 6;
        add(readyButton, gbc);

        gbc.gridy = 7;
        add(goBack, gbc);

    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image image = null;
        try {
            image = new ImageIcon("src/main/resources/other_images/codex_game.jpg").getImage();
        }
        catch (Exception e){
            System.out.println("Path non rilevato");
        }
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }

}
