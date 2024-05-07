package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterPanel extends ButtonListPanel{
    public RegisterPanel(){
        buildPanel();
    }
    private void buildPanel(){
        this.setLayout(new BorderLayout());
        JTextArea textArea= new JTextArea("REGISTER Panel");
        add(textArea, BorderLayout.CENTER);
        JButton Cancel = new JButton("Cancel");
        Cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindow.goToWindow("chooseLoginPanel");
            }
        });

        add(Cancel,BorderLayout.SOUTH);

        //addBottomFrame();

    }



}
