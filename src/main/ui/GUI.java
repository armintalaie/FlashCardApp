package ui;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Account;
import model.Deck;
import model.FlashCard;
import persistence.Reader;
import persistence.Writer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class GUI extends Application {

    private static final int WIDTH = 1600;
    private static final int HEIGHT = 900;
    private Account account;
    private Stage stage;
    private VBox leftVbx;
    private VBox centerVbx;
    private VBox rightVbx;
    private HBox mainBox;
    private FlashCard pendingCard = null;
    private boolean deleteMode = false;
    private Button selected = null;
    private boolean darkMode = false;

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        Pane group = new Pane();
        mainBox = new HBox();
        leftVbx = makeMainVBoxes();
        rightVbx = makeMainVBoxes();
        centerVbx = makeMainVBoxes();
        mainBox.getChildren().addAll(leftVbx, centerVbx, rightVbx);
        group.getChildren().add(mainBox);
        Scene scene = new Scene(group, WIDTH, HEIGHT);
        Circle cir2 = new Circle(WIDTH - 40, HEIGHT - 40, 30);
        darkModeButton(cir2);
        group.getChildren().add(cir2);
        primaryStage.setScene(scene);
        primaryStage.show();
        launchButton();
        stage = primaryStage;
        mainScreenAnimation();
    }

    private void darkModeButton(Circle cir2) throws FileNotFoundException {
        Image image = new Image(new FileInputStream("data/moon.png"));
        Image image1 = new Image(new FileInputStream("data/sun.png"));
        cir2.setFill(new ImagePattern(image));
        cir2.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
        cir2.setOnMouseClicked(event -> {
            if (darkMode) {
                cir2.setFill(new ImagePattern(image));
            } else {
                cir2.setFill(new ImagePattern(image1));
            }
            rightVbx.getChildren().clear();
            try {
                leftVbx.getChildren().remove(1, leftVbx.getChildren().size());
            } catch (Exception e) {
                leftVbx.getChildren().clear();
            }

            darkMode = !darkMode;

        });

    }

    private void mainScreenAnimation() {
        final Animation animation = new Transition() {

            {
                setCycleDuration(Duration.millis(5000));
                setInterpolator(Interpolator.LINEAR);
                setAutoReverse(true);
                setCycleCount(Timeline.INDEFINITE);
            }

            @Override
            protected void interpolate(double frac) {
                ArrayList<Integer> colors = checkDarkAndDelete();

                Color e = Color.rgb((int) (colors.get(0) - frac * 20), (int) (colors.get(1) - frac * 20),
                        (int) (colors.get(2) - frac * 20));
                mainBox.setBackground(new Background(new BackgroundFill(e, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        };
        animation.play();
    }

    private ArrayList<Integer> checkDarkAndDelete() {
        int startR = 102;
        int startG = 153;
        int startB = 253;

        if (deleteMode) {
            startR = 240;
            startG = 148;
            startB = 105;
        }

        if (darkMode) {
            startR = 28;
            startG = 48;
            startB = 84;
        }
        ArrayList<Integer> b = new ArrayList<>();
        b.add(startR);
        b.add(startG);
        b.add(startB);

        return b;
    }


    private VBox makeMainVBoxes() {
        VBox vbox = new VBox();
        vbox.setPrefSize(WIDTH / 3, HEIGHT);
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }

    private void launchButton() {
        Button button = makeAButton("Launch App");
        button.setAlignment(Pos.CENTER);
        centerVbx.getChildren().add(button);

        button.setOnMouseClicked(event -> {
            makeIntroMenu();
            centerVbx.getChildren().remove(button);
        });
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
        textField.setStyle("-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.1) , 10, 0.7 , 4 , 9 );\n"
                + "-fx-border-color: #b3d9ff; -fx-padding:3px;\n"
                + "-fx-font-weight: bold;\n"
                + "-fx-font-size: 60px;\n"
                + "-fx-background-color: linear-gradient(#b3d9ff, #b3d9ff);\n"
                + "-fx-border-radius: 10;\n"
                + "-fx-background-radius: 10;\n"
                + "-fx-text-fill: #FFFFFF;\n");
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
        decks.setOnMouseClicked(event -> showAllDecks());
    }

    private void showAllDecks() {
        if (centerVbx == mainBox.getChildren().get(1)) {
            mainBox.getChildren().remove(1, 3);
            mainBox.getChildren().addAll(rightVbx, centerVbx);
        }
        Button edit;
        if (deleteMode) {
            edit = makeAButton("Done");
        } else {
            edit = makeAButton("Edit");
        }
        createEditButton(edit);
        VBox vbox = makeVBox();

        removeDecks();
        for (Deck deck : this.account.getDecks()) {
            Button button = new Button(deck.getName());
            deckButtonDesign(button);
            deckManageButton(button);
            vbox.getChildren().add(button);
        }
        vbox.getChildren().add(edit);
        leftVbx.getChildren().add(vbox);
        vbox.setAlignment(Pos.CENTER);
    }

    private void createEditButton(Button button) {
        button.setOnMouseClicked(event -> {
            if (!deleteMode) {
                deleteMode = true;
                button.setText("Done");
                showAllDecks();
            } else {
                deleteMode = false;
                button.setText("Edit");
            }
        });
        button.setStyle("-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.1) , 10, 0.7 , 4 , 9 );\n"
                + "-fx-border-color: #b3d9ff; -fx-padding:3px;\n"
                + "-fx-font-weight: bold;\n"
                + "-fx-font-size: 40px;\n"
                + "-fx-background-color: linear-gradient(#e63054, #c91c3e);\n"
                + "-fx-border-radius: 10;\n"
                + "-fx-background-radius: 10;\n"
                + "-fx-text-fill: #FFFFFF;\n");

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

    private boolean deleteDeckDialogue(Deck deck) {
        if (deleteMode) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "are you sure you want to delete "
                    + deck.getName() + " and the cards in it?");
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    this.account.removeDeck(deck);
                    showAllDecks();
                    rightVbx.getChildren().clear();
                }
            });
            return true;
        }
        return false;
    }

    private void deckManageButton(Button button) {
        button.setOnMouseClicked(event -> {
            Deck deck = this.account.findDeck(button.getText());
            if (deleteDeckDialogue(deck)) {
                return;
            }
            deckSelected(button);
            if (pendingCard != null) {
                if (!deck.addCard(pendingCard)) {
                    return;
                }
                pendingCard = null;
            }
            makeCardBoxes(deck);
        });
    }

    private void deckSelected(Button button) {
        selected = button;
        showAllDecks();
    }


    private void deckButtonDesign(Button button) {
        if (selected != null && button.getText().equals(selected.getText())) {
            button.setPrefSize(360, 100);
        } else {
            button.setPrefSize(300, 80);
        }

        String color = "#b3d9ff";
        if (darkMode) {
            color = "#445573";
        }
        button.setStyle("-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.1) , 10, 0.7 , 4 , 9 );\n"
                + "-fx-border-color: " + color + "; -fx-padding:3px;\n"
                + "-fx-font-weight: bold;\n"
                + "-fx-font-size: 50px;\n"
                + "-fx-background-color: linear-gradient(" + color + ", " + color + ");\n"
                + "-fx-border-radius: 10;\n"
                + "-fx-background-radius: 10;\n"
                + "-fx-text-fill: #FFFFFF;\n");
        button.setAlignment(Pos.CENTER);
    }

    private void makeCardBoxes(Deck deck) {
        VBox vbox = makeVBox();
        removeCards();

        for (FlashCard card : deck.getCards()) {
            Button button1 = showCard(card.getFront(), card.getBack(), deck);
            vbox.getChildren().add(button1);
        }
        rightVbx.getChildren().add(vbox);
        vbox.setAlignment(Pos.TOP_CENTER);
    }

    private Button showCard(String front, String back, Deck deck) {
        Button button1 = new Button(front);
        button1.setPrefSize(500, 120);
        button1.setAlignment(Pos.CENTER);
        String color = "#b3d9ff";
        if (darkMode) {
            color = "#445573";
        }
        button1.setStyle("-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.1) , 10, 0.7 , 4 , 9 );\n"
                + "-fx-border-color: " + color + "; -fx-padding:3px;\n"
                + "-fx-font-weight: bold;\n"
                + "-fx-font-size: 60px;\n"
                + "-fx-background-color: linear-gradient(" + color + ", " + color + ");\n"
                + "-fx-border-radius: 10;\n"
                + "-fx-background-radius: 10;\n"
                + "-fx-text-fill: #FFFFFF;\n");
        cardManageButton(button1, front, back, deck);
        return button1;

    }


    private void cardManageButton(Button button, String front, String back, Deck deck) {
        button.setOnMouseClicked(event -> {


            deleteCardDialogue(front, back, deck);

            rotate90Deg(button, 0);

            if (button.getText().equals(front)) {
                button.setText(back);
                button.setScaleX(-1);
            } else if (button.getText().equals(back)) {
                button.setText(front);
            }
            rotate90Deg(button, 1);
        });
    }

    private void deleteCardDialogue(String front, String back, Deck deck) {
        if (deleteMode) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "are you sure you want to delete this card?");
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    deck.removeCard(front);
                    showAllDecks();
                    makeCardBoxes(deck);
                }
            });
        }
    }

    private void createCardButton(Button createCard) {
        createCard.setOnMouseClicked(event -> {
            moveMenu();
            TextField textField = new TextField();
            textField.setPrefSize(500, 120);
            rightVbx.getChildren().clear();
            rightVbx.getChildren().addAll(instructionLabels("Enter Card's Front"), textField);
            textFieldStyle(textField);
            textField.setOnAction(event1 -> {
                String front = textField.getText();
                Button button1 = showCard(front, "", null);
                rightVbx.getChildren().clear();
                rightVbx.getChildren().addAll(instructionLabels("Enter Card's Back"), button1);
                rotate90Deg(button1, 0);
                rotate90Deg(button1, 1);
                textField.clear();
                rightVbx.getChildren().set(1, textField);

                textField.setOnAction(event2 -> addCardToDeck(front, textField.getText()));
            });
        });
    }

    private void rotate90Deg(Node node, int second) {
        RotateTransition rt = new RotateTransition(Duration.millis(300), node);
        rt.setAxis(Rotate.Y_AXIS);
        rt.setFromAngle(second * 90);
        rt.setByAngle(90);
        rt.setAutoReverse(false);
        rt.play();

    }

    private Label instructionLabels(String title) {
        Label label = new Label(title);
        label.setStyle("-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.1) , 10, 0.7 , 4 , 9 );\n"
                + " -fx-padding:3px;\n"
                + "-fx-font-weight: bold;\n"
                + "-fx-font-size: 40px;\n"
                + "-fx-border-radius: 10;\n"
                + "-fx-background-radius: 10;\n"
                + "-fx-text-fill: #FFFFFF;\n");
        label.setPadding(new Insets(0, 0, 60, 0));
        return label;

    }

    private void textFieldStyle(TextField textField) {
        textField.setStyle("-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.1) , 10, 0.7 , 4 , 9 );\n"
                + "-fx-border-color: #b3d9ff; -fx-padding:3px;\n"
                + "-fx-font-weight: bold;\n"
                + "-fx-font-size: 60px;\n"
                + "-fx-background-color: linear-gradient(#b3d9ff, #b3d9ff);\n"
                + "-fx-border-radius: 10;\n"
                + "-fx-background-radius: 10;\n"
                + "-fx-text-fill: #FFFFFF;\n");
        textField.setAlignment(Pos.CENTER);
    }

    private void addCardToDeck(String front, String back) {
        Button button = showCard(front, back, null);
        rightVbx.getChildren().clear();
        rightVbx.getChildren().add(button);
        button.setAlignment(Pos.CENTER);
        pendingCard = new FlashCard(front, back);


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
        save.setOnMouseClicked(event -> saveAccount());
    }

    private void saveAccount() {
        try {
            Writer writer = new Writer(new File(FlashCardApp.STORED_ACCOUNTS + account.getName()));
            writer.write(account);
            writer.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "saved successfully :))");
            alert.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "could not save :((");
            alert.showAndWait();
            makeIntroMenu();
        }
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
            textFieldStyle(textField);
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
            saveAccount();
            stage.close();
        });
    }

    private Button makeAButton(String text) {
        Button button = new Button(text);
        mainButtonDesign(button);

        button.setOnMouseEntered(event -> mainButtonAnimation(button, 3));

        button.setOnMouseExited(event -> mainButtonAnimation(button, -4));

        return button;
    }

    private void mainButtonDesign(Button button) {
        button.setMinSize(280, 60);
        button.setMaxSize(380, 70);
        button.setPrefSize(280, 60);
        button.setStyle("-fx-background-color: linear-gradient(#00e6e6, #00ffff);\n"
                + "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.3) , 2, 1.0 , 1 , 5 );\n"
                + "-fx-text-fill: white;\n"
                + "-fx-background-radius: 20;\n"
                + "-fx-background-insets: 0,1,2,3,0;\n"
                + "-fx-text-fill: #654b00;\n"
                + "-fx-font-weight: bold;\n"
                + "-fx-font-size: 25px;\n"
                + "-fx-padding: 10 20 10 20;");

    }

    private void mainButtonAnimation(Button button, int i) {
        Animation animation = new Transition() {

            {
                setCycleDuration(Duration.millis(400));
                setInterpolator(Interpolator.LINEAR);

            }

            @Override
            protected void interpolate(double frac) {
                if (i == 1 && button.getWidth() == button.getMaxWidth()) {
                    stop();
                }
                button.setPrefSize(button.getWidth() + i * frac, button.getHeight() + i * frac);
            }
        };
        animation.play();
    }

    private VBox makeVBox() {
        VBox vbox = new VBox();
        vbox.setSpacing(30);
        return vbox;
    }
}