package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;

public class ThirdPanel extends JPanel{
    public ThirdPanel(){
        buildPanel();
    }

    private void buildPanel(){
        this.setLayout(new BorderLayout());
        JTextArea textArea= new JTextArea("Third Panel");
        add(textArea, BorderLayout.CENTER);

    }
}
