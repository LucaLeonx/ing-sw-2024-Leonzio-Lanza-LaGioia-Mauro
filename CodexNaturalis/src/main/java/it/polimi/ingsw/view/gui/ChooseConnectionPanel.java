package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ChooseConnectionPanel extends ButtonListPanel {

    public ChooseConnectionPanel() {
        buildPanel();
    }

    private void buildPanel() {
        this.setLayout(new GridBagLayout());

        JButton socketButton= new JButton("Socket");
        JButton rmiButton= new JButton("RMI");
        socketButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        rmiButton.setAlignmentY(Component.CENTER_ALIGNMENT);

        //JTextArea connectionTextArea = new JTextArea();
        JLabel incipitTextArea = new JLabel("Choose your connection:");
        incipitTextArea.setBackground(Color.white);
        incipitTextArea.setHorizontalAlignment(SwingConstants.CENTER);

        rmiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//performed method after pushing a button
                try {
                    MainWindow.setRMIController();
                } catch (NotBoundException | RemoteException ex) {
                    return;
                }

                MainWindow.goToWindow("chooseLoginPanel");
            }
            }
        );
        socketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//performed method after pushing a button
               // connectionTextArea.append("RMI connection selected\n");
                MainWindow.goToWindow("chooseLoginPanel");
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(incipitTextArea, gbc);

        gbc.gridy = 1;
        add(socketButton, gbc);

        gbc.gridy = 2;
        add(rmiButton, gbc);

    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image image = null;
        try {
             image = new ImageIcon("/Users/giovanni/IdeaProjects/ing-sw-2024-Leonzio-Lanza-LaGioia-Mauro/CodexNaturalis/src/main/java/it/polimi/ingsw/view/gui/codex-naturalis.png").getImage();
        }
        catch (Exception e){
            System.out.println("Path non rilevato");
        }
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }
}