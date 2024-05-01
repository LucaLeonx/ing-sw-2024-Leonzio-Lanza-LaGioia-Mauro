package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.InvalidOperationException;
import it.polimi.ingsw.model.map.Point;

import java.util.List;
import java.util.Map;

public interface ClientController {
    // WARNING: The methods may throw unchecked exceptions, if called in the wrong place
    public List<String> getPlayerNames();
    public PlayerSetup getPlayerSetup();
    public ControlledPlayerInfo getControlledPlayerInformation();
    public OpponentInfo getOpponentInformation(String opponentName);
    /**
     *
     * @return The cards that can be drawn, along with their position. Usually there is
     * an entry for each value in DrawChoice enum. If it is missing, it means that no card
     * can be drawn from that position.
     */
    public Map<DrawChoice, CardSideInfo> getDrawableCards();
    public String getCurrentPlayerName();
    public boolean isLastTurn();
    public String getWinner();
    public void makeMove(CardInfo card, Point placementPoint, DrawChoice drawchoice);
    public void start();
    public void close();

    /* Methods to be added later
    public List<LobbyInfo> getLobbyList();

    public LobbyInfo getJoinedLobbyInfo();

    public boolean joinLobby();
     */
}

