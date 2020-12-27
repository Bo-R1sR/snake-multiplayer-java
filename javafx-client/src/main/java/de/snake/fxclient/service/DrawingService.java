package de.snake.fxclient.service;

import de.snake.fxclient.controller.GameController;
import de.snake.fxclient.domain.User;
import de.snake.fxclient.game.*;
import de.snake.fxclient.game.composite.*;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DrawingService {

    private final Playground playground;
    private final ScreenText screenText;
    private final GameController gameController;
    private final User user;
    private final Level level;


    public DrawingService(Playground playground, ScreenText screenText, GameController gameController, User user, Level level) {
        this.playground = playground;
        this.screenText = screenText;
        this.gameController = gameController;
        this.user = user;
        this.level = level;
    }

    // called from SessionHandler when Countdown ScreenText arrives
    public void updateScreenText() {
        GraphicsContext gc = gameController.getGC();
        Shape background = new Square(gc, Color.BLACK, new Point2D(0, 0), playground.getWidth() * playground.getSnakeBodySize(), playground.getHeight() * playground.getSnakeBodySize());
        Shape screenT = new Text(gc, new Point2D(100, 250), screenText.getPlayerText(), Color.WHITE, true);
        Shape counterText = new CompositeShape(gc, List.of(background, screenT));
        counterText.draw();
    }

    // called from SessionHandler when new Playground arrives
    public void updatePlayground() {
        GraphicsContext gc = gameController.getGC();
        // special screen only for game over
        if (playground.isGameOver()) {
            Shape gameOverText = new Text(gc, new Point2D(100, 250), "GAME OVER", Color.WHITE, true);
            Shape score1 = new Text(gc, new Point2D(200, 100), "" + playground.getSnake2().getPoints(), Color.GREEN, false);
            Shape space = new Text(gc, new Point2D(250, 100), " : ", Color.WHITE, false);
            Shape score2 = new Text(gc, new Point2D(300, 100), "" + playground.getSnake1().getPoints(), Color.BLUE, false);
            Shape gameOverScreen = new CompositeShape(gc, List.of(gameOverText, score1, space, score2));
            gameOverScreen.draw();
            user.setReadyToPlay(false);
            return;
        }
        // regular game screen
        Shape background = new Square(gc, Color.BLACK, new Point2D(0, 0), playground.getWidth() * playground.getSnakeBodySize(), playground.getHeight() * playground.getSnakeBodySize());
        Shape level = new CompositeShape(gc, createLevel(playground.getLevelNumber(), Color.WHITE, Color.LIGHTGREY));
        Shape snake1 = new CompositeShape(gc, createSnake(playground.getSnake1(), Color.LIGHTGREEN, Color.GREEN));
        Shape snake2 = new CompositeShape(gc, createSnake(playground.getSnake2(), Color.LIGHTBLUE, Color.BLUE));
        Shape snakes = new CompositeShape(gc, List.of(snake1, snake2));
        Shape food = new Circle(gc, playground.getFood().getFoodColor(), new Point2D(playground.getFood().getFoodPositionX() * playground.getSnakeBodySize(), playground.getFood().getFoodPositionY() * playground.getSnakeBodySize()), playground.getSnakeBodySize());
        Shape playground = new CompositeShape(gc, List.of(background, level, snakes, food));
        playground.draw();
    }

    public List<Shape> createLevel(int levelNumber, Color colorBack, Color colorFront) {
        GraphicsContext gc = gameController.getGC();
        List<Shape> levelList = new ArrayList<>();
        for (SnakeBodyPart levelPart : level.getAllLevels().get(levelNumber)) {
            Shape squareBack = new Square(gc, colorBack,
                    new Point2D(levelPart.getPositionX() * playground.getSnakeBodySize(), levelPart.getPositionY() * playground.getSnakeBodySize()),
                    playground.getSnakeBodySize() - 1, playground.getSnakeBodySize() - 1);
            levelList.add(squareBack);

            Shape squareFront = new Square(gc, colorFront,
                    new Point2D(levelPart.getPositionX() * playground.getSnakeBodySize(), levelPart.getPositionY() * playground.getSnakeBodySize()),
                    playground.getSnakeBodySize() - 2, playground.getSnakeBodySize() - 2);
            levelList.add(squareFront);
        }
        return levelList;
    }

    public List<Shape> createSnake(Snake snake, Color colorBack, Color colorFront) {
        GraphicsContext gc = gameController.getGC();
        List<Shape> snakeList = new ArrayList<>();
        for (SnakeBodyPart sbp : snake.getSnakeBody()) {
            Shape squareBack = new Square(gc, colorBack,
                    new Point2D(sbp.getPositionX() * playground.getSnakeBodySize(), sbp.getPositionY() * playground.getSnakeBodySize()),
                    playground.getSnakeBodySize() - 1, playground.getSnakeBodySize() - 1);
            snakeList.add(squareBack);

            Shape squareFront = new Square(gc, colorFront,
                    new Point2D(sbp.getPositionX() * playground.getSnakeBodySize(), sbp.getPositionY() * playground.getSnakeBodySize()),
                    playground.getSnakeBodySize() - 2, playground.getSnakeBodySize() - 2);
            snakeList.add(squareFront);

            // position where snake can be bitten
            if (sbp.getColor() == 3) {
                Shape biteCircle = new Circle(gc, sbp.getColor(),
                        new Point2D(sbp.getPositionX() * playground.getSnakeBodySize(), sbp.getPositionY() * playground.getSnakeBodySize()), playground.getSnakeBodySize() - 2);
                snakeList.add(biteCircle);
            }

            if (snake.isImmortal()) {
                Shape immortalCircle = new Circle(gc, 2,
                        new Point2D(sbp.getPositionX() * playground.getSnakeBodySize(), sbp.getPositionY() * playground.getSnakeBodySize()), playground.getSnakeBodySize() - 2);
                snakeList.add(immortalCircle);
            }
        }
        return snakeList;
    }

}