# Project choices
- Class Deck only composed of gold and resource cards
- Class ObjectiveCard separated from other cards for different attributes behaviour
- Need to check how to manage a deck for objective card
- Every card can be played front or back, because that changes its functionality. <br> 
The played side is managed by the class that uses the card
- Class Player has a cardsInHand attribute (3 elements array).

- For resource cards, the passed PointFunction returns always 0 
and the passed RequirementsFunction returns always true.
This way, even if golden and resource cards are different kinds of cards, the
code to manipulate them (e.g. checking if it is possible to play them) doesn't
need to account for their differences.
In the future, we may subclass Card into InitialCard, GoldCard, ResourceCard to
add specific methods for each of them.


### Possible improvements

- It may be necessary to rewrite the interfaces _PointFunction_ and _RequirementsFunction_
as abstract classes, in order to attach extra data to represent 
them in the TUI or GUI (es. symbols they require, number of points they reward)

## Concurrency management

Most of the operations made by the clients and the server are reads (e.g. player nicknames, scores, GameFields...),
with less write operations (place a card on the map, draw a card from the deck).
Since we don't want any user of the model to see it in a non-consistent state (for instance, with the player who 
has played a card, but that hasn't drawn from the cards yet), we want to perform writes atomically.
Write operations involve acquiring many resources (a Player hand, a Player Map, all visible decks and cards)
That's why we attach a ReadWriteReentrantLock to the Game class, which allows an unlimited number of accesses in read,
but locks the entire game for just one thread during writes.
This design may not be the best in terms of concurrency (writes may take a lot of time, blocking reads in the meanwhile),
so clients should cache results of previous readings (writes are performed only when it is a players turn).
Moreover, this design has the added benefit that we don't need to rewrite many parts of the model 
to support concurrency.

## Other improvements to be made
- The Optional return value for Deck could be removed, allowing the return of an unchecked 
  EmptyDeckException instead. Indeed, we expect all parts of the code to check whether the deck 
  has at least one card, before drawing from it. Otherwise, something wrong happened, 
  and the program should either abort or try to recover from the situation. 
  We don't want to add useless syntactic overhead to the code
- The RandomPicker and the Deck class have now some methods to see the cards inside. This is needed in 
  order to implement persistence: the order of cards must be known. Other strategies could work
  (e.g. from random seeds, or form cards on the fields and in the setups), but are more complicated to implement.
- 