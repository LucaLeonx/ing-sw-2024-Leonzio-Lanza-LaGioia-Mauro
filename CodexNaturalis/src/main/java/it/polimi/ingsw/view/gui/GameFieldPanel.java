package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.servercontroller.operationexceptions.WrongPhaseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.List;

public class GameFieldPanel extends StandardPanel {
    JButton startGame= new JButton("PRESS TO START GAME");

    public GameFieldPanel() {
        //startGame.setAlignmentY(CENTER_ALIGNMENT);
        this.setLayout(new BorderLayout());
        this.add(startGame, BorderLayout.CENTER);

        startGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buildPanel();;
            }
        });
    }

    private void buildPanel(){
        startGame.setVisible(false);
        this.setLayout(new BorderLayout());

        JPanel hostPlayer = newHostPanel();
        JPanel otherPlayers = newOtherPlayers();
        JPanel rightInfo = newInfo();
        JPanel chat = newChat();
        JPanel game = newGame();

        this.add(hostPlayer, BorderLayout.PAGE_END);
        this.add(otherPlayers, BorderLayout.PAGE_START);
        this.add(rightInfo, BorderLayout.LINE_END);
        this.add(chat, BorderLayout.LINE_START);
        this.add(game, BorderLayout.CENTER);

    }

    private JPanel newHostPanel(){
        JPanel host= new JPanel();
        host.setLayout(new GridBagLayout());

        ImagePanel firstcard = new ImagePanel("img_21");
        ImagePanel secondcard = new ImagePanel("img_31");
        ImagePanel thirdcard = new ImagePanel("img_51");
        ImagePanel fourthcard = new ImagePanel("img_61");

        JButton logout= new JButton("Exit and logout");
        JButton goBack= new JButton("Go Back");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        logout.setAlignmentX(CENTER_ALIGNMENT);
        goBack.setAlignmentX(CENTER_ALIGNMENT);
        buttonPanel.add(logout);
        buttonPanel.add(goBack);

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

        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindow.goToWindow("chooseLoginPanel");
                resetPanel();
                try {
                    MainWindow.getClientController().exitFromLobby();
                    MainWindow.getClientController().logout();
                } catch (RemoteException ex) {
                    System.out.println(ex.getMessage());
                }
                buildPanel();
            }
        });

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindow.goToWindow("chooseLoginPanel");
                resetPanel();
                try{
                    MainWindow.getClientController().exitGame();
                } catch (RemoteException ex) {
                    System.out.println(ex.getMessage());
                }
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
        host.add(buttonPanel, gbc);


        return host;
    }

    private JPanel newOtherPlayers() {
        JPanel otherPlayes = new JPanel();
        otherPlayes.setLayout(new GridBagLayout());

        JButton player2 = new JButton("Player 2");
        JButton player3 = new JButton("Player 3");
        JButton player4 = new JButton("Player 4");

        GridBagConstraints gbc= new GridBagConstraints();

      /*  try {
            List<String> names= MainWindow.getClientController().getPlayerNames();
            for (int i = 0; i<3; names.size())
            {
                if (i==0)
                {
                   player2.setText(names.get(1));
                   gbc.gridx=0;
                   otherPlayes.add(player2, gbc);
                }
                if (i==1)
                {
                    player3.setText(names.get(2));
                    gbc.gridx=1;
                    otherPlayes.add(player3, gbc);
                }
                if (i==2)
                {
                    player4.setText(names.get(3));
                    gbc.gridx=2;
                    otherPlayes.add(player4, gbc);
                }
                i++;
            }
        } catch (RemoteException | WrongPhaseException e) {
            System.out.println(e.getMessage());
        }*/

        gbc.gridx=0;
        otherPlayes.add(player2, gbc);

        gbc.gridx=1;
        otherPlayes.add(player3, gbc);

        gbc.gridx=2;
        otherPlayes.add(player4, gbc);

        return otherPlayes;
    }

    private JPanel newInfo(){
        JPanel player3 = new JPanel();
        return player3;
    }

    private JPanel newChat(){
        JPanel player4 = new JPanel();

        return player4;
    }

    private JPanel newGame(){
        JPanel game = new JPanel();

        return game;
    }

}
