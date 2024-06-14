package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.card.CardOrientation;

import javax.swing.*;
import java.awt.*;
import it.polimi.ingsw.model.map.Point;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class MapPanel extends StandardPanel{
    private Map<ImagePanel,Point> placedCards = new HashMap<ImagePanel,Point>();
    private List<JButton> availablePlaces = new ArrayList<JButton>();
    private final int CardWidth = 100;
    private final int CardHeight = 80;
    private final int CenterCardX = 450;
    private final int CenterCardY = 410;
    private final int offsetX = 77;
    private final int offsetY = 47;
    private int layer = 0;
    private AbstractMap.SimpleEntry<Integer,CardOrientation> cardToPlace;
    private JLayeredPane jLayeredPane = new JLayeredPane();
    private List<Point> availablePoints = new ArrayList<Point>();
    private JButton placeMode;

    public MapPanel(JButton placebutton) {
        this.setLayout(new BorderLayout()); // Imposta il layout manager del pannello principale

        this.placeMode = placebutton;
        this.add(jLayeredPane);
        this.setVisible(true);

        this.placeMode.addActionListener(e -> {
            //Here we save the chosen card to place
            if(cardToPlace == null) {return; }
            for (JButton b : availablePlaces) {
                b.setVisible(true);
            }
        });
    }

    public void insertInitialCard(int id, CardOrientation orientation){
        ImagePanel img1 = new ImagePanel(id);
        if(orientation.equals(CardOrientation.BACK))
            img1.changeSide();
        img1.setBounds(CenterCardX, CenterCardY, CardWidth, CardHeight);
        addAvailablePlace(img1.getBounds(), new Point(0, 0));
        jLayeredPane.add(img1, Integer.valueOf(layer));
        placedCards.put(img1, new Point(0, 0));
    }

    public void setCardToPlace(int id, CardOrientation orientation){  this.cardToPlace = new AbstractMap.SimpleEntry<>(id,orientation);  }

    public void hideAvailableSpaces() {
        for (JButton b : availablePlaces) {
            b.setVisible(false);
        }
    }

    public void addAvailablePlace(Rectangle bounds, Point p) {
        Point ULPoint = new Point(p.x() - 2, p.y() + 2), URPoint = new Point(p.x() + 2, p.y() + 2), DLPoint = new Point(p.x() - 2, p.y() - 2), DRPoint = new Point(p.x() + 2, p.y() - 2);
        JButton UpLSpace, UpRSpace, DownLSpace, DownRSpace;

        layer++;
        if (availablePoints.contains(ULPoint)) {
            UpLSpace = new JButton("UL " + ULPoint.toString());
            UpLSpace.setVisible(false);
            UpLSpace.setBackground(Color.GRAY);
            UpLSpace.setBounds(bounds.x - offsetX, bounds.y - offsetY, CardWidth, CardHeight);
            availablePlaces.add(UpLSpace);
            jLayeredPane.add(UpLSpace, Integer.valueOf(layer));
            UpLSpace.addActionListener(e -> {
                addCardImage(UpLSpace, ULPoint);
            });
        }
        if (availablePoints.contains(URPoint)) {
            UpRSpace = new JButton("UR " + URPoint.toString());
            UpRSpace.setVisible(false);
            UpRSpace.setBackground(Color.GRAY);
            UpRSpace.setBounds(bounds.x + offsetX, bounds.y - offsetY, CardWidth, CardHeight);
            availablePlaces.add(UpRSpace);
            jLayeredPane.add(UpRSpace, Integer.valueOf(layer));
            UpRSpace.addActionListener(e -> {
                addCardImage(UpRSpace, URPoint);
            });
        }
        if (availablePoints.contains(DLPoint)) {
            DownLSpace = new JButton("DL " + DLPoint.toString());
            DownLSpace.setVisible(false);
            DownLSpace.setBackground(Color.GRAY);
            DownLSpace.setBounds(bounds.x - offsetX, bounds.y + offsetY, CardWidth, CardHeight);
            availablePlaces.add(DownLSpace);
            jLayeredPane.add(DownLSpace, Integer.valueOf(layer));
            DownLSpace.addActionListener(e -> {
                addCardImage(DownLSpace, DLPoint);
            });
        }
        if (availablePoints.contains(DRPoint)) {
            DownRSpace = new JButton("DR " + DRPoint.toString());
            DownRSpace.setVisible(false);
            DownRSpace.setBackground(Color.GRAY);
            DownRSpace.setBounds(bounds.x + offsetX, bounds.y + offsetY, CardWidth, CardHeight);
            availablePlaces.add(DownRSpace);
            jLayeredPane.add(DownRSpace, Integer.valueOf(layer));
            DownRSpace.addActionListener(e -> {
                addCardImage(DownRSpace, DRPoint);
            });
        }

    }

    private void addCardImage(JButton availablePosition, Point p) {
        if (availablePoints.contains(p)) {
            availablePoints.remove(p);
            ImagePanel img = new ImagePanel(cardToPlace.getKey());
            if (cardToPlace.getValue().equals(CardOrientation.BACK))
                img.changeSide();
            img.setBounds(availablePosition.getBounds());
            img.setVisible(true);
            placedCards.put(img, new Point(p.x(),p.y()));
            addAvailablePlace(img.getBounds(), p);
            jLayeredPane.add(img, Integer.valueOf(layer));
            jLayeredPane.remove(availablePosition);
            hideAvailableSpaces();
        }
    }

    public void setAvailablePoints(ArrayList<Point> points) {
        this.availablePoints = points;
    }


 /*   public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Map Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 800);

            MapPanel mapPanel = new MapPanel();

            frame.add(mapPanel);
            frame.setVisible(true);
        });
    }*/

}
