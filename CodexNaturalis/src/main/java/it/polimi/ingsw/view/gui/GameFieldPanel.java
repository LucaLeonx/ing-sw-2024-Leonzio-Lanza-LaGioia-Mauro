package it.polimi.ingsw.view.gui;


import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.dataobject.CardInfo;
import it.polimi.ingsw.dataobject.ControlledPlayerInfo;
import it.polimi.ingsw.dataobject.ObjectiveInfo;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.card.CardOrientation;

import javax.swing.*;
import java.awt.*;
import it.polimi.ingsw.model.map.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Actual Panel of the Game where a player can interact with its map
 */
public class GameFieldPanel extends StandardPanel {

    private final Color gray = new Color(220,220,220); // RGB values
    private ClientController controller;
    private MapPanel map;
    private final JLabel isYourTurn = new JLabel("Your turn");
    private final JLabel waitingForOther = new JLabel("Not your turn");
    private JLabel isLastTurn = new JLabel("\"LAST TURN!\"");
    private JLabel cannotPlaceThisCard = new JLabel("\"INVALID MOVE!\"");
    private final JButton placeCardButton = new JButton("Place card");
    private int id;
    private CardOrientation orientation;
    private ArrayList<CardInfo> cardsInHands;
    private ObjectiveInfo secretObjective;
    private final JButton chooseWhereDraw = new JButton("Choose Draw");
    private JLabel choosenDrawLabel = new JLabel("Choosen Draw: ");
    private Optional<DrawChoice> lastDrawChoice = Optional.empty();
    private boolean unlock = false;
    private ExecutorService executor;

    public GameFieldPanel(){

    }

    public void buildPanel() throws RemoteException {
        removeAll();
        revalidate();
        repaint();
        cannotPlaceThisCard.setForeground(Color.RED);
        isLastTurn.setForeground(Color.RED);
        this.setLayout(new BorderLayout());
        this.setSize(1600,1100);
        JPanel otherPlayers = newOtherPlayers();
        JPanel chat = newChat();
        this.map = new MapPanel(placeCardButton);
        isYourTurn.setVisible(false);
        waitingForOther.setVisible(false);
        isLastTurn.setVisible(false);
        cannotPlaceThisCard.setVisible(false);
        executor = Executors.newSingleThreadExecutor();
        id = MainWindow.getClientController().getControlledPlayerInformation().field().placedCards().get(new Point(0,0)).card().id();
        orientation = MainWindow.getClientController().getControlledPlayerInformation().field().placedCards().get(new Point(0,0)).orientation();

        this.add(otherPlayers, BorderLayout.PAGE_START);
        this.add(chat, BorderLayout.LINE_START);
        this.add(map, BorderLayout.CENTER);

        controller = MainWindow.getClientController();

        new Thread(() -> {
            try {
                JPanel hostPlayer = newHostPanel();
                JPanel rightInfo = newInfo();
                this.add(hostPlayer, BorderLayout.PAGE_END);
                this.add(rightInfo, BorderLayout.LINE_END);

                ControlledPlayerInfo controlledPlayer = controller.getControlledPlayerInformation();
                String controlledPlayerName = controlledPlayer.nickname();

                map.setAvailablePoints(controlledPlayer.field().availablePositions());
                map.insertInitialCard(id,orientation);

                while(!controller.hasGameEnded()){
                    Thread.sleep(500);
                    cannotPlaceThisCard.setVisible(false);

                    String currentPlayerName = controller.getCurrentPlayerName();

                    waitingForOther.setVisible(true);
                    if(currentPlayerName.equals(controlledPlayerName)){
                        this.remove(rightInfo);
                        rightInfo = newInfo();
                        this.add(rightInfo, BorderLayout.LINE_END);

                        waitingForOther.setVisible(false);
                        isYourTurn.setVisible(true);
                        placeCardButton.setVisible(true);

                        controlledPlayer = controller.getControlledPlayerInformation();
                        map.setAvailablePoints(controlledPlayer.field().availablePositions());//work only the first time, why??

                        if(controller.isLastTurn()){
                            isLastTurn.setVisible(true);
                        }

                        Point p;
                        CardInfo cardInfo = placeCardPhase();

                        p = map.getLastPointPlaced();
                        if(!cardInfo.getSide(map.getLastOrientationPlaced()).isPlayable()) {
                            map.removeCardImage(p);
                            map.resetLastValues();
                            cannotPlaceThisCard.setVisible(true);
                            Thread.sleep(500);
                            continue;
                        }

                        DrawChoice dChoice = drawCardPhase();

                        controller.makeMove(
                                cardInfo,
                                p,
                                map.getLastOrientationPlaced(),
                                dChoice);

                        controlledPlayer = controller.getControlledPlayerInformation();
                        map.setAvailablePoints(controlledPlayer.field().availablePositions());
                        map.removeAllAvailablePlaces();
                        map.addAvailablePlace();

                        //when turn ended -> label reset
                        waitingForOther.setVisible(true);
                        isYourTurn.setVisible(false);
                        placeCardButton.setVisible(false);

                        map.resetLastValues();

                        lastDrawChoice = Optional.empty();

                        //Deck and Hand update
                        this.remove(rightInfo);
                        rightInfo = newInfo();
                        this.add(rightInfo, BorderLayout.LINE_END);

                        this.remove(hostPlayer);
                        hostPlayer = newHostPanel();
                        this.add(hostPlayer, BorderLayout.PAGE_END);
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
        JPanel host = new JPanel();
        host.setLayout(new GridBagLayout());
        cardsInHands = new ArrayList<>();


        try {
            cardsInHands = MainWindow.getClientController().getControlledPlayerInformation().cardsInHand();
            secretObjective = MainWindow.getClientController().getControlledPlayerInformation().secretObjective();
        } catch (Exception e) {
            System.out.println(e);
        }
        ImagePanel firstcard = new ImagePanel(cardsInHands.get(0).id());
        ImagePanel secondcard = new ImagePanel(cardsInHands.get(1).id());
        ImagePanel thirdcard = new ImagePanel(cardsInHands.get(2).id());
        ImagePanel fourthCard= new ImagePanel(secretObjective.id());
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

        JPanel labelPane = new JPanel();
        labelPane.setLayout(new BoxLayout(labelPane, BoxLayout.Y_AXIS));
        //logout.setAlignmentX(CENTER_ALIGNMENT);
        //goBack.setAlignmentX(CENTER_ALIGNMENT);
        labelPane.add(this.isYourTurn);
        labelPane.add(this.waitingForOther);
        labelPane.add(this.cannotPlaceThisCard);

        labelPane.setBackground(gray);

        placeCardButton.setVisible(false);
        labelPane.add(placeCardButton);
        this.isLastTurn.setBackground(Color.RED);
        labelPane.add(this.isLastTurn);

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
                shownCard1.setText("You have selected this card on " + firstcard.getVisibleOrientation().toString());
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
                shownCard2.setText("You have selected this card on " + secondcard.getVisibleOrientation().toString());
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
                shownCard3.setText("You have selected this card on " + thirdcard.getVisibleOrientation().toString());
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

        gbc.gridx=3;
        gbc.gridy=1;
        host.add(fourthCard, gbc);

        labelPane.setPreferredSize(new Dimension(200, 60));

        gbc.gridy=1;
        gbc.gridx=4;
        gbc.weighty = 2.0;
        host.add(labelPane, gbc);

        gbc.gridy=0;
        gbc.gridx=0;
        host.add(shownCard1, gbc);

        gbc.gridy=0;
        gbc.gridx=1;
        host.add(shownCard2, gbc);

        gbc.gridy=0;
        gbc.gridx=2;
        host.add(shownCard3, gbc);

        host.setBackground(gray);

        this.repaint();
        this.revalidate();
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


            for (String playerName : PlayerNames) {
                if (!playerName.equals(currentPlayerName)) {
                    JButton playerButton = new JButton(playerName);
                    playerButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                //MainWindow.goToWindow("otherMapsPanel");
                                //MainWindow.otherMapsPanel.buildPanel(playerName);
                                JFrame otherMap = new OtherMapsFrame(playerName);
                            } catch (RemoteException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    });
                    playerButtons.add(playerButton);
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

        return otherPlayes;
    }

    private JPanel newInfo(){
        int j=0;
        JPanel info = new JPanel();
        info.setLayout(new GridBagLayout());

        /*
        HashMap<Symbol, Integer> symbolCounter= new HashMap<>();

        JLabel insectPoints= new JLabel();
        JLabel animalPoints= new JLabel();
        JLabel fungiPoints= new JLabel();
        JLabel plantPoints= new JLabel();
        JLabel inkwellPoints = new JLabel();
        JLabel manuscriptPoints = new JLabel();
        JLabel quillPoints = new JLabel(); */

        List<JLabel> playerPoints = new ArrayList<>();
        List<String> playerNames = new ArrayList<>();

        int resourceCardsDeckId;
        int resourceCard1Id;
        int resourceCard2Id;
        int goldCardsDeckId;
        int goldCard1Id;
        int goldCard2Id;
        int commonObjective1Id;
        int commonObjective2Id;


        try {
            //symbolCounter= MainWindow.getClientController().getControlledPlayerInformation().field().symbolCounterMap();
            playerNames = MainWindow.getClientController().getPlayerNames();

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

        /*insectPoints.setText(Symbol_String.INSECT_SYMBOL + symbolCounter.get(Symbol.INSECT).toString());
        animalPoints.setText(Symbol_String.ANIMAL_SYMBOL +symbolCounter.get(Symbol.ANIMAL).toString());
        fungiPoints.setText(Symbol_String.FUNGI_SYMBOL + symbolCounter.get(Symbol.FUNGI).toString());
        plantPoints.setText(Symbol_String.PLANT_SYMBOL + symbolCounter.get(Symbol.PLANT).toString());
        inkwellPoints.setText(Symbol_String.INKWELL_SYMBOL + symbolCounter.get(Symbol.INKWELL).toString());
        manuscriptPoints.setText(Symbol_String.MANUSCRIPT_SYMBOL + symbolCounter.get(Symbol.MANUSCRIPT).toString());
        quillPoints.setText(Symbol_String.QUILL_SYMBOL + symbolCounter.get(Symbol.QUILL).toString());*/

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

        resourceCardsDeck.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                lastDrawChoice = Optional.of(DrawChoice.DECK_RESOURCE);
                choosenDrawLabel.setText("Selected: " + DrawChoice.DECK_RESOURCE);
            }
        });

        resourceCard1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                lastDrawChoice = Optional.of(DrawChoice.RESOURCE_CARD_1);
                choosenDrawLabel.setText("Selected: " + DrawChoice.RESOURCE_CARD_1);
            }
        });

        resourceCard2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                lastDrawChoice = Optional.of(DrawChoice.RESOURCE_CARD_2);
                choosenDrawLabel.setText("Selected: " + DrawChoice.RESOURCE_CARD_2);
            }
        });

        goldCardsDeck.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                lastDrawChoice = Optional.of(DrawChoice.DECK_GOLD);
                choosenDrawLabel.setText("Selected: " + DrawChoice.DECK_GOLD);
            }
        });

        goldCard1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                lastDrawChoice = Optional.of(DrawChoice.GOLD_CARD_1);
                choosenDrawLabel.setText("Selected: " + DrawChoice.GOLD_CARD_1);
            }
        });

        goldCard2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                lastDrawChoice = Optional.of(DrawChoice.GOLD_CARD_2);
                choosenDrawLabel.setText("Selected: " + DrawChoice.GOLD_CARD_2);
            }
        });

        GridBagConstraints gbc= new GridBagConstraints();

        /*gbc.gridx=0;
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
        info.add(quillPoints, gbc);*/

        JLabel scoreboard = new JLabel("SCOREBOARD:");
        scoreboard.setFont(new Font("Arial", Font.BOLD, 16));
        scoreboard.setForeground(Color.gray);
        scoreboard.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridwidth=4;

        for(j=0;j<playerNames.size(); j++){
            try {
                JLabel player = new JLabel(playerNames.get(j) + " has " + MainWindow.getClientController().getOpponentInformation(playerNames.get(j)).score() + " points");
                gbc.gridy=j+1;
                player.setHorizontalAlignment(SwingConstants.CENTER);
                player.setForeground(Color.gray);
                info.add(player, gbc);
            }
            catch(RemoteException e){
                System.out.println(e.getMessage());
            }
        }

        gbc.gridy=0;
        gbc.gridx=0;
        info.add(scoreboard, gbc);

        gbc.gridwidth=2;

        gbc.insets = new Insets(5, 5, 5, 5); // Padding of 5 pixels on all sides

        gbc.gridy=2+j;
        gbc.gridx=0;
        info.add(deckLabel, gbc);

        gbc.gridy=3+j;
        gbc.gridx=0;
        info.add(resourceCardsDeck, gbc);

        gbc.gridx=2;
        info.add(goldCardsDeck, gbc);

        gbc.gridy = 4+j;
        gbc.gridx = 0;
        info.add(resourceLabel, gbc);

        gbc.gridy=5+j;
        gbc.gridx=0;
        info.add(resourceCard1, gbc);

        gbc.gridx=2;
        info.add(resourceCard2, gbc);

        gbc.gridy = 6+j;
        gbc.gridx = 0;
        info.add(goldLabel, gbc);

        gbc.gridy = 7+j;
        gbc.gridx = 0;
        info.add(goldCard1, gbc);

        gbc.gridx=2;
        info.add(goldCard2, gbc);

        gbc.gridy = 9+j;
        gbc.gridx = 0;
        info.add(objectiveLabel, gbc);

        gbc.gridy = 10+j;
        gbc.gridx = 0;
        info.add(commonObjective1, gbc);

        gbc.gridx=2;
        info.add(commonObjective2, gbc);

        gbc.gridx = 0;
        gbc.gridy = 11+j;
        gbc.gridwidth=5;
        info.add(chooseWhereDraw, gbc);

    /*    choosenDrawLabel.setPreferredSize(new Dimension(200, 20));
        choosenDrawLabel.setMinimumSize(new Dimension(200,20));
        choosenDrawLabel.setMaximumSize(new Dimension(200,20));*/

        gbc.gridy = 12+j;
        info.add(choosenDrawLabel, gbc);

        choosenDrawLabel.setVisible(false);
        chooseWhereDraw.setVisible(false);
        // Set the background color of the panel

        info.setBackground(gray);

        // Make the frame visible
        info.setVisible(true);

        this.repaint();
        this.revalidate();
        return info;
    }

    private DrawChoice drawCardPhase() throws InterruptedException {
        DrawChoice dC;
        choosenDrawLabel.setVisible(true);
        chooseWhereDraw.setVisible(true);
        isYourTurn.setVisible(false);
        placeCardButton.setVisible(false);

        chooseWhereDraw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(lastDrawChoice.isPresent())
                    unlock = true;
            }
        });

        while(!unlock){
            Thread.sleep(1000);
        }

        dC = lastDrawChoice.get();
        unlock = false;
        lastDrawChoice = Optional.empty();
        choosenDrawLabel.setText("Selected: ");
        choosenDrawLabel.setVisible(false);
        chooseWhereDraw.setVisible(false);
        isYourTurn.setVisible(true);
        placeCardButton.setVisible(true);
        return dC;
    }

    private CardInfo placeCardPhase() throws InterruptedException {
        map.resetActionMade();
        while(!map.actionMade()){
            Thread.sleep(1000);
        }

        for(CardInfo c : cardsInHands ){
            if(c.id() == map.getLastCardIdPlaced()){
                return c;
            }
        }
        map.resetActionMade();
        placeCardButton.setVisible(false);
        return null;
    }

    private JPanel newChat(){
        JPanel chat= new JPanel();
        return chat;
    }



}
