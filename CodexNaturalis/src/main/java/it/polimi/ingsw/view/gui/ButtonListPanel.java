package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonListPanel extends JPanel {
    public JButton ButtonStyle(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setForeground(Color.BLACK); // Personalizza il colore del testo
        button.setFont(button.getFont().deriveFont(Font.BOLD)); // Rendi il testo in grassetto
        Dimension buttonSize = new Dimension(150, 40);
        button.setPreferredSize(buttonSize);
        return button;
    }

}
