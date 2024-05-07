package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChooseConnectionPanel extends ButtonListPanel {
    public ChooseConnectionPanel() {
        buildPanel();
    }

    private void buildPanel() {
        JPanel buttonsPanel= new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.setLayout(new BorderLayout());

        JButton socketButton= addButton(buttonsPanel,"Socket");
        JButton rmiButton= addButton(buttonsPanel,"RMI");

        //JTextArea connectionTextArea = new JTextArea();
        JLabel incipitTextArea = new JLabel("Choose your connection:");
        incipitTextArea.setHorizontalAlignment(SwingConstants.CENTER);

        socketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//performed method after pushing a button
               // connectionTextArea.append("Socket connection selected\n");
                MainWindow.goToWindow("chooseLoginPanel");
            }
            }
        );
        rmiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//performed method after pushing a button
               // connectionTextArea.append("RMI connection selected\n");
                MainWindow.goToWindow("chooseLoginPanel");
            }
        });

        add(incipitTextArea, BorderLayout.PAGE_START);
      //  add(connectionTextArea, BorderLayout.SOUTH);
        add(buttonsPanel, BorderLayout.CENTER);
       // add(text, BorderLayout.CENTER);

    }


}