package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameFieldPanel extends StandardPanel {
    public GameFieldPanel() { buildPanel(); }

    private void buildPanel(){
        this.setLayout(new BorderLayout());

        JPanel hostPlayer = newHostPanel();
        JPanel player2 = newPlayer2();
        JPanel player3 = newPlayer3();
        JPanel player4 = newPlayer4();
        JPanel game = newGame();

        this.add(hostPlayer, BorderLayout.PAGE_END);
        this.add(player2, BorderLayout.LINE_END);
        this.add(player3, BorderLayout.PAGE_START);
        this.add(player4, BorderLayout.LINE_START);
        this.add(game, BorderLayout.CENTER);

    }

    private JPanel newHostPanel(){
        JPanel host= new JPanel();
        host.setLayout(new GridBagLayout());

        ImagePanel firstcard = new ImagePanel("img_21");
        ImagePanel secondcard = new ImagePanel("img_31");
        ImagePanel thirdcard = new ImagePanel("img_51");
        ImagePanel fourthcard = new ImagePanel("img_61");

        JButton goBacK= new JButton("Go Back");

        firstcard.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                firstcard.changeSide();
            }
        });

        secondcard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                secondcard.changeSide();
            }
        });

        thirdcard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                thirdcard.changeSide();
            }
        });

        fourthcard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                fourthcard.changeSide();
            }
        });

        goBacK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindow.goToWindow("chooseLoginPanel");
                resetPanel();
                buildPanel();
            }
        });



        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        gbc.gridx=0;
        gbc.gridy=1;
        host.add(firstcard, gbc);

        gbc.gridx=1;
        host.add(secondcard, gbc);

        gbc.gridx=2;
        host.add(thirdcard, gbc);

        gbc.gridx=3;
        host.add(fourthcard, gbc);

        gbc.gridx=4;
        host.add(goBacK, gbc);

        return host;
    }

    private JPanel newPlayer2(){
        JPanel player2 = new JPanel();

        return player2;
    }

    private  JPanel newPlayer3(){
        JPanel player3 = new JPanel();

        return player3;
    }

    private  JPanel newPlayer4(){
        JPanel player4 = new JPanel();

        return player4;
    }

    private  JPanel newGame(){
        JPanel game = new JPanel();

        return game;
    }

}
