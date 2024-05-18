package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ImagePanel extends JPanel {
    private final FrontPanel frontPanel;
    private final BackPanel backPanel;
    private boolean side= true; //true is front side set by default

    public ImagePanel(String imageNumber) {
        this.frontPanel = new FrontPanel(Toolkit.getDefaultToolkit().getImage("/Users/giovanni/IdeaProjects/ing-sw-2024-Leonzio-Lanza-LaGioia-Mauro/CodexNaturalis/src/main/java/it/polimi/ingsw/view/gui/front_images/" + imageNumber + ".jpeg"));
        this.backPanel = new BackPanel(Toolkit.getDefaultToolkit().getImage("/Users/giovanni/IdeaProjects/ing-sw-2024-Leonzio-Lanza-LaGioia-Mauro/CodexNaturalis/src/main/java/it/polimi/ingsw/view/gui/back_images/" + imageNumber + ".jpeg"));

        this.add(frontPanel);
        this.add(backPanel);

        this.setPreferredSize(new Dimension(500, 500));

        if(side) {
            frontPanel.setVisible(side); //by default when we create the ImagePanel with both Images one visibilty is set true the other false
            backPanel.setVisible(!side);
        }
    }

    public ImagePanel changeSide()
    {
        this.side = !(this.side);

        if(side) {
            frontPanel.setVisible(true); //by default when we create the ImagePanel with both Images one visibilty is set true the other false
            backPanel.setVisible(false);
        }
        else{
            frontPanel.setVisible(false);
            backPanel.setVisible(true);
        }
        return this;
    }

    private class FrontPanel extends JPanel{
        private final Image frontPanelImage;

        private FrontPanel(Image frontPanelNumber){
            this.frontPanelImage= frontPanelNumber;
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(frontPanelImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private class BackPanel extends JPanel{
        private final Image backPanelImage;

        private BackPanel(Image frontPanelNumber){
            this.backPanelImage= frontPanelNumber;
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backPanelImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

}
