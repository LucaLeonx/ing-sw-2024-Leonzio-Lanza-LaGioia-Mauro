package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.dataobject.LobbyInfo;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PlayerSetup;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;
import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class InternalServerLayer {
    private final InternalServerLayer[] nextLayers;

    public InternalServerLayer(InternalServerLayer... nextLayers) {
        this.nextLayers = nextLayers;
    }

    public Lobby createLobby(User creator, String lobbyName, int playersNumber){
        return nextLayers[0].createLobby(creator, lobbyName, playersNumber);
    }

    public Lobby addUserToLobby(User user, int lobbyId){
        return nextLayers[0].addUserToLobby(user, lobbyId);
    }

    public void removeUserFromLobby(User user, Lobby lobby){
        nextLayers[0].removeUserFromLobby(user, lobby);
    }

    public Lobby getJoinedLobby(User user){
        return nextLayers[0].getJoinedLobby(user);
    }

    public String getCurrentPlayer(Game game){
        return nextLayers[0].getCurrentPlayer(game);
    }

    public Player getPlayer(Game game, String playerName){
        return nextLayers[0].getPlayer(game, playerName);
    }

    public PlayerSetup getPlayerSetup(Game game, String playerName){
        return nextLayers[0].getPlayerSetup(game, playerName);
    }

    public Map<DrawChoice, Card> getDrawableCards(Game game){
        return nextLayers[0].getDrawableCards(game);
    }

    public boolean isLastTurn(Game game){
        return nextLayers[0].isLastTurn(game);
    }

    public boolean hasGameEnded(Game game){
        return nextLayers[0].hasGameEnded(game);
    }

    public void registerPlayerSetup(Game game, String playerName, int objectiveCardId, CardOrientation initialCardSide){
        nextLayers[0].registerPlayerSetup(game, playerName, objectiveCardId, initialCardSide);
    }

    public void registerPlayerMove(Game game, String playerName, int placedCardId, Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice){
        nextLayers[0].registerPlayerMove(game, playerName, placedCardId, placementPoint, chosenSide, drawChoice);
    }

    public List<Player> getLeaderboard(Game game){
        return nextLayers[0].getLeaderboard(game);
    }

    public List<Lobby> getLobbies() {
        return nextLayers[0].getLobbies();
    }

    public void logout(User user){
        nextLayers[0].logout(user);
    }

    public List<String> getPlayerNames(Game joinedGame) {
        return nextLayers[0].getPlayerNames(joinedGame);
    }

    public void exitGame(User user) {
        nextLayers[0].exitGame(user);
    }

    // TODO: choose observer methods

}
