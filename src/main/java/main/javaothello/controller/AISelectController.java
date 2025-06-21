package main.javaothello.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import main.javaothello.MainApplication;
import main.javaothello.model.gameMode;

import java.io.IOException;

public class AISelectController {
    @FXML
    private RadioButton blackButton;

    @FXML
    private RadioButton whiteButton;

    @FXML
    private Button easyButton;

    @FXML
    private Button normalplayerButton;

    @FXML
    private Button hardButton;

    @FXML
    private Button backButton;

    private static int aiColor = 2; // 預設AI執白子
    private static int difficulty = 1; // 預設普通難度：0簡單，1普通，2困難

    @FXML
    private void initialize() {
        // 根據保存的設置初始化選項
        if (aiColor == 1) {
            blackButton.setSelected(true);
            whiteButton.setSelected(false);
        } else {
            blackButton.setSelected(false);
            whiteButton.setSelected(true);
        }
    }

    @FXML
    private void handleBlackButton() {
        aiColor = 1;
        blackButton.setSelected(true);
        whiteButton.setSelected(false);
    }

    @FXML
    private void handleWhiteButton() {
        aiColor = 2;
        blackButton.setSelected(false);
        whiteButton.setSelected(true);
    }

    @FXML
    private void handleEasyPlayerButton() throws IOException {
        difficulty = 0;
        startGame();
    }

    @FXML
    private void handleNormalButton() throws IOException {
        difficulty = 1;
        startGame();
    }

    @FXML
    private void handleHardButton() throws IOException {
        difficulty = 2;
        startGame();
    }

    private void startGame() throws IOException {
        singleplayerGameController.setAISettings(aiColor, difficulty);
        MainApplication.switchScene("/main/javaothello/view/game-view.fxml");
    }

    @FXML
    private void handleBackButton() throws IOException {
        MainApplication.switchScene("/main/javaothello/view/mode-select-view.fxml");
    }
}
