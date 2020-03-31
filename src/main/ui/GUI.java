package ui;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
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
import model.FlashCard;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.*;
import java.util.ArrayList;

// GUI for Flash Card Application
public class GUI extends Application {

    static double WIDTH = 1200;
    static double HEIGHT = 720;
    Account account;
    Stage stage;
    private HBox mainBox;
    FlashCard pendingCard = null;
    boolean deleteMode = false;
    Button selected = null;
    boolean darkMode = false;
    CenterBox centerBox;
    RightBox rightBox;
    LeftBox leftBox;


    // starts application
    // MODIFIES: THIS
    // EFFECTS: creates windows and initial graphics elements
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        Pane group = new Pane();
        mainBox = new HBox();
        leftBox = new LeftBox(this);
        rightBox = new RightBox(this);
        centerBox = new CenterBox(this);
        mainBox.getChildren().addAll(leftBox.getBox(), centerBox.getBox(), rightBox.getBox());
        group.getChildren().add(mainBox);
        Scene scene = new Scene(group, WIDTH, HEIGHT);

        Circle cir2 = new Circle(WIDTH - 50, HEIGHT - 50, 30);
        darkModeButton(cir2);
        group.getChildren().add(cir2);
        primaryStage.setScene(scene);

        centerBox.launchButton();
        primaryStage.show();
        primaryStage.setResizable(false);
        mainScreenAnimation();
        stage = primaryStage;
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
            rightBox.box.getChildren().clear();
            try {
                leftBox.box.getChildren().remove(1, leftBox.box.getChildren().size());
            } catch (Exception e) {
                leftBox.box.getChildren().clear();
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


    // error sound
    // EFFECTS: plays an error sound
    void playErrorSound() {
        try {
            InputStream in = new FileInputStream("data/error-sound.wav");
            AudioStream as = new AudioStream(in);
            AudioPlayer.player.start(as);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // make textfield
    // EFFECTS: creates textfield with certain design
    public TextField makeTextField() {
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

    // rotate node
    // MODIFIES: THIS
    // EFFECTS: rotates node 90 degrees
    void rotate90Deg(Node node, int second) {
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
    public Label instructionLabels(String title) {
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
    void textFieldStyle(TextField textField) {
        textField.setStyle("-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.1) , 10, 0.7 , 4 , 9 );\n"
                + "-fx-border-color: #b3d9ff; -fx-padding:3px;\n"
                + "-fx-font-weight: bold;\n"
                + "-fx-font-size: 60px;\n"
                + "-fx-background-color: linear-gradient(#b3d9ff, #b3d9ff);\n"
                + "-fx-border-radius: 10;\n"
                + "-fx-background-radius: 10;\n"
                + "-fx-text-fill: #FFFFFF;\n");
        textField.autosize();
        textField.setAlignment(Pos.CENTER);
    }

    // move button boxes
    // MODIFIES: THIS
    // EFFECTS: changes the place of right and center v boxes
    void moveMenu() {
        if (centerBox.getBox() == mainBox.getChildren().get(1)) {
            mainBox.getChildren().remove(1, 3);
            mainBox.getChildren().addAll(rightBox.getBox(), centerBox.getBox());
        }
    }

    // clear screen
    // MODIFIES: THIS
    // EFFECTS: empties vboxes: left,center,right
    public void emptyPage() {
        leftBox.getBox().getChildren().clear();
        rightBox.getBox().getChildren().clear();
        centerBox.getBox().getChildren().clear();
    }


}