package model;

import model.exceptions.*;

// Represents a flashcard with front and back
public class FlashCard {

    private static int MAX_CARD_LENGTH = 150;
    private String front;
    private String back;

    //Creates a flashcard
    //EFFECTS : stores the front and back in String
    public FlashCard(String front, String back) throws MaxLengthException {
        setFront(front);
        setBack(back);
    }

    //Changes Card's front
    //MODIFIES: THIS
    //EFFECTS : Changes Card's front
    public void setFront(String front) throws MaxLengthException  {
        if (front.length() > MAX_CARD_LENGTH) {
            throw new MaxLengthException();
        }
        this.front = front;

    }

    //Changes Card's back
    //MODIFIES: THIS
    //EFFECTS : Changes Card's back
    public void setBack(String back) throws MaxLengthException {
        if (back.length() > MAX_CARD_LENGTH) {
            throw new MaxLengthException();
        }
        this.back = back;
    }

    //Gets front of a card
    //EFFECTS : returns Card's front
    public String getFront() {
        return this.front;
    }

    //Gets back of a card
    //EFFECTS : returns Card's back
    public String getBack() {
        return this.back;
    }
}
