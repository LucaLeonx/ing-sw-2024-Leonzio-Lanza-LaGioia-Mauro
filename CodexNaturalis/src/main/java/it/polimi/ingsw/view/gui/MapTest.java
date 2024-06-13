package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MapTest extends JFrame {

    public MapTest(){
        final int CardWidth = 100;
        final int CardHeight = 80;
        final int CenterCardX = 450;
        final int CenterCardY = 410;
        final int offsetX = 77;
        final int offsetY = 42;

        JFrame frame = new JFrame("JLayeredPane Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 900);

        JButton placeMode = new JButton("Place Mode");
        placeMode.setBounds(0,0,150,30);
        placeMode.setVisible(true);

        JLayeredPane jLayeredPane = new JLayeredPane();

        ImagePanel img1 = new ImagePanel(10);
        img1.setBounds(CenterCardX, CenterCardY, CardWidth, CardHeight);
        jLayeredPane.add(img1, Integer.valueOf(0));

        JButton UpLSpace = new JButton("UL space");
        UpLSpace.setVisible(false);
        UpLSpace.setBackground(Color.GRAY);
        UpLSpace.setBounds(CenterCardX-offsetX, CenterCardY-offsetY, CardWidth, CardHeight);

        JButton UpRSpace = new JButton("UR space");
        UpRSpace.setVisible(false);
        UpRSpace.setBackground(Color.GRAY);
        UpRSpace.setBounds(CenterCardX+offsetX, CenterCardY-offsetY, CardWidth, CardHeight);

        JButton DownLSpace = new JButton("DL space");
        DownLSpace.setVisible(false);
        DownLSpace.setBackground(Color.GRAY);
        DownLSpace.setBounds(CenterCardX-offsetX, CenterCardY+offsetY+10, CardWidth, CardHeight);

        JButton DownRSpace = new JButton("DR space");
        DownRSpace.setVisible(false);
        DownRSpace.setBackground(Color.GRAY);
        DownRSpace.setBounds(CenterCardX+offsetX, CenterCardY+offsetY+10, CardWidth, CardHeight);

        jLayeredPane.add(UpLSpace, Integer.valueOf(1));
        jLayeredPane.add(UpRSpace, Integer.valueOf(1));
        jLayeredPane.add(DownLSpace, Integer.valueOf(1));
        jLayeredPane.add(DownRSpace, Integer.valueOf(1));

        frame.add(placeMode);
        frame.add(jLayeredPane);
        frame.setVisible(true);

        placeMode.addActionListener(e -> {
            UpLSpace.setVisible(true);
            UpRSpace.setVisible(true);
            DownLSpace.setVisible(true);
            DownRSpace.setVisible(true);
            }
        );

        UpLSpace.addActionListener(e -> {
            ImagePanel img2 = new ImagePanel(11);
            img2.setBounds(UpLSpace.getBounds());
            img2.setVisible(true);
            jLayeredPane.add(img2, Integer.valueOf(1));
            UpLSpace.setVisible(false);
        });
        UpRSpace.addActionListener(e -> {
            ImagePanel img3 = new ImagePanel(13);
            img3.setBounds(UpRSpace.getBounds());
            img3.setVisible(true);
            jLayeredPane.add(img3, Integer.valueOf(1));
            UpRSpace.setVisible(false);
        });
        DownLSpace.addActionListener(e -> {
            ImagePanel img4 = new ImagePanel(12);
            img4.setBounds(DownLSpace.getBounds());
            jLayeredPane.add(img4, Integer.valueOf(1));
            img4.setVisible(true);
            DownLSpace.setVisible(false);
        });
        DownRSpace.addActionListener(e -> {
            ImagePanel img5 = new ImagePanel(12);
            img5.setBounds(DownRSpace.getBounds());
            img5.setVisible(true);
            jLayeredPane.add(img5, Integer.valueOf(1));
            DownRSpace.setVisible(false);
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MapTest::new);
    }
}
