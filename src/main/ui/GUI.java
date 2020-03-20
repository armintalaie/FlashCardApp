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

// GUI for Flash Card Application
public class GUI extends Application {
    //TODO: add audiovisual elements
    //TODO: add readme step by step instructions to use app
    //TODO: resizing windows
    //TODO: able to make text smaller for longer texts

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

    // starts application
    // MODIFIES: THIS
    // EFFECTS: creates windows and initial graphics elements
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

    // changes dark mode status
    // MODIFIES: THIS
    // EFFECTS: changes moon icon to sun or vice versa and changes the boolean value of darkMode
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

    // sets background color
    // MODIFIES: THIS
    // EFFECTS: changes background based on mode (light,delete,dark) and slightly shifts color as a type of transition
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

    // create color combination
    // MODIFIES: THIS
    // EFFECTS: creates RGB color for scene based on mode
    private ArrayList<Integer> checkDarkAndDelete() {
        int startR = 102;
        int startG = 153;
        int startB = 253;

        if (darkMode) {
            startR = 28;
            startG = 48;
            startB = 84;
        }

        if (deleteMode) {
            startR = 240;
            startG = 148;
            startB = 105;
        }
        ArrayList<Integer> b = new ArrayList<>();
        b.add(startR);
        b.add(startG);
        b.add(startB);

        return b;
    }


    // makes V boxes for main screen
    // MODIFIES: THIS
    // EFFECTS: creates a v box with WIDTH / 3, HEIGHT
    private VBox makeMainVBoxes() {
        VBox vbox = new VBox();
        vbox.setPrefSize(WIDTH / 3, HEIGHT);
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }

    // launch button
    // MODIFIES: THIS
    // EFFECTS: creates launch button
    private void launchButton() {
        Button button = makeAButton("Launch App");
        button.setAlignment(Pos.CENTER);
        centerVbx.getChildren().add(button);

        button.setOnMouseClicked(event -> {
            makeIntroMenu();
            centerVbx.getChildren().remove(button);
        });
    }


    // makes main menu
    // MODIFIES: THIS
    // EFFECTS: creates main menu with create and load buttons
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

    //  create account button
    // MODIFIES: THIS
    // EFFECTS: creates create account button and when it's pressed
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

    // error for account
    // EFFECTS: shows an alert if account exists or if loading and account doesn't exists
    private void accountError(boolean exists) {
        if (exists) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "account with that username exists");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No account with that name exists.");
            alert.showAndWait();
        }


    }

    // make textfield
    // EFFECTS: creates textfield with certain design
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


    // create account
    // MODIFIES: THIS
    // EFFECTS: creates account
    private void createAccount(String text) {
        account = new Account(text);
        createMenu();
    }

    // intro menu button
    // MODIFIES: THIS
    // EFFECTS: creates main buttons for app and layout of screen
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

    // decks button clicked
    // EFFECTS: shows all buttons if decks is pressed
    private void createAllDecksButton(Button decks) {
        decks.setOnMouseClicked(event -> showAllDecks());
    }

    // all decks
    // EFFECTS: prints all decks
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

    // edit button
    // MODIFIES: THIS
    // EFFECTS: edit button design. and changes delete mode when pressed
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

    // hides decks
    // MODIFIES: THIS
    // EFFECTS: removes decks from screen
    private void removeDecks() {
        try {
            Label name = (Label) leftVbx.getChildren().get(0);
            leftVbx.getChildren().clear();
            leftVbx.getChildren().add(name);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // hides cards
    // MODIFIES: THIS
    // EFFECTS: removes cards from screen
    private void removeCards() {
        try {
            rightVbx.getChildren().clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // delete pop up
    // MODIFIES: THIS
    // EFFECTS: shows pop up when deleting something to confirm
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

    // deck button
    // MODIFIES: THIS
    // EFFECTS: if a deck is pressed, it will do the following:
    // if in delete mode will start the process of deleting deck
    // if there's a card in pending card irt will add it to the deck
    // otherwise it will shows the cards in the deck on the center v box on the screen
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

    // selected button
    // MODIFIES: THIS
    // EFFECTS: sets selected to button and shows all decks
    private void deckSelected(Button button) {
        selected = button;
        showAllDecks();
    }


    // deck button design
    // MODIFIES: THIS
    // EFFECTS: deck button size and design
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

    // cards of deck
    // MODIFIES: THIS
    // EFFECTS: shows cards of a deck
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

    // card button
    // MODIFIES: THIS
    // EFFECTS: create card button with design
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


    // card button
    // MODIFIES: THIS
    // EFFECTS: if card pressed and if in delete mode it will delete card, otherwise flip card
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

    // delete card alert
    // MODIFIES: THIS
    // EFFECTS: creates alert when deleting card to confirm. if not confirmed won't delete
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

    // create button
    // MODIFIES: THIS
    // EFFECTS: when create button pressed it will take inputs for front and back and adds to center vbox
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

    // rotate node
    // MODIFIES: THIS
    // EFFECTS: rotates node 90 degrees
    private void rotate90Deg(Node node, int second) {
        RotateTransition rt = new RotateTransition(Duration.millis(300), node);
        rt.setAxis(Rotate.Y_AXIS);
        rt.setFromAngle(second * 90);
        rt.setByAngle(90);
        rt.setAutoReverse(false);
        rt.play();

    }

    // instruction label
    // MODIFIES: THIS
    // EFFECTS: creates instruction label when creating card
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

    // textfield design
    // EFFECTS: a textfield design
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

    // add card
    // MODIFIES: THIS
    // EFFECTS: adds card to pending card to be added to a deck
    private void addCardToDeck(String front, String back) {
        Button button = showCard(front, back, null);
        rightVbx.getChildren().clear();
        rightVbx.getChildren().add(button);
        button.setAlignment(Pos.CENTER);
        pendingCard = new FlashCard(front, back);


    }

    // load button
    // MODIFIES: THIS
    // EFFECTS: when load button presses it will take input and load if account exists otherwise error alert
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


    // save button
    // MODIFIES: THIS
    // EFFECTS: call save method if presses
    private void saveButton(Button save) {
        save.setOnMouseClicked(event -> saveAccount());
    }

    // saves account
    // MODIFIES: THIS
    // EFFECTS: saves account and shows message of saves successfully
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

    // move button boxes
    // MODIFIES: THIS
    // EFFECTS: changes the place of right and center v boxes
    private void moveMenu() {
        if (centerVbx == mainBox.getChildren().get(1)) {
            mainBox.getChildren().remove(1, 3);
            mainBox.getChildren().addAll(rightVbx, centerVbx);
        }
    }

    // clear screen
    // MODIFIES: THIS
    // EFFECTS: empties vboxes: left,center,right
    private void emptyPage() {
        leftVbx.getChildren().clear();
        rightVbx.getChildren().clear();
        centerVbx.getChildren().clear();
    }

    // create deck button
    // MODIFIES: THIS
    // EFFECTS: button if pressed takes input to make deck, if deck exists error otherwise added to account
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

    // quit button
    // MODIFIES: THIS
    // EFFECTS: quit button if pressed will save and close screen
    private void quitButton(Button quit) {
        quit.setOnMouseClicked(event -> {
            saveAccount();
            stage.close();
        });
    }

    // makes a button
    // EFFECTS: return button with text and fixed design
    private Button makeAButton(String text) {
        Button button = new Button(text);
        mainButtonDesign(button);

        button.setOnMouseEntered(event -> mainButtonAnimation(button, 3));

        button.setOnMouseExited(event -> mainButtonAnimation(button, -4));

        return button;
    }

    // main button design
    // MODIFIES: button
    // EFFECTS: sets button to specific design
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

    // button hover
    // MODIFIES: THIS
    // EFFECTS: button animation when hovered over
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

    // makes vbox with spacing
    // EFFECTS: return vbox
    private VBox makeVBox() {
        VBox vbox = new VBox();
        vbox.setSpacing(30);
        return vbox;
    }
}