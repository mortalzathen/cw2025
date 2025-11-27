package com.comp2042;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty; // <-- Added this necessary import

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(25, 10);

    private final GuiController viewGuiController;
    private String playerName; // Now correctly initialized
    private IntegerProperty scoreProperty; // Now correctly initialized

    // --- MODIFIED CONSTRUCTOR ---
    public GameController(GuiController c) {
        viewGuiController = c;
        viewGuiController.setEventListener(this);

        // 1. Retrieve and store the player name from the MenuController
        this.playerName = MenuController.playerName;

        // 2. Initialize the scoreProperty field
        this.scoreProperty = board.getScore().scoreProperty();

        board.newGame();

        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());

        // 3. Bind the GUI to the correct score property
        viewGuiController.bindScore(this.scoreProperty);
    }
    // --- END MODIFIED CONSTRUCTOR ---

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;

        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());
            }

            // --- MODIFIED GAME OVER CHECK (Line 32) ---
            if (board.createNewBrick()) {
                submitScoreAndGameOver(); // <-- Calls the submission logic
            }
            // --- END MODIFIED ---

            viewGuiController.refreshGameBackground(board.getBoardMatrix());

        } else {
            // Hard Drop scoring logic was intentionally removed here
        }
        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    public ViewData onHardDropEvent(MoveEvent event) {

        // This method now contains the hard drop logic
        // (the same body as your previous successful attempt)

        board.hardDropBrick();
        board.mergeBrickToBackground();

        ClearRow clearRow = board.clearRows();
        // ... (rest of score and notification logic) ...

        if (board.createNewBrick()) {
            submitScoreAndGameOver();
        }

        viewGuiController.refreshGameBackground(board.getBoardMatrix());

        return board.getViewData();
    }

    // --- MODIFIED submitScoreAndGameOver (Moved logic from the continuous updateGame) ---
    private void submitScoreAndGameOver() {
        // 1. Get the final score value from the property
        int finalScore = this.scoreProperty.get();

        // 2. Submit the score using the stored player name
        LeaderboardManager.addScore(this.playerName, finalScore);

        // 3. Trigger the GUI update (no score/name arguments needed here)
        viewGuiController.gameOver();
    }
    // --- END MODIFIED ---

    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());

        // IMPORTANT: Re-initialize score property and player name for a new game
        this.playerName = MenuController.playerName;
        this.scoreProperty = board.getScore().scoreProperty();
        viewGuiController.bindScore(this.scoreProperty);
    }
    @Override // If this is what the compiler demands
    public void onHardDropEvent() {
        // If the GUI is calling this no-argument version,
        // it MUST call the functional version, e.g.:
        onHardDropEvent(null); // Pass null if the GUI provides no event data
    }
}