package com.comp2042;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.net.URL;

public class MenuController {
    @FXML
    private TextField nameField;

    public void startGame(ActionEvent event) throws Exception {
        String playerName = nameField.getText().trim();
        if (playerName.isEmpty()) {
            playerName = "Player";
        }

        Stage primaryStage = (Stage) ((Parent) event.getSource()).getScene().getWindow();

        URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        GuiController c = fxmlLoader.getController();

        primaryStage.setScene(new Scene(root, 550, 600));
        primaryStage.setTitle("TetrisJFX - " + playerName);

        new GameController(c);
    }

    public void showLeaderboard(ActionEvent event) {

        System.out.println("Leaderboard button pressed.");
    }

    public void exitGame(ActionEvent event) {
        Platform.exit();
    }
}