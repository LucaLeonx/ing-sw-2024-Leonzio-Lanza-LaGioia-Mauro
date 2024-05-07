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
        JPanel buttonsPanel= new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel incipitTextArea = new JLabel("Choose your connection:\n");

        this.setLayout(new BorderLayout());

        JButton login= addButton(buttonsPanel,"Login");
        JButton register= addButton(buttonsPanel,"Register");

        JTextArea connectionTextArea = new JTextArea();

        //addBottomFrame();

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


        add(incipitTextArea, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.CENTER);

    }
}
