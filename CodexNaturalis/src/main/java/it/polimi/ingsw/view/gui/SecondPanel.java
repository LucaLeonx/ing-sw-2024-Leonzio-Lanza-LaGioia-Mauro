package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;

public class SecondPanel extends JPanel {
    public SecondPanel(){
        buildPanel();
    }

    private void buildPanel(){
        this.setLayout(new BorderLayout());
        JTextArea textArea= new JTextArea("Second Panel");
        add(textArea, BorderLayout.CENTER);

    }
}
