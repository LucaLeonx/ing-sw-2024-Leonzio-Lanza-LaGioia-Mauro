package it.polimi.ingsw.test.model.card;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Deck;
import static it.polimi.ingsw.model.card.CardColor.*;
import junit.framework.*;

import java.util.*;
public class DeckTest extends TestCase{

    private Deck deck;
    private Deck oneCardDeck;
    private Deck emptyDeck;
    public void setUp(){
        deck = new Deck(List.of(
                new Card(1, RED, null, null),
                new Card(2, SKYBLUE, null, null),
                new Card(3, GREEN, null, null),
                new Card(4, GREEN, null, null)
        ));

        oneCardDeck = new Deck(List.of(new Card(5, WHITE, null, null)));
        emptyDeck = new Deck(new HashSet<>());
    }

    public void testIsEmpty(){
        assertFalse(deck.isEmpty());
        assertFalse(oneCardDeck.isEmpty());
        assertTrue(emptyDeck.isEmpty());
    }
}
