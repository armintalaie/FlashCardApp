package model;

import java.util.ArrayList;

// Represents a deck with name that stores flashcards
public class Deck {

    private String name;
    private ArrayList<FlashCard> cards;


    //Creates a Deck
    //EFFECTS : sets Deck's name and creates empty cards list
    public Deck(String name) {
        setName(name);
        cards = new ArrayList<>();
    }

    //Gets a Deck's name
    //EFFECTS : returns Deck's name
    public String getName() {
        return this.name;
    }

    //Changes the deck's name
    //MODIFIES: THIS
    //EFFECTS : sets Deck's name
    public void setName(String name) {
        this.name = name;
    }

    //Adds a card to deck
    //MODIFIES: THIS
    //EFFECTS : adds flashcard to list of cards
    public boolean addCard(FlashCard card) {
        if (cards.contains(card)) {
            return false;
        } else {
            cards.add(card);
            return true;
        }
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
        return cards;
    }

    //Gets number cards in deck
    //EFFECTS : return length of list of cards
    public int numberOfCards() {
        //stub
        return 0;
    }
}
