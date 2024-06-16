package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;

/**
 * JPanel type to inherit some methods on shown pages
 */
public class StandardPanel extends JPanel {
    public JButton ButtonStyle(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setForeground(Color.BLACK);
        button.setFont(button.getFont().deriveFont(Font.BOLD)); // Rendi il testo in grassetto
        Dimension buttonSize = new Dimension(150, 40);
        button.setPreferredSize(buttonSize);
        return button;
    }

    void resetPanel() {
        this.removeAll();
        this.repaint();
    }
}
