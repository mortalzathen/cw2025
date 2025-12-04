package com.comp2042.models.bricks; // Updated package

import com.comp2042.Constants; // Import the new Constants class
import com.comp2042.util.MatrixOperations; // Updated package

import java.util.ArrayList;
import java.util.List;


final class IBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    public IBrick() {
        // Replaced hardcoded IDs (1 and 0) with constants
        brickMatrix.add(new int[][]{
                {Constants.EMPTY_CELL, Constants.EMPTY_CELL, Constants.EMPTY_CELL, Constants.EMPTY_CELL},
                {Constants.I_BRICK_ID, Constants.I_BRICK_ID, Constants.I_BRICK_ID, Constants.I_BRICK_ID},
                {Constants.EMPTY_CELL, Constants.EMPTY_CELL, Constants.EMPTY_CELL, Constants.EMPTY_CELL},
                {Constants.EMPTY_CELL, Constants.EMPTY_CELL, Constants.EMPTY_CELL, Constants.EMPTY_CELL}
        });
        brickMatrix.add(new int[][]{
                {Constants.EMPTY_CELL, Constants.I_BRICK_ID, Constants.EMPTY_CELL, Constants.EMPTY_CELL},
                {Constants.EMPTY_CELL, Constants.I_BRICK_ID, Constants.EMPTY_CELL, Constants.EMPTY_CELL},
                {Constants.EMPTY_CELL, Constants.I_BRICK_ID, Constants.EMPTY_CELL, Constants.EMPTY_CELL},
                {Constants.EMPTY_CELL, Constants.I_BRICK_ID, Constants.EMPTY_CELL, Constants.EMPTY_CELL}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}