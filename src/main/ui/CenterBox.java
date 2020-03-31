package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.Account;
import persistence.Reader;
import persistence.Writer;

import java.io.File;
import java.io.IOException;

// Main column for flashcard app
public class CenterBox extends Box {

    CenterBox(GUI gui) {
        super(gui);
    }

    // launch button
    // MODIFIES: THIS
    // EFFECTS: creates launch button
    public void launchButton() {
        Button button = makeAButton("Launch App");
        button.setAlignment(Pos.CENTER);
        box.getChildren().add(button);

        button.setOnMouseClicked(event -> {
            makeIntroMenu();
            box.getChildren().remove(button);
        });
    }


    // makes main menu
    // MODIFIES: THIS
    // EFFECTS: creates main menu with create and load buttons
    private void makeIntroMenu() {
        VBox vbox = makeVBox();
        box.getChildren().add(vbox);
        vbox.setAlignment(Pos.CENTER);
        Button create = makeAButton("Create an account");
        Button load = makeAButton("Load an account");
        vbox.getChildren().addAll(create, load);

        createAccountButton(create);
        loadButton(load);
    }

    //  create account button
    // MODIFIES: THIS
    // EFFECTS: creates create account button and when it's pressed
    private void createAccountButton(Button create) {
        create.setOnMouseClicked(event19 -> {
            TextField textField = gui.makeTextField();
            box.getChildren().clear();
            box.getChildren().add(gui.instructionLabels("Type Your Username"));
            box.getChildren().add(textField);

            textField.setOnAction(event11 -> {
                String name = textField.getText();
                if (!new File(FlashCardApp.STORED_ACCOUNTS + name).exists()) {
                    gui.emptyPage();
                    createAccount(name);
                } else {
                    accountError(true);
                    gui.emptyPage();
                    makeIntroMenu();
                }
            });
        });
    }

    // intro menu button
    // MODIFIES: THIS
    // EFFECTS: creates main buttons for app and layout of screen
    void createMenu() {
        Label label = new Label("Hi " + this.gui.account.getName() + "!");
        label.setStyle("-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.1) , 10, 0.7 , 4 , 9 );\n"
                + "-fx-font-weight: bold; -fx-font-size: 70px; -fx-text-fill: #FFFFFF;");
        label.setPadding(new Insets(10, 0, 30, 0));
        label.setAlignment(Pos.TOP_CENTER);
        Button createDeck = makeAButton("Create a deck");
        Button createCard = makeAButton("Create a Flashcard");
        Button decks = makeAButton("Decks");
        Button load = makeAButton("Load");
        Button save = makeAButton("Save");
        Button quit = makeAButton("Quit");
        createAllDecksButton(decks);
        gui.rightBox.createDeckButton(createDeck);
        gui.rightBox.createCardButton(createCard);
        loadButton(load);
        saveButton(save);
        quitButton(quit);
        VBox vbox = makeVBox();
        vbox.getChildren().addAll(createDeck, createCard, decks, load, save, quit);
        box.getChildren().add(vbox);
        gui.leftBox.getBox().getChildren().add(label);
        label.setAlignment(Pos.TOP_CENTER);
        vbox.setAlignment(Pos.CENTER);
    }

    // decks button clicked
    // EFFECTS: shows all buttons if decks is pressed
    private void createAllDecksButton(Button decks) {
        decks.setOnMouseClicked(event -> gui.leftBox.showAllDecks());
    }

    // load button
    // MODIFIES: THIS
    // EFFECTS: when load button presses it will take input and load if account exists otherwise error alert
    private void loadButton(Button load) {
        load.setOnMouseClicked(event -> {
            TextField textField = gui.makeTextField();
            box.getChildren().clear();
            box.getChildren().add(gui.instructionLabels("Type Your Username"));
            box.getChildren().add(textField);
            textField.setOnAction(event11 -> {
                try {
                    String name = textField.getText();
                    this.gui.account = Reader.readAccounts(new File(FlashCardApp.STORED_ACCOUNTS + name));
                    gui.emptyPage();
                    createMenu();

                } catch (Exception e) {
                    accountError(false);
                    gui.emptyPage();
                    makeIntroMenu();
                }
            });
        });
    }

    // quit button
    // MODIFIES: THIS
    // EFFECTS: quit button if pressed will save and close screen
    private void quitButton(Button quit) {
        quit.setOnMouseClicked(event -> {
            saveAccount();
            gui.stage.close();
        });
    }

    // saves account
    // MODIFIES: THIS
    // EFFECTS: saves account and shows message of saves successfully
    private void saveAccount() {
        try {
            Writer writer = new Writer(new File(FlashCardApp.STORED_ACCOUNTS + gui.account.getName()));
            writer.write(gui.account);
            writer.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "saved successfully :))");
            alert.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "could not save :((");
            alert.showAndWait();
            makeIntroMenu();
        }
    }

    private void saveButton(Button save) {
        save.setOnMouseClicked(event -> saveAccount());
    }

    // create account
    // MODIFIES: THIS
    // EFFECTS: creates account
    void createAccount(String text) {
        gui.account = new Account(text);
        createMenu();
    }

    // error for account
    // EFFECTS: shows an alert if account exists or if loading and account doesn't exists
    void accountError(boolean exists) {
        gui.playErrorSound();
        if (exists) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "account with that username exists");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No account with that name exists.");
            alert.showAndWait();
        }
    }

}
