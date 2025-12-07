package com.comp2042.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages the high score list, handling persistence (saving/loading to file),
 * score submission, sorting, and trimming the list size using JSON format.
 */
public class LeaderboardManager {

    // IMPORTANT: Changed file extension to .json
    private static final String FILE_PATH = "leaderboard.json";

    private static final List<ScoreEntry> scores = new ArrayList<>();
    private static final int MAX_ENTRIES = 10;

    static {
        loadScoresFromFile();
    }

    /**
     * Submits a new score entry, sorts the list, trims it to MAX_ENTRIES, and saves the file.
     *
     * @param playerName The name of the player.
     * @param score The final score achieved.
     */
    public static void addScore(String playerName, int score) {
        scores.add(new ScoreEntry(playerName, score));
        sortAndTrim();
        saveScoresToFile();
    }

    private static void sortAndTrim() {
        Collections.sort(scores);
        if (scores.size() > MAX_ENTRIES) {
            scores.subList(MAX_ENTRIES, scores.size()).clear();
        }
    }

    /**
     * Retrieves the top scores from the list.
     *
     * @return A new list containing the top scores, sorted high to low.
     */
    public static List<ScoreEntry> getTopScores() {
        return new ArrayList<>(scores);
    }

    private static void loadScoresFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            System.out.println("No existing leaderboard file found. Starting fresh.");
            return;
        }

        Gson gson = new Gson();

        try (Reader reader = new FileReader(FILE_PATH, StandardCharsets.UTF_8)) {

            // Define the type of object we are loading (a List of ScoreEntry)
            Type listType = new TypeToken<List<ScoreEntry>>() {}.getType();

            List<ScoreEntry> loadedScores = gson.fromJson(reader, listType);

            if (loadedScores != null) {
                scores.clear();
                scores.addAll(loadedScores);
                sortAndTrim();
                System.out.println("Leaderboard loaded successfully from JSON.");
            } else {
                System.out.println("Leaderboard file was empty or corrupt.");
            }

        } catch (IOException e) {
            System.err.println("Error loading leaderboard (JSON read error): " + e.getMessage());
        }
    }

    private static void saveScoresToFile() {
        Gson gson = new Gson();

        try (Writer writer = new FileWriter(FILE_PATH, StandardCharsets.UTF_8)) {

            // Convert the scores list to a JSON string and write it to the file
            gson.toJson(scores, writer);
            writer.flush(); // Ensure data is written
            System.out.println("Leaderboard saved successfully to JSON.");

        } catch (IOException e) {
            System.err.println("Error saving leaderboard (JSON write error): " + e.getMessage());
            e.printStackTrace();
        }
    }
}