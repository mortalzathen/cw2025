package com.comp2042;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;
    private static final int PREVIEW_BRICK_SIZE = 14;
    private static final int HIDDEN_ROWS = 2;

    @FXML
    private GridPane gamePanel;

    @FXML
    public Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GameOverPanel gameOverPanel;

    @FXML
    private Label scoreLabel;

    @FXML
    private GridPane nextPanel1;
    @FXML
    private GridPane nextPanel2;
    @FXML
    private GridPane nextPanel3;

    @FXML
    private GridPane ghostPanel;

    @FXML
    private javafx.scene.control.Button shadowButton;

    @FXML
    private javafx.scene.control.Button restartButton;

    @FXML
    private javafx.scene.control.Button pauseButton;

    @FXML
    private javafx.scene.control.Button MenuButton;

    private boolean isShadowEnabled = true;

    private Rectangle[][] ghostRectangles;
    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    public final BooleanProperty isGameOver = new SimpleBooleanProperty();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        if (shadowButton != null) {
            shadowButton.setText("Shadow: " + (isShadowEnabled ? "ON" : "OFF"));
        }

        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                    if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                        refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                        refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                        refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.SPACE) {
                       eventListener.onHardDropEvent();
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.H) {
                        toggleShadow(null);
                        keyEvent.consume();
                    }
                }
                if (keyEvent.getCode() == KeyCode.N) {
                    newGame(null);
                }
                if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
                    if (isPause.get() == Boolean.FALSE) {
                        pauseGame(null);
                    } else {
                        pauseGame(null);
                    }
                }
                if (keyEvent.getCode() == KeyCode.ENTER){
                    if(isGameOver.get() == Boolean.TRUE){
                        new MenuController();
                    }
                }
            }
        });
        gameOverPanel.setVisible(false);

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = HIDDEN_ROWS; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - HIDDEN_ROWS);
            }
        }

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }

        ghostRectangles = new Rectangle[4][4];
        if (ghostPanel != null) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    Rectangle r = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                    r.setFill(Color.TRANSPARENT);
                    r.setArcHeight(9);
                    r.setArcWidth(9);
                    ghostRectangles[i][j] = r;
                    ghostPanel.add(r, j, i);
                }
            }
            ghostPanel.setVisible(isShadowEnabled);
        }

        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);

        if (ghostPanel != null) {
            ghostPanel.setLayoutX(brickPanel.getLayoutX());
            ghostPanel.setLayoutY(brickPanel.getLayoutY());
        }

        updatePreviews(brick);

        timeLine = new Timeline(new KeyFrame(
                Duration.millis(400),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    private Paint getFillColor(int i) {
        Paint returnPaint;
        switch (i) {
            case 0:
                returnPaint = Color.TRANSPARENT;
                break;
            case 1:
                returnPaint = Color.AQUA;
                break;
            case 2:
                returnPaint = Color.BLUEVIOLET;
                break;
            case 3:
                returnPaint = Color.DARKGREEN;
                break;
            case 4:
                returnPaint = Color.YELLOW;
                break;
            case 5:
                returnPaint = Color.RED;
                break;
            case 6:
                returnPaint = Color.BEIGE;
                break;
            case 7:
                returnPaint = Color.BURLYWOOD;
                break;
            default:
                returnPaint = Color.WHITE;
                break;
        }
        return returnPaint;
    }

    // --- MODIFIED: getGhostColor (Now returns translucent grey regardless of piece type) ---
    private Paint getGhostColor(int i) {
        // If the piece is empty, return transparent
        if (i == 0) {
            return Color.TRANSPARENT;
        }

        // Return a semi-transparent grey (e.g., 40% opacity)
        return Color.GRAY.deriveColor(0, 1, 1, 0.4);
    }
    // --- END MODIFIED ---


    public void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {

            int ghostY = brick.getGhostYPosition();
            int deltaY = ghostY - brick.getyPosition();
            double vgap = brickPanel.getVgap();
            double yTranslateDistance = deltaY * (BRICK_SIZE + vgap);

            double calculatedX = gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE;
            double calculatedY = -40 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE;

            brickPanel.setLayoutX(calculatedX);
            brickPanel.setLayoutY(calculatedY);

            if (ghostPanel != null) {
                ghostPanel.setLayoutX(calculatedX);
                ghostPanel.setLayoutY(calculatedY);
            }

            if (isShadowEnabled && ghostPanel != null) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        Rectangle r = ghostRectangles[i][j];
                        int colorIndex = brick.getBrickData()[i][j];

                        r.setTranslateY(0);

                        if (colorIndex != 0) {
                            r.setTranslateY(yTranslateDistance);
                            // Set solid translucent grey fill
                            r.setFill(getGhostColor(colorIndex));

                            r.setStroke(null);
                            r.setStrokeWidth(0);
                        } else {
                            r.setFill(Color.TRANSPARENT);
                        }
                    }
                }
                ghostPanel.setVisible(true);
            } else if (ghostPanel != null) {
                ghostPanel.setVisible(false);
            }
            // --- END MODIFIED ---

            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    Rectangle r = rectangles[i][j];
                    r.setStroke(null);
                    r.setStrokeWidth(0);
                    setRectangleData(brick.getBrickData()[i][j], r);
                }
            }

            updatePreviews(brick);
        }
    }

    private void updatePreviews(ViewData brick) {
        drawPreview(nextPanel1, brick.getNextBrickData1());
        drawPreview(nextPanel2, brick.getNextBrickData2());
        drawPreview(nextPanel3, brick.getNextBrickData3());
    }

    private void drawPreview(GridPane panel, int[][] data) {
        if (panel == null || data == null) return;
        panel.getChildren().clear();
        int rows = data.length;
        int cols = rows > 0 ? data[0].length : 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Rectangle r = new Rectangle(PREVIEW_BRICK_SIZE, PREVIEW_BRICK_SIZE);
                r.setFill(getFillColor(data[i][j]));
                r.setArcHeight(6);
                r.setArcWidth(6);
                panel.add(r, j, i);
            }
        }
    }

    public void refreshGameBackground(int[][] board) {
        for (int i = HIDDEN_ROWS; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
        rectangle.setFill(getFillColor(color));
    }

    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            refreshBrick(downData.getViewData());
        }
    }

    private void hardDrop(ViewData viewData) {
        if (isPause.getValue() == Boolean.FALSE) {
            refreshBrick(viewData);
        }
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty integerProperty) {
        if (scoreLabel != null) {
            scoreLabel.textProperty().bind(integerProperty.asString());
        }
    }

    public void gameOver() {
        timeLine.stop();
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
    }

    public void newGame(ActionEvent actionEvent) {
        timeLine.stop();
        restartButton.setText("Restart");
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        timeLine.play();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
    }

    public void pauseGame(ActionEvent actionEvent) {
        if(isPause.getValue() == Boolean.FALSE) {
            timeLine.stop();
            pauseButton.setText("Pause");
            isPause.setValue(Boolean.TRUE);
        }
        else {
            timeLine.play();
            pauseButton.setText("Resume");
            isPause.setValue(Boolean.FALSE);
        }
    }

    public void toggleShadow(ActionEvent evt) {
        isShadowEnabled = !isShadowEnabled;
        if (shadowButton != null) {
            shadowButton.setText("Shadow: " + (isShadowEnabled ? "ON" : "OFF"));
        }
        if (ghostPanel != null) {
            ghostPanel.setVisible(isShadowEnabled);
        }
        gamePanel.requestFocus();
    }

    public void gotoMenu(ActionEvent evt) throws Exception {
        Stage primaryStage = (Stage) ((Parent) evt.getSource()).getScene().getWindow();
        timeLine.stop();
        MenuButton.setText("Menu");
        URL location = getClass().getClassLoader().getResource("menuLayout.fxml");
        ResourceBundle resources = null;
        FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("TetrisJFX - Menu");
        Scene scene = new Scene(root, 475, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}