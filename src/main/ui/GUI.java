package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Box;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Account;
import model.Deck;
import persistence.Reader;

import java.io.File;
import java.io.IOException;

public class GUI extends Application {

    private static final int WIDTH = 1600;
    private static final int HEIGHT = 900;
    private Account account;
    private Stage stage;
    private BorderPane pane;

    @Override
    public void start(Stage primaryStage) throws Exception {
        pane = new BorderPane();
        pane.setStyle("-fx-background-color: #6699ff");
        Scene scene = new Scene(pane, WIDTH, HEIGHT);

        Button button = makeAButton("Launch App");
        button.setAlignment(Pos.CENTER);
        pane.setCenter(button);

        button.setOnMouseClicked(event -> {
            makeIntroMenu();
            pane.getChildren().remove(button);
        });

        stage = primaryStage;
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    private void makeIntroMenu() {
        VBox vbox = makeVBox();
        pane.setCenter(vbox);
        vbox.setAlignment(Pos.CENTER);
        Button create = makeAButton("Create an account");

        Button load = makeAButton("Load an account");
        vbox.getChildren().addAll(create, load);

        createAccountButton(create);
        loadButton(load);
    }

    private void createAccountButton(Button create) {
        create.setOnMouseClicked(event19 -> {
            TextField textField = makeTextField();
            pane.getChildren().clear();
            pane.setCenter(textField);

            textField.setOnAction(event11 -> {
                String name = textField.getText();
                if (!new File(FlashCardApp.STORED_ACCOUNTS + name).exists()) {
                    createAccount(name);
                    System.out.println("Welcome " + account.getName() + "!");
                } else {
                    accountError(true);
                    pane.getChildren().remove(textField);
                    makeIntroMenu();
                }
                pane.getChildren().remove(textField);
            });
        });
    }

    private void accountError(boolean exists) {
        if (exists) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "account with that username exists");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No account with that name exists.");
            alert.showAndWait();
        }


    }

    private TextField makeTextField() {
        TextField textField = new TextField();
        return textField;
    }

    private void createAccount(String text) {
        account = new Account(text);
        createMenu();
    }

    private void createMenu() {
        Label label = new Label("Hi " + this.account.getName() + "!");
        label.setStyle("-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.1) , 10, 0.7 , 4 , 9 );\n"
                + "-fx-font-weight: bold;\n"
                + "-fx-font-size: 80px;\n"
                + "-fx-text-fill: #FFFFFF;\n");
        label.setPadding(new Insets(10,0,0,0));
        label.setAlignment(Pos.TOP_CENTER);
        Button createDeck = makeAButton("Create a deck");
        Button createCard = makeAButton("Create a Flashcard");
        Button decks = makeAButton("Decks");
        Button load = makeAButton("Load");
        Button save = makeAButton("Save");
        Button quit = makeAButton("Quit");

        createAllDecksButton(decks);
        createDeckButton(createDeck);
        createCardButton(createCard);
        loadButton(load);
        saveButton(save);
        quitButton(quit);

        VBox vbox = makeVBox();
        vbox.getChildren().addAll(createDeck, createCard, decks, load, save, quit);
        pane.setCenter(vbox);
        pane.setTop(label);
        vbox.setAlignment(Pos.CENTER);
    }

    private void createAllDecksButton(Button decks) {
        decks.setOnMouseClicked(event -> {
            VBox vbox = makeVBox();
            for (Deck deck: this.account.getDecks()) {

                Label label = new Label(deck.getName() + "  " + deck.numberOfCards());
                label.setPrefSize(200,50);
                label.setAlignment(Pos.CENTER);
                label.setStyle("-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.1) , 10, 0.7 , 4 , 9 );\n"
                        + "-fx-border-color:black; -fx-padding:3px;\n"
                        + "-fx-font-weight: bold;\n"
                        + "-fx-font-size: 20px;\n"
                        + "-fx-background-color: linear-gradient(#b3d9ff, #b3d9ff);\n"
                        + "-fx-border-radius: 10;\n"
                        + "-fx-background-radius: 10;\n"
                        + "-fx-text-fill: #FFFFFF;\n");
                deckManageButton(label);
                vbox.getChildren().add(label);
            }

            pane.setLeft(vbox);
        });
    }

    private void deckManageButton(Label text) {
    }

    private void createCardButton(Button createCard) {
    }

    private void loadButton(Button load) {

        load.setOnMouseClicked(event -> {
            TextField textField = makeTextField();
            pane.getChildren().clear();
            pane.setCenter(textField);
            textField.setOnAction(event11 -> {
                try {
                    String name = textField.getText();
                    this.account = Reader.readAccounts(new File(FlashCardApp.STORED_ACCOUNTS + name));
                    createMenu();

                } catch (IOException e) {
                    accountError(false);
                    pane.getChildren().remove(textField);
                    makeIntroMenu();
                }
            });
        });
    }


    private void saveButton(Button save) {
    }

    private void createDeckButton(Button createDeck) {
    }

    private void quitButton(Button quit) {
        quit.setOnMouseClicked(event -> {
            stage.close();
        });
    }


    private Button makeAButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(280, 60);
        button.setStyle("-fx-background-color: linear-gradient(#00e6e6, #00ffff);\n"
                + "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.3) , 2, 1.0 , 1 , 5 );\n"
                + "-fx-text-fill: white;\n"
                + "-fx-background-radius: 20;\n"
                + "-fx-background-insets: 0,1,2,3,0;\n"
                + "-fx-text-fill: #654b00;\n"
                + "-fx-font-weight: bold;\n"
                + "-fx-font-size: 20px;\n"
                + "-fx-padding: 10 20 10 20;");

        return button;
    }

    private VBox makeVBox() {
        VBox vbox = new VBox();
        vbox.setSpacing(30);
        return vbox;
    }


}
