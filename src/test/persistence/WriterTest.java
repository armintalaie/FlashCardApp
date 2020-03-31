package persistence;


import model.exceptions.*;
import model.Account;
import model.Deck;
import model.FlashCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;

public class WriterTest {
    private Account account;
    private Writer testWriter;
    private static String TEST_FILE = "./data/testWrite.txt";

    @BeforeEach
    void runBefore() throws FileNotFoundException, UnsupportedEncodingException {
        testWriter = new Writer(new File(TEST_FILE));
        account = new Account("Jack");

        Deck deck = new Deck("Math");
        try {
            deck.addCard(new FlashCard("2+2", "4"));
            deck.addCard(new FlashCard("2+3", "5"));
            deck.addCard(new FlashCard("2+4", "6"));
            account.addDeck(deck);

            deck = new Deck("History");
            deck.addCard(new FlashCard("In what year did India gain its independence from Britain?", "1947"));
            deck.addCard(new FlashCard("In the 1984 vice presidential debates who was George H.W. Bush's opponent?", "Geraldine Ferraro"));
            account.addDeck(deck);
        } catch (MaxLengthException e) {
        }


    }

    @Test
    void testWrite() {

        testWriter.write(account);
        testWriter.close();

        try {
            Account accountRead = Reader.readAccounts(new File(TEST_FILE));
            assertEquals(accountRead.getName(), "Jack");

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

            deck = accountRead.getDecks().get(1);
            assertEquals(deck.getName(), "History");

            flashCard = deck.getCards().get(0);
            assertEquals(flashCard.getFront(), "In what year did India gain its independence from Britain?");
            assertEquals(flashCard.getBack(), "1947");
            flashCard = deck.getCards().get(1);
            assertEquals(flashCard.getFront(), "In the 1984 vice presidential debates who was George H.W. Bush's opponent?");
            assertEquals(flashCard.getBack(), "Geraldine Ferraro");

        } catch (IOException e) {
            System.out.println("Shouldn't have thrown Exception");
        }
    }
}
