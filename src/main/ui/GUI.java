package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Account;
import model.Deck;
import model.FlashCard;
import persistence.Reader;
import persistence.Writer;

import java.io.File;
import java.io.IOException;

public class GUI extends Application {

    private static final int WIDTH = 1600;
    private static final int HEIGHT = 900;
    private Account account;
    private Stage stage;
    private VBox leftVbx;
    private VBox centerVbx;
    private VBox rightVbx;
    private HBox mainBox;

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainBox = new HBox();
        leftVbx = new VBox();
        rightVbx = new VBox();
        centerVbx = new VBox();
        rightVbx.setPrefSize(WIDTH / 3, HEIGHT);
        rightVbx.setAlignment(Pos.CENTER);
        centerVbx.setPrefSize(WIDTH / 3, HEIGHT);
        centerVbx.setAlignment(Pos.CENTER);
        leftVbx.setPrefSize(WIDTH / 3, HEIGHT);
        leftVbx.setAlignment(Pos.TOP_CENTER);
        mainBox.setStyle("-fx-background-color: #6699ff");
        mainBox.getChildren().addAll(leftVbx, centerVbx, rightVbx);
        Scene scene = new Scene(mainBox, WIDTH, HEIGHT);

        primaryStage.setScene(scene);
        primaryStage.show();

        Button button = makeAButton("Launch App");
        button.setAlignment(Pos.CENTER);
        centerVbx.getChildren().add(button);

        button.setOnMouseClicked(event -> {
            makeIntroMenu();
            centerVbx.getChildren().remove(button);
        });


        stage = primaryStage;

    }


    private void makeIntroMenu() {
        VBox vbox = makeVBox();
        centerVbx.getChildren().add(vbox);
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
            centerVbx.getChildren().clear();
            centerVbx.getChildren().add(textField);

            textField.setOnAction(event11 -> {
                String name = textField.getText();
                if (!new File(FlashCardApp.STORED_ACCOUNTS + name).exists()) {
                    createAccount(name);
                    System.out.println("Welcome " + account.getName() + "!");
                } else {
                    accountError(true);
                    centerVbx.getChildren().remove(textField);
                    makeIntroMenu();
                }
                centerVbx.getChildren().remove(textField);
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
                + "-fx-font-weight: bold; -fx-font-size: 80px; -fx-text-fill: #FFFFFF;");
        label.setPadding(new Insets(10, 0, 30, 0));
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
        centerVbx.getChildren().add(vbox);
        leftVbx.getChildren().add(label);
        label.setAlignment(Pos.TOP_CENTER);
        vbox.setAlignment(Pos.CENTER);
    }

    private void createAllDecksButton(Button decks) {
        decks.setOnMouseClicked(event -> {
            showAllDecks();
        });
    }

    private void showAllDecks() {
        if (centerVbx == mainBox.getChildren().get(1)) {
            mainBox.getChildren().remove(1, 3);
            mainBox.getChildren().addAll(rightVbx, centerVbx);
        }

        VBox vbox = makeVBox();
        removeDecks();

        for (Deck deck : this.account.getDecks()) {

            Button button = new Button(deck.getName());
            button.setPrefSize(300, 80);
            button.setAlignment(Pos.CENTER);
            button.setStyle("-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.1) , 10, 0.7 , 4 , 9 );\n"
                    + "-fx-border-color: #b3d9ff; -fx-padding:3px;\n"
                    + "-fx-font-weight: bold;\n"
                    + "-fx-font-size: 40px;\n"
                    + "-fx-background-color: linear-gradient(#b3d9ff, #b3d9ff);\n"
                    + "-fx-border-radius: 10;\n"
                    + "-fx-background-radius: 10;\n"
                    + "-fx-text-fill: #FFFFFF;\n");
            deckManageButton(button);
            vbox.getChildren().add(button);
        }

        leftVbx.getChildren().add(vbox);
        vbox.setAlignment(Pos.CENTER);
    }

    private void removeDecks() {
        try {
            Label name = (Label) leftVbx.getChildren().get(0);
            leftVbx.getChildren().clear();
            leftVbx.getChildren().add(name);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void removeCards() {
        try {
            rightVbx.getChildren().clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void deckManageButton(Button button) {

        button.setOnMouseClicked(event -> {
            Deck deck = this.account.findDeck(button.getText());
            VBox vbox = makeVBox();
            removeCards();

            for (FlashCard card : deck.getCards()) {

                Button button1 = showCard(card.getFront(), card.getBack());

                vbox.getChildren().add(button1);
            }
            rightVbx.getChildren().add(vbox);
            vbox.setAlignment(Pos.TOP_CENTER);

        });
    }

    private Button showCard(String front, String back) {
        Button button1 = new Button(front);
        button1.setPrefSize(500, 120);
        button1.setAlignment(Pos.CENTER);
        button1.setStyle("-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.1) , 10, 0.7 , 4 , 9 );\n"
                + "-fx-border-color: #b3d9ff; -fx-padding:3px;\n"
                + "-fx-font-weight: bold;\n"
                + "-fx-font-size: 60px;\n"
                + "-fx-background-color: linear-gradient(#b3d9ff, #b3d9ff);\n"
                + "-fx-border-radius: 10;\n"
                + "-fx-background-radius: 10;\n"
                + "-fx-text-fill: #FFFFFF;\n");
        cardManageButton(button1, front, back);
        return button1;

    }

    private void cardManageButton(Button button, String front, String back) {
        button.setOnMouseClicked(event -> {
            if (button.getText().equals(front)) {
                button.setText(back);
            } else if (button.getText().equals(back)) {
                button.setText(front);
            }
        });

    }

    private void createCardButton(Button createCard) {
        createCard.setOnMouseClicked(event -> {
            moveMenu();
            TextField textField = new TextField();
            textField.setPrefSize(500, 120);
            textField.setAlignment(Pos.CENTER);
            rightVbx.getChildren().clear();
            rightVbx.getChildren().add(textField);

            textField.setStyle("-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.1) , 10, 0.7 , 4 , 9 );\n"
                    + "-fx-border-color: #b3d9ff; -fx-padding:3px;\n"
                    + "-fx-font-weight: bold;\n"
                    + "-fx-font-size: 60px;\n"
                    + "-fx-background-color: linear-gradient(#b3d9ff, #b3d9ff);\n"
                    + "-fx-border-radius: 10;\n"
                    + "-fx-background-radius: 10;\n"
                    + "-fx-text-fill: #FFFFFF;\n");
            textField.setOnAction(event1 -> {
                String front = textField.getText();
                textField.deleteText(0, textField.getText().length());
                textField.setOnAction(event2 -> {
                    String back = textField.getText();
                    addCardToDeck(front, back);
                });


            });


        });
    }

    private void addCardToDeck(String front, String back) {
        Button button = showCard(front, back);
        rightVbx.getChildren().clear();
        rightVbx.getChildren().add(button);
        button.setOnDragDetected(event -> {
            button.setTranslateX(event.getX());
            System.out.println(event.getTarget());
            System.out.println(event.getX());
            System.out.println(event.getClass());
        });

    }

    private void loadButton(Button load) {

        load.setOnMouseClicked(event -> {
            TextField textField = makeTextField();
            centerVbx.getChildren().clear();
            centerVbx.getChildren().add(textField);
            textField.setOnAction(event11 -> {
                try {
                    String name = textField.getText();
                    this.account = Reader.readAccounts(new File(FlashCardApp.STORED_ACCOUNTS + name));
                    emptyPage();
                    createMenu();

                } catch (IOException e) {
                    accountError(false);
                    centerVbx.getChildren().remove(textField);
                    emptyPage();
                    makeIntroMenu();
                }
            });
        });
    }


    private void saveButton(Button save) {
        save.setOnMouseClicked(event -> {
            try {
                Writer writer = new Writer(new File(FlashCardApp.STORED_ACCOUNTS + account.getName()));
                writer.write(account);
                writer.close();
                System.out.println("saved successfully");
                Alert alert = new Alert(Alert.AlertType.INFORMATION,"saved successfully :))");
                alert.show();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,"could not save :((");
                alert.showAndWait();
                makeIntroMenu();

            }
        });
    }

    private void moveMenu() {
        if (centerVbx == mainBox.getChildren().get(1)) {
            mainBox.getChildren().remove(1, 3);
            mainBox.getChildren().addAll(rightVbx, centerVbx);
        }
    }

    private void emptyPage() {
        leftVbx.getChildren().clear();
        rightVbx.getChildren().clear();
        centerVbx.getChildren().clear();
    }

    private void createDeckButton(Button createDeck) {
        createDeck.setOnMouseClicked(event -> {
            moveMenu();

            TextField textField = new TextField();
            textField.setPrefSize(500, 120);
            textField.setAlignment(Pos.CENTER);
            rightVbx.getChildren().clear();
            rightVbx.getChildren().add(textField);

            textField.setStyle("-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.1) , 10, 0.7 , 4 , 9 );\n"
                    + "-fx-border-color: #b3d9ff; -fx-padding:3px;\n"
                    + "-fx-font-weight: bold;\n"
                    + "-fx-font-size: 60px;\n"
                    + "-fx-background-color: linear-gradient(#b3d9ff, #b3d9ff);\n"
                    + "-fx-border-radius: 10;\n"
                    + "-fx-background-radius: 10;\n"
                    + "-fx-text-fill: #FFFFFF;\n");
            textField.setOnAction(event1 -> {
                if (this.account.makeDeck(textField.getText())) {
                    showAllDecks();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Deck with that name exists");
                    alert.showAndWait();

                }
                rightVbx.getChildren().clear();
            });


        });
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
