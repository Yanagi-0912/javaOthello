package main.javaothello;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    private static Stage primaryStage;
    private static final double WINDOW_WIDTH = 800;
    private static final double WINDOW_HEIGHT = 600;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        initializeStage();
        loadStartScene();
    }

    private void initializeStage() {
        primaryStage.setTitle("黑白棋");
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
    }

    private void loadStartScene() throws IOException {
        try {
            String fxmlPath = "/main/javaothello/view/start-view.fxml";
            var url = getClass().getResource(fxmlPath);

            if (url == null) {
                System.err.println("找不到FXML文件：" + fxmlPath);
                throw new IOException("FXML文件不存在");
            }

            System.out.println("正在加载FXML：" + url);
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            Scene scene = new Scene(fxmlLoader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);

            String cssPath = "/main/javaothello/css/styles.css";
            var cssUrl = getClass().getResource(cssPath);
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("加载场景时发生错误：" + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public static void switchScene(String fxmlPath) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxmlPath));
            Scene scene = new Scene(fxmlLoader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);
            scene.getStylesheets().add(MainApplication.class.getResource("/main/javaothello/css/styles.css").toExternalForm());
            primaryStage.setScene(scene);
        } catch (IOException e) {
            System.err.println("場景切換失敗：" + e.getMessage());
            throw e;
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }
}