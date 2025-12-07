package com.comp2042.ui;

import com.comp2042.models.LeaderboardManager;
import com.comp2042.models.ScoreEntry;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class LeaderboardController implements Initializable {

    @FXML
    private ListView<String> scoreListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadScores();
    }

    public void loadScores() {
        List<ScoreEntry> scores = LeaderboardManager.getTopScores();

        // --- FIX IS HERE: Use AtomicInteger to track rank reliably ---
        AtomicInteger rank = new AtomicInteger(1);

        List<String> displayScores = scores.stream()
                .map(entry -> String.format("#%d: %s - %d",
                        rank.getAndIncrement(), // <-- Uses the guaranteed counter instead of indexOf()
                        entry.getPlayerName(),
                        entry.getScore()))
                .collect(Collectors.toList());
        // -----------------------------------------------------------

        scoreListView.setItems(FXCollections.observableArrayList(displayScores));

        if (displayScores.isEmpty()) {
            scoreListView.getItems().add("No scores recorded yet. Start a game!");
        }
    }


    public void backToMenu(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Parent) event.getSource()).getScene().getWindow();

            URL location = getClass().getClassLoader().getResource("menuLayout.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            Parent root = fxmlLoader.load();

            Scene menuScene = new Scene(root, stage.getWidth(), stage.getHeight());
            stage.setScene(menuScene);
            stage.setTitle("TetrisJFX - Main Menu");

        } catch (IOException e) {
            System.err.println("Error loading menu scene: " + e.getMessage());
            e.printStackTrace();
        }
    }
}