package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FlashCardTest {
    private FlashCard flashCard;

    @BeforeEach
    void runBefore() {
        flashCard = new FlashCard("2 + 2 = ?", "4");
    }

    @Test
    void testConstructor() {
        assertEquals(flashCard.getFront(), "2 + 2 = ?");
        assertEquals(flashCard.getBack(), "4");
    }

    @Test
    void testSetFront() {
        flashCard.setFront("4 x 1 = ?");
        assertEquals(flashCard.getFront(), "4 x 1 = ?");
        assertEquals(flashCard.getBack(), "4");
    }

    @Test
    void testSetBack() {
        flashCard.setBack("4.00");
        assertEquals(flashCard.getFront(), "2 + 2 = ?");
        assertEquals(flashCard.getBack(), "4.00");
    }
}





