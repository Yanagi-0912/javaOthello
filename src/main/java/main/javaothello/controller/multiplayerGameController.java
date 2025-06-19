package main.javaothello.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import main.javaothello.MainApplication;
import main.javaothello.model.GameBoard;
import main.javaothello.model.State;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class multiplayerGameController implements Initializable {
    @FXML private GridPane boardGrid;
    @FXML private Text timerText;
    @FXML private Button pauseButton;
    @FXML private Button newGameButton;
    @FXML private Text statusText;
    @FXML private Text scoreText;

    private Timeline timeline;
    private Timeline gameUpdateTimeline;
    private int seconds = 0;
    private boolean isPaused = false;
    private GameBoard gameBoard;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameBoard = new GameBoard(boardGrid);
        initializeGame();
        initializeTimer();
        setupButtons();
        setupGameUpdates();
    }

    private void setupGameUpdates() {
        gameUpdateTimeline = new Timeline(
            new KeyFrame(Duration.millis(100), event -> updateGameState())
        );
        gameUpdateTimeline.setCycleCount(Timeline.INDEFINITE);
        gameUpdateTimeline.play();
    }

    private void updateGameState() {
        State state = gameBoard.getGameState();
        updateScore(state);
        updateStatus(state);
        checkGameOver(state);
    }

    private void updateScore(State state) {
        scoreText.setText(String.format("黑子: %d  白子: %d",
            state.getBlackScore(), state.getWhiteScore()));
    }

    private void updateStatus(State state) {
        if (state.isGameOver()) {
            statusText.setText(getWinnerText(state));
            if (gameUpdateTimeline != null) {
                gameUpdateTimeline.stop();
            }
        } else if (state.isSkipTurn()) {
            statusText.setText("對手無棋可下，繼續下棋");
            state.setSkipTurn(false);
        } else {
            statusText.setText(state.getCurrentPlayer() == 1 ? "黑子回合" : "白子回合");
        }
    }

    private String getWinnerText(State state) {
        if (state.getBlackScore() > state.getWhiteScore()) {
            return "遊戲結束 - 黑子勝利！";
        } else if (state.getWhiteScore() > state.getBlackScore()) {
            return "遊戲結束 - 白子勝利！";
        } else {
            return "遊戲結束 - 平局！";
        }
    }

    private void checkGameOver(State state) {
        if (state.isGameOver() && !isPaused) {
            timeline.pause();
            gameUpdateTimeline.stop();
            isPaused = true;

            // 保存結果並切換到結算畫面
            String gameTime = timerText.getText();
            ResultController.setFinalState(state, gameTime);
            try {
                MainApplication.switchScene("/main/javaothello/view/result-view.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeGame() {
        gameBoard.initializeBoard();
        State state = gameBoard.getGameState();
        updateScore(state);
        updateStatus(state);
    }

    private void initializeTimer() {
        timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> {
                seconds++;
                updateTimer();
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateTimer() {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        timerText.setText(String.format("%02d:%02d", minutes, secs));
    }

    @FXML
    private void handlePauseButton() {
        if (isPaused) {
            timeline.play();
            gameUpdateTimeline.play();
            pauseButton.setText("暫停");
            statusText.setText("遊戲繼續");
        } else {
            timeline.pause();
            gameUpdateTimeline.pause();
            pauseButton.setText("繼續");
            statusText.setText("遊戲暫停");
        }
        isPaused = !isPaused;
    }

    private void setupButtons() {
        pauseButton.setOnAction(event -> handlePauseButton());
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
        seconds = 0;
        updateTimer();
        if (isPaused) {
            timeline.play();
            gameUpdateTimeline.play();
            pauseButton.setText("暫停");
            isPaused = false;
        }
        initializeGame();
        statusText.setText("新局開始 - 黑子回合");
    }

    @FXML
    private void handleExitButton() throws IOException {
        if (timeline != null) {
            timeline.stop();
        }
        if (gameUpdateTimeline != null) {
            gameUpdateTimeline.stop();
        }
        MainApplication.switchScene("/main/javaothello/view/mode-select-view.fxml");
    }
}
