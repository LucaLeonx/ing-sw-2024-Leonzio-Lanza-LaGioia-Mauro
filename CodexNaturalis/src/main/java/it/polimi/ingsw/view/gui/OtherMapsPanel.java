package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.dataobject.CardCellInfo;
import it.polimi.ingsw.dataobject.GameFieldInfo;
import it.polimi.ingsw.dataobject.OpponentInfo;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OtherMapsPanel extends StandardPanel{

    private final Color bordeaux = new Color(133, 0, 0); // RGB values
    private GameFieldInfo field;
    private JLayeredPane jLayeredPane;
    //private JScrollPane jScrollPane = new JScrollPane(jLayeredPane);
    private ExecutorService executor;

    private final int CardWidth = DefaultCardSizeInfo.CardWidth;
    private final int CardHeight = DefaultCardSizeInfo.CardHeight;
    private final int CenterCardX = 700;
    private final int CenterCardY = 510;
    private final int offsetX = DefaultCardSizeInfo.offsetX;
    private final int offsetY = DefaultCardSizeInfo.offsetY;
    private int layer;

    private AbstractMap.SimpleEntry<Integer, Integer> coordinates;
    private Map<Point,AbstractMap.SimpleEntry<Integer, Integer>> availablePlaces = new HashMap<Point,AbstractMap.SimpleEntry<Integer, Integer>>();
    private JScrollPane jScrollPane;
    public OtherMapsPanel() {

    }

    public void buildPanel(String oppName) throws RemoteException {
        this.removeAll();
        repaint();
        revalidate();
        this.setLayout(new BorderLayout());
        jLayeredPane = new JLayeredPane();
        jLayeredPane.setPreferredSize(new Dimension(1500, 800));
        jLayeredPane.setLayout(null);
        layer=0;
        executor = Executors.newSingleThreadExecutor();
        OpponentInfo player = MainWindow.getClientController().getOpponentInformation(oppName);

        //jLayeredPane.add(opponentName);

        field = player.field();

        insertInitialCard(field.placedCards().get(new Point(0, 0)).card().id(), field.placedCards().get(new Point(0, 0)).orientation());
        drawMap();

        jScrollPane = new JScrollPane(jLayeredPane);
        //jLayeredPane.setVisible(true);
        //jScrollPane.setVisible(true);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(Explanation(player.nickname()), BorderLayout.PAGE_START);
        this.setVisible(true);

        //sometimes it gets stucked, simpy revalidate the screen when everything is correctly built.
        new Thread(() -> {
            try {
                // Wait for 5 seconds (5000 milliseconds)
                Thread.sleep(500);
            }
            catch(InterruptedException IE){
                System.out.println();
            }

            // Update the label text on the Event Dispatch Thread
            SwingUtilities.invokeLater(() -> {
                // Repaint and revalidate the frame
                repaint();
                revalidate();
            });
        }).start();

        executor.submit(()-> {
            while(true) {
                MainWindow.getClientController().waitForTurnChange();
                OtherMapsFrame.goToWindow("otherMapsPanel");
                buildPanel(oppName);
            }

        });

    }


    public void insertInitialCard(int id, CardOrientation orientation) {
        ImagePanel img1 = new ImagePanel(id);
        if (orientation.equals(CardOrientation.BACK))
            img1.changeSide();
        img1.setBounds(CenterCardX, CenterCardY, CardWidth, CardHeight);
        addAvailablePlace(new Point(0,0), CenterCardX, CenterCardY);
        jLayeredPane.add(img1, Integer.valueOf(layer));
    }

    private void addCardImage(Point p, CardCellInfo c, int x, int y) {
        ImagePanel img = new ImagePanel(c.card().id());
        layer++;
        if (c.orientation().equals(CardOrientation.BACK)) {
            img.changeSide();
        }
        img.setBounds(x,y,CardWidth,CardHeight);
        img.setVisible(true);
        jLayeredPane.add(img, Integer.valueOf(layer));
        addAvailablePlace(p,x,y);
    }

    private void drawMap() {
        for (Point p : field.insertionOrder()) {
            CardCellInfo currentCard = field.placedCards().get(p);

            if (p.equals(new Point(0, 0))) {
                continue;
            }
            //placedCards are not sorted
            addCardImage(p,currentCard,availablePlaces.get(p).getKey(),availablePlaces.get(p).getValue());
        }

    }

    private void addAvailablePlace(Point p, int x, int y) {
        Point ULPoint = new Point(p.x() - 2, p.y() + 2);
        Point URPoint = new Point(p.x() + 2, p.y() + 2);
        Point DLPoint = new Point(p.x() - 2, p.y() - 2);
        Point DRPoint = new Point(p.x() + 2, p.y() - 2);

        layer++;
        addSpace(ULPoint, x - offsetX, y - offsetY);
        addSpace(URPoint, x + offsetX, y - offsetY);
        addSpace(DLPoint, x - offsetX, y + offsetY);
        addSpace(DRPoint, x + offsetX, y + offsetY);
    }

    private void addSpace(Point p, int boundsX, int boundsY) {
        coordinates = new AbstractMap.SimpleEntry<>(boundsX, boundsY);
        availablePlaces.put(p, coordinates);
    }

    private JPanel Explanation(String player) {
        JPanel explanation = new JPanel();
        explanation.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel opponentName = new JLabel("This is the map of: " + player);
        opponentName.setFont(new Font("Arial", Font.BOLD, 20));
        opponentName.setForeground(bordeaux);
        gbc.gridx=0;
        gbc.gridy=0;
        explanation.add(opponentName);
        return explanation;
    }
}
