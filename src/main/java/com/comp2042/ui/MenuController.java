package com.comp2042.ui;

import com.comp2042.engine.GameController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;

public class MenuController {
    @FXML
    private TextField nameField;
    public static String playerName;
    public void startGame(ActionEvent event) throws Exception {
        playerName = nameField.getText().trim();
        if (playerName.isEmpty()) {
            playerName = "Player";
        }

        Stage primaryStage = (Stage) ((Parent) event.getSource()).getScene().getWindow();

        URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        GuiController c = fxmlLoader.getController();

        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.setTitle("TetrisJFX - " + playerName);

        new GameController(c);
    }

    public void showLeaderboard(ActionEvent event) throws Exception {
        try {
            Stage primaryStage = (Stage) ((Parent) event.getSource()).getScene().getWindow();

            Scene currentScene = primaryStage.getScene();
            double width = currentScene != null ? currentScene.getWidth() : 600;
            double height = currentScene != null ? currentScene.getHeight() : 600;

            URL location = getClass().getClassLoader().getResource("leaderboardLayout.fxml");
            if (location == null) {
                throw new IOException("Cannot find leaderboardLayout.fxml resource.");
            }
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            Parent root = fxmlLoader.load();

            Scene leaderboardScene = new Scene(root, width, height);

            primaryStage.setScene(leaderboardScene);
            primaryStage.setTitle("TetrisJFX - Leaderboard");

        } catch (IOException e) {
            System.err.println("Error loading leaderboard scene: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void exitGame(ActionEvent event) {
        Platform.exit();
    }
}