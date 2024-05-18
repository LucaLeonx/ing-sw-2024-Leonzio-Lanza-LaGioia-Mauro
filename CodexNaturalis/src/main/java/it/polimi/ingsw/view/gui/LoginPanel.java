package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends StandardPanel {
    private JTextField user;
    private JPasswordField password;
    public LoginPanel(){
        buildPanel();
    }
    private void buildPanel(){
        this.setLayout(new GridBagLayout());
        JButton goBack = new JButton("Go Back");

        JLabel username = new JLabel("Username: ");
        JLabel userpassword= new JLabel("Password: ");
        user = new JTextField(15);
        password = new JPasswordField(15);

        JButton loginButton= new JButton("Login NOW");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindow.goToWindow("gameFieldPanel");
            }
        });


        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindow.goToWindow("chooseLoginPanel");
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;
        add(username, gbc);

        gbc.gridy = 1;
        add(userpassword, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(user, gbc);

        gbc.gridy = 1;
        add(password, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(loginButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(goBack, gbc);

    }


}
