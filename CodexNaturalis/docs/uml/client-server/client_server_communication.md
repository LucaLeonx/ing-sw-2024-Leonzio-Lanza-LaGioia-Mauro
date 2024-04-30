# Client - Server communication

## Player accesses Game

### Assumptions
- Server is always listening 
- Client connects immediately at startup
- Client sends its nickname alongside each request to the server (after having chosen it)
- Client periodically (automatically) requests the board with open games (or the user can do it manually)

1. Client connect to Server address (using tcp://ip:port)
2. Server accepts connection
3. The client chooses a Nickname and sends it to the server
    - If a player with the same nickname is already connected, the server
    asks the client to choose another one
    - If a client with this nickname was disconnected previously the server reconnect him/her to the game.

4. Server sends information about all open games on a gameboard, which have not started
   Each game has:
        - A unique ID (or URL), assigned by the server and visible to players
        - A name, visible to players and chosen by the creator.
          There may be matches with the same name, distinguished by ID
        - The Nickname of the creator
        - Number of players required
        - Number and Nicknames of player ready to start

* Keep in mind that the server should be aware of closed games and disconnected players
* in order to implement persistence

5a. The client chooses to create a new game
    - The client specifies the name and the number of players required
    - The server creates the game and sends the ID to the client
    - The client joins the waiting lobby for the created game (5b)
    - The server updates its local gameboard

5b. The client chooses to join the game created by another player
    - The client sends the join command, with the ID
    - The server accepts the request if the number of players doesn't exceed the maximum required;
    otherwise, the request is rejected
    - If the required number of players has been reached, the game starts
    - The server updates the gameboard with the new info

## Game initialisation 
    - Server gives randomly to a player his/her colour.
    - Server gives to players
        a) 2 resource cards
        b) 1 gold card
        c) initial card 
        d) information about the visible cards and deck 
        e) 2 objective cards
    - players decide the side of the initial card and an objective card
    

## Player plays a card from his/her hand
    - If player is dead he/she will skip the turn (to be dead, a player should not have a possible move)
    - Server sends the info about the game:
        - The points of each player
        - The general maps of each player
        - Visible cards and decks
        - Is last turn (turn number)?
    - Server sends a series a points where player can play the card
    - Player sends the cmd:Play(CardId, CardSide, Point)
    - Server accepts the change or server refuses the change (based on recq): if refuses loop.
    - The server broadcastes the update to the map to all the other players

## Player draws
    - Player decides from which deck to draw, cmd draw(drawable) drawable enum: rd, gd, gc1, gc2, rc1, rc2.
    - Iff the selected drawable was gc1, gc2, rc1, rc2, we need to
        a) refill the table locally in the server 
        b) updates the cards in the hand of the player
    - Server sends to each player the new visible cards (and the back of the cards in the hand of the player?)
    - Server gives information to all player about whose turn is next: (Ste, Luca, *Gio*, Simo) 
    {in this case Luca has just finished the turn and then Gio will play, so is starred}






    