package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends ButtonListPanel{
    public RegisterPanel(){
        buildPanel();
    }
    private void buildPanel(){
        this.setLayout(new BorderLayout());
        JTextArea textArea= new JTextArea("REGISTER Panel");
        add(textArea, BorderLayout.CENTER);

        addBottomFrame();
    }
}
