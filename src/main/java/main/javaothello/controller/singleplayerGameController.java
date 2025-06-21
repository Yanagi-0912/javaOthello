package main.javaothello.controller;

import javafx.fxml.FXML;
import main.javaothello.model.gameMode;

public class singleplayerGameController {
    private static int aiPlayer = 2; // 預設AI執白子
    private static int aiDifficulty = 1; // 預設普通難度

    // 設置AI參數的靜態方法
    public static void setAISettings(int color, int difficulty) {
        aiPlayer = color;
        aiDifficulty = difficulty;
    }

    // 獲取AI玩家顏色
    public static int getAIPlayer() {
        return aiPlayer;
    }

    // 獲取AI難度
    public static int getAIDifficulty() {
        return aiDifficulty;
    }

    // ...existing code...
}
