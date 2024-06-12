package it.polimi.ingsw.view.gui;

import com.sun.tools.javac.Main;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.WrongPhaseException;
import it.polimi.ingsw.dataobject.CardInfo;
import it.polimi.ingsw.dataobject.ControlledPlayerInfo;
import it.polimi.ingsw.dataobject.ObjectiveInfo;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.view.tui.Symbol_String;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
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
        MainWindow.getClientController().waitForGameToStart(); //problem in this wait
        waitingForOthers.setVisible(false);
        this.setLayout(new BorderLayout());

        JPanel hostPlayer = newHostPanel();
        JPanel otherPlayers = newOtherPlayers();
        JPanel rightInfo = newInfo();
        JPanel chat = newChat();
        JPanel setupGame = newSetupGame();
        JPanel game = newGame();

        this.add(hostPlayer, BorderLayout.PAGE_END);
        this.add(otherPlayers, BorderLayout.PAGE_START);
        this.add(rightInfo, BorderLayout.LINE_END);
        this.add(chat, BorderLayout.LINE_START);
        this.add(setupGame, BorderLayout.CENTER);

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
            numberOfPlyayers= MainWindow.getClientController().getPlayerNames().size();
            for(int i=0; i<=numberOfPlyayers; i++) {
                if(MainWindow.getClientController().getLeaderboard().get(i).nickname().equals(MainWindow.getClientController().getCurrentPlayerName())){
                    continue;
                }
                if(i==0)
                    player2.setText(MainWindow.getClientController().getLeaderboard().get(i).nickname());
                if(i==1)
                    player3.setText(MainWindow.getClientController().getLeaderboard().get(i).nickname());
                if(i==2)
                    player4.setText(MainWindow.getClientController().getLeaderboard().get(i).nickname());
                if(i==3)
                    player4.setText(MainWindow.getClientController().getLeaderboard().get(i).nickname());

            }
        } catch (Exception e) {
            System.out.println(e + " button error");
        }

        player2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        GridBagConstraints gbc= new GridBagConstraints();

        for(int i = 0; i<numberOfPlyayers-1; i++) {
            gbc.gridy= 0;
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
        info.setLayout(new GridBagLayout());

        HashMap<Symbol, Integer> symbolCounter= new HashMap<>();

        JLabel insectPoints= new JLabel();
        JLabel animalPoints= new JLabel();
        JLabel fungiPoints= new JLabel();
        JLabel plantPoints= new JLabel();
        JLabel inkwellPoints = new JLabel();
        JLabel manuscriptPoints = new JLabel();
        JLabel quillPoints = new JLabel();

        int resourceCardsDeckId;
        int resourceCard1Id;
        int resourceCard2Id;
        int goldCardsDeckId;
        int goldCard1Id;
        int goldCard2Id;

        try {
            symbolCounter= MainWindow.getClientController().getControlledPlayerInformation().field().symbolCounterMap();
            resourceCardsDeckId = MainWindow.getClientController().getDrawableCards().drawableCards().get(DrawChoice.DECK_RESOURCE).id();
            resourceCard1Id = MainWindow.getClientController().getDrawableCards().drawableCards().get(DrawChoice.RESOURCE_CARD_1).id();
            resourceCard2Id = MainWindow.getClientController().getDrawableCards().drawableCards().get(DrawChoice.RESOURCE_CARD_2).id();

            goldCardsDeckId = MainWindow.getClientController().getDrawableCards().drawableCards().get(DrawChoice.DECK_GOLD).id();
            goldCard1Id = MainWindow.getClientController().getDrawableCards().drawableCards().get(DrawChoice.GOLD_CARD_1).id();
            goldCard2Id= MainWindow.getClientController().getDrawableCards().drawableCards().get(DrawChoice.GOLD_CARD_2).id();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        insectPoints.setText(Symbol_String.INSECT_SYMBOL + symbolCounter.get(Symbol.INSECT).toString());
        animalPoints.setText(Symbol_String.ANIMAL_SYMBOL +symbolCounter.get(Symbol.ANIMAL).toString());
        fungiPoints.setText(Symbol_String.FUNGI_SYMBOL + symbolCounter.get(Symbol.FUNGI).toString());
        plantPoints.setText(Symbol_String.PLANT_SYMBOL + symbolCounter.get(Symbol.PLANT).toString());
        inkwellPoints.setText(Symbol_String.INKWELL_SYMBOL + symbolCounter.get(Symbol.INKWELL).toString());
        manuscriptPoints.setText(Symbol_String.MANUSCRIPT_SYMBOL + symbolCounter.get(Symbol.MANUSCRIPT).toString());
        quillPoints.setText(Symbol_String.QUILL_SYMBOL + symbolCounter.get(Symbol.QUILL).toString());

        ImagePanel resourceCardsDeck = new ImagePanel(resourceCardsDeckId);
        resourceCardsDeck.changeSide();
        ImagePanel resourceCard1 = new ImagePanel(resourceCard1Id);
        ImagePanel resourceCard2 = new ImagePanel(resourceCard2Id);
        ImagePanel goldCardsDeck = new ImagePanel(goldCardsDeckId);
        goldCardsDeck.changeSide();
        ImagePanel goldCard1 = new ImagePanel(goldCard1Id);
        ImagePanel goldCard2 = new ImagePanel(goldCard2Id);

        GridBagConstraints gbc= new GridBagConstraints();

        gbc.gridx=0;
        gbc.gridy=0;
        info.add(insectPoints, gbc);

        gbc.gridx=1;
        info.add(animalPoints, gbc);

        gbc.gridx=2;
        info.add(fungiPoints, gbc);

        gbc.gridx=3;
        info.add(plantPoints, gbc);

        gbc.gridy=1;
        gbc.gridx=0;
        info.add(inkwellPoints, gbc);

        gbc.gridx=1;
        info.add(manuscriptPoints, gbc);

        gbc.gridx=2;
        info.add(quillPoints, gbc);

        gbc.gridwidth=2;

        gbc.gridy=3;
        gbc.gridx=0;
        info.add(resourceCardsDeck, gbc);

        gbc.gridx=2;
        info.add(goldCardsDeck, gbc);

        gbc.gridy=5;
        gbc.gridx=0;
        info.add(resourceCard1, gbc);

        gbc.gridx=2;
        info.add(goldCard1, gbc);

        gbc.gridy=6;
        gbc.gridx=0;
        info.add(resourceCard2, gbc);

        gbc.gridx=2;
        info.add(goldCard2, gbc);

        return info;
    }

    private JPanel newChat(){
        JPanel player4 = new JPanel();

        return player4;
    }

    private JPanel newSetupGame(){
        JPanel setupGame = new JPanel();
        setupGame.setLayout(new GridBagLayout());

        ObjectiveInfo firstObjective;
        ObjectiveInfo secondObjective;
        CardInfo initialCard;

        class ChosenSetUp{
            ObjectiveInfo choosenObjective;
            CardOrientation choosenOrientation;
        }
        ChosenSetUp chosenSetUp = new ChosenSetUp();

        try {
            firstObjective= MainWindow.getClientController().getPlayerSetup().objective1();
            secondObjective = MainWindow.getClientController().getPlayerSetup().objective2();
            initialCard = MainWindow.getClientController().getPlayerSetup().initialCard();

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        ImagePanel firstObjectiveImage = new ImagePanel(firstObjective.id());
        ImagePanel secondObjectiveImage = new ImagePanel(secondObjective.id());
        ImagePanel initialCardFront = new ImagePanel(initialCard.id());
        ImagePanel initialCardBack = new ImagePanel(initialCard.id());
        initialCardBack.changeSide();

        JLabel chooseYourObjective= new JLabel("Choose your Objective card");
        chooseYourObjective.setAlignmentX(CENTER_ALIGNMENT);

        JButton firstObjectiveButton= new JButton("Choose First");
        JButton secondObjectiveButton= new JButton("Choose Second");
        JButton frontSide= new JButton("Choose Front");
        JButton backSide= new JButton("Choose back");

        firstObjectiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chosenSetUp.choosenObjective = firstObjective;
                setupGame.remove(firstObjectiveButton);
                setupGame.remove(secondObjectiveButton);
            }
        });

        secondObjectiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chosenSetUp.choosenObjective = secondObjective;
                setupGame.remove(firstObjectiveButton);
                setupGame.remove(secondObjectiveButton);
            }
        });

        frontSide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chosenSetUp.choosenOrientation=initialCard.front().side();
            }
        });

        backSide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chosenSetUp.choosenOrientation=initialCard.back().side();
            }
        });



        GridBagConstraints gbc= new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.weightx=2;
        setupGame.add(chooseYourObjective, gbc);

        gbc.gridy=1;
        gbc.weightx=1;
        setupGame.add(firstObjectiveImage, gbc);

        gbc.gridx=1;
        setupGame.add(secondObjectiveImage, gbc);

        gbc.gridx=2;
        setupGame.add(initialCardFront, gbc);

        gbc.gridx=3;
        setupGame.add(initialCardBack, gbc);

        gbc.gridy=2;
        gbc.gridx=0;
        setupGame.add(firstObjectiveButton, gbc);

        gbc.gridx=1;
        setupGame.add(secondObjectiveButton, gbc);

        gbc.gridx=2;
        setupGame.add(frontSide, gbc);

        gbc.gridx=3;
        setupGame.add(backSide, gbc);


        return setupGame;
    }

    private JPanel newGame(){
        JPanel game = new JPanel();

        return game;
    }


}