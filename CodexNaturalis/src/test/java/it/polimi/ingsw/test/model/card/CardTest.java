package it.polimi.ingsw.test.model.card;

import it.polimi.ingsw.model.Requirement;
import it.polimi.ingsw.model.Reward;
import it.polimi.ingsw.model.card.*;
import junit.framework.TestCase;
import org.junit.*;

import java.util.*;

import static it.polimi.ingsw.model.card.AnglePosition.*;
import static it.polimi.ingsw.model.card.AnglePosition.DOWN_LEFT;
import static it.polimi.ingsw.model.card.Symbol.*;
import static it.polimi.ingsw.model.card.Symbol.HIDDEN;
import static java.util.Map.entry;

public class CardTest extends TestCase {

    private Card card;
    public void setUp(){
        CardSide backSide = new CardSide(
                new HashSet<>(),
                Map.ofEntries(
                        entry(UP_RIGHT, BLANK),
                        entry(UP_LEFT, BLANK),
                        entry(DOWN_RIGHT, BLANK),
                        entry(DOWN_LEFT, BLANK)
                ),
                new Requirement(new HashMap<>()),
                new Reward("NONE", GameFunctionFactory.createPointsRewardFunction(0)));
        CardSide frontSide = new CardSide(
                new HashSet<>(List.of(ANIMAL)),
                Map.ofEntries(
                        entry(UP_RIGHT, BLANK),
                        entry(UP_LEFT, ANIMAL),
                        entry(DOWN_RIGHT, QUILL),
                        entry(DOWN_LEFT, BLANK)
                ),
                new Requirement(new HashMap<>(Map.ofEntries(
                        entry(ANIMAL, 2),
                        entry(INSECT, 1)
                ))),
                new Reward("POINTS", GameFunctionFactory.createPointsRewardFunction(3)));

        card = new Card(11, CardColor.SKYBLUE, frontSide, backSide);
    }

    public void testGetId(){
        assertEquals(11, card.getId());
    }

    public void testGetColor(){
        assertEquals(CardColor.SKYBLUE, card.getCardColor());
    }

    public void testGetSide(){
        assertEquals(card.getSide(CardOrientation.BACK).getDisplayedSymbols(), List.of(BLANK, BLANK, BLANK, BLANK));
        assertEquals(card.getSide(CardOrientation.FRONT).getCenterSymbols(), Set.of(ANIMAL));
    }






}
