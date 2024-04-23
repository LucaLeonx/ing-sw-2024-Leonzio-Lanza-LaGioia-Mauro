package it.polimi.ingsw.test.model.card;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Deck;
import static it.polimi.ingsw.model.card.CardColor.*;
import junit.framework.*;

import java.util.*;
import java.util.stream.Collectors;

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

    public void testGetTopCard(){

        assertTrue(deck.getTopCard().isPresent());

        assertTrue(oneCardDeck.getTopCard().isPresent());
        assertEquals(WHITE, oneCardDeck.getTopCard().get().getCardColor());

        assertTrue(emptyDeck.getTopCard().isEmpty());
    }

    public void testGetNumberOfCards(){
        assertEquals(4, deck.getNumberOfCards());
        assertEquals(1, oneCardDeck.getNumberOfCards());
        assertEquals(0, emptyDeck.getNumberOfCards());
    }

    public void testDraw(){

        List<Card> cardList = new ArrayList<>();

        while(!deck.isEmpty()){
            assertTrue(deck.getTopCard().isPresent());
            cardList.add(deck.draw().get());
        }

        assertTrue(deck.isEmpty());
        assertTrue(deck.draw().isEmpty());
        assertEquals(0, deck.getNumberOfCards());
        assertTrue(deck.getTopCard().isEmpty());

        List<Integer> idList = cardList.stream().map(Card::getId).sorted().toList();
        assertEquals(idList, List.of(1,2,3,4));

        if(!oneCardDeck.isEmpty()) {
            Card card = oneCardDeck.draw().get();
            assertTrue(oneCardDeck.getTopCard().isEmpty());
        }

        assertTrue(emptyDeck.draw().isEmpty());
    }
}
