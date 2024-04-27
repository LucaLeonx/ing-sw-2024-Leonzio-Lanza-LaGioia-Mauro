package it.polimi.ingsw.test.model.card;


import it.polimi.ingsw.model.card.AnglePosition;
import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.card.GameFunctionFactory;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.map.GameField;
import junit.framework.TestCase;

import java.util.*;

import static java.util.Map.entry;
import static it.polimi.ingsw.model.card.Symbol.*;
import static it.polimi.ingsw.model.card.AnglePosition.*;

public class CardSideTest extends TestCase {

    private CardSide resourceCardSide;
    private CardSide initialCardSide;
    private CardSide goldenCardSide;

    public void setUp(){
           resourceCardSide = new CardSide(
                   new HashSet<>(),
                   Map.ofEntries(
                           entry(UP_RIGHT, BLANK),
                           entry(UP_LEFT, BLANK),
                           entry(DOWN_RIGHT, BLANK),
                           entry(DOWN_LEFT, BLANK)
                   ),
                   GameFunctionFactory.createRequiredSymbolsFunction(new HashMap<>()),
                   GameFunctionFactory.createPointsRewardFunction(0));

           initialCardSide = new CardSide(new HashSet<>(List.of(FUNGI, PLANT, ANIMAL)),
                   Map.ofEntries(
                        entry(UP_RIGHT, BLANK),
                        entry(UP_LEFT, BLANK),
                        entry(DOWN_RIGHT, HIDDEN),
                        entry(DOWN_LEFT, HIDDEN)
        ),
                GameFunctionFactory.createRequiredSymbolsFunction(new HashMap<>()),
                GameFunctionFactory.createPointsRewardFunction(0));

        goldenCardSide = new CardSide(
                new HashSet<>(List.of(ANIMAL)),
                Map.ofEntries(
                        entry(UP_RIGHT, BLANK),
                        entry(UP_LEFT, INKWELL),
                        entry(DOWN_RIGHT, QUILL),
                        entry(DOWN_LEFT, BLANK)
                ),
                GameFunctionFactory.createRequiredSymbolsFunction(new HashMap<>(Map.ofEntries(
                        entry(FUNGI, 1)
                ))),
                GameFunctionFactory.createPointsRewardFunction(7));
    }

    public void testGetCenterSymbols(){
        assertEquals(resourceCardSide.getCenterSymbols(), new HashSet<>());
        assertEquals(initialCardSide.getCenterSymbols(), new TreeSet<>(List.of(ANIMAL, FUNGI, PLANT)));
        assertEquals(goldenCardSide.getCenterSymbols(), new HashSet<>(List.of(ANIMAL)));
    }

    public void testGetSymbolsFromAngle(){
        for(AnglePosition angle : AnglePosition.values()) {
            assertEquals(resourceCardSide.getSymbolFromAngle(angle), BLANK);
        }

        assertEquals(initialCardSide.getSymbolFromAngle(DOWN_RIGHT), HIDDEN);

        assertEquals(goldenCardSide.getSymbolFromAngle(UP_LEFT), INKWELL);
        assertEquals(goldenCardSide.getSymbolFromAngle(DOWN_RIGHT), QUILL);
    }

    public void testGetDisplayedSymbols(){
        Comparator<Symbol> symbolComparator = Comparator.comparing(Enum::toString);

        List<Symbol> resourceSymbols = resourceCardSide.getDisplayedSymbols();
        List<Symbol> initialSymbols = initialCardSide.getDisplayedSymbols();
        List<Symbol> goldenSymbols = goldenCardSide.getDisplayedSymbols();

        // Just to see how the returned lists are sorted. It is actually insertion order
        // System.out.println(goldenSymbols);

        initialSymbols.sort(symbolComparator);
        goldenSymbols.sort(symbolComparator);

        assertEquals(List.of(BLANK, BLANK, BLANK, BLANK), resourceSymbols);
        assertEquals(List.of(ANIMAL, BLANK, BLANK, FUNGI, HIDDEN, HIDDEN, PLANT), initialSymbols);
        assertEquals(List.of(ANIMAL, BLANK, BLANK, INKWELL, QUILL), goldenSymbols);
    }

    public void testGetRequirementFunction(){

        GameField field = new GameField(); // Empty GameField just to test the getters

        assertTrue(resourceCardSide.getPlayingRequirements().isSatisfied(field));
        assertEquals(resourceCardSide.getPlayingReward().getPoints(field), 0);

        assertFalse(goldenCardSide.getPlayingRequirements().isSatisfied(field));
        assertEquals(goldenCardSide.getPlayingReward().getPoints(field), 7);
    }
}
