package model;

import java.util.ArrayList;

public class Account {
    private String name;
    private ArrayList<Deck> decks;

    public Account(String name) {
        setName(name);
        decks = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean makeDeck(String name) {
        for (Deck d : decks) {
            if (d.getName().equals(name)) {
                return false;
            }
        }
        Deck deck = new Deck(name);
        this.decks.add(deck);
        return true;
    }

    public boolean addDeck(Deck deck) {
        Deck d = findDeck(deck.getName());
        if (d == null) {
            this.decks.add(deck);
            return true;
        }
        return false;
    }

    public boolean removeDeck(Deck deck) {
        Deck d = findDeck(deck.getName());
        if (d != null) {
            this.decks.remove(d);
            return true;
        }
        return false;
    }

    public ArrayList<Deck> getDecks() {
        return this.decks;
    }

    public int numberOfDecks() {
        return this.decks.size();
    }

    public Deck findDeck(String name) {
        for (Deck d : decks) {
            if (d.getName().equals(name)) {
                return d;
            }
        }
        return null;
    }
}
