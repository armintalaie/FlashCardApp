package ui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.Deck;

// left column for flashcard app
public class LeftBox extends Box {

    LeftBox(GUI gui) {
        super(gui);
    }

    // all decks
    // EFFECTS: prints all decks
    void showAllDecks() {
        gui.moveMenu();
        Button edit;
        if (gui.deleteMode) {
            edit = makeAButton("Done");
        } else {
            edit = makeAButton("Edit");
        }
        createEditButton(edit);
        VBox vbox = makeVBox();

        removeDecks();
        for (Deck deck : this.gui.account.getDecks()) {
            Button button = new Button(deck.getName());
            deckButtonDesign(button);
            deckManageButton(button);
            vbox.getChildren().add(button);
        }
        vbox.getChildren().add(edit);
        box.getChildren().add(vbox);
        vbox.setAlignment(Pos.CENTER);
    }

    // hides decks
    // MODIFIES: THIS
    // EFFECTS: removes decks from screen
    private void removeDecks() {
        try {
            Label name = (Label) box.getChildren().get(0);
            box.getChildren().clear();
            box.getChildren().add(name);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // edit button
    // MODIFIES: THIS
    // EFFECTS: edit button design. and changes delete mode when pressed
    private void createEditButton(Button button) {
        button.setOnMouseClicked(event -> {
            if (!gui.deleteMode) {
                gui.deleteMode = true;
                button.setText("Done");
                showAllDecks();
            } else {
                gui.deleteMode = false;
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

    // deck button design
    // MODIFIES: THIS
    // EFFECTS: deck button size and design
    private void deckButtonDesign(Button button) {
        if (gui.selected != null && button.getText().equals(gui.selected.getText())) {
            button.setPrefSize(gui.WIDTH / 4, gui.HEIGHT / 10);
        } else {
            button.setPrefSize(gui.WIDTH / 4 - 30, gui.HEIGHT / 10 - 30);
        }

        String color = "#b3d9ff";
        if (gui.darkMode) {
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

    // deck button
    // MODIFIES: THIS
    // EFFECTS: if a deck is pressed, it will do the following:
    // if in delete mode will start the process of deleting deck
    // if there's a card in pending card irt will add it to the deck
    // otherwise it will shows the cards in the deck on the center v box on the screen
    private void deckManageButton(Button button) {
        button.setOnMouseClicked(event -> {
            Deck deck = this.gui.account.findDeck(button.getText());
            if (deleteDeckDialogue(deck)) {
                return;
            }
            deckSelected(button);
            if (gui.pendingCard != null) {
                if (!deck.addCard(gui.pendingCard)) {
                    return;
                }
                gui.pendingCard = null;
            }
            gui.rightBox.makeCardBoxes(deck);
        });
    }

    // selected button
    // MODIFIES: THIS
    // EFFECTS: sets selected to button and shows all decks
    void deckSelected(Button button) {
        gui.selected = button;
        showAllDecks();
    }

    // delete pop up
    // MODIFIES: THIS
    // EFFECTS: shows pop up when deleting something to confirm
    boolean deleteDeckDialogue(Deck deck) {
        if (gui.deleteMode) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "are you sure you want to delete "
                    + deck.getName() + " and the cards in it?");
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    this.gui.account.removeDeck(deck);
                    showAllDecks();
                    gui.rightBox.box.getChildren().clear();
                }
            });
            return true;
        }
        return false;
    }
}
