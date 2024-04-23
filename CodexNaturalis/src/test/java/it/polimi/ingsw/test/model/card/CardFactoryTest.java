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

        RandomPicker<Card> initalCardPicker = CardFactory.getInitialCards();
        Optional<Card> Optcarta;
        Card carta;

        while (!initalCardPicker.isEmpty()) {
            Optcarta = initalCardPicker.extractRandomElement();
            if (Optcarta.isPresent()) {
                carta = Optcarta.get();
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
    }

    public void testResourceCard() throws FileNotFoundException {
        RandomPicker<Card> resourcePicker = CardFactory.getResourceCards();
        Set<Integer> resourceIds = new HashSet<Integer>();
        Set<Integer> pickerIds = new HashSet<Integer>();
        for (int i = 1; i < 40; i++) {
            resourceIds.add(i);
        }
        Optional<Card> Optcarta;
        Card carta;
        while (!resourcePicker.isEmpty()) {
            //resourcePicker.extractRandomElement().ifPresent(card -> pickerIds.add(card.getId()));
            Optcarta = resourcePicker.extractRandomElement();
            if (Optcarta.isPresent()) {
                carta = Optcarta.get();
                System.out.println("id: " + carta.getId());
            }
        }
        //assertEquals(resourceIds.size(), pickerIds.size());
        //assertTrue(resourceIds.containsAll(pickerIds));
    }
}
