package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.dataobject.CardCellInfo;
import it.polimi.ingsw.dataobject.GameFieldInfo;
import it.polimi.ingsw.dataobject.OpponentInfo;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class OtherMapsPanel extends JFrame {
    private GameFieldInfo field;
    private JLayeredPane jLayeredPane = new JLayeredPane();
    //private JScrollPane jScrollPane = new JScrollPane(jLayeredPane);

    private final int CardWidth = DefaultCardSizeInfo.CardWidth;
    private final int CardHeight = DefaultCardSizeInfo.CardHeight;
    private final int CenterCardX = 700;
    private final int CenterCardY = 510;
    private final int offsetX = DefaultCardSizeInfo.offsetX;
    private final int offsetY = DefaultCardSizeInfo.offsetY;
    private int layer = 0;

    private AbstractMap.SimpleEntry<Point, ImagePanel> lastPointPlaced;
    private Map<Point,Rectangle> availablePlaces = new HashMap<Point,Rectangle>();

    public OtherMapsPanel(String oppName) throws RemoteException {
        OpponentInfo player = MainWindow.getClientController().getOpponentInformation(oppName);

        JLabel opponentName = new JLabel("Opponent Name: " + player.nickname());

        this.setSize(1500, 800);
        opponentName.setBounds(0, 0, 100, 30);

        field = player.field();
        opponentName.setVisible(true);

        insertInitialCard(field.placedCards().get(new Point(0, 0)).card().id(), field.placedCards().get(new Point(0, 0)).orientation());
        drawMap();

        this.add(opponentName);
        //this.add(jScrollPane, BorderLayout.CENTER);
        this.add(jLayeredPane);
        this.setVisible(true);
    }

    public void insertInitialCard(int id, CardOrientation orientation) {
        ImagePanel img1 = new ImagePanel(id);
        if (orientation.equals(CardOrientation.BACK))
            img1.changeSide();
        img1.setBounds(CenterCardX, CenterCardY, CardWidth, CardHeight);
        addAvailablePlace(new Point(0,0), CenterCardX, CenterCardY);
        jLayeredPane.add(img1, Integer.valueOf(layer));
    }

    private void addCardImage(Point p, CardCellInfo c,Rectangle r) {
        ImagePanel img = new ImagePanel(c.card().id());
        layer++;
        if (c.orientation().equals(CardOrientation.BACK)) {
            img.changeSide();
        }
        img.setBounds(r);
        img.setVisible(true);
        jLayeredPane.add(img, Integer.valueOf(layer));
        addAvailablePlace(p, r.x, r.y);
    }

    private void drawMap() {
        for (Point p : field.insertionOrder()) {
            CardCellInfo currentCard = field.placedCards().get(p);

            if (p.equals(new Point(0, 0))) {
                continue;
            }
            //placedCards are not sorted
            addCardImage(p,currentCard,availablePlaces.get(p));
        }

    }

    private void addAvailablePlace(Point p, int x, int y) {
        Point ULPoint = new Point(p.x() - 2, p.y() + 2);
        Point URPoint = new Point(p.x() + 2, p.y() + 2);
        Point DLPoint = new Point(p.x() - 2, p.y() - 2);
        Point DRPoint = new Point(p.x() + 2, p.y() - 2);

        layer++;
        if(field.availablePositions().contains(ULPoint))
            addSpace(ULPoint, x - offsetX, y - offsetY);
        if(field.availablePositions().contains(URPoint))
            addSpace(URPoint, x + offsetX, y - offsetY);
        if(field.availablePositions().contains(DLPoint))
            addSpace(DLPoint, x - offsetX, y + offsetY);
        if(field.availablePositions().contains(DRPoint))
            addSpace(DRPoint, x + offsetX, y + offsetY);

    }

    private void addSpace(Point p, int boundsX, int boundsY) {
        if(!availablePlaces.containsKey(p))
            availablePlaces.put(p,new Rectangle(boundsX,boundsY,CardWidth,CardHeight));
    }

}
