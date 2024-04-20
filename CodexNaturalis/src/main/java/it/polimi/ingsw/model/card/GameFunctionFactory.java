package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.map.GameField;
import it.polimi.ingsw.model.map.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.lang.Math.floor;

public abstract class GameFunctionFactory {

    public static RewardFunction createPointsRewardFunction(int points) {
        return new RewardFunction() {
            @Override
            public int getPoints(GameField field) {
                return points;
            }
        };
    }

    public static RewardFunction createCountSymbolsFunction(Symbol symbol){
        return new RewardFunction() {
            @Override
            public int getPoints(GameField field) {
                return field.getCounter(symbol);
            }
        };
    }

    /**
     * @param symbolOccurrences
     * @return
     */

    public static RewardFunction createCountGroupSymbolsFunction(Map<Symbol, Integer> symbolOccurrences){
        return new RewardFunction() {
            @Override

            public int getPoints(GameField field) {
                List<Integer> numberOfGroups = new ArrayList<>();

                return symbolOccurrences.keySet().stream()
                        .mapToInt(symbol -> field.getCounter(symbol) / symbolOccurrences.get(symbol))
                        .min()
                        .orElse(0);
            }
            // return floor(field.getCounter(symbolOccurrences.keySet().stream().findFirst())/3); //stream().limit(1).collect(collector)
        };
    }

    public static RewardFunction createCoveredAnglesFunction(){
        return new RewardFunction() {
            @Override

            public int getPoints(GameField field) {
                return 0;
            }
        };
    }

    public static RewardFunction createDiagonalPatternMatchFunction(boolean angle, CardColor color_pattern)
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

                if(angle == true){
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
                                rightmost.set(i, pField);
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

                Point temp; // A variable to go through the diagonal
                for(int i = 0; i < leftmost.size(); i++) {
                    temp = leftmost.get(i);
                    cardsNum = 0;
                    while (!temp.equals(rightmost.get(i))) {
                        if (!field.getCards().containsKey(temp) || field.getCards().get(temp).getCardColor() != color_pattern) {
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
                        startingPoint = topmost;
                        endingPoint = lowermost;
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

                    while(!temp.equals(endingPoint.get(i)))
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
     *
     * @param requiredSymbols The symbols required, alongside with their count
     * @return true iff gamefield passed as field passed has more symbols that the one required.
     */
    public static RequirementFunction createRequiredSymbolsFunction(Map<Symbol, Integer> requiredSymbols){
        return new RequirementFunction() {
            @Override

            public boolean isSatisfied(GameField field) {
                for(Symbol s: requiredSymbols.keySet())
                {
                    if(field.getCounter(s)<requiredSymbols.get(s))
                        return false;
                }
                return true;
            }
        };
    }

}
