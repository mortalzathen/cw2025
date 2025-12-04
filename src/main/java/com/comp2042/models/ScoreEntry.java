package com.comp2042.models;

import java.io.Serializable;

public class ScoreEntry implements Comparable<ScoreEntry>, Serializable {
    private static final long serialVersionUID = 1L;

    private final String playerName;
    private final int score;

    public ScoreEntry(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(ScoreEntry other) {
        return Integer.compare(other.score, this.score);
    }
}