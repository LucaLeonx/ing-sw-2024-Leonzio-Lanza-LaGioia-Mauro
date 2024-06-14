package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.controller.servercontroller.operationexceptions.*;
import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;

import java.rmi.RemoteException;
import java.util.List;

/**
 * This interface represents the possible interaction that a client may
 * have with the server
 */
public interface ClientController {
    /**
     * Register user to the server
     * @param username The username chosen by the user
     * @return temporary code to log in as the user
     * @throws InvalidOperationException If the username is already in use
     * @throws RemoteException If there are issues with the remote execution of the method
     */
    public int register(String username) throws RemoteException;

    /**
     * Login to the game as the given user
     * @param username The name of the user who is logging in
     * @param tempCode The temporary code associated to the user, supplied during registration
     * @throws InvalidCredentialsException If either the username or the password
     *                                      does not correspond to those of a registered user
     * @throws RemoteException If there are issues with the remote execution of the method
     */
    public void login(String username, int tempCode) throws RemoteException;

    /**
     * The user logouts from her account. This operation is possible only if the user
     * is still choosing a lobby to join. On successful logout, the user is removed from the list of
     * registered users, and the corresponding username returns available for registration
     * @throws WrongPhaseException If the user is attempting to log out in a situation when it is not possible
     *                              (e.g. The user has joined a lobby or is in a game)
     * @throws RemoteException If there are issues with the remote execution of the method
     */
    public void logout() throws RemoteException;

    /**
     * Returns the list of available lobbies
     * @return A list with the information about the available lobbies
     * @throws InvalidOperationException If the operation is not possible: 
     *       in particular, if the user is not logged in, it cannot perform game-related operations
     * @throws RemoteException If there are issues with the remote execution of the method
     */
    public List<LobbyInfo> getLobbyList() throws RemoteException;

    /**
     * Create a new lobby and join it
     * @param lobbyName The name to give to the newly created lobby
     * @param requiredPlayers The number of players required to start the game.
     *                        It must be between 2 and 4 (included)
     * @return The information about the created lobby
     * @throws InvalidParameterException If the supplied number of players is out of range (between 2 and 4)
     * @throws WrongPhaseException If the user cannot perform the operation in the current situation
     *                             (e.g. it has already joined a lobby or is playing a game)
     * @throws InvalidOperationException If the operation is not possible. In particular, if the user is not logged in,
     *                                   it cannot perform game-related operations
     * @throws RemoteException If there are issues with the remote execution of the method
     */
    public LobbyInfo createLobby(String lobbyName, int requiredPlayers) throws RemoteException;

    /**
     * Join the lobby with the given ID. As soon as the required number of players
     * in the lobby is reached, a game starts automatically
     * @param lobbyId The ID of the lobby to join
     * @throws ElementNotFoundException It the lobby with the supplied ID is not available,
     *                                  because it became full and the game already started
     * @throws WrongPhaseException If the user cannot perform the operation in the current situation
     *                             (e.g. it has already joined a lobby or is playing a game)
     * @throws InvalidOperationException If the operation is not possible. In particular, if the user is not logged in,
     *                                   it cannot perform game-related operations
     * @throws RemoteException If there are issues with the remote execution of the method
     */
    void joinLobby(int lobbyId) throws RemoteException;
    /**
     * Returns the information about the joined lobby
     * @return The information about the joined lobby
     * @throws WrongPhaseException If the user is not in a lobby
     * @throws InvalidOperationException If the operation is not possible. In particular, if the user is not logged in,
     *                                   it cannot perform game-related operations
     * @throws RemoteException If there are issues with the remote execution of the method
     */
    public LobbyInfo getJoinedLobbyInfo() throws RemoteException;

    /**
     * Exit from the joined lobby
     * @throws WrongPhaseException If the user is not in a lobby (e.g. The game already started,
     *                             or it has not joined a lobby at all)
     * @throws InvalidOperationException If the operation is not possible. In particular, if the user is not logged in,
     *                                   it cannot perform game-related operations
     * @throws RemoteException If there are issues with the remote execution of the method
     */
    public void exitFromLobby() throws RemoteException;

    /**
     * This method blocks the caller until there isn't an update to the lobby list or
     * there is an exception
     */
    public void waitForLobbyListUpdate();

    /**
     * This method blocks the caller until the game in the joined lobby doesn't start
     * or there is an exception
     */
    public void waitForGameToStart();

    /**
     * This method blocks the caller until there isn't an update to the joined lobby
     * or there is an exception
     */
    public void waitForJoinedLobbyUpdate();

    /**
     * This method blocks the caller until the setup phase of the game hasn't finished
     * or there is an exception
     */
    public void waitForSetupFinished();

    /**
     * This method blocks the caller until the turn hasn't changed
     */
    public void waitForTurnChange();

    /**
     * This method blocks the caller until the game hasn't ended
     */
    public void waitForGameEnded();

    /**
     * Check whether the user is in a game or not
     * @return true if the user is currently in a game
     * @throws RemoteException If there are issues with the remote execution of the method
     */
    public boolean isInGame() throws RemoteException;

    /**
     * Returns the name of the player who is playing on the current turn
     * @return The nickname of the player currently playing
     * @throws WrongPhaseException If the user is not in any game
     * @throws InvalidOperationException If the operation is not possible. In particular, if the user is not logged in,
     *                                   it cannot perform game-related operations
     * @throws RemoteException If there are issues with the remote execution of the method
     */
    public String getCurrentPlayerName() throws RemoteException;
    /**
     * Returns the name of the players of a game
     * @return The nickname of the player currently playing
     * @throws WrongPhaseException If the user is not in any game
     * @throws InvalidOperationException If the operation is not possible. In particular, if the user is not logged in,
     *                                   it cannot perform game-related operations
     * @throws RemoteException If there are issues with the remote execution of the method
     */
    public List<String> getPlayerNames() throws RemoteException;

    /**
     * Returns the setup of the controlled player
     * @return The information about the setup of the controlled player
     * @throws WrongPhaseException If the user is not in any game
     * @throws InvalidOperationException If the operation is not possible. In particular, if the user is not logged in,
     *                                   it cannot perform game-related operations
     * @throws RemoteException If there are issues with the remote execution of the method
     */
    public PlayerSetupInfo getPlayerSetup() throws RemoteException;

    /**
     * Returns the name of the player who is playing on the current turn
     * @return The nickname of the player currently playing
     * @throws WrongPhaseException If the user is not in any game
     * @throws InvalidOperationException If the operation is not possible. In particular, if the user is not logged in,
     *                                   it cannot perform game-related operations
     * @throws RemoteException If there are issues with the remote execution of the method
     */
    public List<ObjectiveInfo> getCommonObjectives() throws RemoteException;

    /**
     * Returns the information about the controlled player
     * @return the information about the controlled player
     * @throws WrongPhaseException If the user is not in any game
     * @throws InvalidOperationException If the operation is not possible. In particular, if the user is not logged in,
     *                                   it cannot perform game-related operations
     * @throws RemoteException If there are issues with the remote execution of the method
     */
    public ControlledPlayerInfo getControlledPlayerInformation() throws RemoteException;

    /**
     * Returns the information about the opponent with the specified username
     * @param opponentName The nickname of the opponent about which the information is sought
     * @return The information about the specified opponent
     * @throws ElementNotFoundException If no opponent has the supplied username
     * @throws WrongPhaseException If the user is not in any game
     * @throws InvalidOperationException If the operation is not possible. In particular, if the user is not logged in,
     *                                   it cannot perform game-related operations
     * @throws RemoteException If there are issues with the remote execution of the method
     */
    public OpponentInfo getOpponentInformation(String opponentName) throws RemoteException;

    /** Returns the possible cards which can be drawn
     * @return The cards that can be drawn, along with their position. Usually there is
     * an entry for each value in DrawChoice enum. If it is missing, it means that no card
     * can be drawn from that position.
     * @throws WrongPhaseException If the user is not in any game
     * @throws InvalidOperationException If the operation is not possible. In particular, if the user is not logged in,
     *                                   it cannot perform game-related operations
     * @throws RemoteException If there are issues with the remote execution of the method
     */
    public DrawableCardsInfo getDrawableCards() throws RemoteException;


    /**
     * Returns true if it is the last turn of the game
     * @return True if it is the last turn of the game
     * @throws WrongPhaseException If the user is not in any game
     * @throws InvalidOperationException If the operation is not possible. In particular, if the user is not logged in,
     *      *                                   it cannot perform game-related operations
     * @throws RemoteException If there are issues with the remote execution of the method
     */
    public boolean isLastTurn() throws RemoteException;

    /**
     * Returns true if the game has ended
     * @return True if the game has ended
     * @throws WrongPhaseException If the user is not in any game
     * @throws InvalidOperationException If the operation is not possible. In particular, if the user is not logged in,
     *      *                                   it cannot perform game-related operations
     * @throws RemoteException If there are issues with the remote execution of the method
     */
    public boolean hasGameEnded() throws RemoteException;

    /**
     * Returns the name of the winner of the game, if available
     * @return The name of the winner of the game
     * @throws WrongPhaseException If the user is not in any game, or the game has not finished yet
     * @throws InvalidOperationException If the operation is not possible. In particular, if the user is not logged in,
     *                                   it cannot perform game-related operations
     * @throws RemoteException If there are issues with the remote execution of the method
     */
    public String getWinner() throws RemoteException;

    /**
     * Returns the leaderboard of the game. It can be requested only at the end of a game.
     * The leaderboard is a list with the information of each player, sorted in decreasing order of points
     * (e.g. The 0-index element of the list is the winner, the 1-index element of the list is the
     * player with the second-highest score...).
     * @return The leaderboard of an ended game
     * @throws WrongPhaseException If the user is not in any game, or the game has not finished yet
     * @throws InvalidOperationException If the operation is not possible. In particular, if the user is not logged in,
     *                                   it cannot perform game-related operations
     * @throws RemoteException If there are issues with the remote execution of the method
     */
    public List<ControlledPlayerInfo> getLeaderboard() throws RemoteException;

    /**
     * Returns the leaderboard of the game. It can be requested only at the end of a game.
     * The leaderboard is a list with the information of each player, sorted in decreasing order of points
     * (e.g. The 0-index element of the list is the winner, the 1-index element of the list is the
     * player with the second-highest score...).
     * @return The leaderboard of an ended game
     * @throws WrongPhaseException If the user is not in any game, or the game has not finished yet
     * @throws InvalidOperationException If the operation is not possible. In particular, if the user is not logged in,
     *                                   it cannot perform game-related operations
     * @throws RemoteException If there are issues with the remote execution of the method
     */
    public void setPlayerSetup(ObjectiveInfo chosenObjective, CardOrientation initialCardSide) throws RemoteException;

    /**
     * Perform a move with the controlled player, playing a card on her own map and drawing another one.
     * @param card The information about the card to play
     * @param placementPoint The point of the map where the card is played
     * @param chosenSide The side chosen to play the card
     * @param drawChoice The card on the field or on the deck to draw at the end of the move
     * @throws WrongPhaseException If the user is not in any game, or it is not its turn
     * @throws InvalidParameterException If it is not possible to perform the move (either the card cannot be played
     * @throws ElementNotFoundException If the card that the player is attempting to draw is non-existent
     * @throws RemoteException If there are issues with the remote execution of the method
     */
    public void makeMove(CardInfo card, Point placementPoint, CardOrientation chosenSide, DrawChoice drawChoice) throws RemoteException;


    /**
     * Exit from the game. It is possible doing it only at the end of the game
     * @throws WrongPhaseException If the game has not finished yet
     * @throws RemoteException If there are issues with the remote execution of the method
     */
    public void exitGame() throws InvalidOperationException, RemoteException;

    /**
     * Returns true if the player is waiting in a joined lobby to start a game
     * @return True if the player is waiting in a joined lobby to start a game
     */
    boolean isWaitingInLobby();
}

