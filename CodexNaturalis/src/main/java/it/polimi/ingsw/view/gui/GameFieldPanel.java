package it.polimi.ingsw.view.gui;


import it.polimi.ingsw.dataobject.CardInfo;
import it.polimi.ingsw.model.DrawChoice;
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
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

public class GameFieldPanel extends StandardPanel {
    private ExecutorService executor;
    public GameFieldPanel(){

    }

    public void buildPanel(){
        this.setLayout(new BorderLayout());
        JPanel hostPlayer = newHostPanel();
        JPanel rightInfo = newInfo();
        JPanel otherPlayers = newOtherPlayers();
        JPanel chat = newChat();
        JPanel game = newGame();
        executor =  Executors.newSingleThreadExecutor();


        this.add(otherPlayers, BorderLayout.PAGE_START);
        this.add(hostPlayer, BorderLayout.PAGE_END);
        this.add(rightInfo, BorderLayout.LINE_END);
        this.add(chat, BorderLayout.LINE_START);
        this.add(game, BorderLayout.CENTER);

        revalidate();
        repaint();

        executor.submit(() -> {
            MainWindow.getClientController().waitForGameEnded();
            MainWindow.goToWindow("endGamePanel");
            MainWindow.endGamePanel.buildPanel();
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
        GridBagConstraints gbc = new GridBagConstraints();

        java.util.List<JButton> playerButtons = new ArrayList<>();

        try {
            List<String> PlayerNames = MainWindow.getClientController().getPlayerNames();
            String currentPlayerName = MainWindow.getClientController().getControlledPlayerInformation().nickname();

            System.out.println("Current player name: " + currentPlayerName);
            System.out.println("Leaderboard size: " + PlayerNames.size());

            for (String playerName : PlayerNames) {
                System.out.println("Checking player: " + playerName);
                if (!playerName.equals(currentPlayerName)) {
                    JButton playerButton = new JButton(playerName);
                    playerButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Action for player button
                        }
                    });
                    playerButtons.add(playerButton);
                    System.out.println("Added button for player: " + playerName);
                }
            }
        } catch (Exception e) {
            System.out.println(e + " button error");
        }

        for (int i = 0; i < playerButtons.size(); i++) {
            gbc.gridx = i;
            gbc.gridy = 0;
            gbc.insets = new Insets(10, 0, 0, 10);
            otherPlayes.add(playerButtons.get(i), gbc);
        }

        System.out.println("Total buttons added: " + playerButtons.size());

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
        JPanel chat= new JPanel();

        return chat;
    }

    private JPanel newGame(){
        JPanel game= new JPanel();
        JLabel gameLabel = new JLabel("MAP");
        gameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gameLabel.setFont(new Font("Arial", Font.BOLD, 30));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(gameLabel, gbc);
        return game;
    }

    }
