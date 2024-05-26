package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.map.GameField;
import it.polimi.ingsw.model.map.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * This class is a factory for the Reward and Requirement functions
 * evaluated when playing cards
 */
public abstract class GameFunctionFactory {

    /**
     * Creates a function that awards points, regardless of the field status
     * @param points The number of points to give
     * @return A RewardFunction that gives a fixed amount of points
     */
    public static RewardFunction createPointsRewardFunction(int points) {
        return new RewardFunction() {
            @Override
            public int getPoints(GameField field) {
                return points;
            }
        };
    }

    /**
     * Creates a function that awards a point for each occurrence of the specified symbol on the field.
     * @param symbol The symbol to count on the field
     * @return A RewardFunction that awards a point for each occurrence of the specified symbol on the field
     */
    public static RewardFunction createCountSymbolsFunction(Symbol symbol){
        return new RewardFunction() {
            @Override
            public int getPoints(GameField field) {
                return field.getSymbolCounter(symbol);
            }
        };
    }

    /**
     * Creates a function that gives a certain number of points for each symbols' grouping found on the map.
     * @param symbolOccurrences Specifies how many occurrences of a symbol should be present in each group
     * @param points The number of points awarded for each group
     * @return A function that gives a given number of points for each group of symbols present on the map
     */
    public static RewardFunction createCountGroupSymbolsFunction(Map<Symbol, Integer> symbolOccurrences, int points){
        return new RewardFunction() {
            @Override

            public int getPoints(GameField field) {

                return symbolOccurrences.keySet().stream()
                        .mapToInt(symbol -> field.getSymbolCounter(symbol) / symbolOccurrences.get(symbol))
                        .min()
                        .orElse(0) * points;
            }

        };
    }

    /**
     * Creates a function that gives two points  for each angle covered by a card on the map
     * @param cardId The id of the card to search
     * @return A function that gives two points for each angle covered by a card on the map
     */
    public static RewardFunction createCoveredAnglesFunction(int cardId){
        return new RewardFunction() {
            @Override

            public int getPoints(GameField field) {

                Point cardPosition = new Point(0, 0);
                int coveredAngles = 0;

                // Find the position of the card on the field
                try {
                    cardPosition = field.getCards().entrySet()
                            .stream().filter((entry) -> entry.getValue().getId() == cardId)
                            .findFirst()
                            .map(Map.Entry::getKey)
                            .orElseThrow();
                } catch(Exception e){
                    // Searched card is not on the map, it cannot cover angles
                    return 0;
                }

                for(Point anglePoint : Point.getAdjacentPositions(cardPosition)){
                    //if bottom position of the angle card == to the same card means that it is the only card for that angle -->
                    if(!field.getAngleCells().get(anglePoint).bottomCardPosition().equals(cardPosition) ){
                        coveredAngles++;
                    }
                }

                return coveredAngles * 2;
            }
        };
    }

    /**
     * Creates a function that awards a certain number of points for disjoint group
     * of cards that form a diagonal pattern.
     * @param isSlopePositive Specifies whether the diagonal pattern is from lower left to upper right or not
     *                        (otherwise it is from lower right to upper left)
     * @param colorPattern The color of the cards of the pattern
     * @return A function that hat awards a certain number of points for disjoint group
     *         of cards that form a diagonal pattern.
     */
    public static RewardFunction createDiagonalPatternMatchFunction(boolean isSlopePositive, CardColor colorPattern)
    {
        return new RewardFunction() {
            @Override

            public int getPoints(GameField field) {


                List<Point> leftmost = new ArrayList<>();
                List<Point> rightmost = new ArrayList<>();

                int cardsNum = 0;
                int score = 0;

                Point translationVector = new Point(0,0);
                int slope = 0;

                if(isSlopePositive){
                    translationVector = new Point(2, 2);
                    slope = 1;
                }
                else{
                    translationVector = new Point(2, -2);
                    slope = -1;
                }


                //if(angle == true) {
                for(Point pField: field.getCards().keySet())
                {
                    boolean isInNewDiagonal = true;
                    for(int i = 0; i < leftmost.size(); i++) {
                        if (leftmost.get(i).y() - slope * leftmost.get(i).x() == pField.y() - slope * pField.x()) {
                            if (pField.x() < leftmost.get(i).x()) {
                                leftmost.set(i, pField);
                            } else if (pField.x() > rightmost.get(i).x()) {
                                rightmost.set(i, pField);
                            }
                            isInNewDiagonal = false;
                        }
                    }

                    // If I don't find any points already in the arraylist that are on that diagonal
                    // we add the point to both arraylist at the bottom.
                    if(isInNewDiagonal) {
                        rightmost.add(pField);
                        leftmost.add(pField);
                    }
                }

                //now we start checking each diagonal one by one
                Point temp;
                for(int i = 0; i < leftmost.size(); i++) {
                    temp = leftmost.get(i);
                    cardsNum = 0;
                    while (!temp.equals((rightmost.get(i).sum(translationVector)))){
                        if (!field.getCards().containsKey(temp) || field.getCards().get(temp).getCardColor() != colorPattern) {
                            cardsNum = 0;
                        } else {
                            cardsNum++;
                        }

                        if (cardsNum % 3 == 0 && cardsNum > 0) {
                            score = score + 2;
                        }

                        temp = temp.sum(translationVector);
                    }
                }
                return score;
            }
        };
    }

    /**
     * Creates a function that awards a certain number of points for disjoint group
     * of cards that form a "block" pattern: two card of the same color on the same column,
     * with a third one attached to one of the angles.
     * @param angle the angle of the column block covered by the other card
     * @param colorBlock The color of the cards in the same column
     * @param colorAngle The color of the card in the angle
     * @return a function that awards a certain number of points for disjoint group
     *      * of cards that form a "block" pattern.
     */
    public static RewardFunction createBlockPatternMatchFunction(AnglePosition angle, CardColor colorBlock , CardColor colorAngle){
        return new RewardFunction() {
            @Override

            public int getPoints(GameField field) {
                List<Point> topmost = new ArrayList<>();
                List<Point> lowermost = new ArrayList<>();

                boolean isInNewColumn = true;

                // Find vertical lines
                for(Point pField: field.getCards().keySet())
                {
                    isInNewColumn = true;
                    for(int i=0; i < lowermost.size() && isInNewColumn; i++) {

                        if (lowermost.get(i).x() == pField.x()) {
                            if (pField.y() < lowermost.get(i).y()) {
                                lowermost.set(i, pField);
                            }
                            else if (pField.y() > topmost.get(i).y()) {
                                topmost.set(i, pField);
                            }
                            isInNewColumn = false;
                        }
                    }
                    // If I don't find any points already in the arraylist that are on that column
                    // we add the point to both arraylist at the bottom.
                    if(isInNewColumn){
                        topmost.add(new Point(pField.x(), pField.y()));
                        lowermost.add(new Point(pField.x(), pField.y()));
                    }
                }

                // respective position of the angle card to the block
                List<Point> startingPoint = new ArrayList<>();
                List<Point> endingPoint = new ArrayList<>();
                Point positionOfAngleCard = new Point(0,0);
                Point direction = new Point(0,0);
                int cardsNum = 0;
                int score = 0;


                switch(angle) {
                    case AnglePosition.UP_RIGHT: {
                        positionOfAngleCard = new Point(2, 2);
                        startingPoint = lowermost;
                        endingPoint = topmost;
                        direction = new Point(0, 4);
                        break;
                    }
                    case AnglePosition.UP_LEFT:{
                        positionOfAngleCard = new Point(-2, 2);
                        startingPoint = lowermost;
                        endingPoint = topmost;
                        direction = new Point(0, 4);
                        break;
                    }
                    case AnglePosition.DOWN_LEFT: {
                        positionOfAngleCard = new Point(-2, -2);
                        startingPoint = topmost;
                        endingPoint = lowermost;
                        direction = new Point(0, -4);
                        break;
                    }
                    case AnglePosition.DOWN_RIGHT: {
                        positionOfAngleCard = new Point(2, -2);
                        startingPoint = topmost;
                        endingPoint = lowermost;
                        direction = new Point(0, -4);
                        break;
                    }
                    default: break;
                }

                //now we start checking each column
                //this is needed to update each time the temp point
                Point temp; // A variable to go through the column
                for(int i=0; i < startingPoint.size(); i++)
                {
                    temp = startingPoint.get(i);
                    cardsNum = 0;

                    while(!temp.equals(endingPoint.get(i).sum(direction)))
                    {
                        if(!field.getCards().containsKey(temp) || field.getCards().get(temp).getCardColor() != colorBlock)
                            cardsNum = 0;
                        else
                        {
                            if(cardsNum < 2)
                                cardsNum++;
                        }
                        if(cardsNum == 2) {
                            if(field.getCards().containsKey(temp.sum(positionOfAngleCard)) && field.getCards().get(temp.sum(positionOfAngleCard)).getCardColor() == colorAngle)
                                score = score + 3;
                        }
                        temp = temp.sum(direction);
                    }
                }
                return score;
            }
        };
    }


    /**
     * Creates a function that specifies the requirement of having a certain number of symbols
     * of a specific type on the field.
     * @param requiredSymbols The symbols required, alongside with their minimum number of occurrences
     * @return A function that evaluates to true if and only if the number of occurrences of each specified
     *         symbols reaches the minimum required.
     */
    public static RequirementFunction createRequiredSymbolsFunction(Map<Symbol, Integer> requiredSymbols){
        return new RequirementFunction() {
            @Override

            public boolean isSatisfied(GameField field) {
                for(Symbol s: requiredSymbols.keySet())
                {
                    if(field.getSymbolCounter(s) < requiredSymbols.get(s))
                        return false;
                }
                return true;
            }
        };
    }

}
