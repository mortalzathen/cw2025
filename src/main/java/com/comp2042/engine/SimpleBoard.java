package com.comp2042.engine;

import com.comp2042.util.BrickRotator;
import com.comp2042.util.MatrixOperations;
import com.comp2042.util.NextShapeInfo;
import com.comp2042.events.ViewData;
import com.comp2042.models.bricks.Brick;
import com.comp2042.models.bricks.BrickGenerator;
import com.comp2042.models.bricks.RandomBrickGenerator;
import com.comp2042.models.ClearRow;
import com.comp2042.models.Score;
import com.comp2042.Constants;

import java.awt.geom.Point2D;
import java.util.List;

public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;

    private Point2D currentOffset;
    private final Score score;

    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    public int getGhostYPosition() {
        int ghostY = (int) currentOffset.getY();

        while (!MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), ghostY + 1)) {
            ghostY++;
        }
        return ghostY;
    }

    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);

        Point2D.Double p = new Point2D.Double(currentOffset.getX(), currentOffset.getY());

        p.setLocation(p.getX() + 0, p.getY() + 1.0);

        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());

        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }


    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point2D.Double p = new Point2D.Double(currentOffset.getX(), currentOffset.getY());

        p.setLocation(p.getX() - 1, p.getY() + 0);

        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean moveBrickRight() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point2D.Double p = new Point2D.Double(currentOffset.getX(), currentOffset.getY());

        p.setLocation(p.getX() + 1, p.getY() + 0);

        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean rotateLeftBrick() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        NextShapeInfo nextShape = brickRotator.getNextShape();

        boolean conflict = MatrixOperations.intersect(currentMatrix, nextShape.getShape(), (int) currentOffset.getX(), (int) currentOffset.getY());

        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }

    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);

        currentOffset = new Point2D.Double(Constants.START_X, Constants.START_Y);

        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    @Override
    public ViewData getViewData() {
        int[][] n1;
        int[][] n2;
        int[][] n3;
        if (brickGenerator instanceof RandomBrickGenerator rbg) {
            List<Brick> list = rbg.getNextBricks(3);
            n1 = list.size() > 0 ? list.get(0).getShapeMatrix().get(0) : new int[0][0];
            n2 = list.size() > 1 ? list.get(1).getShapeMatrix().get(0) : new int[0][0];
            n3 = list.size() > 2 ? list.get(2).getShapeMatrix().get(0) : new int[0][0];
        } else {
            int[][] single = brickGenerator.getNextBrick().getShapeMatrix().get(0);
            n1 = single;
            n2 = single;
            n3 = single;
        }

        return new ViewData(brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY(), n1, n2, n3, getGhostYPosition());
    }

    public void hardDropBrick() {
        int ghostY = getGhostYPosition();
        currentOffset.setLocation(currentOffset.getX(), (double) ghostY);
    }

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;
    }

    @Override
    public Score getScore() {
        return score;
    }


    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        createNewBrick();
    }
}