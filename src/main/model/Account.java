package model;

import persistence.Reader;
import persistence.Saveable;

import javax.print.DocFlavor;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Account implements Saveable {
    private static int NAME_ID = 2;
    private static int DECK_ID = 0;
    private static int CARD_ID = 1;
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
