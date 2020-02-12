package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class AccountTest {
    private Account account;
    private Deck deck = new Deck("Math");
    private Deck deck1 = new Deck("English");

    @BeforeEach
    void runBefore() {
        account = new Account("Armin");
    }

    @Test
    void testConstructor() {
        assertEquals(account.getName(), "Armin");
    }

    @Test
    void testSetName() {
        account.setName("Jack");
        assertEquals(account.getName(), "Jack");
    }

    @Test
    void makeDeck() {
        assertEquals(account.numberOfDecks(), 0);
        assertTrue(account.makeDeck("Physics"));
        assertEquals(account.numberOfDecks(), 1);

        assertFalse(account.makeDeck("Physics"));
        assertEquals(account.numberOfDecks(), 1);

        assertTrue(account.makeDeck("Chemistry"));
        assertEquals(account.numberOfDecks(), 2);
    }

    @Test
    void testAddDeck() {
        assertTrue(account.addDeck(new Deck("Chemistry")));
        assertEquals(account.numberOfDecks(), 1);
        assertFalse(account.addDeck(new Deck("Chemistry")));
    }

    @Test
    void testRemoveDeck() {


        account.addDeck(deck);
        assertEquals(account.numberOfDecks(), 1);

        account.removeDeck(deck);
        assertEquals(account.numberOfDecks(), 0);

        account.addDeck(deck1);
        assertEquals(account.numberOfDecks(), 1);

        account.removeDeck(deck);
        assertEquals(account.numberOfDecks(), 1);
    }

    @Test
    void TestGetDecks() {
        ArrayList<Deck> decks = new ArrayList<>();
        decks.add(deck);
        account.addDeck(deck);
        assertEquals(account.getDecks(), decks);

        account.addDeck(deck1);
        decks.add(deck1);
        assertEquals(account.getDecks(), decks);
    }

    @Test
    void TestFindDeck() {
        account.addDeck(deck);
        assertEquals(account.findDeck(deck.getName()), deck);

        assertNull(account.findDeck(deck1.getName()));
    }

}



