package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PlayerSetup;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.card.ObjectiveCard;
import it.polimi.ingsw.model.map.Point;
import it.polimi.ingsw.model.player.Player;

import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.model.DrawChoice.DECK_GOLD;
import static it.polimi.ingsw.model.DrawChoice.DECK_RESOURCE;
/*
public class ExecutionLayer extends InternalServerLayer{
    private final LobbyList lobbyList;
    private final UserList userList;
    private final GameList gameList;

    public ExecutionLayer(LobbyList lobbyList, UserList userList, GameList gameList, InternalServerLayer... nextLayers) {
        super(nextLayers);
        this.lobbyList = lobbyList;
        this.userList = userList;
        this.gameList = gameList;
    }

    @Override
    public Lobby createLobby(User creator, String lobbyName, int playersNumber) {



        Lobby newLobby = lobbyList.createLobby(creator, lobbyName, playersNumber);
        creator.setJoinedLobby(newLobby);
        super.createLobby(creator, lobbyName, playersNumber);
        return newLobby;
    }

    @Override
    public Lobby addUserToLobby(User user, int lobbyId) {

        Lobby lobby;

        synchronized (lobbyList){
            lobby = lobbyList.getLobbyById(lobbyId);
            lobby.addUser(user);
            if(lobby.readyToStart()){
                lobbyList.removeLobby(lobbyId);
            }
        }

        user.setStatus(UserStatus.WAITING_TO_START);

        if(lobby.readyToStart()){

            Game startedGame = new Game(lobby.getConnectedUserNames());
            gameList.addGame(startedGame, lobby.getConnectedUsers());

            for(User lobbyUser: lobby.getConnectedUsers()){
                lobbyUser.setStatus(UserStatus.IN_GAME);
                lobbyUser.setJoinedGame(startedGame);
            }
        }

        super.addUserToLobby(user, lobbyId);
        return lobby;
    }

    @Override
    public void removeUserFromLobby(User user, Lobby lobby) {
        lobby.removeUser(user.getUsername());
        super.removeUserFromLobby(user, lobby);
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
    public List<ObjectiveCard> getCommonObjectives(Game game) {
        return game.getCommonObjectiveCards();
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
        game.registerPlayerSetup(playerName, objectiveCardId, initialCardSide);
        super.registerPlayerSetup(game, playerName, objectiveCardId, initialCardSide);
    }

    @Override
    public void registerPlayerMove(Game game, String playerName, int placedCardId, Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice) {
            Player player = game.getPlayer(playerName);
            game.makePlayerPlaceCard(player, placedCardId, placementPoint, chosenSide);
            game.makePlayerDraw(player, drawChoice);
            game.changeCurrentPlayer();
            super.registerPlayerMove(game, playerName, placedCardId, placementPoint, chosenSide, drawChoice);
    }

    @Override
    public List<Player> getLeaderboard(Game game) {
        return game.getLeaderBoard();
    }

    @Override
    public List<Lobby> getLobbies() {
        return lobbyList.getLobbies();
    }

    @Override
    public void logout(User user){

    }

    @Override
    public void exitGame(User user){
        user.setJoinedGame(null);
        super.exitGame(user);
    }
}*/
