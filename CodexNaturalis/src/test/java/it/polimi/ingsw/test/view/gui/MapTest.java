package it.polimi.ingsw.test.view.gui;

import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.view.gui.ImagePanel;
import it.polimi.ingsw.view.gui.StandardPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapTest extends StandardPanel {
    private Map<ImagePanel,Point> placedCards = new HashMap<ImagePanel,Point>();
    private List<JButton> availablePlaces = new ArrayList<JButton>();
    private final int CardWidth = 100;
    private final int CardHeight = 80;
    private final int CenterCardX = 450;
    private final int CenterCardY = 410;
    private final int offsetX = 77;
    private final int offsetY = 47;
    private int layer = 0;
    private JLayeredPane jLayeredPane = new JLayeredPane();
    private List<Point> availablePoints = new ArrayList<Point>();

    public MapTest(){

        //availablePoints.add(new Point(0,0));
        availablePoints.add(new Point(2,2));
        availablePoints.add(new Point(4,4));
        availablePoints.add(new Point(6,4));
        availablePoints.add(new Point(8,4));
        availablePoints.add(new Point(2,4));
        availablePoints.add(new Point(0,4));

        JFrame frame = new JFrame("JLayeredPane Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 900);

        JButton placeMode = new JButton("Place Mode");
        placeMode.setBounds(0,0,150,30);
        placeMode.setVisible(true);

        ImagePanel img1 = new ImagePanel(10);
        img1.setBounds(CenterCardX, CenterCardY, CardWidth, CardHeight);
        addAvailablePlace(img1.getBounds(),new Point(0,0));
        jLayeredPane.add(img1, Integer.valueOf(layer));
        placedCards.put(img1, new Point(0,0));

        frame.add(placeMode);
        frame.add(jLayeredPane);
        frame.setVisible(true);

        placeMode.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for(JButton b : availablePlaces) {
                    b.setVisible(true);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

    }

    public void hideAvailableSpaces(){
        for(JButton b: availablePlaces){
            b.setVisible(false);
        }
    }

    public void addAvailablePlace(Rectangle bounds, Point p){
        Point ULPoint = new Point(p.x-2,p.y+2),URPoint = new Point(p.x+2,p.y+2),DLPoint = new Point(p.x-2,p.y-2),DRPoint = new Point(p.x+2,p.y-2);
        JButton UpLSpace,UpRSpace,DownLSpace,DownRSpace;

        layer++;
        if(availablePoints.contains(ULPoint)) {
            UpLSpace = new JButton("UL (" + ULPoint.x + ", " + ULPoint.y + ")");
            UpLSpace.setVisible(false);
            UpLSpace.setBackground(Color.GRAY);
            UpLSpace.setOpaque(false);
            UpLSpace.setBounds(bounds.x - offsetX, bounds.y - offsetY, CardWidth, CardHeight);
            availablePlaces.add(UpLSpace);
            jLayeredPane.add(UpLSpace, Integer.valueOf(layer));
            UpLSpace.addActionListener(e -> {
                addCardImage(UpLSpace, ULPoint);
            });
        }
        if(availablePoints.contains(URPoint)) {
            UpRSpace = new JButton("UR (" + URPoint.x + ", " + URPoint.y + ")");
            UpRSpace.setVisible(false);
            UpRSpace.setBackground(Color.GRAY);
            UpRSpace.setOpaque(false);
            UpRSpace.setBounds(bounds.x + offsetX, bounds.y - offsetY, CardWidth, CardHeight);
            availablePlaces.add(UpRSpace);
            jLayeredPane.add(UpRSpace, Integer.valueOf(layer));
            UpRSpace.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addCardImage(UpRSpace, URPoint);
                }
            });
        }
        if(availablePoints.contains(DLPoint)) {
            DownLSpace = new JButton("DL (" + DLPoint.x + ", " + DLPoint.y + ")");
            DownLSpace.setVisible(false);
            DownLSpace.setBackground(Color.GRAY);
            DownLSpace.setOpaque(false);
            DownLSpace.setBounds(bounds.x - offsetX, bounds.y + offsetY, CardWidth, CardHeight);
            availablePlaces.add(DownLSpace);
            jLayeredPane.add(DownLSpace, Integer.valueOf(layer));
            DownLSpace.addActionListener(e -> {
                addCardImage(DownLSpace, DLPoint);
            });
        }
        if(availablePoints.contains(DRPoint)) {
            DownRSpace = new JButton("DR (" + DRPoint.x + ", " + DRPoint.y + ")");
            DownRSpace.setVisible(false);
            DownRSpace.setBackground(Color.GRAY);
            DownRSpace.setOpaque(false);
            DownRSpace.setBounds(bounds.x + offsetX, bounds.y + offsetY, CardWidth, CardHeight);
            availablePlaces.add(DownRSpace);
            jLayeredPane.add(DownRSpace, Integer.valueOf(layer));
            DownRSpace.addActionListener(e -> {
                addCardImage(DownRSpace, DRPoint);
            });
        }

    }

    private void addCardImage(JButton availablePosition,Point p){
        if(availablePoints.contains(p)) {
            availablePoints.remove(p);
            ImagePanel img = new ImagePanel(11);
            img.setBounds(availablePosition.getBounds());
            img.setVisible(true);
            placedCards.put(img, new Point());
            addAvailablePlace(img.getBounds(), p);
            jLayeredPane.add(img, Integer.valueOf(layer));
            jLayeredPane.remove(availablePosition);
            hideAvailableSpaces();
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(MapTest::new);
    }

}
