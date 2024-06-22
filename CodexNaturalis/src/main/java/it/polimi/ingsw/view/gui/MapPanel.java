package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.dataobject.CardCellInfo;
import it.polimi.ingsw.dataobject.GameFieldInfo;
import it.polimi.ingsw.model.card.CardOrientation;

import javax.swing.*;
import java.awt.*;

import it.polimi.ingsw.model.map.Point;

import java.rmi.RemoteException;
import java.util.*;
import java.util.List;

/**
 * Panel of the map inserted inside the GameField
 */
public class MapPanel extends StandardPanel{
    private Map<Point,ImagePanel> placedCards = new HashMap<Point,ImagePanel>();
    private Map<Point,JButton> availablePlaces = new HashMap<Point,JButton>();

    private final int CardWidth = DefaultCardSizeInfo.CardWidth;
    private final int CardHeight = DefaultCardSizeInfo.CardHeight;
    private final int CenterCardX = DefaultCardSizeInfo.CenterCardX;
    private final int CenterCardY = DefaultCardSizeInfo.CenterCardY;
    private final int offsetX = DefaultCardSizeInfo.offsetX;
    private final int offsetY = DefaultCardSizeInfo.offsetY;
    private int layer = 0;

    private AbstractMap.SimpleEntry<Integer,CardOrientation> cardToPlace;
    private JLayeredPane jLayeredPane = new JLayeredPane();

    private JScrollPane jScrollPane = new JScrollPane(jLayeredPane);
    private List<Point> availablePointsFromModel = new ArrayList<Point>();
    private JButton placeMode;

    private Point lastPointPlaced = new Point(0,0);
    private int lastCardIdPlaced = 0;
    private CardOrientation lastOrientationPlaced = CardOrientation.FRONT;
    private boolean actionMade = false;
    boolean buttonShown = false;

    public MapPanel(JButton placebutton) {
        this.setLayout(new BorderLayout()); // Imposta il layout manager del pannello principale

        jLayeredPane.setPreferredSize(new Dimension(2000, 1400));
        jLayeredPane.setLayout(null);
        this.placeMode = placebutton;
        this.add(jScrollPane, BorderLayout.CENTER);
        this.setVisible(true);


        this.placeMode.addActionListener(e -> {
            if(cardToPlace == null) {return; }

            if(buttonShown) {
                for (JButton b : availablePlaces.values()) {
                    b.setVisible(false);
                }
                buttonShown = false;
            }else{
                for (JButton b : availablePlaces.values()) {
                    b.setVisible(true);
                }
                buttonShown = true;
            }

        });

    }

    public void insertInitialCard(int id, CardOrientation orientation){
        ImagePanel img1 = new ImagePanel(id);
        if(orientation.equals(CardOrientation.BACK))
            img1.changeSide();
        img1.setBounds(CenterCardX, CenterCardY, CardWidth, CardHeight);
        jLayeredPane.add(img1, Integer.valueOf(layer));
        placedCards.put(new Point(0, 0), img1);
        addAvailablePlace();
    }

    public void setCardToPlace(int id, CardOrientation orientation){  this.cardToPlace = new AbstractMap.SimpleEntry<>(id,orientation);  }

    public void hideAvailableSpaces() {
        for (JButton b : availablePlaces.values()) {
            b.setVisible(false);
        }
    }

    public void addAvailablePlace() {
        for(Point p : placedCards.keySet()) {
            Rectangle bounds = this.placedCards.get(p).getBounds();
            Point ULPoint = new Point(p.x() - 2, p.y() + 2), URPoint = new Point(p.x() + 2, p.y() + 2), DLPoint = new Point(p.x() - 2, p.y() - 2), DRPoint = new Point(p.x() + 2, p.y() - 2);

            layer++;
            if (availablePointsFromModel.contains(ULPoint)) {
                addSpaceButton(ULPoint, bounds.x - offsetX, bounds.y - offsetY);
            }
            if (availablePointsFromModel.contains(URPoint)) {
                addSpaceButton(URPoint, bounds.x + offsetX, bounds.y - offsetY);
            }
            if (availablePointsFromModel.contains(DLPoint)) {
                addSpaceButton(DLPoint, bounds.x - offsetX, bounds.y + offsetY);
            }
            if (availablePointsFromModel.contains(DRPoint)) {
                addSpaceButton(DRPoint, bounds.x + offsetX, bounds.y + offsetY);
            }
        }
    }

    private void addCardImage(JButton availableButtonPosition, Point p) throws RemoteException {
        int idToPlace = cardToPlace.getKey();
        if (availablePointsFromModel.contains(p)) {
            availablePointsFromModel.remove(p);
            ImagePanel img = new ImagePanel(idToPlace);
            if (cardToPlace.getValue().equals(CardOrientation.BACK)) {
                img.changeSide();
                lastOrientationPlaced = CardOrientation.BACK;
            }
            img.setBounds(availableButtonPosition.getBounds());
            img.setVisible(true);
            placedCards.put(new Point(p.x(),p.y()),img);
            //addAvailablePlace(img, p);
            jLayeredPane.add(img, Integer.valueOf(layer));
            jLayeredPane.remove(availableButtonPosition);
            hideAvailableSpaces();
            buttonShown = false;

            actionMade = true;
            lastPointPlaced = p;
            lastCardIdPlaced = idToPlace;
            cardToPlace = null;
        }
    }

    public void removeCardImage(Point p){
        ImagePanel img = placedCards.get(p);
        jLayeredPane.remove(img);
        placedCards.remove(p,img);
        availablePointsFromModel.add(p);

        addSpaceButton(p,img.getBounds().x,img.getBounds().y);
        resetLastValues();
        resetActionMade();
    }

    public void removeAllAvailablePlaces(){
        List<JButton> tempList = new ArrayList<>(availablePlaces.values());
        for(JButton b : tempList) {
            b.setVisible(false);
            jLayeredPane.remove(b);
        }
        availablePlaces.clear();
    }

    private void addSpaceButton(Point p, int boundsX, int boundsY){
        JButton newButton = new JButton(p.toString());
        newButton.setVisible(false);
        newButton.setBackground(new Color(112,112,112,50));
        newButton.setBounds(boundsX, boundsY, CardWidth, CardHeight);
        availablePlaces.put(p,newButton);
        jLayeredPane.add(newButton, Integer.valueOf(layer));
        newButton.addActionListener(e -> {
            try {
                addCardImage(newButton, p);
            } catch (RemoteException ex) {
                return;
            }
        });
    }

    public void setAvailablePointsFromModel(ArrayList<Point> points) {
        this.availablePointsFromModel = points;
    }

    public Point getLastPointPlaced() { return lastPointPlaced; }

    public int getLastCardIdPlaced() { return lastCardIdPlaced; }

    public CardOrientation getLastOrientationPlaced() { return lastOrientationPlaced; }

    public boolean actionMade(){ return actionMade; }

    public void resetActionMade(){ this.actionMade = false; }

    public void resetLastValues(){
        lastPointPlaced = new Point(0,0);
        lastCardIdPlaced = 0;
        lastOrientationPlaced = CardOrientation.FRONT;
    }

    public void drawOldMap() throws RemoteException {
        GameFieldInfo field = MainWindow.getClientController().getControlledPlayerInformation().field();
        for(Point p : field.insertionOrder()){
            if(p.equals(new Point(0,0)))
                continue;
            availablePointsFromModel.add(p);
            addAvailablePlace();
            CardCellInfo cardInfo = field.placedCards().get(p);
            setCardToPlace(cardInfo.card().id(),cardInfo.orientation());
            addCardImage(availablePlaces.get(p),p);
        }
        removeAllAvailablePlaces();
        addAvailablePlace();
    }

}
