package ui;

import model.exceptions.*;
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
    // TODO: --EXTRA-- add passwords to accounts
    // TODO: --EXTRA-- ability to move cards between decks

    public static String STORED_ACCOUNTS = "data/Accounts/";
    private Account account;
    private Scanner scanner = new Scanner(System.in);
    private HashMap<String, Integer> commands;

    // EFFECTS: runs runApp() function
    public FlashCardApp() {
        commands = new HashMap<>();
        commandToNumberRef(commands);
        runApp();
    }

    // MODIFIES: THIS
    // EFFECTS: creates account with input as name an runs the app to read input
    private void runApp() {
        boolean keepGoing = initialAccountSetUp();

        while (keepGoing) {
            possibleCommands();
            String command = scanner.nextLine().toLowerCase();
            try {
                keepGoing = readCommand(commands.get(command));
            } catch (NullPointerException e) {
                System.out.println("wrong command");
            }
        }
    }

    // Gets input and decides whether to load or create account
    // MODIFIES: THIS
    // EFFECTS: creates account with input as name an runs the app to read input or load account with input
    private boolean initialAccountSetUp() {
        System.out.println("Hello\nDo you want to create or load an account?");
        while (true) {
            String command = scanner.nextLine().toLowerCase();
            if (command.contains("load")) {
                if (!loadAccount()) {
                    return false;
                }
                System.out.println("Welcome back " + account.getName() + "!");
                break;
            } else if (command.contains("create")) {
                if (createAccount()) {
                    break;
                }
            } else {
                System.out.println("please type either \"create\" or \"load\" ");
            }
        }
        return true;
    }

    // Creates account
    // MODIFIES: THIS
    // EFFECTS: creates account with input as name an runs the app to read input
    private boolean createAccount() {
        System.out.println("Awesome! What's your name?");
        String name = scanner.nextLine();
        if (!new File(STORED_ACCOUNTS + name).exists()) {
            account = new Account(name);
            System.out.println("Welcome " + account.getName() + "!");
            return true;
        } else {
            System.out.println("Sorry, an account with that name already exists\n please choose another name.");
            return false;
        }
    }

    // Sets keys to commands
    // MODIFIES: THIS
    // EFFECTS: creates hashmap with keys for commands
    private void commandToNumberRef(HashMap<String, Integer> commands) {
        commands.put("create a deck", 1);
        commands.put("create a flashcard", 2);
        commands.put("decks", 3);
        commands.put("load", 4);
        commands.put("save", 5);
        commands.put("quit", 6);

        commands.put("1", 1);
        commands.put("2", 2);
        commands.put("3", 3);
        commands.put("4", 4);
        commands.put("5", 5);
        commands.put("6", 6);
    }

    // EFFECTS: prints possible commands you can type
    private void possibleCommands() {
        System.out.println("What do you want to do, " + account.getName() + "?\t(type option or its number)");
        System.out.print("1. Create a deck      ");
        System.out.println("\t2. Create a flashcard");
        System.out.print("3. Decks              ");
        System.out.println("\t4. Load              ");
        System.out.print("5. Save               ");
        System.out.println("\t6. Quit              ");

    }

    // EFFECTS: calls the right method based on the input. returns false if user quits. otherwise true
    private boolean readCommand(int command) {

        if (command == 1) {
            createDeck();
        } else if (command == 2) {
            createAndAddCard();
        } else if (command == 3) {
            allDecksManagement();
        } else if (command == 4) {
            loadAccount();
        } else if (command == 5) {
            saveAccount(account);
        } else if (command == 6) {
            saveAccount(account);
            System.out.println("Bye " + account.getName() + "!");
            return false;
        }
        return true;
    }

    // Saves account
    // MODIFIES: Account folder
    // EFFECTS: saves account to STORED_ACCOUNTS path as a text file
    private void saveAccount(Account account) {

        try {
            Writer writer = new Writer(new File(STORED_ACCOUNTS + account.getName()));
            writer.write(account);
            writer.close();
            System.out.println("saved successfully");
        } catch (IOException e) {
            System.out.println("could not save :((");
        }
    }

    // Loads account
    // MODIFIES: THIS
    // EFFECTS: loads account from STORED_ACCOUNTS path and user's name as a text file. returns true if found, false if
    // no account existed
    private boolean loadAccount() {
        System.out.println("Please type your account's name");
        while (true) {
            String name = scanner.nextLine();
            if (name.contains("quit")) {
                return false;
            }
            try {
                this.account = Reader.readAccounts(new File(STORED_ACCOUNTS + name));
                break;
            } catch (Exception e) {
                System.out.println("No account with that name exists. try again or type quit");
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
            if (command.contains("remove") || Integer.toString(account.numberOfDecks() + 1).equals(command)) {
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
        System.out.println("options: ");
        System.out.println("a) type deck to see it \nb)remove + deck name to remove it \nc) back to go to menu\n");
        int removeIndex = printAllDecks(account);
        //System.out.println(removeIndex + ". Delete a deck ");
    }

    // REQUIRES : deck is not null
    // MODIFIES: this
    // EFFECTS: deletes deck of choosing if remove is typed or finds a deck
    private void singleDeckManagement(Deck deck) {

        while (true) {
            showAllCards(deck.getCards());
            System.out.println("options: \na) type choose to select a card");
            System.out.println("b)remove + card name to remove it \nc) back\n");
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
        System.out.println("options: \na) type card to see its answer");
        System.out.println("b) back\n");
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

    // MODIFIES: THIS
    // EFFECTS: creates a flashcard from inputs for front and back and adds them to a previously created deck
    private void createAndAddCard() {
        FlashCard flashCard = createCard();
        if (flashCard == null) {
            return;
        }
        System.out.println("card successfully made. which deck do you want ot add it to?");
        while (true) {
            printAllDecks(account);
            String command = scanner.nextLine();
            if (command.equals("back")) {
                return;
            }
            Deck deck = account.findDeck(command);

            if (deck != null) {
                if (deck.addCard(flashCard)) {
                    System.out.println("Flashcard successfully added to " + deck.getName());
                    return;
                } else {
                    System.out.println("Flashcard exists. choose another deck or type back to return to menu");
                }
            } else {
                System.out.println("No deck with that name. try again type back to return to menu");
            }
        }
    }

    // EFFECTS: creates a flashcard from inputs for front and back and returns it
    private FlashCard createCard() {
        System.out.println("Enter Flashcard's Front");
        String front = scanner.nextLine();
        System.out.println("Enter Flashcard's back");
        String back = scanner.nextLine();
        try {
            return new FlashCard(front, back);
        } catch (MaxLengthException e) {
            System.out.println("card's front or back is too long. card wasn't made");
            return null;
        }
    }


    // MODIFIES: this
    // EFFECTS: creates deck with name from input, if one with that name exists, it will show an error message
    private void createDeck() {
        System.out.println("Enter Deck's Name");
        String deckName = scanner.nextLine();
        if (account.makeDeck(deckName)) {
            System.out.println("Deck successfully made");
        } else {
            System.out.println("Deck with same name already exists");
        }
    }

    // EFFECTS: prints the name of all decks currently active
    private int printAllDecks(Account account) {
        ArrayList<Deck> decks = account.getDecks();
        int i = 1;
        for (Deck d : decks) {

            System.out.println(i++ + ". " + d.getName());
        }
        return i;
    }


}


