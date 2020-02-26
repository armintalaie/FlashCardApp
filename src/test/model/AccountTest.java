package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.Reader;
import persistence.Writer;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

class AccountTest {
    private Account account;
    private Deck deck = new Deck("Math");
    private Deck deck1 = new Deck("English");
    private static String saveFile = "data/samplefile.txt";

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

    @Test
    void testSave() {

        try {
            this.account.setName("Joe");
            Deck deck = new Deck("Math");
            deck.addCard(new FlashCard("2+2", "4"));
            deck.addCard(new FlashCard("2+3", "5"));
            deck.addCard(new FlashCard("2+4", "6"));
            account.addDeck(deck);
            deck = new Deck("History");
            deck.addCard(new FlashCard("In what year did India gain its independence from Britain?", "1947"));
            deck.addCard(new FlashCard("In the 1984 vice presidential debates who was George H.W. Bush's opponent?", "Geraldine Ferraro"));
            account.addDeck(deck);
            Writer writer = new Writer(new File(saveFile));
            writer.write(account);
            writer.close();


        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            System.out.println("save file is missing");
            fail();
        }

        try {
            Account accountRead = Reader.readAccounts(new File(saveFile));
            assertEquals(accountRead.getName(), "Joe");
            Deck deck = accountRead.getDecks().get(0);
            assertEquals(deck.getName(), "Math");

            FlashCard flashCard = deck.getCards().get(0);
            assertEquals(flashCard.getFront(), "2+2");
            assertEquals(flashCard.getBack(), "4");
            flashCard = deck.getCards().get(1);
            assertEquals(flashCard.getFront(), "2+3");
            assertEquals(flashCard.getBack(), "5");
            flashCard = deck.getCards().get(2);
            assertEquals(flashCard.getFront(), "2+4");
            assertEquals(flashCard.getBack(), "6");

        } catch (IOException e) {
            System.out.println("Shouldn't have thrown Exception");
            fail();
        }

    }
}



