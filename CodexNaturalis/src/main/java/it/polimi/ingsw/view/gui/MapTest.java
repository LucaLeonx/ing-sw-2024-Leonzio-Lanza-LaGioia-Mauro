package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MapTest extends StandardPanel{
    private List<ImagePanel> placedCards = new ArrayList<ImagePanel>();
    private List<JButton> availablePlaces = new ArrayList<JButton>();
    private final int CardWidth = 100;
    private final int CardHeight = 80;
    private final int CenterCardX = 450;
    private final int CenterCardY = 410;
    private final int offsetX = 77;
    private final int offsetY = 47;
    private JLayeredPane jLayeredPane = new JLayeredPane();


    public MapTest(){

        JFrame frame = new JFrame("JLayeredPane Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 900);

        JButton placeMode = new JButton("Place Mode");
        placeMode.setBounds(0,0,150,30);
        placeMode.setVisible(true);


        ImagePanel img1 = new ImagePanel(10);
        img1.setBounds(CenterCardX, CenterCardY, CardWidth, CardHeight);
        addAvailablePlace(img1.getBounds());
        jLayeredPane.add(img1, Integer.valueOf(0));

        frame.add(placeMode);
        frame.add(jLayeredPane);
        frame.setVisible(true);

        placeMode.addActionListener(e -> {
            for(JButton b : availablePlaces){
                b.setVisible(true);
            }
        });

    }

    public void hideAvailableSpaces(){
        for(JButton b: availablePlaces){
            b.setVisible(false);
        }
    }

    public void addAvailablePlace(Rectangle bounds){
        JButton UpLSpace = new JButton("UL space");
        UpLSpace.setVisible(false);
        UpLSpace.setBackground(Color.GRAY);
        UpLSpace.setBounds(bounds.x-offsetX, bounds.y-offsetY, CardWidth, CardHeight);

        JButton UpRSpace = new JButton("UR space");
        UpRSpace.setVisible(false);
        UpRSpace.setBackground(Color.GRAY);
        UpRSpace.setBounds(bounds.x+offsetX, bounds.y-offsetY, CardWidth, CardHeight);

        JButton DownLSpace = new JButton("DL space");
        DownLSpace.setVisible(false);
        DownLSpace.setBackground(Color.GRAY);
        DownLSpace.setBounds(bounds.x-offsetX, bounds.y+offsetY, CardWidth, CardHeight);

        JButton DownRSpace = new JButton("DR space");
        DownRSpace.setVisible(false);
        DownRSpace.setBackground(Color.GRAY);
        DownRSpace.setBounds(bounds.x+offsetX, bounds.y+offsetY, CardWidth, CardHeight);

        availablePlaces.add(UpLSpace);
        availablePlaces.add(UpRSpace);
        availablePlaces.add(DownLSpace);
        availablePlaces.add(DownRSpace);
        jLayeredPane.add(UpLSpace, Integer.valueOf(1));
        jLayeredPane.add(UpRSpace, Integer.valueOf(1));
        jLayeredPane.add(DownLSpace, Integer.valueOf(1));
        jLayeredPane.add(DownRSpace, Integer.valueOf(1));

        UpLSpace.addActionListener(e -> {
            ImagePanel img2 = new ImagePanel(11);
            img2.setBounds(UpLSpace.getBounds());
            img2.setVisible(true);
            addAvailablePlace(img2.getBounds());
            jLayeredPane.add(img2, Integer.valueOf(1));
            jLayeredPane.remove(UpLSpace);
            hideAvailableSpaces();
        });
        UpRSpace.addActionListener(e -> {
            ImagePanel img3 = new ImagePanel(13);
            img3.setBounds(UpRSpace.getBounds());
            img3.setVisible(true);
            addAvailablePlace(img3.getBounds());
            jLayeredPane.add(img3, Integer.valueOf(1));
            jLayeredPane.remove(UpRSpace);
            hideAvailableSpaces();
        });
        DownLSpace.addActionListener(e -> {
            ImagePanel img4 = new ImagePanel(12);
            img4.setBounds(DownLSpace.getBounds());
            img4.setVisible(true);
            addAvailablePlace(img4.getBounds());
            jLayeredPane.add(img4, Integer.valueOf(1));
            jLayeredPane.remove(DownLSpace);
            hideAvailableSpaces();
        });
        DownRSpace.addActionListener(e -> {
            ImagePanel img5 = new ImagePanel(12);
            img5.setBounds(DownRSpace.getBounds());
            img5.setVisible(true);
            addAvailablePlace(img5.getBounds());
            jLayeredPane.add(img5, Integer.valueOf(1));
            jLayeredPane.remove(DownRSpace);
            hideAvailableSpaces();
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MapTest::new);
    }
}
