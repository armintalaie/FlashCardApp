package ui;

import model.Account;
import model.Deck;
import model.FlashCard;

import java.util.ArrayList;
import java.util.Scanner;

public class FlashCardApp {
    private Account account;
    private Scanner scanner = new Scanner(System.in);

    public FlashCardApp() {
        runApp();
    }

    private void runApp() {
        System.out.println("Hello; To create an account type your name");
        account = new Account(scanner.next());
        boolean keepGoing = true;

        while (keepGoing) {
            String command = scanner.nextLine().toLowerCase();
            System.out.println(command);
            switch (command) {
                case "create deck": {
                    createDeck();
                    break;
                }
                case "create flashcard": {
                    creatFlashCard();
                    break;
                }
                case "decks": {
                    deckManagement();
                    break;
                }
                case "quit": {
                    keepGoing = false;
                }
            }
        }
    }

    private void deckManagement() {
        printAllDecks(account);
        boolean keepGoing = true;

        while (keepGoing) {
            String command = scanner.nextLine();
            System.out.println(command);
            Deck deck = account.findDeck(scanner.next());
            System.out.println(deck);

            if (deck != null) {
                System.out.println("not null");
                showAllCards(deck.getCards());
                System.out.println(deck.getCards().size() + "ffff");
                keepGoing = false;
            } else {
                System.out.println("mmmm");
            }
        }

    }

    private void showAllCards(ArrayList<FlashCard> cards) {
        System.out.println(cards.size() + "popo");
        for (FlashCard card : cards) {
            System.out.println("kdkdkdkd");
            System.out.println(card.getFront());
        }

    }

    private void creatFlashCard() {
        boolean keepGoing = true;
        System.out.println("Enter Flashcard's Front");
        String front = scanner.next();
        System.out.println("Enter Flashcard's back");
        String back = scanner.next();
        FlashCard flashCard = new FlashCard(front, back);
        System.out.println("card successfully made. which deck do you want ot add it to?");
        while (keepGoing) {
            printAllDecks(account);
            Deck deck = account.findDeck(scanner.next());

            if (deck != null) {
                if (deck.addCard(flashCard)) {
                    keepGoing = false;
                    System.out.println("Flashcard successfully added to " + deck.getName());
                } else {
                    System.out.println("Flashcard exists. choose another deck");
                }
            } else {
                System.out.println("No deck with that name. try again");
            }
        }
    }

    private void createDeck() {
        System.out.println("Enter Deck's Name");
        String deckName = scanner.next();
        if (account.makeDeck(deckName)) {
            System.out.println("Deck successfully made");
        } else {
            System.out.println("Deck with same name already exists");
        }
    }

    private void printAllDecks(Account account) {
        ArrayList<Deck> decks = account.getDecks();

        for (Deck d : decks) {
            System.out.println(d.getName());
        }
    }
}


