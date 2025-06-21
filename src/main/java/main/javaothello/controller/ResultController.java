package main.javaothello.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import main.javaothello.MainApplication;
import main.javaothello.model.State;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ResultController implements Initializable {
    @FXML private Text scoreText;
    @FXML private Text winnerText;
    @FXML private Text timeText;
    @FXML private Button newGameButton;

    private static State finalState;
    private static String gameTime;

    public static void setFinalState(State state, String time) {
        finalState = state;
        gameTime = time;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupResults();
        setupButtons();
    }

    private void setupResults() {
        if (finalState != null) {
            scoreText.setText(String.format("黑子: %d  白子: %d",
                finalState.getBlackScore(), finalState.getWhiteScore()));

            if (finalState.getBlackScore() > finalState.getWhiteScore()) {
                winnerText.setText("黑子勝利！");
            } else if (finalState.getWhiteScore() > finalState.getBlackScore()) {
                winnerText.setText("白子勝利！");
            } else {
                winnerText.setText("平局！");
            }
        }

        if (gameTime != null) {
            timeText.setText("遊戲時間：" + gameTime);
        }
    }

    private void setupButtons() {
        newGameButton.setOnAction(event -> {
            try {
                handleNewGameButton();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void handleNewGameButton() throws IOException {
        MainApplication.switchScene("/main/javaothello/view/game-view.fxml");
    }

    @FXML
    private void handleExitButton() throws IOException {
        MainApplication.switchScene("/main/javaothello/view/mode-select-view.fxml");
    }
}
