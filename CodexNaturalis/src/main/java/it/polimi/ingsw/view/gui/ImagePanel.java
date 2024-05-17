package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ImagePanel extends JPanel {
    private Image imageFront;
    private Image imageBack;
    private boolean isMouseOver;

    public ImagePanel(String imagePathFront, String imagePathBack) {
        imageFront = Toolkit.getDefaultToolkit().getImage(imagePathFront);
        imageBack = Toolkit.getDefaultToolkit().getImage(imagePathBack);

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                // Verifica se il mouse è sopra l'area dell'immagine
                isMouseOver = e.getX() >= 0 && e.getY() >= 0 && e.getX() < getWidth() && e.getY() < getHeight();
                // Ridisegna il pannello per aggiornare l'immagine visualizzata
                repaint();
            }
        });

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imageFront != null && isMouseOver) {
            g.drawImage(imageFront, 0, 0, getWidth(), getHeight(), this);
        }
        else{
            g.drawImage(imageBack, 0, 0, getWidth(), getHeight(), this);
        }
    }

}
