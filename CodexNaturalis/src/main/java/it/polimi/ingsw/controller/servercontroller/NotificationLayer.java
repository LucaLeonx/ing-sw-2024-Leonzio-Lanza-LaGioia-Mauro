package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PlayerSetup;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;
import it.polimi.ingsw.model.player.Player;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NotificationLayer extends InternalServerLayer {
    private final List<NotificationSubscriber> subscribers;

    public NotificationLayer(InternalServerLayer... nextLayers) {
        super(nextLayers);
        this.subscribers = Collections.synchronizedList(new LinkedList<>());
    }

    @Override
    public Lobby createLobby(User creator, String lobbyName, int playersNumber) {
        return super.createLobby(creator, lobbyName, playersNumber);
    }

    @Override
    public Lobby addUserToLobby(User user, int lobbyId) {
        return super.addUserToLobby(user, lobbyId);
    }

    @Override
    public void removeUserFromLobby(User user, Lobby lobby) {
        super.removeUserFromLobby(user, lobby);
    }

    @Override
    public Lobby getJoinedLobby(User user) {
        return super.getJoinedLobby(user);
    }

    @Override
    public String getCurrentPlayer(Game game) {
        return super.getCurrentPlayer(game);
    }

    @Override
    public Player getPlayer(Game game, String playerName) {
        return super.getPlayer(game, playerName);
    }

    @Override
    public PlayerSetup getPlayerSetup(Game game, String playerName) {
        return super.getPlayerSetup(game, playerName);
    }

    @Override
    public Map<DrawChoice, Card> getDrawableCards(Game game) {
        return super.getDrawableCards(game);
    }

    @Override
    public boolean isLastTurn(Game game) {
        return super.isLastTurn(game);
    }

    @Override
    public boolean hasGameEnded(Game game) {
        return super.hasGameEnded(game);
    }

    @Override
    public void registerPlayerSetup(Game game, String playerName, int objectiveCardId, CardOrientation initialCardSide) {
        super.registerPlayerSetup(game, playerName, objectiveCardId, initialCardSide);
    }

    @Override
    public void registerPlayerMove(Game game, String playerName, int placedCardId, Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice) {
        super.registerPlayerMove(game, playerName, placedCardId, placementPoint, chosenSide, drawChoice);
    }

    @Override
    public List<Player> getLeaderboard(Game game) {
        return super.getLeaderboard(game);
    }

    @Override
    public List<Lobby> getLobbies() {
        return super.getLobbies();
    }

    @Override
    public void logout(User user) {
        super.logout(user);
    }

    @Override
    public List<String> getPlayerNames(Game joinedGame) {
        return super.getPlayerNames(joinedGame);
    }

    @Override
    public void exitGame(User user) {
        super.exitGame(user);
    }

}
