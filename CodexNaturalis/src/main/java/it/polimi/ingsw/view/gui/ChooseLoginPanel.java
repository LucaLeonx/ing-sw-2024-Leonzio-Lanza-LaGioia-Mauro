package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChooseLoginPanel extends ButtonListPanel {
    public ChooseLoginPanel(){
        buildPanel();
    }

    private void buildPanel(){
        this.setLayout(new GridBagLayout());
        JLabel incipitTextArea = new JLabel("Choose your connection:\n");

        JButton login= new JButton("Login");
        JButton register= new JButton("Register");

        JTextArea connectionTextArea = new JTextArea();

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//performed method after pushing a button
                connectionTextArea.append("Login selected\n");
                MainWindow.goToWindow("loginPanel");
            }
        }
        );

        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//performed method after pushing a button
                connectionTextArea.append("Register selected\n");
                MainWindow.goToWindow("registerPanel");
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;
        add(incipitTextArea, gbc);

        gbc.gridy = 1;
        add(login, gbc);

        gbc.gridy = 2;
        add(register, gbc);

    }
}
