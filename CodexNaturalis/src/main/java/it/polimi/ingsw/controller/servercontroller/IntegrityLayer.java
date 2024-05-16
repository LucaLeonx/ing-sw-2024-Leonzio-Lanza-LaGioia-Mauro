package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.InvalidOperationException;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.GameField;
import it.polimi.ingsw.model.map.Point;
import it.polimi.ingsw.model.player.Player;

import java.util.List;

import static it.polimi.ingsw.controller.servercontroller.UserStatus.*;
/*
public class IntegrityLayer extends FrontierServerLayer {
    private final LobbyList lobbyList;

    public IntegrityLayer(LobbyList lobbyList, FrontierServerLayer... nextLayers) {
        super(nextLayers);
        this.lobbyList = lobbyList;
    }

    @Override
    public LobbyInfo createLobby(User user, String lobbyName, int requiredPlayersNum) {
        if(user.getStatus() != LOBBY_CHOICE){
            throw new InvalidOperationException("Cannot create Lobby when not choosing one");
        } else if (requiredPlayersNum < 2 || requiredPlayersNum > 4){
            throw new InvalidOperationException("Invalid number of required players");
        }

        return super.createLobby(user, lobbyName, requiredPlayersNum);
    }

    @Override
    public void joinLobby(User user, int lobbyId) {
        if(user.getStatus() != LOBBY_CHOICE){
            throw new InvalidOperationException("Cannot join lobby when already in one");
        }
        synchronized (lobbyList) {
            if(lobbyList.getLobbyById(lobbyId) == null){
                throw new InvalidOperationException("Lobby is non-existent");
            } else if(lobbyList.getLobbyById(lobbyId).readyToStart()) {
                throw new InvalidOperationException("Cannot join full lobby");
            }
        }

        super.joinLobby(user, lobbyId);
    }

    @Override
    public void exitFromLobby(User user) {
        if(user.getStatus() != WAITING_TO_START){
            throw new InvalidOperationException("Cannot exit a lobby if not in one");
        }
        synchronized (lobbyList) {
            if(user.getJoinedLobby().readyToStart()) {

            }
        }
        super.exitFromLobby(user);
    }

    @Override
    public List<LobbyInfo> getLobbies(User user) {
        return super.getLobbies(user);
    }

    @Override
    public LobbyInfo getJoinedLobbyInfo(User user) {
        if(user.getStatus() != WAITING_TO_START) {
            throw new InvalidOperationException("The user is not in any lobby");
        }

        return super.getJoinedLobbyInfo(user);
    }

    @Override
    public String getCurrentPlayer(User user) {
        if(user.getStatus() != IN_GAME){
            throw new InvalidOperationException("The user is not in any game");
        }

        return super.getCurrentPlayer(user);
    }

    @Override
    public ControlledPlayerInfo getControlledPlayerInfo(User user) {

        if(user.getStatus() != IN_GAME){
            throw new InvalidOperationException("The user is not in any game");
        }

        return super.getControlledPlayerInfo(user);
    }

    @Override
    public OpponentInfo getOpponentInfo(User user, String opponentName) {

        if(user.getStatus() != IN_GAME){
            throw new InvalidOperationException("The user is not in any game");
        } else if(user.getJoinedGame().getPlayers().stream().noneMatch((player) -> player.getNickname().equals(opponentName))){
            throw new InvalidOperationException("Cannot query information about a non-existent player");
        }

        return super.getOpponentInfo(user, opponentName);
    }

    @Override
    public DrawableCardsInfo getDrawableCardsInfo(User user) {

        if(user.getStatus() != IN_GAME){
            throw new InvalidOperationException("The user is not in any game");
        }

        return super.getDrawableCardsInfo(user);
    }

    @Override
    public PlayerSetupInfo getPlayerSetupInfo(User user) {
        if(user.getStatus() != IN_GAME){
            throw new InvalidOperationException("The user is not in any game");
        }

        return super.getPlayerSetupInfo(user);
    }

    @Override
    public boolean isLastTurn(User user) {
        if(user.getStatus() != IN_GAME){
            throw new InvalidOperationException("The user is not in any game");
        }

        return super.isLastTurn(user);
    }

    @Override
    public boolean hasGameEnded(User user) {
        if(user.getStatus() != IN_GAME){
            throw new InvalidOperationException("The user is not in any game");
        }

        return super.hasGameEnded(user);
    }

    @Override
    public void registerPlayerSetup(User user, int objectiveCardId, CardOrientation initialCardSide) {
        if(user.getStatus() != IN_GAME){
            throw new InvalidOperationException("The user is not in any game");
        }

        Game game = user.getJoinedGame();

        if(game.getPlayerSetup(user.getUsername()) != null){
           throw new InvalidOperationException("Player has already setup");
        }

        super.registerPlayerSetup(user, objectiveCardId, initialCardSide);
    }

    @Override
    public void registerPlayerMove(User user, int placedCardId, Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice) {
        if(user.getStatus() != IN_GAME){
            throw new InvalidOperationException("The user is not in any game");
        }

        Game joinedGame = user.getJoinedGame();

        if(!user.getUsername().equals(joinedGame.getCurrentPlayerNickname())){
            throw new InvalidOperationException("It is not the turn of the player");
        }

        if(joinedGame.isEnded()){
            throw new InvalidOperationException("The game has ended, cannot move");
        }

        Player userPlayer = joinedGame.getPlayer(user.getUsername());
        GameField field = userPlayer.getField();

        if(!field.getAvailablePositions().contains(placementPoint)){
            throw new InvalidOperationException("The provided position is not among available ones");
        }

        Card placedCard = userPlayer.getCardsInHand().stream().filter((card) -> card.getId() == placedCardId).findFirst().orElseThrow(() -> new InvalidOperationException("Card with id: " + placedCardId + "is not existent"));

        if(!placedCard.getSide(chosenSide).isPlayable(field)){
            throw new InvalidOperationException("The card with id " + placedCard + "is not currently playable on the " + chosenSide);
        }


        super.registerPlayerMove(user, placedCardId, placementPoint, chosenSide, drawChoice);
    }

    @Override
    public String getWinnerName(User user) {

        if(user.getStatus() != IN_GAME){
            throw new InvalidOperationException("The user is not in any game");
        } else if(!user.getJoinedGame().isEnded()) {
            throw new InvalidOperationException("The game has not finished yet. Cannot declare a winner");
        }

        return super.getWinnerName(user);
    }

    @Override
    public void logout(User user){
        if(user.getStatus() == IN_GAME){
            throw new InvalidOperationException("Cannot logout during a game");
        } else if (user.getStatus() == WAITING_TO_START){
            throw new InvalidOperationException("Cannot logout when in a lobby");
        } else if(user.getStatus() != LOBBY_CHOICE){
            throw new InvalidOperationException("Cannot logout when performing other actions");
        }

        super.logout(user);
    }

    @Override
    public List<String> getPlayersNames(User user){
        if(user.getStatus() != IN_GAME){
            throw new InvalidOperationException("The user is not in any game");
        }

        return super.getPlayerNames(user);
    }

    @Override
    public List<ControlledPlayerInfo> getLeaderboard(User user){
        if(!user.getJoinedGame().isEnded()){
            throw new InvalidOperationException("The game has not ended. Cannot provide a leaderboard");
        }

        return super.getLeaderboard(user);
    }

    @Override
    public void exitGame(User user){
        if(user.getStatus() != IN_GAME){
            throw new InvalidOperationException("The user is not in any game");
        } else if(!user.getJoinedGame().isEnded()){
            throw new InvalidOperationException("Cannot exit from the game, it has not ended yet");
        }

        super.exitGame(user);
    }

    @Override
    public List<ObjectiveInfo> getCommonObjectives(User user) {
        if(user.getStatus() != IN_GAME){
            throw new InvalidOperationException("The user is not in any game");
        }

        return super.getCommonObjectives(user);
    }
}*/
