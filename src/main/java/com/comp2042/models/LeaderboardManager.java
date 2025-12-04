package com.comp2042.models;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardManager {

    private static final String FILE_PATH = "leaderboard.dat";

    private static final List<ScoreEntry> scores = new ArrayList<>();
    private static final int MAX_ENTRIES = 10;

    static {
        loadScoresFromFile();
    }

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


    public static List<ScoreEntry> getTopScores() {
        return new ArrayList<>(scores);
    }

    private static void loadScoresFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {

            List<ScoreEntry> loadedScores = (List<ScoreEntry>) ois.readObject();

            scores.clear();
            scores.addAll(loadedScores);
            sortAndTrim();
            System.out.println("Leaderboard loaded successfully.");

        } catch (FileNotFoundException e) {
            System.out.println("No existing leaderboard file found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading leaderboard: " + e.getMessage());
        }
    }

    private static void saveScoresToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {

            oos.writeObject(scores);
            System.out.println("Leaderboard saved successfully.");

        } catch (IOException e) {
            System.err.println("Error saving leaderboard: " + e.getMessage());
        }
    }
}