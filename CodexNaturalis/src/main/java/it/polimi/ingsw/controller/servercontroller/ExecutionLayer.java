package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.InvalidOperationException;
import it.polimi.ingsw.model.PlayerSetup;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.card.Deck;
import it.polimi.ingsw.model.card.ObjectiveCard;
import it.polimi.ingsw.model.map.GameField;
import it.polimi.ingsw.model.map.Point;
import it.polimi.ingsw.model.player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.model.DrawChoice.DECK_GOLD;
import static it.polimi.ingsw.model.DrawChoice.DECK_RESOURCE;
import static it.polimi.ingsw.model.card.CardOrientation.BACK;
import static it.polimi.ingsw.model.card.CardOrientation.FRONT;

public class ExecutionLayer extends InternalServerLayer{
    LobbyList lobbyList;
    UserList userList;
    List<Game> games;

    private final InternalServerLayer[] nextLayers;


    public ExecutionLayer(LobbyList lobbyList, UserList userList, List<Game> games, InternalServerLayer... nextLayers) {
        super(nextLayers);
        this.lobbyList = lobbyList;
        this.userList = userList;
        this.games = games;
        this.nextLayers = nextLayers;
    }

    @Override
    public Lobby createLobby(User creator, String lobbyName, int playersNumber) {
        Lobby newLobby = lobbyList.createLobby(creator, lobbyName, playersNumber);
        nextLayers[0].createLobby(creator, lobbyName, playersNumber);
        return newLobby;
    }

    @Override
    public Lobby addUserToLobby(User user, int lobbyId) {
        Lobby lobby = lobbyList.getLobbyById(lobbyId);
        lobby.addUser(user);
        return lobby;
    }

    @Override
    public void removeUserFromLobby(User user, Lobby lobby) {
        lobby.removeUser(user.getUsername());
    }

    @Override
    public Lobby getJoinedLobby(User user) {
        return user.getJoinedLobby();
    }

    @Override
    public String getCurrentPlayer(Game game) {
        return game.getCurrentPlayerNickname();
    }

    @Override
    public Player getPlayer(Game game, String playerName) {
        return game.getPlayer(playerName);
    }

    @Override
    public PlayerSetup getPlayerSetup(Game game, String playerName) {
        return game.getPlayerSetup(playerName);
    }

    @Override
    public Map<DrawChoice, Card> getDrawableCards(Game game) {
        Map<DrawChoice, Card> drawableCards = game.getVisibleCards();

        if(!game.getResourceCardDeck().isEmpty()) {
            Card resourceTopCard = game.getResourceCardDeck().getTopCard();
            drawableCards.put(DECK_RESOURCE, resourceTopCard);
        }

        if(!game.getGoldenCardDeck().isEmpty()) {
            Card resourceTopCard = game.getGoldenCardDeck().getTopCard();
            drawableCards.put(DECK_GOLD, resourceTopCard);
        }

        return drawableCards;
    }

    @Override
    public boolean isLastTurn(Game game) {
        return game.isLastTurn();
    }

    @Override
    public boolean hasGameEnded(Game game) {
        return game.isEnded();
    }

    @Override
    public void registerPlayerSetup(Game game, String playerName, int objectiveCardId, CardOrientation initialCardSide) {
        Player player = game.getPlayer(playerName);
        PlayerSetup setup = game.getPlayerSetup(playerName);
        ObjectiveCard chosenObjective = (objectiveCardId == setup.objective1().getId()) ? setup.objective1() : setup.objective2();
        player.setSecretObjective(chosenObjective);
        player.getField().placeCard(setup.initialCard(), initialCardSide, new Point(0,0));
    }

    @Override
    public void registerPlayerMove(Game game, String playerName, int placedCardId, Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice) {
            Player player = game.getPlayer(playerName);
            game.makePlayerPlaceCard(player, placedCardId, placementPoint, chosenSide);
            game.makePlayerDraw(player, drawChoice);
            game.changeCurrentPlayer();
    }

    @Override
    public List<Player> getLeaderboard(Game game) {
        return game.getLeaderBoard();
    }

    @Override
    public List<Lobby> getLobbies() {
        return lobbyList.getLobbies();
    }
}
