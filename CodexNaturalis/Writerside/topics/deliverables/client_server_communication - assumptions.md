# Assumptions about Client - Server communication

- Server is always listening for incoming connections and commands
- Each Client is uniquely identified by its Nickname (chosen when connecting to the Server)
  The Client prepends it to each request
- The GameBoard keeps track of the Games currently created on the Server which have not started yet.
- Each Game is identified by a unique ID (or URL), chosen by the Server and visible to the players.
  Moreover, a Game has a human-readable name, given by the creator. This doesn't need to be unique
  between different Games.