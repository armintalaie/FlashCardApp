package persistence;

import model.Account;
import model.Deck;
import model.FlashCard;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// A reader that can read account data from a file
public class Reader {
    // TODO: add proper try/catch blocks
    public static final String DELIMITER = ",";

    // EFFECTS: returns an account parsed from file; throws
    // IOException if an exception is raised when opening / reading from file
    public static Account readAccounts(File file) throws IOException {
        List<String> fileContent = readFile(file);
        return parseContent(fileContent);
    }

    // EFFECTS: returns content of file as a list of strings, each string
    // containing the content of one row of the file
    private static List<String> readFile(File file) throws IOException {
        return Files.readAllLines(file.toPath());
    }

    // EFFECTS: returns an account parsed from list of strings
    // where each string contains data for one account/one deck/one card
    private static Account parseContent(List<String> fileContent) {
        Account account = null;
        Deck deck = null;

        for (String line : fileContent) {
            ArrayList<String> lineComponents = splitString(line);
            switch (Integer.parseInt(lineComponents.get(0))) {
                case 2: {
                    account = new Account(lineComponents.get(1));
                    break;
                }
                case 0: {
                    deck = new Deck(lineComponents.get(1));
                    account.addDeck(deck);
                    break;
                }
                case 1: {
                    deck.addCard(new FlashCard(lineComponents.get(1), lineComponents.get(2)));
                    break;
                }
            }
        }
        return account;
    }

    // EFFECTS: returns a list of strings obtained by splitting line on DELIMITER
    private static ArrayList<String> splitString(String line) {
        String[] splits = line.split(DELIMITER);
        return new ArrayList<>(Arrays.asList(splits));
    }
}
