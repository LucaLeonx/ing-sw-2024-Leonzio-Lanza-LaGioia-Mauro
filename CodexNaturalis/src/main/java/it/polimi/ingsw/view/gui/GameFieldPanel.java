package it.polimi.ingsw.view.gui;

import com.sun.tools.javac.Main;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.WrongPhaseException;
import it.polimi.ingsw.dataobject.CardInfo;
import it.polimi.ingsw.dataobject.ControlledPlayerInfo;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.view.tui.Symbol_String;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameFieldPanel extends StandardPanel {
    JLabel waitingForOthers= new JLabel("Waiting for other players to join...");
    private final Timer timer;

    public GameFieldPanel() {
        //startGame.setAlignmentY(CENTER_ALIGNMENT);
        this.setLayout(new BorderLayout());
        this.add(waitingForOthers, BorderLayout.CENTER);
        waitingForOthers.setAlignmentX(CENTER_ALIGNMENT);
        waitingForOthers.setSize(100,100);

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MainWindow.getClientController().isWaitingInLobby();
                    buildPanel();
                    timer.stop();
                    }
                catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });
        timer.start();

    }

    private void buildPanel(){
        MainWindow.getClientController().waitForGameToStart();
        waitingForOthers.setVisible(false);
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
        ArrayList<CardInfo> cardsInHands = new ArrayList<>();

        try {
            cardsInHands = MainWindow.getClientController().getControlledPlayerInformation().cardsInHand();
        } catch (Exception e) {
            System.out.println(e);
        }
        ImagePanel firstcard = new ImagePanel(cardsInHands.get(0).id());
        ImagePanel secondcard = new ImagePanel(cardsInHands.get(1).id());
        ImagePanel thirdcard = new ImagePanel(cardsInHands.get(2).id());

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

        gbc.gridx=4;
        host.add(buttonPanel, gbc);


        return host;
    }

    private JPanel newOtherPlayers() {
        JPanel otherPlayes = new JPanel();
        otherPlayes.setLayout(new GridBagLayout());

        int numberOfPlyayers = 0;

        JButton player2 = new JButton();
        JButton player3 = new JButton();
        JButton player4 = new JButton();

        try {
            numberOfPlyayers= MainWindow.getClientController().getLeaderboard().size();
            for(int i=0; i<numberOfPlyayers; i++) {
                if(i==0)
                    player2.setText(MainWindow.getClientController().getLeaderboard().get(i).nickname());
                if(i==1)
                    player3.setText(MainWindow.getClientController().getLeaderboard().get(i).nickname());
                if(i==2)
                    player4.setText(MainWindow.getClientController().getLeaderboard().get(i).nickname());

            }

        } catch (Exception e) {
            System.out.println(e);
        }



        GridBagConstraints gbc= new GridBagConstraints();

        player2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


        for(int i = 0; i<numberOfPlyayers-1; i++) {
            gbc.gridx = i;
            if(i==0)
                otherPlayes.add(player2, gbc);
            if(i==1)
                otherPlayes.add(player3, gbc);
            if(i==2)
                otherPlayes.add(player4, gbc);
        }

        return otherPlayes;
    }

    private JPanel newInfo(){
        JPanel info = new JPanel();

        HashMap<Symbol, Integer> symbolCounter= new HashMap<>();

        JLabel insectPoints= new JLabel();
        JLabel animalPoints= new JLabel();
        JLabel fungiPoints= new JLabel();
        JLabel plantPoints= new JLabel();

        try {
            symbolCounter= MainWindow.getClientController().getControlledPlayerInformation().field().symbolCounterMap();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        insectPoints.setText(Symbol_String.INSECT_SYMBOL + symbolCounter.get(Symbol.INSECT).toString());
        animalPoints.setText(Symbol_String.ANIMAL_SYMBOL +symbolCounter.get(Symbol.ANIMAL).toString());
        fungiPoints.setText(Symbol_String.FUNGI_SYMBOL + symbolCounter.get(Symbol.FUNGI).toString());
        plantPoints.setText(Symbol_String.PLANT_SYMBOL + symbolCounter.get(Symbol.PLANT).toString());




        GridBagConstraints gbc= new GridBagConstraints();

        gbc.gridx=0;
        gbc.gridy=0;
        info.add(insectPoints, gbc);

        gbc.gridy=1;
        info.add(animalPoints, gbc);

        gbc.gridy=2;
        info.add(fungiPoints, gbc);

        gbc.gridy=3;
        info.add(plantPoints, gbc);

        return info;
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
