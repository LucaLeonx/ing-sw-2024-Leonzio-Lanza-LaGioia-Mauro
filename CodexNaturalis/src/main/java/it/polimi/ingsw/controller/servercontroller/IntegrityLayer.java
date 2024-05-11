package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.InvalidOperationException;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import java.util.List;

import static it.polimi.ingsw.controller.servercontroller.UserStatus.*;

public class IntegrityLayer extends FrontierServerLayer {
    @Override
    public int createLobby(User user, String lobbyName, int requiredPlayersNum) {

        if(user.getStatus() != LOBBY_CHOICE){
            throw new InvalidOperationException("Cannot create Lobby when not choosing one");
        }

        return super.createLobby(user, lobbyName, requiredPlayersNum);
    }

    @Override
    public void joinLobby(User user, int lobbyId) {

        if(user.getStatus() != LOBBY_CHOICE){
            throw new InvalidOperationException("Cannot join lobby when already in one");
        }

        super.joinLobby(user, lobbyId);
    }

    @Override
    public void exitFromLobby(User user) {

        if(user.getStatus() != WAITING_TO_START){
            throw new InvalidOperationException("Cannot exit a lobby if not in one");
        }

        super.exitFromLobby(user);
    }

    @Override
    public List<LobbyInfo> getLobbies(User user) {
        return super.getLobbies(user);
    }

    @Override
    public LobbyInfo getJoinedLobbyInfo(User user) {
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

        if(!user.getUsername().equals(user.getJoinedGame().getCurrentPlayerNickname())){
            throw new InvalidOperationException("It is not the turn of the player");
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
}
