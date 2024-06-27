# Design proposal for the Game Field

We decided to represent the GameField as a matrix,
where the center of card occupies one specific cell,
while its angles fill the remaining cells nearby.


|   |    |   |    |   |
|---|----|---|----|---|
| a |    | b |    |   |
|   | C1 |   |    |   |
| d |    | c |    | e |
|   |    |   | C2 |   |
|   |    | g |    | f |
|   |    |   |    |   |
|   |    |   |    |   |


Managing in memory a dynamic matrix is complex and may
lead to an eccessive use of computational resources.
A possible solution is to keep track only of the filled cells, 
alongside their positions.

## Elements of the GameField

The GameField is characterized by the following elements:

__Cell__: a generic cell of the matrix. Each of them is uniquely
identified by its cartesian coordinates (x, y), with x and y integer values
(even negative).

__CardCell__: It represents a cell filled by (the center of) a card.
It should keep track of the following information:
- The Card positioned on that cells
- Its visible side (front, back)

__AngleCell__: It represents a cell occupied by an angle of a card.
It should store:
- The symbol visible on the top
- The cards attached to that angle
- The references about the card to which the angle belongs

The following invariants hold for the positions of CardCells and AngleCells:

- The position of the initial card (CardCell) is (0,0)
- The positions of the angles (AngleCells) of the initial
  card are (1, -1), (1, 1), (-1, -1), (1, 1)


|         |         |        |
|---------|---------|--------|
| (-1, 1) |         | (1, 1) |
|         | IC(0,0) |        |
| (-1,-1) |         | (1,-1) |

- All CardCell may have positions only of the form (2i, 2j), i, j in Z (integer set),
  that is only even coordinates;
- All AngleCell  may have positions only of the form (2i+1, 2j+1), i, j in Z, 
  that is, only odd coordinates;
- Given a CardCell, with its own position on the field, the positions
  of the adjacent AngleCells are the following:

|            |         |            |
|------------|---------|------------|
| (i-1, j+1) |         | (i+1, j+1) |
|            | C(i, j) |            |
| (i-1, j-1) |         | (i+1, j-1) |

There are many other interesting properties as well:
- Each AngleCell may display on top the symbol of the card to which it belongs
 or the one of the card attached to it. Moreover, we know that, for instance, 
 if the upper-right angle of a card is free, another card may cover it only
 with its lower-left angle.
- We can traverse adjacent cards by subsequently adding or subtracting 2 
  from the coordinates of the initial card 

## Representing and using the GameField

Considering all these aspects, it is possible to
represent the GameField using three sets:

- Set<CardCell> cards: List of all CardCell on the field
- Set<AngleCell> angles: List of all AngleCells on the field
- Set<Point> availableCells: List of all positions where a free AngleCell is present -
  that is, that can be covered with an additional card

We will now analyze the (worst-case) computational complexity of
different actions performed on the map. We will use as parameter
the number of placed cards n; So, for each new added card, the number
of angles will increase of 3. The total number of elements in the 
three sets which represents the GameField is about 7n, that is
the spatial complexity of this kind of representation is O(n).

Other operations will have different temporal complexities:

- Adding a new card, knowing its position and orientation:
    1. First of all, we can check that the supplied position is actually free,
       that it is present in availableCells;
    2. Add the card to a newly created CardCell
    3. Create all the AngleCells associated with the adjacent angles or, if they are
       already available, update their top symbol
    4. Add the new availableCells created by the addition of the new card:
       these are the cells with position in the form (2i ± 2, 2j ± 2), (i, j) position of the placed card,
       where there isn't a card yet and which are not adjacent to a hidden angle
    
    Temporal complexity: O(1)

- Counting the number of visible symbols on the GameField, for each category:
    1. Count the number of center symbols on the cards in CardCells
    2. Count the visible top symbols of each AngleCell in angles
    
    Temporal complexity: O(n)
    It can be made O(1) if counters with the occurrences of each symbol are saved
    and updated after the addition of a new card. This is the preferred solution

- Check the number of occurrences of a specific color pattern:
   It should be possible in O(n), by analyzing groups of cards one by one in a specific order
   (e.g. from the lower-left corner to the upper-right) and avoiding counting groups with
   overlapping cards twice.

