package model;

import java.util.ArrayList;

// Represents an account with name that stores decks and flashcards
public class Account {

    private String name;
    private ArrayList<Deck> decks;

    //Creates a Deck
    //EFFECTS : sets Account's name and creates a deck's list with a default deck
    public Account(String name) {
        setName(name);
        decks = new ArrayList<>();
        //Deck defaultDeck = new Deck("default");
    }

    //gets account's name
    //EFFECTS : return's account's name
    public String getName() {
        return this.name;
    }

    //changes account's name
    //MODIFIES: THIS
    //EFFECTS : changes Account's name
    public void setName(String name) {
        this.name = name;
    }

    //makes a deck
    //MODIFIES: THIS
    //EFFECTS : gets a String and makes a deck and adds to list of decks if there isn't one with that name
    // and returns true otherwise it will return false
    public boolean makeDeck(String name) {
        for (Deck d : this.decks) {
            if (d.getName().equals(name)) {
                return false;
            }
        }
        Deck deck = new Deck(name);
        this.decks.add(deck);
        return true;
    }

    //adds a deck
    //MODIFIES: THIS
    //EFFECTS : gets a Deck adds to list of decks if there isn't one with that name and returns true
    // otherwise it will return false
    public boolean addDeck(Deck deck) {
        for (Deck d : this.decks) {
            if (d.getName().equals(deck.getName())) {
                return false;
            }
        }
        this.decks.add(deck);
        return true;
    }

    //removes a deck
    //MODIFIES: THIS
    //EFFECTS : removes a Deck from list of decks if deck exists that name and returns true
    // otherwise it will return false
    public boolean removeDeck(Deck deck) {
        for (Deck d : this.decks) {
            if (d.getName().equals(deck.getName())) {
                this.decks.remove(d);
                return true;
            }
        }
        return false;
    }

    //Gets list of decks
    //EFFECTS : returns list of decks
    public ArrayList<Deck> getDecks() {
        return this.decks;
    }

    //Gets number decks in account
    //EFFECTS : return length of list of decks
    public int numberOfDecks() {
        return this.decks.size();
    }

    public Deck findDeck(String deck) {
        for (Deck d : this.decks) {
            if (d.getName().equals(deck)) {
                return d;
            }
        }
        return null;
    }
}
