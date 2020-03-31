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
        if (null != findCard(card.getFront())) {
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
        if (cards.contains(card)) {
            this.cards.remove(card);
            return true;
        } else {
            return false;
        }
    }

    // remove card
    // MODIFIES: THIS
    // EFFECTS: removes card with front as its front and returns true. if card doesn't exist return false
    public boolean removeCard(String front) {
        FlashCard card = findCard(front);
        if (card != null) {
            removeCard(card);
            return true;
        }
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
        return cards.size();
    }

    // finds card
    // EFFECTS: returns card with front as its front. if it doesn't exist, null
    public FlashCard findCard(String front) {
        for (FlashCard card : this.cards) {
            if (card.getFront().equals(front)) {
                return card;
            }
        }
        return null;
    }

    // finds card with back text
    // EFFECTS: returns card with back as its back text and null if doesn't exist
    public FlashCard findCardWithBack(String back) {
        for (FlashCard card : this.cards) {
            if (card.getBack().equals(back)) {
                return card;
            }
        }
        return null;
    }
}

