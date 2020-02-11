package model;

import java.util.ArrayList;

// Represents an account with name that stores decks and flashcards
public class Account {


    //Creates a Deck
    //EFFECTS : sets Account's name and creates a deck's list with a default deck
    public Account(String name) {
        //stub
    }

    //gets account's name
    //EFFECTS : return's account's name
    public String getName() {
        //stub
        return null;
    }

    //changes account's name
    //MODIFIES: THIS
    //EFFECTS : changes Account's name
    public void setName(String name) {
        //stub
    }

    //makes a deck
    //MODIFIES: THIS
    //EFFECTS : gets a String and makes a deck and adds to list of decks if there isn't one with that name
    // and returns true otherwise it will return false
    public boolean makeDeck(String name) {
        //stub
        return false;
    }

    //adds a deck
    //MODIFIES: THIS
    //EFFECTS : gets a Deck adds to list of decks if there isn't one with that name and returns true
    // otherwise it will return false
    public boolean addDeck(Deck deck) {
        //stub
        return false;
    }

    //removes a deck
    //MODIFIES: THIS
    //EFFECTS : removes a Deck from list of decks if deck exists that name and returns true
    // otherwise it will return false
    public boolean removeDeck(Deck deck) {
        //stub
        return false;
    }

    //Gets list of decks
    //EFFECTS : returns list of decks
    public ArrayList<Deck> getDecks() {
        //stub
        return null;
    }

    //Gets number decks in account
    //EFFECTS : return length of list of decks
    public int numberOfDecks() {
        //stub
        return 0;
    }
}
