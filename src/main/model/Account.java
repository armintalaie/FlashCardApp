package model;

import persistence.Reader;
import persistence.Saveable;

import java.io.PrintWriter;
import java.util.ArrayList;


// Represents an account with name that stores decks and flashcards
public class Account implements Saveable {
    private static int NAME_ID = 2;
    private static int DECK_ID = 0;
    private static int CARD_ID = 1;
    private String name;
    private ArrayList<Deck> decks;

    //creates an account
    //EFFECTS : sets account name and makes and empty list for decks
    public Account(String name) {
        setName(name);
        decks = new ArrayList<>();
    }

    //gets account name
    //EFFECTS : returns account name
    public String getName() {
        return this.name;
    }

    //sets account name
    //MODIFIES : THIS
    //EFFECTS : sets account name
    public void setName(String name) {
        this.name = name;
    }


    //Create deck with given name
    //MODIFIES : THIS
    //EFFECTS : return false if a deck with that name exists. otherwise it will make a deck with that name and add
    // it to the list and return true
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


    //Adds deck to account
    //MODIFIES : THIS
    //EFFECTS : return false if a deck with that name exists. otherwise it will add the deck
    // to the list and return true
    public boolean addDeck(Deck deck) {
        Deck d = findDeck(deck.getName());
        if (d == null) {
            this.decks.add(deck);
            return true;
        }
        return false;
    }

    //Removes deck from account
    //MODIFIES : THIS
    //EFFECTS : return false if a deck with that name doesn't exists. otherwise it will remove the deck from list
    public boolean removeDeck(Deck deck) {
        Deck d = findDeck(deck.getName());
        if (d != null) {
            this.decks.remove(d);
            return true;
        }
        return false;
    }

    //gets decks from account
    //EFFECTS : returns list decks in account
    public ArrayList<Deck> getDecks() {
        return this.decks;
    }

    //removes number decks in account
    //EFFECTS : returns size of the deck list in account
    public int numberOfDecks() {
        return this.decks.size();
    }

    //removes deck from account
    //MODIFIES : THIS
    //EFFECTS : return false if a deck with that name doesn't exists. otherwise it will remove the deck from list
    public Deck findDeck(String name) {
        for (Deck d : decks) {
            if (d.getName().equals(name)) {
                return d;
            }
        }
        return null;
    }

    //Saves account
    //EFFECTS : saves accounts name, decks and flashcards to printwriter
    @Override
    public void save(PrintWriter printWriter) {
        printWriter.print(NAME_ID);
        printWriter.print(Reader.DELIMITER);
        printWriter.println(this.name);

        for (Deck deck : this.decks) {
            printWriter.print(DECK_ID);
            printWriter.print(Reader.DELIMITER);
            printWriter.println(deck.getName());

            for (FlashCard card : deck.getCards()) {
                printWriter.print(CARD_ID);
                printWriter.print(Reader.DELIMITER);
                printWriter.print(card.getFront());
                printWriter.print(Reader.DELIMITER);
                printWriter.println(card.getBack());
            }
        }


    }
}
