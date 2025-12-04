package com.comp2042.models.bricks;

import com.comp2042.Constants;
import com.comp2042.util.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

final class JBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    public JBrick() {
        brickMatrix.add(new int[][]{
                {Constants.EMPTY_CELL, Constants.EMPTY_CELL, Constants.EMPTY_CELL, Constants.EMPTY_CELL},
                {Constants.J_BRICK_ID, Constants.J_BRICK_ID, Constants.J_BRICK_ID, Constants.EMPTY_CELL},
                {Constants.EMPTY_CELL, Constants.EMPTY_CELL, Constants.J_BRICK_ID, Constants.EMPTY_CELL},
                {Constants.EMPTY_CELL, Constants.EMPTY_CELL, Constants.EMPTY_CELL, Constants.EMPTY_CELL}
        });
        brickMatrix.add(new int[][]{
                {Constants.EMPTY_CELL, Constants.EMPTY_CELL, Constants.EMPTY_CELL, Constants.EMPTY_CELL},
                {Constants.EMPTY_CELL, Constants.J_BRICK_ID, Constants.J_BRICK_ID, Constants.EMPTY_CELL},
                {Constants.EMPTY_CELL, Constants.J_BRICK_ID, Constants.EMPTY_CELL, Constants.EMPTY_CELL},
                {Constants.EMPTY_CELL, Constants.J_BRICK_ID, Constants.EMPTY_CELL, Constants.EMPTY_CELL}
        });
        brickMatrix.add(new int[][]{
                {Constants.EMPTY_CELL, Constants.EMPTY_CELL, Constants.EMPTY_CELL, Constants.EMPTY_CELL},
                {Constants.EMPTY_CELL, Constants.J_BRICK_ID, Constants.EMPTY_CELL, Constants.EMPTY_CELL},
                {Constants.EMPTY_CELL, Constants.J_BRICK_ID, Constants.J_BRICK_ID, Constants.J_BRICK_ID},
                {Constants.EMPTY_CELL, Constants.EMPTY_CELL, Constants.EMPTY_CELL, Constants.EMPTY_CELL}
        });
        brickMatrix.add(new int[][]{
                {Constants.EMPTY_CELL, Constants.EMPTY_CELL, Constants.J_BRICK_ID, Constants.EMPTY_CELL},
                {Constants.EMPTY_CELL, Constants.EMPTY_CELL, Constants.J_BRICK_ID, Constants.EMPTY_CELL},
                {Constants.EMPTY_CELL, Constants.J_BRICK_ID, Constants.J_BRICK_ID, Constants.EMPTY_CELL},
                {Constants.EMPTY_CELL, Constants.EMPTY_CELL, Constants.EMPTY_CELL, Constants.EMPTY_CELL}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}