package main.javaothello;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SceneNavigator {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}