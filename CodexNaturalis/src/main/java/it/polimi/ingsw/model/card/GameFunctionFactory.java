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

    public static RewardFunction createCountGroupSymbolsFunction(Map<Symbol, Integer> symbolOccurrences ){
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

    //To see:
    // 1) can we add a method .equals in point?
    // 2) !field.getCards().containsKey(temp) i' m not to sure that this thing work as intended

    public static RewardFunction createCoveredAnglesFunction(){
        return new RewardFunction() {
            @Override

            public int getPoints(GameField field) {
                return 0;
            }
        };
    }

    public static RewardFunction createDiagonalPatternMatchFunction(boolean angle ,CardColor color_pattern)
    {
        return new RewardFunction() {
            @Override

            public int getPoints(GameField field) {
                List<Point> rightmost = new ArrayList<>();
                List<Point> leftmost = new ArrayList<>();
                int NOC=0;
                int score=0;
                if(angle==true)
                {
                    for(Point pField: field.getCards().keySet())
                    {
                        for(int i=0; i<leftmost.size(); i++) {
                            if (leftmost.get(i).y() - leftmost.get(i).x() == pField.y() - pField.x()) {
                                if (pField.x() < leftmost.get(i).x()) {
                                    leftmost.remove(i);
                                    leftmost.add(i, new Point(pField.x(), pField.y()));
                                }
                                if (pField.x() > rightmost.get(i).x()) {
                                    rightmost.remove(i);
                                    rightmost.add(i, new Point(pField.x(), pField.y()));
                                }
                                break;
                            }
                            // If I don't find any points already in the arraylist that are on that diagonal
                            // we add the point to both arraylist at the bottom.
                            leftmost.add(new Point(pField.x(), pField.y()));
                            rightmost.add(new Point(pField.x(), pField.y()));
                        }
                    }
                    //now we start checking each diagonal one by one
                    Point direction=new Point(2, 2); //this is needed to update each time the temp point
                    Point temp; // A variable to go through the diagonal
                    for(int i=0; i< leftmost.size(); i++)
                    {
                       temp=new Point(leftmost.get(i).x(), leftmost.get(i).y());
                        NOC=0;
                        while(!temp.equals(rightmost.get(i))
                        {
                            if(!field.getCards().containsKey(temp) || field.getCards().get(temp).getCardColor()!=color_pattern)
                                NOC=0;
                            else
                                NOC++;
                            if(NOC%3==0 && NOC>0)
                                score=score+2;
                            temp=temp.sum(direction);
                        }
                    }
                }

                else
                {
                    for(Point pField: field.getCards().keySet())
                    {
                        for(int i=0; i<leftmost.size(); i++) {
                            if (leftmost.get(i).y() + leftmost.get(i).x() == pField.y() + pField.x()) {
                                if (pField.x() < leftmost.get(i).x()) {
                                    leftmost.remove(i);
                                    leftmost.add(i, new Point(pField.x(), pField.y()));
                                }
                                if (pField.x() > rightmost.get(i).x()) {
                                    rightmost.remove(i);
                                    rightmost.add(i, new Point(pField.x(), pField.y()));
                                }
                                break;
                            }
                            // If I don't find any points already in the arraylist that are on that diagonal
                            // we add the point to both arraylist at the bottom.
                            leftmost.add(new Point(pField.x(), pField.y()));
                            rightmost.add(new Point(pField.x(), pField.y()));
                        }
                    }
                    //now we start checking each diagonal one by one
                    Point direction=new Point(2, -2); //this is needed to update each time the temp point
                    Point temp; // A variable to go through the diagonal
                    for(int i=0; i< leftmost.size(); i++)
                    {
                        temp=new Point(leftmost.get(i).x(), leftmost.get(i).y());
                        NOC=0;
                        while(!temp.equals(rightmost.get(i))
                        {
                            if(!field.getCards().containsKey(temp) || field.getCards().get(temp).getCardColor()!=color_pattern)
                                NOC=0;
                            else
                                NOC++;
                            if(NOC%3==0 && NOC>0)
                                score=score+2;
                            temp=temp.sum(direction);
                        }
                    }
                }
                return score;
            }
        };
    }

    public static RewardFunction createBlockPatternMatchFunction(AnglePosition angle, CardColor color_block , CardColor color_Angle){
        return new RewardFunction() {
            @Override

            public int getPoints(GameField field) {
                List<Point> topmost = new ArrayList<>();
                List<Point> downmost = new ArrayList<>();
                int NOC=0;
                int score=0;
                Point PositionOfAngleCard; // respective position of the angle card to the block.

                if(angle==AnglePosition.UP_RIGHT)
                {
                    PositionOfAngleCard=new Point(2,2);
                }
                if(angle==AnglePosition.UP_LEFT)
                {
                    PositionOfAngleCard=new Point(-2,2);
                }
                if(angle==AnglePosition.DOWN_LEFT)
                {
                    PositionOfAngleCard=new Point(-2,-2);
                }
                if(angle==AnglePosition.DOWN_RIGHT)
                {
                    PositionOfAngleCard=new Point(2,-2);
                }


                for(Point pField: field.getCards().keySet())
                {
                    for(int i=0; i<downmost.size(); i++) {
                        if (downmost.get(i).x() == pField.x()) {
                            if (pField.y() < downmost.get(i).y()) {
                                downmost.remove(i);
                                downmost.add(i, new Point(pField.x(), pField.y()));
                            }
                            if (pField.y() > topmost.get(i).y()) {
                                topmost.remove(i);
                                topmost.add(i, new Point(pField.x(), pField.y()));
                            }
                            break;
                        }
                        // If I don't find any points already in the arraylist that are on that column
                        // we add the point to both arraylist at the bottom.
                        topmost.add(new Point(pField.x(), pField.y()));
                        downmost.add(new Point(pField.x(), pField.y()));
                    }
                }

                if(angle==AnglePosition.UP_RIGHT || angle==AnglePosition.UP_LEFT)
                {
                    //now we start checking each column
                    Point direction=new Point(0, 4); //this is needed to update each time the temp point
                    Point temp; // A variable to go through the column
                    for(int i=0; i< downmost.size(); i++)
                    {
                        temp=new Point(downmost.get(i).x(), downmost.get(i).y());
                        NOC=0;
                        while(!temp.equals(topmost.get(i))
                        {
                            if(!field.getCards().containsKey(temp) || field.getCards().get(temp).getCardColor()!=color_block)
                                NOC=0;
                            else
                            {
                                if(NOC<2)
                                    NOC++;
                            }
                            if(NOC==2) {
                                if(field.getCards().containsKey(temp.sum(PositionOfAngleCard)) && field.getCards().get(temp.sum(PositionOfAngleCard)).getCardColor()==color_Angle)
                                    score=score+3;
                            }
                            temp=temp.sum(direction);
                        }
                    }
                }

                else
                {
                    //now we start checking each column
                    Point direction=new Point(0, -4); //this is needed to update each time the temp point
                    Point temp; // A variable to go through the column
                    for(int i=0; i< downmost.size(); i++)
                    {
                        temp=new Point(topmost.get(i).x(), topmost.get(i).y());
                        NOC=0;
                        while(!temp.equals(downmost.get(i))
                        {
                            if(!field.getCards().containsKey(temp) || field.getCards().get(temp).getCardColor()!=color_block)
                                NOC=0;
                            else
                            {
                                if(NOC<2)
                                    NOC++;
                            }
                            if(NOC==2) {
                                if(field.getCards().containsKey(temp.sum(PositionOfAngleCard)) && field.getCards().get(temp.sum(PositionOfAngleCard)).getCardColor()==color_Angle)
                                    score=score+3;
                            }
                            temp=temp.sum(direction);
                        }
                    }
                }
                return score;
            }
        };
    }

    /**
     *
     * @param requiredSymbols
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
