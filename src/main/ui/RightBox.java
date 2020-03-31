package ui;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import model.Deck;
import model.FlashCard;

// Right (content) column for flashcard app
public class RightBox extends Box {

    RightBox(GUI gui) {
        super(gui);
    }

    // cards of deck
    // MODIFIES: THIS
    // EFFECTS: shows cards of a deck
    void makeCardBoxes(Deck deck) {
        VBox vbox = makeVBox();
        removeCards();

        for (FlashCard card : deck.getCards()) {
            Button button1 = showCard(card.getFront(), card.getBack(), deck);
            vbox.getChildren().add(button1);
        }
        box.getChildren().add(vbox);
        vbox.setAlignment(Pos.TOP_CENTER);
    }

    // card button
    // MODIFIES: THIS
    // EFFECTS: create card button with design
    private Button showCard(String front, String back, Deck deck) {
        Button button1 = new Button(front);
        button1.setPrefSize(gui.WIDTH / 3, gui.HEIGHT / 10);
        button1.setAlignment(Pos.CENTER);
        String color = "#b3d9ff";
        if (gui.darkMode) {
            color = "#445573";
        }
        button1.setStyle("-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.1) , 10, 0.7 , 4 , 9 );\n"
                + "-fx-border-color: " + color + "; -fx-padding:5px;\n"
                + "-fx-font-weight: bold;\n"
                + "-fx-font-size: 30px;\n"
                + "-fx-background-color: linear-gradient(" + color + ", " + color + ");\n"
                + "-fx-border-radius: 10;\n"
                + "-fx-background-radius: 10;\n"
                + "-fx-text-fill: #FFFFFF;\n");
        cardManageButton(button1, front, back, deck);
        button1.setWrapText(true);
        button1.setTextAlignment(TextAlignment.CENTER);

        return button1;

    }

    // hides cards
    // MODIFIES: THIS
    // EFFECTS: removes cards from screen
    private void removeCards() {
        try {
            box.getChildren().clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // card button
    // MODIFIES: THIS
    // EFFECTS: if card pressed and if in delete mode it will delete card, otherwise flip card
    private void cardManageButton(Button button, String front, String back, Deck deck) {
        button.setOnMouseClicked(event -> {
            deleteCardDialogue(front, back, deck);

            gui.rotate90Deg(button, 0);

            if (button.getText().equals(front)) {
                button.setText(back);
                button.setScaleX(-1);
            } else if (button.getText().equals(back)) {
                button.setText(front);
            }
            gui.rotate90Deg(button, 1);
        });
    }

    void createDeckButton(Button createDeck) {
        createDeck.setOnMouseClicked(event -> {
            gui.moveMenu();
            TextField textField = new TextField();
            textField.setPrefSize(gui.WIDTH / 3, gui.HEIGHT / 10);
            textField.setAlignment(Pos.CENTER);
            box.getChildren().clear();
            box.getChildren().add(gui.instructionLabels("Type Deck Name"));
            box.getChildren().add(textField);
            gui.textFieldStyle(textField);
            textField.setOnAction(event1 -> {
                if (this.gui.account.makeDeck(textField.getText())) {
                    gui.leftBox.showAllDecks();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Deck with that name exists");
                    alert.showAndWait();
                }
                box.getChildren().clear();
            });
        });
    }

    // create button
    // MODIFIES: THIS
    // EFFECTS: when create button pressed it will take inputs for front and back and adds to center vbox
    void createCardButton(Button createCard) {
        createCard.setOnMouseClicked(event -> {
            if (decksEmpty()) {
                return;
            }
            gui.moveMenu();
            TextField textField = new TextField();
            textField.setPrefSize(gui.WIDTH / 3, gui.HEIGHT / 10);
            box.getChildren().clear();
            box.getChildren().addAll(gui.instructionLabels("Enter Card's Front"), textField);
            gui.textFieldStyle(textField);

            textField.setOnAction(event1 -> {
                String front = textField.getText();
                Button button1 = showCard(front, "", null);
                box.getChildren().clear();
                box.getChildren().addAll(gui.instructionLabels("Enter Card's Back"), button1);
                gui.rotate90Deg(button1, 0);
                gui.rotate90Deg(button1, 1);
                textField.clear();
                box.getChildren().set(1, textField);
                textField.setOnAction(event2 -> addCardToDeck(front, textField.getText()));
            });
        });
    }

    // add card
    // MODIFIES: THIS
    // EFFECTS: adds card to pending card to be added to a deck
    private void addCardToDeck(String front, String back) {
        Button button = showCard(front, back, null);
        box.getChildren().clear();
        box.getChildren().add(button);
        button.setAlignment(Pos.CENTER);
        try {
            gui.pendingCard = new FlashCard(front, back);
        } catch (Exception e) {
            //
        }

        gui.leftBox.showAllDecks();
        Node node = box.getChildren().get(0);
        box.getChildren().clear();
        box.getChildren().addAll(gui.instructionLabels("Click a deck to add"), node);

    }

    // delete card alert
    // MODIFIES: THIS
    // EFFECTS: creates alert when deleting card to confirm. if not confirmed won't delete
    void deleteCardDialogue(String front, String back, Deck deck) {
        if (gui.deleteMode) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "are you sure you want to delete this card?");
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    deck.removeCard(front);
                    gui.leftBox.showAllDecks();
                    makeCardBoxes(deck);
                }
            });
        }
    }

    // deck empty alert
    // EFFECTS: play an error sound if decks are empty and returns true otherwise does nothing and returns false
    boolean decksEmpty() {
        if (gui.account.numberOfDecks() == 0) {
            gui.playErrorSound();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "there are no decks. first make one");
            alert.showAndWait();
            return true;
        }
        return false;
    }

}
