package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameFieldPanel extends StandardPanel {
    public GameFieldPanel() { buildPanel(); }

    private void buildPanel(){
        this.setLayout(new BorderLayout());

        JPanel hostPlayer = newHostPanel();
        JPanel player2 = newPlayer2();
        JPanel player3 = newPlayer3();
        JPanel player4 = newPlayer4();
        JPanel game = newGame();

        this.add(hostPlayer, BorderLayout.PAGE_END);
        this.add(player2, BorderLayout.LINE_END);
        this.add(player3, BorderLayout.PAGE_START);
        this.add(player4, BorderLayout.LINE_START);
        this.add(game, BorderLayout.CENTER);

    }

    private JPanel newHostPanel(){
        JPanel host= new JPanel();
        host.setLayout(new GridBagLayout());

        ImagePanel firstcard = new ImagePanel("/Users/giovanni/IdeaProjects/ing-sw-2024-Leonzio-Lanza-LaGioia-Mauro/CodexNaturalis/src/main/java/it/polimi/ingsw/view/gui/front_images/img_21.jpeg",
                "/Users/giovanni/IdeaProjects/ing-sw-2024-Leonzio-Lanza-LaGioia-Mauro/CodexNaturalis/src/main/java/it/polimi/ingsw/view/gui/back_images/img_21.jpeg");
        ImagePanel secondcard = new ImagePanel("/Users/giovanni/IdeaProjects/ing-sw-2024-Leonzio-Lanza-LaGioia-Mauro/CodexNaturalis/src/main/java/it/polimi/ingsw/view/gui/front_images/img_31.jpeg",
                "/Users/giovanni/IdeaProjects/ing-sw-2024-Leonzio-Lanza-LaGioia-Mauro/CodexNaturalis/src/main/java/it/polimi/ingsw/view/gui/back_images/img_31.jpeg");
        ImagePanel thirdcard = new ImagePanel("/Users/giovanni/IdeaProjects/ing-sw-2024-Leonzio-Lanza-LaGioia-Mauro/CodexNaturalis/src/main/java/it/polimi/ingsw/view/gui/front_images/img_51.jpeg",
                "/Users/giovanni/IdeaProjects/ing-sw-2024-Leonzio-Lanza-LaGioia-Mauro/CodexNaturalis/src/main/java/it/polimi/ingsw/view/gui/back_images/img_51.jpeg");
        ImagePanel fourthcard = new ImagePanel("/Users/giovanni/IdeaProjects/ing-sw-2024-Leonzio-Lanza-LaGioia-Mauro/CodexNaturalis/src/main/java/it/polimi/ingsw/view/gui/front_images/img_61.jpeg",
                "/Users/giovanni/IdeaProjects/ing-sw-2024-Leonzio-Lanza-LaGioia-Mauro/CodexNaturalis/src/main/java/it/polimi/ingsw/view/gui/back_images/img_61.jpeg");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=1;
        host.add(firstcard, gbc);

        gbc.gridx=1;
        host.add(secondcard, gbc);

        gbc.gridx=2;
        host.add(thirdcard, gbc);

        gbc.gridx=3;
        host.add(fourthcard, gbc);

        return host;
    }

    private  JPanel newPlayer2(){
        JPanel player2 = new JPanel();

        return player2;
    }

    private  JPanel newPlayer3(){
        JPanel player3 = new JPanel();

        return player3;
    }

    private  JPanel newPlayer4(){
        JPanel player4 = new JPanel();

        return player4;
    }

    private  JPanel newGame(){
        JPanel game = new JPanel();

        return game;
    }

}
