# Controller design

We provide an interface Controller, which represents the commands that the client may call
on the Server to work on the game.

Internally, the implementation of each method is based on the current state of the controller.
Relevant actions which need to change the state need to call the private method changeState().

private synchronized State changeState(){
    state = state.transitionFunction();
}

Each state inherits from the abstract class ControllerState.
This class provides the default constructor implementation and the default methods implementations.
These implementations throw all InvalidOperation exceptions, which must be handled either by the controller or 
within the state (more on this later, about extending ControllerState).

Games are provided from an external constructor to the Controller. This is necessary because some other 
thread may need to access the model (e.g. for persistence, to save data on the disk), or to restart
a Game from the previous state

## A few notes on particular methods.

- getPlayerPublicInformation() provides a copy of the specified Player (with GameField, score, etc.), where the private information, that is
  the secret objective and the cards in hand, are changed with dummy cards with id = 0, and the front changed
  with the back of the original card. For objectives, the reward function that gives 0 points is substituted.
- getPlayerFullInformation() allows to get every single needed information. 
- This strategy is used also to hide the cards into the decks and display just the back of the top card.
- If one value of DrawChoice is missing from getDrawableCards(), it means it is missing (e.g. the decks are empty).

## State automata

The default ControllerStates are three and implement the following logic:
- SetupState: asks each player with its initial game setup (objective cards, initial cardside);
  Once all players have done their setup, it transitions to PlayState
- PlayState: performs all game operations (getters, drawing, playingcards). At each transition,
  it updates the current player and the lastTurn variable in the Game class when isLastPlayer(). If 
  isLastTurn() && isLastPlayer(), the lastTurn has ended, so transitionFunction() leads to EndGameState
- EndGameState determines the points from objectives and shows the winner to the players. Its transition function
  remains at the same state. Moreover, its getters display full information about players, regardless of the caller.

## How extensions of state should be used

We expect the ServerController and ClientController class to be ready directly for RMI with simple ControllerState,
while those controllers working with Sockets should use ServerState and ClientState extensions.
These allow to extend basic states by adding network logic. For example

public ServerPlayState extends PlayState {
    ...
    public makePlayerMove(int cardId, CardOrientation orientation, Point position){
        // Network logic to get parameters values from the client
        try {
                super.makePlayerMove(cardId, orientation, position);
                // Send Acceptance code
        } except InvalidMoveException {
            // Discard the move, or send an error code to the client
        }
    }
}

## Handling authentication within Controller calls

One possibility is exploiting ControllerClass extensions for Servers to handle authentication and modify getters
return value depending on the player who calls them. For instance, this allows to choose between
getPlayerPublicInformation() and getPlayerFullInformation() when calling getPlayerInformation(),
or when deciding whether a client is allowed to call makePlayerMove(), makePlayerDraw().
To authenticate the player, the secret temporary code is used
(stored into the application). 

Keep in mind that other strategies may be possible. 

## Other modifications to be made to the server

- GameFactory generates a generic base Game. It should support also restoring games from snapshots
- CardFactory class should return cards lists as Singletons. It shouldn't load cards from disk every single time.
  Getting cards should be a concurrent operation (it should use concurrent lists).