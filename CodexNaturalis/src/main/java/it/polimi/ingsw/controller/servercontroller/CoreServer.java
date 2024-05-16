package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import java.util.List;

public class CoreServer {

    public void logout(User user){

    }

    public void addNotificationSubscriber(User user, NotificationSubscriber subscriber){

    }

    public List<LobbyInfo> getLobbies(User user){

    }

    public LobbyInfo getJoinedLobbyInfo(User user){

    }

    public LobbyInfo createLobby(User user, String lobbyName, int requiredPlayersNum){

    }

    public void joinLobby(User user, int lobbyId){


    }

    public void exitFromLobby(User user){

    }

    public List<String> getPlayerNames(User user) {

    }

    public String getCurrentPlayer(User user){

    }

    public ControlledPlayerInfo getControlledPlayerInfo(User user){

    }

    public OpponentInfo getOpponentInfo(User user, String opponentName){

    }

    public DrawableCardsInfo getDrawableCardsInfo(User user){

    }

    public List<ObjectiveInfo> getCommonObjectives(User user) {

    }

    public PlayerSetupInfo getPlayerSetupInfo(User user){

    }

    public boolean isLastTurn(User user){

    }

    public boolean hasGameEnded(User user){

    }

    public void registerPlayerSetup(User user, int objectiveCardId, CardOrientation initialCardSide){

    }

    public void registerPlayerMove(User user, int placedCardId, Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice){

    }
    public String getWinnerName(User user){

    }

    public List<ControlledPlayerInfo> getLeaderboard(User user) {

    }

    public void exitGame(User user) {

    }
}
