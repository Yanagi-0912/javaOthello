package main.javaothello.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import main.javaothello.MainApplication;
import main.javaothello.model.gameMode;

import java.io.IOException;

public class AISelectController {
    @FXML
    private RadioButton blackButton;

    @FXML
    private RadioButton whiteButton;

    @FXML
    private ToggleGroup colorGroup;

    @FXML
    private Button easyButton;

    @FXML
    private Button normalButton;

    @FXML
    private Button hardButton;

    @FXML
    private Button backButton;

    private static int aiColor = 2; // 預設AI執白子
    private static int difficulty = 0; // 預設普通難度：0簡單，1普通，2困難

    @FXML
    private void initialize() {
        // 初始化 ToggleGroup
        if (colorGroup == null) {
            colorGroup = new ToggleGroup();
            blackButton.setToggleGroup(colorGroup);
            whiteButton.setToggleGroup(colorGroup);
        }

        // 根據保存的設置���始化選項
        if (aiColor == 1) {
            blackButton.setSelected(true);
        } else {
            whiteButton.setSelected(true);
        }
    }

    @FXML
    private void handleBlackButton() {
        if (blackButton.isSelected()) {
            aiColor = 1;
        }
    }

    @FXML
    private void handleWhiteButton() {
        if (whiteButton.isSelected()) {
            aiColor = 2;
        }
    }

    @FXML
    private void handleEasyButton() throws IOException {
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
        GameController.setAISettings(aiColor, difficulty);
        MainApplication.switchScene("/main/javaothello/view/game-view.fxml");
    }

    @FXML
    private void handleBackButton() throws IOException {
        MainApplication.switchScene("/main/javaothello/view/mode-select-view.fxml");
    }
}
