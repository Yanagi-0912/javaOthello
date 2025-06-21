package main.javaothello.model;

public class AI {
    private State gameState; // The current game state
    private int aiPlayer; // The AI player (1 for black, 2 for white)

    public AI(State gameState, int aiPlayer) {
        this.gameState = gameState;
        this.aiPlayer = aiPlayer;
    }

    int nextRow(int row, int direction) {
        switch (direction) {
            case 0: return row - 1; // Up
            case 1: return row - 1; // Up-Right
            case 2: return row;     // Right
            case 3: return row + 1; // Down-Right
            case 4: return row + 1; // Down
            case 5: return row + 1; // Down-Left
            case 6: return row;     // Left
            case 7: return row - 1; // Up-Left
            default: return row;    // Invalid direction, stay in place
        }
    }

    int nextCol(int col, int direction) {
        switch (direction) {
            case 0: return col;     // Up
            case 1: return col + 1; // Up-Right
            case 2: return col + 1; // Right
            case 3: return col + 1; // Down-Right
            case 4: return col;     // Down
            case 5: return col - 1; // Down-Left
            case 6: return col - 1; // Left
            case 7: return col - 1; // Up-Left
            default: return col;    // Invalid direction, stay in place
        }
    }

    public int countFlipPieces(int row, int col) {
        int result = 0;
        // 檢查是否是合法的位置
        if (gameState.getBoard()[row][col] != 0) {
            return 0;
        }

        for (int direction = 0; direction < 8; direction++) {
            int count = 0;  // 移到這裡，每個方向重新計數
            int r = nextRow(row, direction);
            int c = nextCol(col, direction);

            while (r >= 0 && r < 8 && c >= 0 && c < 8) {
                int piece = gameState.getBoard()[r][c];
                if (piece == 0) {
                    break; // 空格，停止檢查此方向
                } else if (piece != aiPlayer) {
                    count++; // 找到對手的棋子，繼續檢查
                } else {
                    if (count > 0) {
                        result += count; // 找到有效的翻轉
                    }
                    break; // 找到自己的棋子，停止檢查此方向
                }
                r = nextRow(r, direction);
                c = nextCol(c, direction);
            }
        }
        return result;
    }

    public State flipPieces(int row, int col) {
        State newState = new State(gameState.getGameMode());
        int[][] newBoard = new int[8][8];  // 創建新的棋盤
        // 複製原始棋盤
        for (int i = 0; i < 8; i++) {
            System.arraycopy(gameState.getBoard()[i], 0, newBoard[i], 0, 8);
        }
        newState.setBoard(newBoard);
        newState.setCurrentPlayer(aiPlayer);

        // 放置新棋子
        newBoard[row][col] = aiPlayer;

        // 檢查八個方向
        for (int direction = 0; direction < 8; direction++) {
            java.util.ArrayList<int[]> toFlip = new java.util.ArrayList<>();
            int r = nextRow(row, direction);
            int c = nextCol(col, direction);

            while (r >= 0 && r < 8 && c >= 0 && c < 8) {
                int piece = newBoard[r][c];
                if (piece == 0) {
                    break; // 空格，停止檢查
                } else if (piece != aiPlayer) {
                    toFlip.add(new int[]{r, c}); // 記錄可能要翻轉的位置
                } else {
                    // 找到自己的棋子，翻轉中間的所有棋子
                    for (int[] pos : toFlip) {
                        newBoard[pos[0]][pos[1]] = aiPlayer;
                    }
                    break;
                }
                r = nextRow(r, direction);
                c = nextCol(c, direction);
            }
        }

        // 更新分數
        newState.updateScores();
        return newState;
    }

    public int minNode(int depth, int alpha, int beta) {
        if (depth == 0 || gameState.isGameOver()) {
            return gameState.getScore(aiPlayer) - gameState.getScore(3-aiPlayer);
        }

        int minEval = Integer.MAX_VALUE;
        boolean hasValidMove = false;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (gameState.isValidMove(row, col)) {
                    hasValidMove = true;
                    State newState = flipPieces(row, col);
                    State oldState = gameState;
                    gameState = newState;
                    int eval = maxNode(depth - 1, alpha, beta);
                    gameState = oldState;
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    if (beta <= alpha) {
                        break; // Alpha-Beta pruning
                    }
                }
            }
        }

        // 如果沒有合法移動，立即返回當前局面評分
        if (!hasValidMove) {
            return gameState.getScore(aiPlayer) - gameState.getScore(3-aiPlayer);
        }

        return minEval;
    }

    public int maxNode(int depth, int alpha, int beta) {
        if (depth == 0 || gameState.isGameOver()) {
            return gameState.getScore(aiPlayer) - gameState.getScore(3-aiPlayer);
        }

        int maxEval = Integer.MIN_VALUE;
        boolean hasValidMove = false;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (gameState.isValidMove(row, col)) {
                    hasValidMove = true;
                    State newState = flipPieces(row, col);
                    State oldState = gameState;
                    gameState = newState;
                    int eval = minNode(depth - 1, alpha, beta);
                    gameState = oldState;
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha) {
                        break; // Alpha-Beta pruning
                    }
                }
            }
        }

        // 如果沒有合法移動，立即返回當前局面評分
        if (!hasValidMove) {
            return gameState.getScore(aiPlayer) - gameState.getScore(3-aiPlayer);
        }

        return maxEval;

    }

    public Position minMax(int depth) {
        Position bestMove = null;
        int bestValue = Integer.MIN_VALUE;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (gameState.isValidMove(row, col)) {
                    State newState = flipPieces(row, col);
                    State oldState = gameState;
                    gameState = newState;
                    int value = minNode(depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    gameState = oldState;

                    if (value > bestValue) {
                        bestValue = value;
                        bestMove = new Position(row, col);
                    }
                }
            }
        }
        return bestMove;
    }
}