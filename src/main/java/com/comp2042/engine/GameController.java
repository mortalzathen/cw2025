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

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(Constants.BOARD_HEIGHT, Constants.BOARD_WIDTH);

    private final GuiController viewGuiController;
    private String playerName;
    private IntegerProperty scoreProperty;

    public GameController(GuiController c) {
        viewGuiController = c;
        viewGuiController.setEventListener(this);

        this.playerName = MenuController.playerName;
        this.scoreProperty = board.getScore().scoreProperty();

        board.newGame();

        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(this.scoreProperty);
    }

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

    private void submitScoreAndGameOver() {

        int finalScore = this.scoreProperty.get();

        LeaderboardManager.addScore(this.playerName, finalScore);

        viewGuiController.gameOver();
    }

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