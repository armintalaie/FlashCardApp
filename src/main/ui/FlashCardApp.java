package ui;

import model.Account;
import model.Deck;
import model.FlashCard;
import persistence.Reader;
import persistence.Writer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


// Flash Card Application
public class FlashCardApp {
    // TODO: fix main menu to either load or create and account

    private static String SAVE_PATH = "data/samplefile2";
    private static String SAVE_FILE = "data/samplefile.txt";
    private Account account;
    private Scanner scanner = new Scanner(System.in);
    private HashMap<String, Integer> commands;

    // EFFECTS: runs runApp() function
    public FlashCardApp() {
        commands = new HashMap<>();
        commandToNumberRef(commands);
        runApp();
    }

    // MODIFIES: this
    // EFFECTS: creates account with input as name an runs the app to read input
    private void runApp() {
        System.out.println("Hello\nTo create an account type your name");
        account = new Account(scanner.nextLine());
        boolean keepGoing = true;
        possibleCommands();

        while (keepGoing) {
            String command = scanner.nextLine().toLowerCase();
            try {
                keepGoing = readCommand(commands.get(command));
            } catch (NullPointerException e) {
                System.out.println("wrong command");
                possibleCommands();
            }

        }
    }

    private void commandToNumberRef(HashMap<String, Integer> commands) {
        commands.put("create deck", 0);
        commands.put("create flashcard", 1);
        commands.put("deck", 2);
        commands.put("decks", 3);
        commands.put("load", 4);
        commands.put("save", 5);
        commands.put("quit", 6);
    }

    // EFFECTS: prints possible commands you can type
    private void possibleCommands() {
        System.out.println("Options:");
        System.out.println("create deck");
        System.out.println("create flashcard");
        System.out.println("decks");
        System.out.println("load");
        System.out.println("save");
        System.out.println("quit");

    }

    // TODO: break down readCommand method into smaller methods
    // EFFECTS: calls the right method based on the input
    private boolean readCommand(int command) {

        if (command == 0) {
            createDeck();
        } else if (command == 1) {
            creatFlashCard();
        } else if (command == 2) {
            allDecksManagement();
        } else if (command == 3) {
            System.out.println("bye " + account.getName());
            return false;
        } else if (command == 4) {
            loadAccount();
        } else if (command == 5) {
            saveAccount(account);
        }
        return true;
    }

    private void saveAccount(Account account) {

        try {
            Writer writer = new Writer(new File(SAVE_PATH));
            writer.write(account);
            writer.close();
        } catch (IOException e) {
            System.out.println("kdkdkd");
        }
    }

    private void loadAccount() {
        try {
            this.account = Reader.readAccounts(new File(SAVE_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }


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


