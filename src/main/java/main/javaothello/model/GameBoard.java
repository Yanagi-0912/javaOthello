package main.javaothello.model;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.javaothello.controller.GameController;

public class GameBoard {
    private final GridPane boardGrid;        // JavaFX網格布局，用於顯示棋盤
    private final State gameState;           // 遊戲狀態對象，保存當前遊戲狀態
    private static final int BOARD_SIZE = 8; // 棋盤大小（8x8）
    private static final double CELL_SIZE = 60.0;    // 每個棋格的大小（像素）
    private static final double DISC_RADIUS = 20.0;  // 棋子半徑（像素）
    private gameMode gameMode;
    private int aiPlayer = 0; // AI玩家編號，0表示無AI
    private final int depth[] = {1, 3, 5};
    private int difficulty = 0; // 默認難度級別
    /**
     * 構造函數
     * @param boardGrid JavaFX網格布局組件，用於顯示棋盤
     */
    public GameBoard(GridPane boardGrid, gameMode gameMode, int aiPlayer) {
        this.boardGrid = boardGrid;
        this.gameState = new State(gameMode);
        if(aiPlayer != 0) {
            this.aiPlayer = aiPlayer; // 設置AI玩家編號
            this.difficulty = GameController.getAIDifficulty(); // 從GameController獲取難度設置
        }
    }

    /**
     * 初始化棋盤
     * 清空棋盤，創建空白格子，設置初始棋子
     */
    public void initializeBoard() {
        boardGrid.getChildren().clear();     // 清空棋盤上所��元素
        createEmptyCells();                  // 創建空白格子
        initializeGameState();               // 初始化遊戲狀態
        updateBoard();                       // 更新棋盤顯示

        // 如果是AI先手（執黑），則立即下棋
        if (gameState.getGameMode() == gameMode.SINGLE_PLAYER && aiPlayer == 1) {
            AI ai = new AI(gameState, aiPlayer);
            Position aiMove = ai.minMax(depth[difficulty]);
            if (aiMove != null) {
                makeMove(aiMove.row, aiMove.col, aiPlayer);
                gameState.updateScores();
                changePlayer(); // 切換到玩家回合
            }
        }
    }

    /**
     * 初始化遊戲狀���
     * 設置初始的四個棋子（黑白各兩個）
     */
    private void initializeGameState() {
        int[][] board = gameState.getBoard();
        // 設置初始四顆棋子的狀態（棋盤中心的2x2格子）
        board[3][3] = 2; // 白子
        board[3][4] = 1; // 黑子
        board[4][3] = 1; // 黑子
        board[4][4] = 2; // 白子
        gameState.setBoard(board);
        gameState.setCurrentPlayer(1); // 設置黑子先手
        gameState.updateScores();      // 更新遊戲狀態
    }

    /**
     * 創建空白棋盤格
     * 為棋盤的每個位置創建一個可點擊的格子
     */
    private void createEmptyCells() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                StackPane cell = createCell(row, col);
                boardGrid.add(cell, col, row);
            }
        }
    }

    /**
     * 創建單個棋格
     * @param row 行號
     * @param col 列號
     * @return 返回創建好的棋格（StackPane對象）
     */
    private StackPane createCell(int row, int col) {
        StackPane cell = new StackPane();
        cell.setPrefSize(CELL_SIZE, CELL_SIZE);                           // 設置格子大小
        cell.setStyle("-fx-background-color: green; -fx-border-color: black;"); // 設置格子樣式
        cell.setOnMouseClicked(event -> handleCellClick(row, col));      // 設置點擊事件處理
        return cell;
    }

    /**
     * 處理棋格點擊事件
     * @param row 被點��的格子的行號
     * @param col 被點擊的格子的列號
     */
    private void handleCellClick(int row, int col) {
        // 檢查該位置是否為空
        if (gameState.getBoard()[row][col] == 0) {
            int currentPlayer = gameState.getCurrentPlayer();

            // 如果是AI的回合，則不處理玩家的點擊
            if (gameState.getGameMode() == gameMode.SINGLE_PLAYER && currentPlayer == aiPlayer) {
                return;
            }

            // 檢查是否為合法落子位置
            if (gameState.isValidMove(row, col)) {
                makeMove(row, col, currentPlayer);    // 落子
                gameState.updateScores();    // 更新遊戲狀態

                // 檢查是否遊戲結束
                if (isGameOver()) {
                    gameState.setGameOver(true);
                    return;
                }

                // 檢查對手是否有合法移動
                int opponent = gameState.getOpponent(currentPlayer);
                if (!gameState.hasValidMoves(opponent)) {
                    // 如果當前玩家也沒有合法移動，遊戲結束
                    if (!gameState.hasValidMoves(currentPlayer)) {
                        gameState.setGameOver(true);
                        return;
                    } else {
                        // 對手沒有合法移動，跳過其回合
                        gameState.setSkipTurn(true);
                        return;
                    }
                }

                changePlayer();
                // 單人模式：切換到AI
                if (gameState.getGameMode() == gameMode.SINGLE_PLAYER) {
                    currentPlayer = gameState.getCurrentPlayer();
                    if (currentPlayer == aiPlayer) {
                        // 如果是AI玩家，讓AI進行下一步
                        AI ai = new AI(gameState, aiPlayer);
                        Position aiMove = ai.minMax(depth[difficulty]);
                        if (aiMove != null) {
                            makeMove(aiMove.row, aiMove.col, aiPlayer);
                            gameState.updateScores();

                            // 檢查AI下棋後是否遊戲結束
                            if (isGameOver()) {
                                gameState.setGameOver(true);
                                return;
                            }

                            // 檢查玩家是否有合法移動
                            if (!gameState.hasValidMoves(gameState.getOpponent(aiPlayer))) {
                                // 如果AI也沒有合法移動，遊戲結束
                                if (!gameState.hasValidMoves(aiPlayer)) {
                                    gameState.setGameOver(true);
                                    return;
                                } else {
                                    // 玩家沒有合法移動，AI繼續
                                    gameState.setSkipTurn(true);
                                    return;
                                }
                            }
                            changePlayer(); // 切換回人類玩家
                        } else {
                            // AI無子可下，檢查玩家是否有子可下
                            if (!gameState.hasValidMoves(gameState.getOpponent(aiPlayer))) {
                                gameState.setGameOver(true);
                                return;
                            }
                            gameState.setSkipTurn(true);
                            changePlayer();
                        }
                    }
                }
            }
        }
    }

    /**
     * 切換玩家
     * 將當前玩家更改為對手
     */
    private void changePlayer() {
        int currentPlayer = gameState.getCurrentPlayer();
        gameState.setCurrentPlayer(currentPlayer == 1 ? 2 : 1);
    }

    /**
     * 落子
     * 在指定位置放置棋子，並翻轉被夾住的對手棋子
     * @param row 行號
     * @param col 列號
     * @param player 當前玩家（1代表黑子，2代表白子）
     */
    private void makeMove(int row, int col, int player) {
        gameState.getBoard()[row][col] = player;
        updateCell(row, col, player == 1 ? Color.BLACK : Color.WHITE);
        flipDiscs(row, col, player);
    }

    /**
     * 更新棋盤顯示
     * 根據當前遊戲狀態更新棋盤上所有棋子的顯示
     */
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

    /**
     * 更新單個棋格的顯示
     * @param row 行號
     * @param col 列號
     * @param color 棋子顏色
     */
    private void updateCell(int row, int col, Color color) {
        Circle disc = new Circle(DISC_RADIUS, color);
        StackPane cell = (StackPane) boardGrid.getChildren().get(row * BOARD_SIZE + col);
        if (cell != null) {
            cell.getChildren().clear();
            cell.getChildren().add(disc);
        }
    }


    /**
     * 翻轉被夾住的對手棋子
     * @param row 行號
     * @param col 列號
     * @param player 當前玩家（1代表黑子，2代表白子）
     */
    private void flipDiscs(int row, int col, int player) {
        int[][] directions = {{-1,-1}, {-1,0}, {-1,1}, {0,-1}, {0,1}, {1,-1}, {1,0}, {1,1}};
        int[][] board = gameState.getBoard();
        int opponent = gameState.getOpponent(player);

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


    /**
     * 檢查遊戲是否結束
     * @return 如果遊戲結束，返回true；否則返回false
     */
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
        return boardIsFull || (!gameState.hasValidMoves(1) && !gameState.hasValidMoves(2));
    }

    /**
     * 獲取當前遊戲狀態
     * @return 當前遊戲狀態對象
     */
    public State getGameState() {
        return gameState;
    }
}
