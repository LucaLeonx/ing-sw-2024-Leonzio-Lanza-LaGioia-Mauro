package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.dataobject.ControlledPlayerInfo;
import it.polimi.ingsw.dataobject.ObjectiveInfo;
import it.polimi.ingsw.model.player.PlayerColor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import java.awt.*;


public class EndGamePanel extends StandardPanel {
    public EndGamePanel() {
    }

    public void buildPanel() {
        this.setLayout(new GridBagLayout());
        List<String> leaderboard = new ArrayList<>();

        String winnerName = "";
        try {
            String winner = MainWindow.getClientController().getWinner();
            List<ControlledPlayerInfo> players = MainWindow.getClientController().getLeaderboard();
            //Testing
            /*String winner = "Asdrubale";
            ControlledPlayerInfo Asdrubale = new ControlledPlayerInfo("Asdrubale", PlayerColor.RED, new ObjectiveInfo(5), 28, null, null);
            ControlledPlayerInfo Fabio = new ControlledPlayerInfo("Fabio", PlayerColor.YELLOW, new ObjectiveInfo(10), 23, null, null);

            List<ControlledPlayerInfo> players = new ArrayList<>();
            players.add(Asdrubale);
            players.add(Fabio);
            */

            for (ControlledPlayerInfo player : players) {
                leaderboard.add(player.nickname() + " has reached " + player.score() + " points");
            }
            winnerName = winner;
        } catch (Exception e) {
            System.out.println(e);
        }

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


        for (int i = 0; i < leaderboard.size(); i++) {
            JLabel player = new JLabel();
            player.setText(leaderboard.get(i));
            player.setForeground(Color.darkGray);
            gbc.gridy=2+i;
            add(player, gbc);
        }


    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Leaderboard");
        EndGamePanel panel = new EndGamePanel();
        panel.buildPanel();
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }

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