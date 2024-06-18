package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.dataobject.CardCellInfo;
import it.polimi.ingsw.dataobject.GameFieldInfo;
import it.polimi.ingsw.dataobject.OpponentInfo;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.CardCell;
import it.polimi.ingsw.model.map.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.AbstractMap;
import java.util.Map;

public class OtherMapsPanel extends StandardPanel{
    private JButton goBack = new JButton("Go Back");
    private GameFieldInfo field;
    private JLayeredPane jLayeredPane = new JLayeredPane();
    private JScrollPane jScrollPane = new JScrollPane(jLayeredPane);

    private final int CardWidth = DefaultCardSizeInfo.CardWidth;
    private final int CardHeight = DefaultCardSizeInfo.CardHeight;
    private final int CenterCardX = DefaultCardSizeInfo.CenterCardX;
    private final int CenterCardY = DefaultCardSizeInfo.CenterCardY;
    private final int offsetX = DefaultCardSizeInfo.offsetX;
    private final int offsetY = DefaultCardSizeInfo.offsetY;
    private int layer = 0;

    private AbstractMap.SimpleEntry<Point,ImagePanel> lastPointPlaced;


    public OtherMapsPanel(OpponentInfo player, JButton returnHome){
        JLabel opponentName = new JLabel("Opponent Name: "+ player.nickname());

        field = player.field();
        goBack = returnHome;
        goBack.setVisible(true);

        jLayeredPane.setPreferredSize(new Dimension(2000, 1400));
        jLayeredPane.setLayout(null);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.setVisible(true);

        for(Map.Entry<Point,CardCellInfo> entry : field.placedCards().entrySet()){
            Point currentPoint = entry.getKey();
            CardCellInfo currentCard = entry.getValue();

            if(entry.getKey().equals(new Point(0,0))){
                insertInitialCard(currentCard.card().id(),currentCard.orientation());
                continue;
            }
            if(currentPoint.x() == lastPointPlaced.getKey().x() + 2  && currentPoint.y() == lastPointPlaced.getKey().y() + 2){ //Up Right
                lastPointPlaced = new AbstractMap.SimpleEntry<>(
                        currentPoint,
                        addCardImage(currentCard,lastPointPlaced.getValue().getX() + offsetX,lastPointPlaced.getValue().getY() + offsetY )
                );
            }
            else if(currentPoint.x() == lastPointPlaced.getKey().x() + 2  && currentPoint.y() == lastPointPlaced.getKey().y() - 2){ //Down Right
                lastPointPlaced = new AbstractMap.SimpleEntry<>(
                        currentPoint,
                        addCardImage(currentCard,lastPointPlaced.getValue().getX() + offsetX,lastPointPlaced.getValue().getY() - offsetY )
                );
            }
            else if(currentPoint.x() == lastPointPlaced.getKey().x() - 2  && currentPoint.y() == lastPointPlaced.getKey().y() + 2){ //Up Left
                lastPointPlaced = new AbstractMap.SimpleEntry<>(
                        currentPoint,
                        addCardImage(currentCard,lastPointPlaced.getValue().getX() - offsetX,lastPointPlaced.getValue().getY() + offsetY )
                );
            }
            else if(currentPoint.x() == lastPointPlaced.getKey().x() - 2  && currentPoint.y() == lastPointPlaced.getKey().y() - 2){ //Down Left
                lastPointPlaced = new AbstractMap.SimpleEntry<>(
                        currentPoint,
                        addCardImage(currentCard,lastPointPlaced.getValue().getX() - offsetX,lastPointPlaced.getValue().getY() - offsetY )
                );
            }


        }

    }

    public void insertInitialCard(int id, CardOrientation orientation){
        ImagePanel img1 = new ImagePanel(id);
        if(orientation.equals(CardOrientation.BACK))
            img1.changeSide();
        img1.setBounds(CenterCardX, CenterCardY, CardWidth, CardHeight);
        jLayeredPane.add(img1, Integer.valueOf(layer));
    }

    private ImagePanel addCardImage(CardCellInfo c,int x, int y) {
        ImagePanel img = new ImagePanel(c.card().id());
        layer++;
        if (c.orientation().equals(CardOrientation.BACK)) {
            img.changeSide();
        }
        img.setBounds(x,y, CardWidth, CardHeight);
        img.setVisible(true);
        jLayeredPane.add(img, Integer.valueOf(layer));
        return img;
    }

}
