package ui;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

//abstract for columns (vertical boxes) in flashcard app
public abstract class Box {
    GUI gui;
    VBox box;

    Box(GUI gui) {
        this.gui = gui;
        makeMainVBoxes();
    }

    // makes V boxes for main screen
    // MODIFIES: THIS
    // EFFECTS: creates a v box with WIDTH / 3, HEIGHT
    private void makeMainVBoxes() {
        box = new VBox();
        box.setPrefSize(gui.WIDTH / 3, gui.HEIGHT);
        box.setAlignment(Pos.CENTER);
    }

    VBox getBox() {
        return this.box;
    }

    public void setBox(VBox box) {
        this.box = box;
    }


    // makes a button
    // EFFECTS: return button with text and fixed design
    Button makeAButton(String text) {
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
        button.setMinSize(gui.WIDTH / 7, gui.HEIGHT / 14);
        button.setMaxSize(gui.WIDTH / 5, gui.HEIGHT / 12);
        button.setPrefSize(gui.WIDTH / 6, gui.HEIGHT / 13);
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
    VBox makeVBox() {
        VBox vbox = new VBox();
        vbox.setSpacing(30);
        return vbox;
    }
}
