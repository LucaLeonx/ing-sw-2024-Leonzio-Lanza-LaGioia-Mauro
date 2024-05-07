package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends ButtonListPanel{
    public LoginPanel(){
        buildPanel();
    }
    private void buildPanel(){
        this.setLayout(new BorderLayout());
        JLabel textArea= new JLabel("LOGIN Panel");
        JButton Cancel = new JButton("Cancel");
        Cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindow.goToWindow("chooseLoginPanel");
            }
        });


        add(Cancel,BorderLayout.SOUTH);
        add(textArea, BorderLayout.CENTER);
        //addBottomFrame();


    }


}
