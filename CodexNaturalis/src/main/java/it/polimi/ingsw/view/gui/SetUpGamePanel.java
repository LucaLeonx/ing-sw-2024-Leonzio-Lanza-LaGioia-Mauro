package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.dataobject.CardInfo;
import it.polimi.ingsw.dataobject.ObjectiveInfo;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.card.CardOrientation;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

/**
 * When all the players are ready they can decide theis set-up cards
 */
public class SetUpGamePanel extends StandardPanel {
    private final Color gray = new Color(220,220,220); // RGB values
    ExecutorService executor;
    ObjectiveInfo choosenObjective;
    CardOrientation choosenOrientation;
    int InitialCardId;

    public SetUpGamePanel() {
    }

    public void buildPanel(){
        removeAll();
        executor =  Executors.newSingleThreadExecutor();

        this.setLayout(new BorderLayout());

        JPanel hostPlayer = newHostPanel();
        JPanel rightInfo = newInfo();
        JPanel chat = newChat();
        JPanel setupGame = newSetupGame();

        this.add(hostPlayer, BorderLayout.PAGE_END);
        this.add(rightInfo, BorderLayout.LINE_END);
        this.add(chat, BorderLayout.LINE_START);
        this.add(setupGame, BorderLayout.CENTER);

        executor.submit(() -> {
            while (true) {
                if(choosenOrientation   != null && choosenObjective != null) {
                    try {
                        MainWindow.getClientController().setPlayerSetup(choosenObjective, choosenOrientation);
                        break;
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                else {
                    try {
                        sleep(1000);
                        revalidate();
                        repaint();
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            MainWindow.getClientController().waitForSetupFinished();
            MainWindow.goToWindow("gameFieldPanel");
            try {
                MainWindow.gameFieldPanel.buildPanel();
                //MainWindow.gameFieldPanel.setInitialChoice(InitialCardId,choosenOrientation);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });


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

        /*JButton logout= new JButton("Exit and logout");
        JButton goBack= new JButton("Go Back");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        logout.setAlignmentX(CENTER_ALIGNMENT);
        goBack.setAlignmentX(CENTER_ALIGNMENT);
        buttonPanel.add(logout);
        buttonPanel.add(goBack);*/

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


        /*logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executor.shutdownNow();
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
        });*/

        /*goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executor.shutdownNow();
                MainWindow.goToWindow("chooseLoginPanel");
                resetPanel();
                try{
                    MainWindow.getClientController().exitGame();
                } catch (RemoteException ex) {
                    System.out.println(ex.getMessage());
                }
                buildPanel();
            }
        });*/


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
        //host.add(buttonPanel, gbc);

        host.setBackground(gray);


        return host;
    }


    private JPanel newInfo(){
        int j=0;
        JPanel info = new JPanel();
        info.setLayout(new GridBagLayout());


        int resourceCardsDeckId;
        int resourceCard1Id;
        int resourceCard2Id;
        int goldCardsDeckId;
        int goldCard1Id;
        int goldCard2Id;
        int commonObjective1Id;
        int commonObjective2Id;


        try {
            resourceCardsDeckId = MainWindow.getClientController().getDrawableCards().drawableCards().get(DrawChoice.DECK_RESOURCE).id();
            resourceCard1Id = MainWindow.getClientController().getDrawableCards().drawableCards().get(DrawChoice.RESOURCE_CARD_1).id();
            resourceCard2Id = MainWindow.getClientController().getDrawableCards().drawableCards().get(DrawChoice.RESOURCE_CARD_2).id();

            goldCardsDeckId = MainWindow.getClientController().getDrawableCards().drawableCards().get(DrawChoice.DECK_GOLD).id();
            goldCard1Id = MainWindow.getClientController().getDrawableCards().drawableCards().get(DrawChoice.GOLD_CARD_1).id();
            goldCard2Id= MainWindow.getClientController().getDrawableCards().drawableCards().get(DrawChoice.GOLD_CARD_2).id();
            commonObjective1Id=MainWindow.getClientController().getCommonObjectives().get(0).id();
            commonObjective2Id=MainWindow.getClientController().getCommonObjectives().get(1).id();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ImagePanel resourceCardsDeck = new ImagePanel(resourceCardsDeckId);
        resourceCardsDeck.changeSide();
        ImagePanel resourceCard1 = new ImagePanel(resourceCard1Id);
        ImagePanel resourceCard2 = new ImagePanel(resourceCard2Id);
        ImagePanel goldCardsDeck = new ImagePanel(goldCardsDeckId);
        goldCardsDeck.changeSide();
        ImagePanel goldCard1 = new ImagePanel(goldCard1Id);
        ImagePanel goldCard2 = new ImagePanel(goldCard2Id);
        ImagePanel commonObjective1 = new ImagePanel(commonObjective1Id);
        ImagePanel commonObjective2 = new ImagePanel(commonObjective2Id);

        JLabel deckLabel = new JLabel("Drawable Decks:\n");
        JLabel resourceLabel = new JLabel("Resource Cards:\n");
        JLabel goldLabel= new JLabel("Gold Cards: \n");
        JLabel objectiveLabel= new JLabel("Objective Cards: \n");
        JLabel splitter = new JLabel("\n\n\n\n\n\n\n\n\n\n");
        splitter.setForeground(gray);


        GridBagConstraints gbc= new GridBagConstraints();


        gbc.insets = new Insets(5, 5, 5, 5); // Padding of 5 pixels on all sides

        gbc.gridwidth=2;


        gbc.gridy = 0;
        gbc.gridx = 0;
        info.add(deckLabel, gbc);

        gbc.gridy=1;
        gbc.gridx=0;
        info.add(resourceCardsDeck, gbc);

        gbc.gridx=2;
        info.add(goldCardsDeck, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        info.add(resourceLabel, gbc);

        gbc.gridy=3;
        gbc.gridx=0;
        info.add(resourceCard1, gbc);

        gbc.gridx=2;
        info.add(resourceCard2, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        info.add(goldLabel, gbc);

        gbc.gridy = 5;
        gbc.gridx = 0;
        info.add(goldCard1, gbc);

        gbc.gridx=2;
        info.add(goldCard2, gbc);

        gbc.gridy = 6;
        gbc.gridx = 0;
        info.add(splitter, gbc);

        gbc.gridy = 7;
        gbc.gridx = 0;
        info.add(objectiveLabel, gbc);

        gbc.gridy = 8;
        gbc.gridx = 0;
        info.add(commonObjective1, gbc);

        gbc.gridx=2;
        info.add(commonObjective2, gbc);

        info.setBackground(gray);

        // Make the frame visible
        info.setVisible(true);

        this.repaint();
        this.revalidate();
        return info;
    }

    private JPanel newSetupGame(){
        JPanel setupGame = new JPanel();
        setupGame.setLayout(new GridBagLayout());

        ObjectiveInfo firstObjective;
        ObjectiveInfo secondObjective;
        CardInfo initialCard;


        try {
            firstObjective= MainWindow.getClientController().getPlayerSetup().objective1();
            secondObjective = MainWindow.getClientController().getPlayerSetup().objective2();
            initialCard = MainWindow.getClientController().getPlayerSetup().initialCard();
            this.InitialCardId = initialCard.id();
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

        JLabel objectiveChoosen = new JLabel( "Objective card choosen!");
        JLabel initialChoosen = new JLabel( "Initial card choosen!");
        objectiveChoosen.setVisible(false);
        initialChoosen.setVisible(false);


        JButton firstObjectiveButton= new JButton("Choose First");
        JButton secondObjectiveButton= new JButton("Choose Second");
        JButton frontSide= new JButton("Choose Front");
        JButton backSide= new JButton("Choose back");

        firstObjectiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choosenObjective = firstObjective;
                firstObjectiveButton.setVisible(false);
                secondObjectiveButton.setVisible(false);
                objectiveChoosen.setVisible(true);
            }
        });

        secondObjectiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choosenObjective = secondObjective;
                firstObjectiveButton.setVisible(false);
                secondObjectiveButton.setVisible(false);
                objectiveChoosen.setVisible(true);
            }
        });

        frontSide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choosenOrientation=initialCard.front().side();
                frontSide.setVisible(false);
                backSide.setVisible(false);
                initialChoosen.setVisible(true);
            }
        });

        backSide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choosenOrientation=initialCard.back().side();
                backSide.setVisible(false);
                frontSide.setVisible(false);
                initialChoosen.setVisible(true);
            }
        });


        GridBagConstraints gbc= new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.weightx=2;
        setupGame.add(chooseYourObjective, gbc);

        gbc.gridy=1;
        gbc.gridwidth=1;
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

        gbc.gridy=3;
        gbc.gridx=0;
        gbc.gridwidth=1;
        setupGame.add(objectiveChoosen, gbc);

        gbc.gridx=2;
        setupGame.add(initialChoosen, gbc);

        return setupGame;
    }

    private JPanel newChat(){
        JPanel chat= new JPanel();
        return chat;
    }
}