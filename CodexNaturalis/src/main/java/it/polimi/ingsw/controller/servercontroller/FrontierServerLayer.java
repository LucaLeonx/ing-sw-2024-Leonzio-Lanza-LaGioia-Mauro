package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import java.util.List;

public abstract class FrontierServerLayer {
    private final FrontierServerLayer[] nextLayers;
    private final LobbyList lobbyList;

    public FrontierServerLayer(LobbyList lobbyList, FrontierServerLayer... nextLayers) {
        this.lobbyList = lobbyList;
        this.nextLayers = nextLayers;
    }

    public LobbyInfo createLobby(User user, String lobbyName, int requiredPlayersNum){
        return nextLayers[0].createLobby(user, lobbyName, requiredPlayersNum);
    }

    public void joinLobby(User user, int lobbyId){
        nextLayers[0].joinLobby(user, lobbyId);
    }

    public void exitFromLobby(User user){
        nextLayers[0].exitFromLobby(user);
    }

    public List<LobbyInfo> getLobbies(User user){
        return nextLayers[0].getLobbies(user);
    }

    public LobbyInfo getJoinedLobbyInfo(User user){
        return nextLayers[0].getJoinedLobbyInfo(user);
    }
    public String getCurrentPlayer(User user){
        return nextLayers[0].getCurrentPlayer(user);
    }

    public ControlledPlayerInfo getControlledPlayerInfo(User user){
        return nextLayers[0].getControlledPlayerInfo(user);
    }

    public OpponentInfo getOpponentInfo(User user, String opponentName){
        return nextLayers[0].getOpponentInfo(user, opponentName);
    }

    public DrawableCardsInfo getDrawableCardsInfo(User user){
        return nextLayers[0].getDrawableCardsInfo(user);
    }

    public PlayerSetupInfo getPlayerSetupInfo(User user){
        return nextLayers[0].getPlayerSetupInfo(user);
    }

    public boolean isLastTurn(User user){
        return nextLayers[0].isLastTurn(user);
    }

    public boolean hasGameEnded(User user){
        return nextLayers[0].hasGameEnded(user);
    }

    public void registerPlayerSetup(User user, int objectiveCardId, CardOrientation initialCardSide){
        nextLayers[0].registerPlayerSetup(user, objectiveCardId, initialCardSide);
    }

    public void registerPlayerMove(User user, int placedCardId, Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice){
        nextLayers[0].registerPlayerMove(user, placedCardId, placementPoint, chosenSide, drawChoice);
    }
    public String getWinnerName(User user){
        return nextLayers[0].getWinnerName(user);
    }

    public void logout(User user){
        nextLayers[0].logout(user);
    }

    public List<String> getPlayerNames(User user) {
        return nextLayers[0].getPlayerNames(user);
    }

    public List<String> getPlayersNames(User user){
        return nextLayers[0].getPlayersNames(user);
    }

    public List<ControlledPlayerInfo> getLeaderboard(User user) {
        return nextLayers[0].getLeaderboard(user);
    }
}
