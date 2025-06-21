package main.javaothello.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import main.javaothello.MainApplication;

import java.io.IOException;

public class ModeSelectController {
    @FXML
    private Text titleText;

    @FXML
    private Button singlePlayerButton;

    @FXML
    private Button multiplayerButton;

    @FXML
    private Button backButton;

    @FXML
    private void handleSinglePlayerButton() throws IOException {
        MainApplication.switchScene("/main/javaothello/view/ai-select-view.fxml");
    }

    @FXML
    private void handleMultiplayerButton() throws IOException {
        MainApplication.switchScene("/main/javaothello/view/game-view.fxml");
    }

    @FXML
    private void handleBackButton() throws IOException {
        MainApplication.switchScene("/main/javaothello/view/start-view.fxml");
    }

}
