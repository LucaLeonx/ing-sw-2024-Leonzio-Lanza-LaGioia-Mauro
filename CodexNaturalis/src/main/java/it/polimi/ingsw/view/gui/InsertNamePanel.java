package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InsertNamePanel extends StandardPanel {
    private JTextField registerName;
    private String generatedPassword= "**password**";

    public InsertNamePanel(){
        buildPanel();
    }
    private void buildPanel() {
        this.setLayout(new GridBagLayout());
        JButton goBack = new JButton("Go Back");

        JLabel nameLabel = new JLabel("Register name: ");
        registerName = new JTextField(15);

        JButton registerButton = new JButton("Register NOW");

        JLabel password = new JLabel();
        password.setVisible(false);
        JLabel passwordValue = new JLabel();
        passwordValue.setVisible(false);

        JButton readyButton = new JButton(("Ready to play"));
        JLabel warningMessage = new JLabel ("Be Careful! Remember your password before Playing!");
        warningMessage.setForeground(Color.RED);
        warningMessage.setVisible(false);
        readyButton.setVisible(false);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerName.setEditable(false);
                registerButton.setVisible(false);
                password.setText("Password given: ");
                passwordValue.setText(generatedPassword);
                passwordValue.setVisible(true);
                password.setVisible(true);
                warningMessage.setVisible(true);
                readyButton.setVisible(true);
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
                MainWindow.goToWindow("registerPanel");
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
        add(registerButton, gbc);

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
}
