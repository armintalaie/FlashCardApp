package model;

import model.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlashCardTest {
    private FlashCard flashCard;

    @BeforeEach
    void runBefore() {
        try {
            flashCard = new FlashCard("2 + 2 = ?", "4");
        } catch (MaxLengthException e) {
            fail();
        }
    }

    @Test
    void testConstructor() {
        assertEquals(flashCard.getFront(), "2 + 2 = ?");
        assertEquals(flashCard.getBack(), "4");
    }

    @Test
    void testSetFront() {

        try {
            flashCard.setFront("4 x 1 \\= \\?");
            assertEquals(flashCard.getFront(), "4 x 1 \\= \\?");
            assertEquals(flashCard.getBack(), "4");

            String temp = "0123456789";
            String front = "";

            for (int i = 0; i < 15; i++) {
                front += temp;
            }

            flashCard.setFront(front);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testSetFrontAndBackException() {

        String temp = "0123456789";
        String text = "";

        for (int i = 0; i < 15; i++) {
            text += temp;
        }

        text += "a";
        try {
            new FlashCard(text, "a");
            fail();
        } catch (Exception e) {
            // should throw exception
        }

        try {
            new FlashCard("a", text);
            fail();
        } catch (Exception e) {
            // should throw exception
        }

    }

    @Test
    void testSetBack() {
        try {
            flashCard.setBack("4.00");
            assertEquals(flashCard.getFront(), "2 + 2 = ?");
            assertEquals(flashCard.getBack(), "4.00");
        } catch (MaxLengthException e) {
            fail();
        }

    }
}





