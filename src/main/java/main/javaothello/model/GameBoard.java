package main.javaothello.model;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GameBoard {
    private final GridPane boardGrid;
    private final State gameState;
    private static final int BOARD_SIZE = 8;
    private static final double CELL_SIZE = 60.0;
    private static final double DISC_RADIUS = 20.0;

    public GameBoard(GridPane boardGrid) {
        this.boardGrid = boardGrid;
        this.gameState = new State();
    }

    public void initializeBoard() {
        boardGrid.getChildren().clear();
        createEmptyCells();
        initializeGameState();
        updateBoard();  // 替換 placeInitialDiscs
    }

    private void initializeGameState() {
        int[][] board = gameState.getBoard();
        // 設置初始四顆棋子的狀態
        board[3][3] = 2; // 白子
        board[3][4] = 1; // 黑子
        board[4][3] = 1; // 黑子
        board[4][4] = 2; // 白子
        gameState.setBoard(board);
        gameState.setCurrentPlayer(1); // 黑子先手
        updateGameStatus();  // 替換 updateScores
    }

    private void updateGameStatus() {
        int blackCount = 0;
        int whiteCount = 0;
        int[][] board = gameState.getBoard();

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] == 1) blackCount++;
                else if (board[row][col] == 2) whiteCount++;
            }
        }

        gameState.setBlackScore(blackCount);
        gameState.setWhiteScore(whiteCount);
    }

    private void createEmptyCells() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                StackPane cell = createCell(row, col);
                boardGrid.add(cell, col, row);
            }
        }
    }

    private StackPane createCell(int row, int col) {
        StackPane cell = new StackPane();
        cell.setPrefSize(CELL_SIZE, CELL_SIZE);
        cell.setStyle("-fx-background-color: green; -fx-border-color: black;");
        cell.setOnMouseClicked(event -> handleCellClick(row, col));
        return cell;
    }

    private void handleCellClick(int row, int col) {
        if (gameState.getBoard()[row][col] == 0) {
            int currentPlayer = gameState.getCurrentPlayer();
            if (isValidMove(row, col)) {
                makeMove(row, col, currentPlayer);
                updateGameStatus();  // 替換 updateScores
                if (!hasValidMoves(getOpponent(currentPlayer))) {
                    if (!hasValidMoves(currentPlayer)) {
                        gameState.setGameOver(true);
                    } else {
                        gameState.setSkipTurn(true);
                    }
                } else {
                    changePlayer();  // 替換 switchPlayer
                }
            }
        }
    }

    private void changePlayer() {
        int currentPlayer = gameState.getCurrentPlayer();
        gameState.setCurrentPlayer(currentPlayer == 1 ? 2 : 1);
    }

    private void makeMove(int row, int col, int player) {
        gameState.getBoard()[row][col] = player;
        updateCell(row, col, player == 1 ? Color.BLACK : Color.WHITE);  // 替換 addDisc
        flipDiscs(row, col, player);
    }

    private void updateBoard() {
        int[][] board = gameState.getBoard();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] != 0) {
                    updateCell(row, col, board[row][col] == 1 ? Color.BLACK : Color.WHITE);
                }
            }
        }
    }

    private void updateCell(int row, int col, Color color) {
        Circle disc = new Circle(DISC_RADIUS, color);
        StackPane cell = (StackPane) boardGrid.getChildren().get(row * BOARD_SIZE + col);
        if (cell != null) {
            cell.getChildren().clear();
            cell.getChildren().add(disc);
        }
    }

    private boolean isValidMove(int row, int col) {
        if (gameState.getBoard()[row][col] != 0) {
            return false;
        }

        int currentPlayer = gameState.getCurrentPlayer();
        int opponent = getOpponent(currentPlayer);
        int[][] board = gameState.getBoard();

        // 檢查八個方向
        int[][] directions = {{-1,-1}, {-1,0}, {-1,1}, {0,-1}, {0,1}, {1,-1}, {1,0}, {1,1}};

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            boolean hasOpponentDisc = false;

            // 檢查這個方向是否可以翻轉對手的棋子
            while (newRow >= 0 && newRow < BOARD_SIZE && newCol >= 0 && newCol < BOARD_SIZE) {
                if (board[newRow][newCol] == opponent) {
                    hasOpponentDisc = true;
                } else if (board[newRow][newCol] == currentPlayer && hasOpponentDisc) {
                    return true;
                } else {
                    break;
                }
                newRow += dir[0];
                newCol += dir[1];
            }
        }
        return false;
    }

    private void flipDiscs(int row, int col, int player) {
        int[][] directions = {{-1,-1}, {-1,0}, {-1,1}, {0,-1}, {0,1}, {1,-1}, {1,0}, {1,1}};
        int[][] board = gameState.getBoard();
        int opponent = getOpponent(player);

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            boolean hasOpponentDisc = false;
            java.util.ArrayList<int[]> toFlip = new java.util.ArrayList<>();

            while (newRow >= 0 && newRow < BOARD_SIZE && newCol >= 0 && newCol < BOARD_SIZE) {
                if (board[newRow][newCol] == opponent) {
                    hasOpponentDisc = true;
                    toFlip.add(new int[]{newRow, newCol});
                } else if (board[newRow][newCol] == player && hasOpponentDisc) {
                    // 翻轉這個方向上的所有棋子
                    for (int[] pos : toFlip) {
                        board[pos[0]][pos[1]] = player;
                        updateCell(pos[0], pos[1], player == 1 ? Color.BLACK : Color.WHITE);
                    }
                    break;
                } else {
                    break;
                }
                newRow += dir[0];
                newCol += dir[1];
            }
        }
    }

    private int getOpponent(int player) {
        return player == 1 ? 2 : 1;
    }

    private boolean hasValidMoves(int player) {
        int[][] board = gameState.getBoard();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] == 0) {
                    int currentPlayer = gameState.getCurrentPlayer();
                    gameState.setCurrentPlayer(player);
                    boolean isValid = isValidMove(row, col);
                    gameState.setCurrentPlayer(currentPlayer);
                    if (isValid) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isGameOver() {
        int[][] board = gameState.getBoard();
        boolean boardIsFull = true;

        // 檢查棋盤是否已滿
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] == 0) {
                    boardIsFull = false;
                    break;
                }
            }
        }

        // 如果棋盤已滿或雙方都無法下棋，遊戲結束
        return boardIsFull || (!hasValidMoves(1) && !hasValidMoves(2));
    }

    public State getGameState() {
        return gameState;
    }
}
