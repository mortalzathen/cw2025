package com.comp2042.engine;

import com.comp2042.events.DownData;
import com.comp2042.events.InputEventListener;
import com.comp2042.events.MoveEvent;
import com.comp2042.events.ViewData;
import com.comp2042.models.ClearRow;
import com.comp2042.models.LeaderboardManager;
import com.comp2042.ui.GuiController;
import com.comp2042.ui.MenuController;
import com.comp2042.ui.NotificationPanel;
import com.comp2042.Constants;
import javafx.beans.property.IntegerProperty;

/**
 * The main controller for the Tetris game logic. Implements the InputEventListener
 * interface to handle all user input and timed events (down movements).
 * This class manages the board state, score submission, and synchronizes with the GUI.
 */
public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(Constants.BOARD_HEIGHT, Constants.BOARD_WIDTH);

    private final GuiController viewGuiController;
    private String playerName;
    private IntegerProperty scoreProperty;

    /**
     * Initializes the GameController, setting up the board, retrieving the player name,
     * and binding the score property to the GUI.
     *
     * @param c The GuiController instance responsible for rendering the game.
     */
    public GameController(GuiController c) {
        viewGuiController = c;
        viewGuiController.setEventListener(this);

        this.playerName = MenuController.playerName;
        this.scoreProperty = board.getScore().scoreProperty();

        board.newGame();

        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(this.scoreProperty);
    }

    /**
     * Handles the soft drop or timed movement event. Merges the brick if collision occurs,
     * clears rows, and checks for game over.
     *
     * @param event Details about the move event (Source, Type).
     * @return DownData containing any cleared row information and the current ViewData.
     */
    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;

        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());
                // Removed showScoreNotification call
            }

            if (board.createNewBrick()) {
                submitScoreAndGameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());

        } else {
            // Soft Drop Logic (No points awarded, as requested)
        }
        return new DownData(clearRow, board.getViewData());
    }

    /**
     * Handles the left movement event.
     *
     * @param event Details about the move event.
     * @return ViewData showing the new position of the brick.
     */
    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    /**
     * Handles the right movement event.
     *
     * @param event Details about the move event.
     * @return ViewData showing the new position of the brick.
     */
    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    /**
     * Handles the rotate movement event.
     *
     * @param event Details about the move event.
     * @return ViewData showing the new position of the brick.
     */
    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    /**
     * Handles the hard drop event (SPACE key). Drops the piece instantly, merges it,
     * submits the score, and checks for game over.
     */
    public ViewData onHardDropEvent(MoveEvent event) {

        board.hardDropBrick();
        board.mergeBrickToBackground();

        ClearRow clearRow = board.clearRows();
        if (clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());
            NotificationPanel notificationPanel = new NotificationPanel("+" + clearRow.getScoreBonus());
            viewGuiController.groupNotification.getChildren().add(notificationPanel);
            notificationPanel.showScore(viewGuiController.groupNotification.getChildren());
        }

        if (board.createNewBrick()) {
            viewGuiController.gameOver();
        }

        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        viewGuiController.refreshBrick(board.getViewData());
        return board.getViewData();
    }

    /**
     * Submits the final score and player name to the LeaderboardManager and triggers the GUI game over screen.
     */
    private void submitScoreAndGameOver() {

        int finalScore = board.getScore().scoreProperty().get(); // Assuming this is how you get the score
        System.out.println("Attempting to save score: " + finalScore + " for " + this.playerName);
        LeaderboardManager.addScore(this.playerName, finalScore);
        viewGuiController.gameOver();
    }

    /**
     * Resets the game board and initializes a new game instance.
     */
    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());

        this.playerName = MenuController.playerName;
        this.scoreProperty = board.getScore().scoreProperty();
        viewGuiController.bindScore(this.scoreProperty);
    }

    public void onHardDropEvent(){
        onHardDropEvent(null);
    }
}