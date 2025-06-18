package main.javaothello.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import main.javaothello.MainApplication;

import java.io.IOException;

public class StartController {
    @FXML
    private Text titleText;

    @FXML
    private Button startButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button exitButton;

    @FXML
    private void handleStartButton() throws IOException {
        MainApplication.switchScene("/main/javaothello/view/mode-select-view.fxml");
    }

    @FXML
    private void handleSettingsButton() throws IOException {
        MainApplication.switchScene("/main/javaothello/view/settings-view.fxml");
    }

    @FXML
    private void handleExitButton() {
        System.exit(0);
    }
}