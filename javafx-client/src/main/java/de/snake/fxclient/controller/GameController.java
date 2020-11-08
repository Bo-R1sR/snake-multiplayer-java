package de.snake.fxclient.controller;

import de.snake.fxclient.domain.User;
import de.snake.fxclient.game.*;
import de.snake.fxclient.game.composite.*;
import de.snake.fxclient.game.message.Message;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@FxmlView("game-stage.fxml")
public class GameController {

    private final BackgroundController backgroundController;
    private final User user;
    private final Playground playground;
    private final ScreenText screenText;

    private SnakeDirection direction;
    private GraphicsContext gc;
    private StompSession session;

    @FXML
    private TextArea chatArea;
    @FXML
    private TextField chatMess;
    @FXML
    private Canvas gameCanvas;

    public GameController(BackgroundController backgroundController, User user, Playground playground, ScreenText screenText) {
        this.backgroundController = backgroundController;
        this.user = user;
        this.playground = playground;
        this.screenText = screenText;
    }

    @FXML
    public void initialize() {
        gc = gameCanvas.getGraphicsContext2D();
        drawBackground();
    }

    public void back() {
        if (user.getSession() != null) {
            user.getSession().disconnect();
        }
        backgroundController.changeView(MainController.class);
    }

    public void initializeArrowKeys() {
        Scene scene = backgroundController.getViewHolder().getParent().getScene();
        scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            // only send key if it is arrow
            if (key.getCode() == KeyCode.UP) {
                direction = SnakeDirection.UP;
                key.consume();
                session.send("/app/direction" + user.getPlayerId(), direction);
            }
            if (key.getCode() == KeyCode.LEFT) {
                direction = SnakeDirection.LEFT;
                key.consume();
                session.send("/app/direction" + user.getPlayerId(), direction);
            }
            if (key.getCode() == KeyCode.DOWN) {
                direction = SnakeDirection.DOWN;
                key.consume();
                session.send("/app/direction" + user.getPlayerId(), direction);
            }
            if (key.getCode() == KeyCode.RIGHT) {
                direction = SnakeDirection.RIGHT;
                key.consume();
                session.send("/app/direction" + user.getPlayerId(), direction);
            }

        });
    }

    public void initializeGame() {
        session = user.getSession();
        initializeArrowKeys();
        session.send("/app/playerId/" + user.getName(), "connect");
    }

    public void startGame() {
        session.send("/app/playerActive/" + user.getPlayerId(), "connect");
    }

    public void restartGame() {
        session.send("/app/playerRestart/" + user.getPlayerId(), "connect");
    }

    public void updateScreenText() {
        drawBackground();
        drawScreenText();
    }

    public void updatePlayground() {
        if (playground.isGameOver()) {
            Shape gameOverText = new Text(gc, new Point2D(100, 250), "GAME OVER", Color.RED);
            gameOverText.draw();
            return;
        }

        Shape background = new Square(gc, Color.BLACK, new Point2D(0, 0), playground.getWidth() * playground.getSnakeBodySize(), playground.getHeight() * playground.getSnakeBodySize());

        Shape snake1 = new CompositeShape(gc, createSnake(playground.getSnake1(), Color.LIGHTGREEN, Color.GREEN));
        Shape snake2 = new CompositeShape(gc, createSnake(playground.getSnake2(), Color.LIGHTBLUE, Color.BLUE));
        Shape snakes = new CompositeShape(gc, List.of(snake1, snake2));

        Shape food = new Circle(gc, playground.getFood().getFoodColor(), new Point2D(playground.getFood().getFoodPositionX() * playground.getSnakeBodySize(), playground.getFood().getFoodPositionY() * playground.getSnakeBodySize()), playground.getSnakeBodySize());

//        Shape score1 = new Text(gc, new Point2D(100, 250), "Speed " + (20 - playground.getSnake1().getSpeed())/2, Color.GREEN);
//        Shape score2 = new Text(gc, new Point2D(300, 250), "Speed " + (20 - playground.getSnake2().getSpeed())/2, Color.BLUE);

        Shape score1 = new Text(gc, new Point2D(100, 50), "Score ", Color.GREEN);
        Shape score2 = new Text(gc, new Point2D(300, 50), "Score ", Color.BLUE);

        Shape playground = new CompositeShape(gc, List.of(background, snakes, food, score1, score2));
        playground.draw();
    }

    public void drawBackground() {
        Shape background = new Square(gc, Color.BLACK, new Point2D(0, 0), playground.getWidth() * playground.getSnakeBodySize(), playground.getHeight() * playground.getSnakeBodySize());
        background.draw();
    }

    public void drawScreenText() {
        Shape screenT = new Text(gc, new Point2D(100, 250), screenText.getPlayerText(), Color.RED);
        screenT.draw();
    }

    public List<Shape> createSnake(Snake snake, Color colorBack, Color colorFront) {
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

    private Message getNewMessage() {
        Message msg = new Message();
        msg.setFrom(user.getName());
        msg.setText(chatMess.getText());
        return msg;
    }

    public void appendChatMessage(String message) {
        chatArea.appendText(message);
    }

    public void submitMessage() {
        session = user.getSession();
        session.send("/app/message", getNewMessage());
        chatMess.setText("");
    }
}
