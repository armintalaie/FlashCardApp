package model;

import java.util.ArrayList;

// Represents a deck with name that stores flashcards
public class Deck {


    //Creates a Deck
    //EFFECTS : sets Deck's name and creates empty cards list
    public Deck(String name) {
        //stub
    }

    //Gets a Deck's name
    //EFFECTS : returns Deck's name
    public String getName() {
        //stub
        return null;
    }

    //Changes the deck's name
    //MODIFIES: THIS
    //EFFECTS : sets Deck's name
    public void setName(String name) {
        //stub
    }

    //Adds a card to deck
    //MODIFIES: THIS
    //EFFECTS : adds flashcard to list of cards
    public void addCard(FlashCard card) {
        //stub
    }

    //Removes a card to deck
    //MODIFIES: THIS
    //EFFECTS : removes flashcard from list of cards
    public boolean removeCard(FlashCard card) {
        //stub
        return false;
    }

    //Gets list of flashcards
    //EFFECTS : returns list of cards
    public ArrayList<FlashCard> getCards() {
        //stub
        return null;
    }

    //Gets number cards in deck
    //EFFECTS : return length of list of cards
    public int numberOfCards() {
        //stub
        return 0;
    }
}
