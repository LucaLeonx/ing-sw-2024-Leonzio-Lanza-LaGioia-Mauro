package it.polimi.ingsw.test.model.card;

import it.polimi.ingsw.model.card.*;
import junit.framework.TestCase;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CardFactoryTest extends TestCase {
    private Set<Integer> Ids;
    private Set<CardColor> colors;

    public void setUp() {

    }

    public void testInitialCard() throws FileNotFoundException {

        List<Card> initialCards = CardFactory.getInitialCards();
        Card carta;

        for (int i = 0; i < 6; i++) {
            carta = initialCards.get(i);
            System.out.println("\n\nid: " + carta.getId());
            System.out.println("Color: " + carta.getCardColor().toString());
            Set<Symbol> frontCenter = carta.getSide(CardOrientation.valueOf("FRONT")).getCenterSymbols();
            System.out.print("Front center symbols: ");
            for (Symbol s : frontCenter) {
                System.out.print(" " + s.toString());
            }

            List<Symbol> backDisplayedSymbols = carta.getSide(CardOrientation.valueOf("BACK")).getDisplayedSymbols();
            System.out.print("\nAll displayed symbols in the back: ");
            for (Symbol s : backDisplayedSymbols) {
                System.out.print(" " + s.toString());
            }
        }
    }


    public void testResourceCard() throws FileNotFoundException {
        List<Card> resourceCards = CardFactory.getResourceCards();
        Set<Integer> resourceIds = new HashSet<Integer>();
        Set<Integer> actualIds = new HashSet<Integer>();
        Card card;
        for (int i = 1; i <= 40; i++) {
            resourceIds.add(i);
        }
        for(int j = 0; j<40; j++) {
            //resourcePicker.extractRandomElement().ifPresent(card -> pickerIds.add(card.getId()));
            card = resourceCards.get(j);
            actualIds.add(card.getId());
            System.out.println("id: " + card.getId());

        }
        assertEquals(resourceIds.size(), actualIds.size());
        assertTrue(resourceIds.containsAll(actualIds));
    }

    public void testGoldCard() throws FileNotFoundException {
        List<Card> goldCards = CardFactory.getGoldCards();
        Set<Integer> goldIds = new HashSet<Integer>();
        Set<Integer> actualIds = new HashSet<Integer>();
        Card card;
        for (int i = 41; i <= 80; i++) {
            goldIds.add(i);
        }
        for(int j = 0; j<40; j++) {
            //resourcePicker.extractRandomElement().ifPresent(card -> pickerIds.add(card.getId()));
            card = goldCards.get(j);
            actualIds.add(card.getId());
            System.out.println("id: " + card.getId());

        }
        assertEquals(goldIds.size(), actualIds.size());
        assertTrue(goldIds.containsAll(actualIds));
    }

    public void testObjectiveCard() throws FileNotFoundException {
        List<ObjectiveCard> objCards = CardFactory.getObjectiveCards();
        Set<Integer> Ids = new HashSet<Integer>();
        Set<Integer> actualIds = new HashSet<Integer>();
        ObjectiveCard card;
        for (int i = 87; i <= 102; i++) {
            Ids.add(i);
        }
        for (int j = 0; j < 16; j++) {
            //resourcePicker.extractRandomElement().ifPresent(card -> pickerIds.add(card.getId()));
            card = objCards.get(j);
            actualIds.add(card.getId());
            System.out.println("id: " + card.getId());

        }
        assertEquals(Ids.size(), actualIds.size());
        assertTrue(Ids.containsAll(actualIds));
    }
}
