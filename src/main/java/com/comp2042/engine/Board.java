package com.comp2042.engine;

import com.comp2042.events.ViewData;
import com.comp2042.models.ClearRow;
import com.comp2042.models.Score;

public interface Board {

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    boolean createNewBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();

    ClearRow clearRows();

    Score getScore();

    void newGame();

    void hardDropBrick();
}
