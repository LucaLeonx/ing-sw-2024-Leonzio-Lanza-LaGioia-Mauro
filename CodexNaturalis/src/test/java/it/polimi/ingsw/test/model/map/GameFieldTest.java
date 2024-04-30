package it.polimi.ingsw.test.model.map;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.map.AngleCell;
import it.polimi.ingsw.model.map.CardCell;
import it.polimi.ingsw.model.map.GameField;
import it.polimi.ingsw.model.map.Point;
import junit.framework.TestCase;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class GameFieldTest extends TestCase {

    private List<Card> resourceCards;
    private List<Card> goldenCards;
    private List<Card> initialCards;

    private GameField emptyField;
    private GameField diagonalField;
    private GameField negativeDiagonalField;

    public void setUp(){
        try {
            resourceCards = CardFactory.getResourceCards();
            goldenCards = CardFactory.getGoldCards();
            initialCards = CardFactory.getInitialCards();
        } catch (Exception e){
            e.printStackTrace();
            fail();
        }

        emptyField = new GameField();
        diagonalField = new GameField();
        diagonalField.placeCard(resourceCards.get(0), CardOrientation.BACK, new Point(2,2));
        diagonalField.placeCard(resourceCards.get(1), CardOrientation.BACK, new Point(4,4));
        diagonalField.placeCard(resourceCards.get(2), CardOrientation.BACK, new Point(6,6));

    }

    private void checkInvariants(GameField field){
        Map<Point, AngleCell> angles = field.getAngleCells();
        Map<Point, CardCell> cards = field.getCardCells();

        assertTrue(checkAllAnglesCoordinatesAreOdd(field));
        assertTrue(checkAllCardsCoordinatesAreEven(field));
        assertTrue(checkAllAnglesExists(field));
        assertTrue(checkAllAnglesSymbolsAreCoherent(field));
        assertTrue(checkAllAnglesAttachOppositeCards(field));
    }

    private boolean checkAllCardsCoordinatesAreEven(GameField field){
        return field.getCardCells().keySet().stream()
                .allMatch((position) -> (Math.abs(position.x()) % 2 == 0 && Math.abs(position.y()) % 2 == 0));
    }

    private boolean checkAllAnglesCoordinatesAreOdd(GameField field){
        return field.getAngleCells().keySet().stream()
                .allMatch((position) -> (Math.abs(position.x()) % 2 == 1 && Math.abs(position.y()) % 2 == 1));
    }

    private boolean checkAllAnglesExists(GameField field){
        return field.getCardCells().keySet().stream()
                .flatMap((point) -> Point.getAdjacentPositions(point).stream())
                .allMatch((angle) -> field.getAnglesSymbols().get(angle) != null);
    }

    private boolean checkAllAnglesAttachOppositeCards(GameField field){
        return field.getAngleCells().entrySet().stream()
                .allMatch((entry) -> {
                    Point anglePosition = entry.getKey();
                    Point topCardPosition = entry.getValue().topCardPosition();
                    Point bottomCardPosition = entry.getValue().bottomCardPosition();

                    return(topCardPosition.equals(bottomCardPosition) ||
                            topCardPosition.sum(anglePosition.inverse()).equals(anglePosition.sum(bottomCardPosition.inverse())));

                }
                );
    }

    private boolean checkAllAnglesSymbolsAreCoherent(GameField field){
        for(Map.Entry<Point, CardCell> pointCardCellEntry : field.getCardCells().entrySet()){

            Point cardPosition = pointCardCellEntry.getKey();
            CardSide cardSide = pointCardCellEntry.getValue().visibleCardSide();

            for(AnglePosition angle : AnglePosition.values()) {

                Point adjacentAngle = cardPosition.sum(angle.getRelativePosition());
                AngleCell adjacentAngleCell = field.getAngleCells().get(adjacentAngle);

                // All AngleCell should exist
                if (adjacentAngleCell == null) {
                    return false;
                }

                // Each symbol on an angle of a card should be recorded in the AngleCell,
                // either as topSymbol or bottomSymbol
                if (adjacentAngleCell.topCardPosition().equals(cardPosition)) {
                    if (adjacentAngleCell.topSymbol() != cardSide.getSymbolFromAngle(angle)) {
                        return false;
                    }
                } else if (adjacentAngleCell.bottomCardPosition().equals(cardPosition)) {
                    if (adjacentAngleCell.bottomSymbol() != cardSide.getSymbolFromAngle(angle)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public void testGetters(){
        GameField field = new GameField();
        field.placeCard(initialCards.get(2), CardOrientation.FRONT, new Point(0,0));
        field.placeCard(resourceCards.get(18), CardOrientation.FRONT, new Point(2, -2));
        field.placeCard(resourceCards.get(11), CardOrientation.FRONT, new Point(4, -4));
        field.placeCard(resourceCards.get(12), CardOrientation.FRONT, new Point(2, 2));

        checkInvariants(field);

        RewardFunction rewardFunction = GameFunctionFactory.createDiagonalPatternMatchFunction(true, CardColor.SKYBLUE);
        assertEquals(2, rewardFunction.getPoints(field));
    }

    public void testFieldConstruction(){
        checkInvariants(emptyField);
        checkInvariants(diagonalField);
    }

    public void testDiagonalPattern(){
        RewardFunction patternReward = GameFunctionFactory.createDiagonalPatternMatchFunction(true, CardColor.RED);
        assertEquals(patternReward.getPoints(diagonalField), 2);
    }



    public void testDiagonalPattern2(){

        GameField field = new GameField();

        field.placeCard(initialCards.get(2), CardOrientation.FRONT, new Point(0,0));
        field.placeCard(resourceCards.get(18), CardOrientation.FRONT, new Point(2, -2));
        field.placeCard(resourceCards.get(11), CardOrientation.FRONT, new Point(4, -4));
        field.placeCard(resourceCards.get(12), CardOrientation.FRONT, new Point(6, -6));
        field.placeCard(goldenCards.get(19), CardOrientation.FRONT, new Point(4, 0));
        field.placeCard(resourceCards.get(20), CardOrientation.FRONT, new Point(-2, -2));
        field.placeCard(resourceCards.get(24), CardOrientation.FRONT, new Point(-4, -4));
        field.placeCard(resourceCards.get(35), CardOrientation.FRONT, new Point(-2, 2));
        field.placeCard(goldenCards.get(31), CardOrientation.FRONT, new Point(0, 4));
        field.placeCard(resourceCards.get(39), CardOrientation.FRONT, new Point(-2, 6));
        field.placeCard(goldenCards.get(36), CardOrientation.BACK, new Point(2, 2));
        field.placeCard(goldenCards.get(25), CardOrientation.FRONT, new Point(-6, -6));
        field.placeCard(goldenCards.get(33), CardOrientation.FRONT, new Point(-4, 4));
        field.placeCard(resourceCards.get(29), CardOrientation.FRONT, new Point(-8, -8));

        checkInvariants(field);

        RewardFunction rewardFunction = GameFunctionFactory.createDiagonalPatternMatchFunction(true, CardColor.SKYBLUE);
        assertEquals(2, rewardFunction.getPoints(field));
    }


}
