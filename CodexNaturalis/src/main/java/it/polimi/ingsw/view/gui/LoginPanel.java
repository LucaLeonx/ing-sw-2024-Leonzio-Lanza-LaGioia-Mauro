package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends ButtonListPanel{
    public LoginPanel(){
        buildPanel();
    }
    private void buildPanel(){
        this.setLayout(new BorderLayout());
        JTextArea textArea= new JTextArea("LOGIN Panel");
        add(textArea, BorderLayout.CENTER);
        addBottomFrame();
    }
}
