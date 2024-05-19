package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private final FrontPanel frontPanel;
    private final BackPanel backPanel;
    private boolean side= true; //true is front side set by default

    public ImagePanel(String imageNumber) {
        this.frontPanel = new FrontPanel(Toolkit.getDefaultToolkit().getImage("/Users/giovanni/IdeaProjects/ing-sw-2024-Leonzio-Lanza-LaGioia-Mauro/CodexNaturalis/src/main/java/it/polimi/ingsw/view/gui/front_images/" + imageNumber + ".jpeg"));
        this.backPanel = new BackPanel(Toolkit.getDefaultToolkit().getImage("/Users/giovanni/IdeaProjects/ing-sw-2024-Leonzio-Lanza-LaGioia-Mauro/CodexNaturalis/src/main/java/it/polimi/ingsw/view/gui/back_images/" + imageNumber + ".jpeg"));

        this.add(frontPanel);
        this.add(backPanel);

        if(side) {
            frontPanel.setVisible(side); //by default when we create the ImagePanel with both Images one visibilty is set true the other false
            backPanel.setVisible(!side);
        }
    }

    public void changeSide()
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
    }

    private class FrontPanel extends JPanel{
        private final Image frontPanelImage;

        private FrontPanel(Image frontPanelNumber){
            this.frontPanelImage= frontPanelNumber;
            this.setPreferredSize(new Dimension(100,80));
        }
        @Override
        protected void paintComponent(Graphics g) {
            g.drawImage(frontPanelImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private class BackPanel extends JPanel{
        private final Image backPanelImage;

        private BackPanel(Image frontPanelNumber){
            this.backPanelImage= frontPanelNumber;
            this.setPreferredSize(new Dimension(100,80));
        }
        @Override
        protected void paintComponent(Graphics g) {
            g.drawImage(backPanelImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

}
