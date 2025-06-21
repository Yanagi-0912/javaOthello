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

    /**
     * 獲取對手玩家
     * @param player 當前玩家（1代表黑子，2代表白子）
     * @return 對手玩家（1代表黑子，2代表白子）
     */
    public int getOpponent(int player) {
        return player == 1 ? 2 : 1;
    }

    /**
     * 檢查玩家是否有合法移動
     * @param player 玩家編號（1或2）
     * @return 如果玩家有合法移動，返回true；否則返回false
     */
    public boolean hasValidMoves(int player) {
        int[][] board = getBoard();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] == 0) {
                    int currentPlayer = getCurrentPlayer();
                    setCurrentPlayer(player);
                    boolean isValid = isValidMove(row, col);
                    setCurrentPlayer(currentPlayer);
                    if (isValid) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 檢查是否為合法落子位置
     * @param row 行號
     * @param col 列號
     * @return 如果該位置為合法落子位置，返回true；否則返回false
     */
    public boolean isValidMove(int row, int col) {
        if (getBoard()[row][col] != 0) {
            return false;
        }

        int currentPlayer = getCurrentPlayer();
        int opponent = getOpponent(currentPlayer);
        int[][] board = getBoard();

        // 檢查八個方向
        int[][] directions = {{-1,-1}, {-1,0}, {-1,1}, {0,-1}, {0,1}, {1,-1}, {1,0}, {1,1}};

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            boolean hasOpponentDisc = false;

            // 檢查這個方向是否可以翻轉對手的棋子
            while (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
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
}