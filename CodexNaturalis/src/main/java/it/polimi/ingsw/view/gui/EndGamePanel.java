package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.dataobject.CardInfo;
import it.polimi.ingsw.dataobject.ControlledPlayerInfo;
import it.polimi.ingsw.dataobject.ObjectiveInfo;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.map.Point;
import it.polimi.ingsw.model.player.PlayerColor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import java.awt.*;

/**
 * Last Panel of the game where final results of the game are shown
 */
public class EndGamePanel extends StandardPanel {
    public EndGamePanel() {
    }

    public void buildPanel() {
        int i=0;
        this.setLayout(new GridBagLayout());
        List<String> leaderboard = new ArrayList<>();
        JLabel splitter = new JLabel("\n");

        String winnerName = "";
        try {
            String winner = MainWindow.getClientController().getWinner();
            List<ControlledPlayerInfo> players = MainWindow.getClientController().getLeaderboard();
//            Testing
//            String winner = "Asdrubale";
//            ControlledPlayerInfo Asdrubale = new ControlledPlayerInfo("Asdrubale", PlayerColor.RED, new ObjectiveInfo(5), 28, null, null);
//            ControlledPlayerInfo Fabio = new ControlledPlayerInfo("Fabio", PlayerColor.YELLOW, new ObjectiveInfo(10), 23, null, null);
//
//            List<ControlledPlayerInfo> players = new ArrayList<>();
//            players.add(Asdrubale);
//            players.add(Fabio);

            for (ControlledPlayerInfo player : players) {
                leaderboard.add(player.nickname() + " has reached " + player.score() + " points");
            }
            winnerName = winner;
        } catch (Exception e) {
            System.out.println(e);
        }

        JButton goBack = new JButton("Go Back");
        Dimension buttonSize = new Dimension(300, 25); // Imposta le dimensioni desiderate (larghezza x altezza)
        goBack.setPreferredSize(buttonSize);

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MainWindow.getClientController().exitGame();
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
                MainWindow.goToWindow("registerPanel");
            }
        });



        JLabel winnerMessage = new JLabel("The winner of the match is: " + winnerName);

        winnerMessage.setHorizontalAlignment(SwingConstants.CENTER);
        winnerMessage.setFont(new Font("Arial", Font.BOLD, 32));
        winnerMessage.setForeground(Color.white);


        String leaderboardText = "This is the final scoreboard of the match:";

        JLabel leaderboardLabel = new JLabel(leaderboardText);

        leaderboardLabel.setHorizontalAlignment(SwingConstants.CENTER);
        leaderboardLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        leaderboardLabel.setForeground(Color.darkGray);


        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth=1;
        add(winnerMessage, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        add(leaderboardLabel, gbc);


        for (i = 0; i < leaderboard.size(); i++) {
            JLabel player = new JLabel();
            player.setText(leaderboard.get(i));
            player.setForeground(Color.darkGray);
            gbc.gridy=2+i;
            add(player, gbc);
        }

        gbc.gridy=3+i;
        add(splitter,gbc);
        gbc.gridy=4+i;
        add(goBack, gbc);


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

    }


//    public static void main(String[] args) {
//        JFrame frame = new JFrame("Leaderboard");
//        EndGamePanel panel = new EndGamePanel();
//        panel.buildPanel();
//        frame.add(panel);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(400, 400);
//        frame.setVisible(true);
//    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image image = null;
        try {
            image = new ImageIcon("src/main/resources/other_images/EndGame.jpg").getImage();
        }
        catch (Exception e){
            System.out.println("Path non rilevato");
        }
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }

}