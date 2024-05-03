package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FirstPanel extends JPanel {
    public FirstPanel() {
        buildPanel();
    }

    private void buildPanel() {
        JPanel buttonsPanel= new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        this.setLayout(new BorderLayout());

        JButton socketButton= addButton(buttonsPanel,"Socket");
        JButton rmiButton= addButton(buttonsPanel,"RMI");
        JTextArea connectionTextArea = new JTextArea();
        JTextArea incipitTextArea = new JTextArea();
        incipitTextArea.append("Choose your connection:\n");


        socketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//performed method after pushing a button
                connectionTextArea.append("Socket connection selected\n");
                MainWindow.goToNextWindow();
                }
            }
        );
        rmiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//performed method after pushing a button
                connectionTextArea.append("RMI connection selected\n");
                MainWindow.goToNextWindow();
            }
        });

        add(incipitTextArea, BorderLayout.NORTH);
        add(connectionTextArea, BorderLayout.SOUTH);
        add(buttonsPanel, BorderLayout.CENTER);

    }

    private JButton addButton(JPanel panel, String name) {
        JButton button = new JButton(name);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(button);
        return button;
    }
}