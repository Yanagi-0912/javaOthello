package main.javaothello.model;

public class State {
    private int currentPlayer; // 1 for black, 2 for white
    private int[][] board; // 8x8 board represented as a 2D array
    private int blackScore;
    private int whiteScore;
    private boolean isGameOver;
    private boolean skipTurn;
    private gameMode gameMode; // Game mode (single player or multiplayer)

    public State(gameMode gameMode) {
        this.currentPlayer = 1; // Start with black
        this.board = new int[8][8]; // Initialize an empty board
        this.blackScore = 2; // Initial score for black
        this.whiteScore = 2; // Initial score for white
        this.isGameOver = false;
        this.skipTurn = false;
        this.gameMode = gameMode; // Set the game mode
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public int getBlackScore() {
        return blackScore;
    }

    public void setBlackScore(int blackScore) {
        this.blackScore = blackScore;
    }

    public int getWhiteScore() {
        return whiteScore;
    }

    public void setWhiteScore(int whiteScore) {
        this.whiteScore = whiteScore;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public boolean isSkipTurn() {
        return skipTurn;
    }

    public void setSkipTurn(boolean skipTurn) {
        this.skipTurn = skipTurn;
    }
    public gameMode getGameMode() {
        return gameMode;
    }
}
