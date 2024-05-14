package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChooseConnectionPanel extends ButtonListPanel {
    public ChooseConnectionPanel() {
        buildPanel();
    }

    private void buildPanel() {
        this.setLayout(new GridBagLayout());

        JButton socketButton= new JButton("Socket");
        JButton rmiButton= new JButton("RMI");
        socketButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        rmiButton.setAlignmentY(Component.CENTER_ALIGNMENT);

        //JTextArea connectionTextArea = new JTextArea();
        JLabel incipitTextArea = new JLabel("Choose your connection:");
        incipitTextArea.setHorizontalAlignment(SwingConstants.CENTER);

        socketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//performed method after pushing a button
               // connectionTextArea.append("Socket connection selected\n");
                MainWindow.goToWindow("chooseLoginPanel");
            }
            }
        );
        rmiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//performed method after pushing a button
               // connectionTextArea.append("RMI connection selected\n");
                MainWindow.goToWindow("chooseLoginPanel");
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(incipitTextArea, gbc);

        gbc.gridy = 1;
        add(socketButton, gbc);

        gbc.gridy = 2;
        add(rmiButton, gbc);

    }


}