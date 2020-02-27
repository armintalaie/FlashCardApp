package persistence;

import model.Account;
import model.Deck;
import model.FlashCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

public class ReaderTest {
    private static String saveFile = "data/samplefile2";

    @Test
    void testReaderConstructor(){
        new Reader();
        // according to the @637 piazza
    }

    @Test
    void parseContent() {
        try {
            Account accountRead = Reader.readAccounts(new File(saveFile));
            assertEquals(accountRead.getName(), "Joe");

            Deck deck = accountRead.getDecks().get(0);
            assertEquals(deck.getName(), "Math");

            FlashCard flashCard = deck.getCards().get(0);
            assertEquals(flashCard.getFront(), "2*2");
            assertEquals(flashCard.getBack(), "4");
            flashCard = deck.getCards().get(1);
            assertEquals(flashCard.getFront(), "2*3");
            assertEquals(flashCard.getBack(), "6");
            flashCard = deck.getCards().get(2);
            assertEquals(flashCard.getFront(), "2*4");
            assertEquals(flashCard.getBack(), "8");
            flashCard = deck.getCards().get(3);
            assertEquals(flashCard.getFront(), "2*5");
            assertEquals(flashCard.getBack(), "10");

            deck = accountRead.getDecks().get(1);
            assertEquals(deck.getName(), "French");

            flashCard = deck.getCards().get(0);
            assertEquals(flashCard.getFront(), "bonjour");
            assertEquals(flashCard.getBack(), "hello");
            flashCard = deck.getCards().get(1);
            assertEquals(flashCard.getFront(), "livre");
            assertEquals(flashCard.getBack(), "book");
            flashCard = deck.getCards().get(2);
            assertEquals(flashCard.getFront(), "au revoir");
            assertEquals(flashCard.getBack(), "goodbye");
            flashCard = deck.getCards().get(3);
            assertEquals(flashCard.getFront(), "anglais");
            assertEquals(flashCard.getBack(), "english");

        } catch (IOException e) {
            System.out.println("Shouldn't have thrown Exception");
        }

    }
}



