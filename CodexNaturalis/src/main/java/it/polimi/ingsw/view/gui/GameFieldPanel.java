package it.polimi.ingsw.view.gui;


import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.dataobject.CardInfo;
import it.polimi.ingsw.dataobject.ControlledPlayerInfo;
import it.polimi.ingsw.dataobject.DrawableCardsInfo;
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

public class GameFieldPanel extends StandardPanel {

    private ClientController controller;
    private MapPanel map;
    private JLabel isYourTurn = new JLabel("Is your turn");
    private JLabel waitingForOther = new JLabel("Waiting for other players to make their move");
    private JLabel isLastTurn = new JLabel("\"ATTENTION: IT IS YOUR LAST TURN!!\"");
    private JButton placeCardButton = new JButton("Place card");
    private int id;
    private CardOrientation orientation;
    JPanel info = new JPanel();

    private ExecutorService executor;
    public GameFieldPanel(){   }

    public void setInitialChoice(int id, CardOrientation orientation){
        this.id = id;
        this.orientation = orientation;
    }

    public void buildPanel() throws RemoteException {
        removeAll();
        revalidate();
        repaint();
        this.setLayout(new BorderLayout());
        this.setSize(1600,1100);
        JPanel hostPlayer = newHostPanel();
        JPanel rightInfo = newInfo();
        JPanel otherPlayers = newOtherPlayers();
        JPanel chat = newChat();
        this.map = new MapPanel(placeCardButton);
        isYourTurn.setVisible(false);
        waitingForOther.setVisible(false);
        isLastTurn.setVisible(false);
        executor =  Executors.newSingleThreadExecutor();

        //this.add(isYourTurn, BorderLayout.EAST);
        //this.add(waitingForOther, BorderLayout.EAST);
        this.add(otherPlayers, BorderLayout.PAGE_START);
        this.add(hostPlayer, BorderLayout.PAGE_END);
        this.add(rightInfo, BorderLayout.LINE_END);
        this.add(chat, BorderLayout.LINE_START);
        this.add(map, BorderLayout.CENTER);

        controller = MainWindow.getClientController();

        new Thread(() -> {
            try {
                // Wait for 5 seconds (5000 milliseconds)
                Thread.sleep(1000);

                String currentPlayer = controller.getCurrentPlayerName();
                ControlledPlayerInfo controlledPlayer = controller.getControlledPlayerInformation();
                String controlledPlayerName = controlledPlayer.nickname();

              /*  Point initialCardPoint = new Point(0,0);
                int initialCardId= MainWindow.getClientController().getControlledPlayerInformation().field().placedCards().get(initialCardPoint).card().id();
                CardOrientation initialCardOrientation = MainWindow.getClientController().getControlledPlayerInformation().field().placedCards().get(initialCardPoint).orientation();
              */
                map.setAvailablePoints(controlledPlayer.field().availablePositions());
                map.insertInitialCard(id,orientation);

                while(!controller.hasGameEnded()){
                    String currentPlayerName = controller.getCurrentPlayerName();
                    DrawableCardsInfo drawableCards = controller.getDrawableCards();
                    waitingForOther.setVisible(true);
                    if(currentPlayer.equals(controlledPlayerName)){
                        controlledPlayer = controller.getControlledPlayerInformation();

                        if(controller.isLastTurn()){
                            isLastTurn.setVisible(true);
                        }

                        placeCardPhase();
                        //wait until card Placed
                        Thread.sleep(100000);
                        //controller make move
                        DrawChoice dChoice = drawCardPhase();

                        //controller.makeMove(,map.getLastPointPlaced(),,dChoice);
                        map.setAvailablePoints(controlledPlayer.field().availablePositions());
                        //when turn ended -> label reset
                        //waitingForOther.setVisible(true);
                        //isYourTurn.setVisible(false);
                        //placeCardButton.setVisible(false);
                    }

                }
            } catch (RemoteException | InterruptedException e) {
                e.printStackTrace();
            }

            // Update the label text on the Event Dispatch Thread
            SwingUtilities.invokeLater(() -> {
                // Repaint and revalidate the frame
                repaint();
                revalidate();
            });
        }).start();

        executor.submit(() -> {
            controller.waitForGameEnded();
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
        JButton rotateFirstCard = new JButton("Rotate this card");
        JButton rotateSecondCard = new JButton("Rotate this card");
        JButton rotateThirdCard = new JButton("Rotate this card");
        JLabel shownCard1 = new JLabel("You have selected this card");
        JLabel shownCard2 = new JLabel("You have selected this card");
        JLabel shownCard3 = new JLabel("You have selected this card");
        shownCard1.setVisible(false);
        shownCard2.setVisible(false);
        shownCard3.setVisible(false);

        //JButton logout= new JButton("Exit and logout");
        //JButton goBack= new JButton("Go Back");

        JPanel LabelPanel = new JPanel();
        LabelPanel.setLayout(new BoxLayout(LabelPanel, BoxLayout.Y_AXIS));
        //logout.setAlignmentX(CENTER_ALIGNMENT);
        //goBack.setAlignmentX(CENTER_ALIGNMENT);
        LabelPanel.add(this.isYourTurn);
        LabelPanel.add(this.waitingForOther);

        placeCardButton.setVisible(false);
        LabelPanel.add(placeCardButton);
        this.isLastTurn.setBackground(Color.RED);
        LabelPanel.add(this.isLastTurn);

        rotateFirstCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                firstcard.changeSide();
            }
        });

        rotateSecondCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondcard.changeSide();
            }
        });

        rotateThirdCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thirdcard.changeSide();
            }
        });

        firstcard.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                shownCard1.setVisible(true);
                shownCard2.setVisible(false);
                shownCard3.setVisible(false);
                map.setCardToPlace(firstcard.getId(),firstcard.getVisibleOrientation());
            }
        });

        secondcard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                shownCard1.setVisible(false);
                shownCard2.setVisible(true);
                shownCard3.setVisible(false);
                map.setCardToPlace(secondcard.getId(),secondcard.getVisibleOrientation());
            }
        });

        thirdcard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                shownCard1.setVisible(false);
                shownCard2.setVisible(false);
                shownCard3.setVisible(true);
                map.setCardToPlace(thirdcard.getId(),thirdcard.getVisibleOrientation());
            }
        });

        /*
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
        });*/

        GridBagConstraints gbc = new GridBagConstraints();


        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5,5,5,5);

        gbc.gridx=0;
        gbc.gridy=1;
        host.add(firstcard, gbc);
        gbc.gridy=2;
        host.add(rotateFirstCard, gbc);

        gbc.gridx=1;
        gbc.gridy=1;
        host.add(secondcard, gbc);
        gbc.gridy=2;
        host.add(rotateSecondCard, gbc);

        gbc.gridx=2;
        gbc.gridy=1;
        host.add(thirdcard, gbc);
        gbc.gridy=2;
        host.add(rotateThirdCard, gbc);

        gbc.gridx=4;
        gbc.weighty = 2.0;
        host.add(LabelPanel, gbc);

        gbc.gridy=0;
        gbc.gridx=0;
        host.add(shownCard1, gbc);

        gbc.gridy=0;
        gbc.gridx=1;
        host.add(shownCard2, gbc);

        gbc.gridy=0;
        gbc.gridx=2;
        host.add(shownCard3, gbc);

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

        gbc.insets = new Insets(10, 5, 10, 5); // Padding of 5 pixels on all sides

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

    private DrawChoice drawCardPhase(){
        DrawChoice dC = null;

        return dC;
    }

    private void placeCardPhase(){
        waitingForOther.setVisible(false);
        isYourTurn.setVisible(true);
        placeCardButton.setVisible(true);


    }

    private JPanel newChat(){
        JPanel chat= new JPanel();
        return chat;
    }



}
