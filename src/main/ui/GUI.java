package ui;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Account;

public class GUI extends Application {

    private static final int WIDTH = 1600;
    private static final int HEIGHT = 900;
    private Account account;
    private BorderPane borderPane;

    @Override
    public void start(Stage primaryStage) throws Exception {
        borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: #6699ff");
        Scene scene = new Scene(borderPane, WIDTH, HEIGHT);
        makeButton(borderPane);
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    private void makeButton(BorderPane pane) {
        Button button = makeAButton("Launch App");
        button.setAlignment(Pos.CENTER);
        pane.setCenter(button);
        button.setOnMouseClicked(event -> {
            VBox vbox = makeVBox();
            pane.getChildren().remove(button);
            pane.setCenter(vbox);
            vbox.setAlignment(Pos.CENTER);
            Button create = makeAButton("Create an account");
            Button load = makeAButton("Load an account");
            vbox.getChildren().addAll(create, load);
            create.setOnMouseClicked(event1 -> {
                TextField textField = new TextField();
                pane.getChildren().remove(vbox);
                pane.setTop(textField);

                textField.setOnAction(event11 -> {
                    createAccount(textField.getText());

                    borderPane.getChildren().remove(textField);
                });
            });
        });
    }

    private void createAccount(String text) {
        account = new Account(text);
        Button createDeck = makeAButton("Create a deck");
        Button createCard = makeAButton("Create a Flashcard");
        Button load = makeAButton("Load");
        Button save = makeAButton("Save");
        Button quit = makeAButton("Quit");
        VBox vbox = makeVBox();
        vbox.getChildren().addAll(createDeck,createCard,load,save,quit);
        borderPane.setCenter(vbox);
        vbox.setAlignment(Pos.CENTER);
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
