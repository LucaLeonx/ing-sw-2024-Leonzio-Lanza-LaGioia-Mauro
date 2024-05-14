package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonListPanel extends JPanel {
    public JButton addButton(JPanel panel, String name) {
        JButton button = new JButton(name);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setForeground(Color.BLACK); // Personalizza il colore del testo
        button.setFocusPainted(false); // Rimuovi il bordo attorno al testo
        button.setFont(button.getFont().deriveFont(Font.BOLD)); // Rendi il testo in grassetto
        panel.add(button);
        return button;
    }

    public void addBottomFrame()
    {
        // Creazione del pannello per i bottoni
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2)); // Layout con due colonne

        JButton button1 = addButton(buttonPanel, "Previous");
        JButton button2 = addButton(buttonPanel, "Next");

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//performed method after pushing a button
                MainWindow.goToPreviousWindow();
            }
        }
        );
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//performed method after pushing a button
                MainWindow.goToNextWindow();
            }
        });
        add(buttonPanel, BorderLayout.SOUTH);

    };
}
