package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.card.CardOrientation;

import javax.swing.*;
import java.awt.*;

/**
 * This Panel is the container of all the images of the cards of the game
 */
public class ImagePanel extends JPanel {
    private final JPanel cardPanel;
    private final CardLayout cardLayout;
    private CardOrientation currentOrientation = CardOrientation.FRONT;
    private int id;

    public ImagePanel(Integer imageNumber) {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        id = imageNumber;

        FrontPanel frontPanel = new FrontPanel(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/cropped_fronte/img_" + imageNumber + ".jpeg")));
        BackPanel backPanel = new BackPanel(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/cropped_retro/img_" + imageNumber + ".jpeg")));

        cardPanel.add(frontPanel, "FRONT");
        cardPanel.add(backPanel, "BACK");

        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);

        showFrontPanel(); // Show front panel by default
    }

    public void changeSide() {
        cardLayout.next(cardPanel);
        currentOrientation = (currentOrientation == CardOrientation.FRONT) ? CardOrientation.BACK : CardOrientation.FRONT ;
    }

    private void showFrontPanel() {
        cardLayout.show(cardPanel, "FRONT");
    }

    private void showBackPanel() {
        cardLayout.show(cardPanel, "BACK");
    }

    private class FrontPanel extends JPanel {
        private final Image frontPanelImage;

        private FrontPanel(Image frontPanelImage) {
            this.frontPanelImage = frontPanelImage;
            this.setPreferredSize(new Dimension(DefaultCardSizeInfo.CardWidth, DefaultCardSizeInfo.CardHeight));
            this.setBackground(new Color(0,0,0,0));;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(frontPanelImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private class BackPanel extends JPanel {
        private final Image backPanelImage;

        private BackPanel(Image backPanelImage) {
            this.backPanelImage = backPanelImage;
            this.setPreferredSize(new Dimension(DefaultCardSizeInfo.CardWidth, DefaultCardSizeInfo.CardHeight));
            this.setBackground(new Color(0,0,0,0));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backPanelImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public int getId(){ return id; }

    public CardOrientation getVisibleOrientation(){ return this.currentOrientation; }
}
