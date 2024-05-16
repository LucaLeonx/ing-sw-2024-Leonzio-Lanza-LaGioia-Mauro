package it.polimi.ingsw.controller.servercontroller;

import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import java.util.List;

import static it.polimi.ingsw.dataobject.InfoTranslator.convertToLobbyInfo;

public class ConversionLayer extends FrontierServerLayer {
    private final InternalServerLayer internalServerLayer;
    private final LobbyList lobbyList;

    public ConversionLayer(LobbyList lobbyList,InternalServerLayer internalServerLayer) {
        super();
        this.lobbyList = lobbyList;
        this.internalServerLayer = internalServerLayer;
    }

    @Override
    public LobbyInfo createLobby(User user, String lobbyName, int requiredPlayersNum) {
        return convertToLobbyInfo(internalServerLayer.createLobby(user, lobbyName, requiredPlayersNum));
    }

    @Override
    public void joinLobby(User user, int lobbyId) {
        internalServerLayer.addUserToLobby(user, lobbyId);
    }

    @Override
    public void exitFromLobby(User user) {
        internalServerLayer.removeUserFromLobby(user, user.getJoinedLobby());
    }

    @Override
    public List<LobbyInfo> getLobbies(User user) {
        return internalServerLayer.getLobbies().stream()
                .map(InfoTranslator::convertToLobbyInfo)
                .toList();
    }

    @Override
    public LobbyInfo getJoinedLobbyInfo(User user) {
        return convertToLobbyInfo(internalServerLayer.getJoinedLobby(user));
    }

    @Override
    public String getCurrentPlayer(User user) {
        return internalServerLayer.getCurrentPlayer(user.getJoinedGame());
    }

    @Override
    public ControlledPlayerInfo getControlledPlayerInfo(User user) {
        return InfoTranslator.convertToControlledPlayerInfo(internalServerLayer.getPlayer(user.getJoinedGame(), user.getUsername()));
    }

    @Override
    public OpponentInfo getOpponentInfo(User user, String opponentName) {
        return InfoTranslator.convertToOpponentPlayerInfo(internalServerLayer.getPlayer(user.getJoinedGame(), opponentName));
    }

    @Override
    public DrawableCardsInfo getDrawableCardsInfo(User user) {
        return InfoTranslator.convertToDrawableCardsInfo(internalServerLayer.getDrawableCards(user.getJoinedGame()));
    }

    @Override
    public PlayerSetupInfo getPlayerSetupInfo(User user) {
        return InfoTranslator.convertToPlayerSetupInfo(internalServerLayer.getPlayerSetup(user.getJoinedGame(), user.getUsername()));
    }

    @Override
    public boolean isLastTurn(User user) {
        return internalServerLayer.isLastTurn(user.getJoinedGame());
    }

    @Override
    public boolean hasGameEnded(User user) {
        return internalServerLayer.hasGameEnded(user.getJoinedGame());
    }

    @Override
    public void registerPlayerSetup(User user, int objectiveCardId, CardOrientation initialCardSide) {
        internalServerLayer.registerPlayerSetup(user.getJoinedGame(), user.getUsername(), objectiveCardId, initialCardSide);
    }

    @Override
    public void registerPlayerMove(User user, int placedCardId, Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice) {
        internalServerLayer.registerPlayerMove(user.getJoinedGame(), user.getUsername(), placedCardId, placementPoint, chosenSide, drawChoice);
    }

    @Deprecated
    @Override
    public String getWinnerName(User user) {
        return internalServerLayer.getLeaderboard(user.getJoinedGame()).getFirst().getNickname();
    }

    public List<ControlledPlayerInfo> getLeaderboard(User user) {
        return internalServerLayer.getLeaderboard(user.getJoinedGame()).stream().map(InfoTranslator::convertToControlledPlayerInfo).toList();
    }

    @Override
    public List<String> getPlayerNames(User user){
        return internalServerLayer.getPlayerNames(user.getJoinedGame());
    }

    @Override
    public List<String> getPlayersNames(User user) {
        return internalServerLayer.getPlayerNames(user.getJoinedGame());
    }

    @Override
    public void logout(User user){
        internalServerLayer.logout(user);
    }

    @Override
    public void exitGame(User user){
        internalServerLayer.exitGame(user);
    }

    @Override
    public List<ObjectiveInfo> getCommonObjectives(User user){
        return internalServerLayer.getCommonObjectives(user.getJoinedGame()).stream().map(InfoTranslator::convertToObjectiveInfo).toList();
    }
}