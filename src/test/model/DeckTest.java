package model;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    private Deck deck;
    private FlashCard flashCard;
    private FlashCard flashCard1;

    @BeforeEach
    void runBefore() {
        deck = new Deck("Math");
        flashCard = new FlashCard("2 + 2 = ?", "4");
        flashCard1 = new FlashCard("2 x 3 = ?", "6");
    }

    @Test
    void testConstructor() {
        assertEquals(deck.getName(), "Math");
        assertEquals(deck.numberOfCards(), 0);
    }

    @Test
    void testSetName() {
        deck.setName("Math101");
        assertEquals(deck.getName(), "Math101");
    }

    @Test
    void testAddCard() {
        assertEquals(deck.numberOfCards(), 0);
        FlashCard flashCard = new FlashCard("2 + 2 = ?", "4");
        deck.addCard(flashCard);
        assertEquals(deck.numberOfCards(), 1);
        assertEquals(deck.getCards().get(0), flashCard);
        assertFalse(deck.addCard(flashCard));
    }

    @Test
    void testMultipleAddCard() {
        assertEquals(deck.numberOfCards(), 0);

        deck.addCard(flashCard);
        assertEquals(deck.numberOfCards(), 1);

        deck.addCard(flashCard1);
        assertEquals(deck.numberOfCards(), 2);

        ArrayList<FlashCard> list = new ArrayList<FlashCard>();
        list.add(flashCard);
        list.add(flashCard1);

        assertEquals(deck.getCards(), list);
    }

    @Test
    void removeCard() {

        deck.addCard(flashCard);
        assertFalse(deck.removeCard(flashCard1));
        assertEquals(deck.numberOfCards(), 1);

        deck.addCard(flashCard1);
        assertEquals(deck.numberOfCards(), 2);
        assertTrue(deck.removeCard(flashCard1));
        assertEquals(deck.numberOfCards(), 1);


    }

    @Test
    void removeCardWithFront() {

        deck.addCard(flashCard);
        assertFalse(deck.removeCard(flashCard1.getFront()));
        assertEquals(deck.numberOfCards(), 1);

        deck.addCard(flashCard1);
        assertEquals(deck.numberOfCards(), 2);
        assertTrue(deck.removeCard(flashCard1.getFront()));
        assertEquals(deck.numberOfCards(), 1);

    }

    @Test
    void testGetCards() {
        ArrayList<FlashCard> cards = new ArrayList<>();

        deck.addCard(flashCard);
        cards.add(flashCard);
        assertEquals(deck.getCards(), cards);

        deck.addCard(flashCard1);
        cards.add(flashCard1);
        assertEquals(deck.getCards(), cards);
    }
}


