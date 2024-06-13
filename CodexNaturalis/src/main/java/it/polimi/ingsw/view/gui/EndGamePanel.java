package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.dataobject.ControlledPlayerInfo;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class EndGamePanel extends StandardPanel {
    public EndGamePanel(){

    }

    public void buildPanel(){
        List<String> leaderboard = new ArrayList<>();

        try {
            String winner= MainWindow.getClientController().getWinner();
            List<ControlledPlayerInfo> players= MainWindow.getClientController().getLeaderboard();

            for(ControlledPlayerInfo player : players)
            {
                leaderboard.add(player.nickname());
            }
        }
        catch (Exception e){
            System.out.println(e);
        }

        JLabel winnerMessage = new JLabel("The winner of the match is: ");
        JLabel winner= new JLabel();

        JLabel leaderboardMessage = new JLabel("This is the leaderboard of the match: ");
        StringBuilder leaderboardText = new StringBuilder();
        for (int i = 0; i < leaderboard.size(); i++) {
            leaderboardText.append(i + 1).append(". ").append(leaderboard.get(i)).append("\n");
        }

        JLabel leaderboardLabel = new JLabel(leaderboardText.toString());

    }
}
