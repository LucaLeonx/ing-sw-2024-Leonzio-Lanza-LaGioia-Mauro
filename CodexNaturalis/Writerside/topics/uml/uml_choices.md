# Project choices
- Class Deck only composed of gold and resource cards
- Class ObjectiveCard separated from other cards for different attributes behaviour
- Need to check how to manage a deck for objective card
- Every card can be played front or back, because that changes its functionality. <br> 
The played side is managed by the class that uses the card
- Class Player has a cardsInHand attribute(3 elements array).

- For resource cards, the passed PointFunction returns always 0 
and the passed RequirementsFunction returns always true.
This way, even if golden and resource cards are different kinds of cards, the
code to manipulate them (e.g. checking if it is possible to play them) doesn't
need to account for their differences.
In the future, we may subclass Card into InitialCard, GoldCard, ResourceCard to
add specific methods for each of them.

### To do
- Game field set-up
- 

### Possible improvements

- It may be necessary to rewrite the interfaces _PointFunction_ and _RequirementsFunction_
as abstract classes, in order to attach extra data to represent 
them in the TUI or GUI (es. symbols they require, number of points they reward)
