package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;

public class GameFieldPanel extends ButtonListPanel{
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
