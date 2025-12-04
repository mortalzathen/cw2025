package com.comp2042.events;

import com.comp2042.util.MatrixOperations;

public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData1;
    private final int[][] nextBrickData2;
    private final int[][] nextBrickData3;

    private final int ghostYPosition;

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData1, int[][] nextBrickData2, int[][] nextBrickData3, int ghostYPosition) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData1 = nextBrickData1;
        this.nextBrickData2 = nextBrickData2;
        this.nextBrickData3 = nextBrickData3;
        this.ghostYPosition = ghostYPosition;
    }

    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public int[][] getNextBrickData1() {
        return MatrixOperations.copy(nextBrickData1);
    }

    public int[][] getNextBrickData2() {
        return MatrixOperations.copy(nextBrickData2);
    }

    public int[][] getNextBrickData3() {
        return MatrixOperations.copy(nextBrickData3);
    }

    public int getGhostYPosition() {
        return ghostYPosition;
    }
}