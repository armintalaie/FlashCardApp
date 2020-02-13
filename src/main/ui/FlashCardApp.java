package ui;

import model.Account;
import model.Deck;
import model.FlashCard;

import java.util.ArrayList;
import java.util.Scanner;


// Flash Card Application
public class FlashCardApp {
    private Account account;
    private Scanner scanner = new Scanner(System.in);

    // EFFECTS: runs runApp() function
    public FlashCardApp() {
        runApp();
    }

    // MODIFIES: this
    // EFFECTS: creates account with input as name an runs the app to read input
    private void runApp() {
        System.out.println("Hello\nTo create an account type your name");
        account = new Account(scanner.next());
        boolean keepGoing = true;
        possibleCommands();

        while (keepGoing) {
            String command = scanner.nextLine().toLowerCase();
            keepGoing = readCommand(command);

        }
    }

    // EFFECTS: prints possible commands you can type
    private void possibleCommands() {
        System.out.println("Options:");
        System.out.println("create deck");
        System.out.println("create flashcard");
        System.out.println("decks");
        System.out.println("quit");

    }

    // EFFECTS: calls the right method based on the input
    private boolean readCommand(String command) {
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
                allDecksManagement();
                break;
            }
            case "quit": {
                System.out.println("bye " + account.getName());
                return false;
            }
        }
        return true;
    }

    // MODIFIES: this
    // EFFECTS: deletes deck of choosing if remove is typed or finds a deck
    private void allDecksManagement() {
        while (true) {
            printDeckOptions();
            String command = scanner.nextLine();
            if (command.contains("back")) {
                return;
            }
            if (command.contains("remove")) {
                if (!account.removeDeck(account.findDeck(command.replace("remove ", "")))) {
                    System.out.println("no such deck");
                    continue;
                } else if (account.numberOfDecks() == 0) {
                    System.out.println("No more decks; will return to main menu");
                } else {
                    continue;
                }
            }
            Deck deck = account.findDeck(command);
            if (deck != null) {
                singleDeckManagement(deck);
            } else {
                System.out.println("no such deck");
            }
        }
    }


    // EFFECTS: prints a message and all decks
    private void printDeckOptions() {
        System.out.println("options : type remove + deck name to remove it or the deck name to see the answer");
        printAllDecks(account);
    }

    // REQUIRES : deck is not null
    // MODIFIES: this
    // EFFECTS: deletes deck of choosing if remove is typed or finds a deck
    private void singleDeckManagement(Deck deck) {

        while (true) {
            showAllCards(deck.getCards());
            System.out.println("options : type remove + card name to remove it or cards to select one");
            String command = scanner.nextLine();

            if (command.contains("remove")) {
                if (!deck.removeCard(command.replace("remove ", ""))) {
                    System.out.println("no such card");
                } else if (deck.numberOfCards() == 0) {
                    System.out.println("No more cards; will return to decks menu");
                    allDecksManagement();
                }
            } else if (command.contains("choose")) {
                chooseFlashCard(deck);
            } else if (command.contains("back")) {
                return;
            }
        }
    }


    // REQUIRES : deck is not null
    // EFFECTS: prints all cards in deck and by typing a correct card you can see an answer
    // otherwise you will get an error message
    private void chooseFlashCard(Deck deck) {

        showAllCards(deck.getCards());
        String command = scanner.nextLine();
        while (true) {

            if (command.equals("back")) {
                return;
            }
            FlashCard card = deck.findCard(command);

            if (card != null) {
                System.out.println("Front: " + card.getFront() + "\t Back: " + card.getBack());
            } else {
                System.out.println("no card with that front exists");
                return;
            }

            command = scanner.nextLine();
        }
    }

    // EFFECTS: prints all cards in list
    private void showAllCards(ArrayList<FlashCard> cards) {
        for (FlashCard card : cards) {
            System.out.println(card.getFront());
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a flashcard from inputs for front and back and adds them to a previously created deck
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


    // MODIFIES: this
    // EFFECTS: creates deck with name from input, if one with that name exists, it will show an error message
    private void createDeck() {
        System.out.println("Enter Deck's Name");
        String deckName = scanner.next();
        if (account.makeDeck(deckName)) {
            System.out.println("Deck successfully made");
        } else {
            System.out.println("Deck with same name already exists");
        }
    }

    // EFFECTS: prints the name of all decks currently active
    private void printAllDecks(Account account) {
        ArrayList<Deck> decks = account.getDecks();

        for (Deck d : decks) {
            System.out.println(d.getName());
        }
    }
}


