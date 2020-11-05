package de.snake.fxclient.controller;

import de.snake.fxclient.domain.User;
import de.snake.fxclient.game.*;
import de.snake.fxclient.game.Message;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Component;

@Component
@FxmlView("game-stage.fxml")
public class GameController {

    private final BackgroundController backgroundController;
    private final User user;
    private final Playground playground;
    private final ScreenText screenText;
    private SnakeDirection direction;
    @FXML
    private Label testMessage;
    @FXML
    private TextField chatMess;
    @FXML
    private Canvas gameCanvas;
    private GraphicsContext gc;
    private StompSession session;

    public GameController(BackgroundController backgroundController, User user, Playground playground, ScreenText screenText, de.snake.fxclient.game.Playground playground1, de.snake.fxclient.game.ScreenText screenText1) {
        this.backgroundController = backgroundController;
        this.user = user;
        this.playground = playground1;
        this.screenText = screenText1;
    }

    @FXML
    public void initialize() {
//        Scene scene = backgroundController.getViewHolder().getParent().getScene();
//        List<Node> stackPane = backgroundController.getViewHolder().getChildren();
//        Node erg = stackPane.get(0);
//        scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
//        //stackPane.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
//            if (key.getCode() == KeyCode.UP) {
//                direction = SnakeDirection.UP;
//            }
//            if (key.getCode() == KeyCode.LEFT) {
//                direction = SnakeDirection.LEFT;
//            }
//            if (key.getCode() == KeyCode.DOWN) {
//                direction = SnakeDirection.DOWN;
//            }
//            if (key.getCode() == KeyCode.RIGHT) {
//                direction = SnakeDirection.RIGHT;
//            }
//            session.send("/app/direction" + user.getPlayerId(), direction);
//
//        });
        gc = gameCanvas.getGraphicsContext2D();
        drawBackground();
    }

    public void back() {
        backgroundController.changeView(MainController.class);
    }

    public void initializeArrowKeys() {
        Scene scene = backgroundController.getViewHolder().getParent().getScene();
        scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            //stackPane.addEventFilter(KeyEvent.KEY_PRESSED, key -> {

            // only send key if it is arrow
            if (key.getCode() == KeyCode.UP) {
                direction = SnakeDirection.UP;
                session.send("/app/direction" + user.getPlayerId(), direction);
            }
            if (key.getCode() == KeyCode.LEFT) {
                direction = SnakeDirection.LEFT;
                session.send("/app/direction" + user.getPlayerId(), direction);
            }
            if (key.getCode() == KeyCode.DOWN) {
                direction = SnakeDirection.DOWN;
                session.send("/app/direction" + user.getPlayerId(), direction);
            }
            if (key.getCode() == KeyCode.RIGHT) {
                direction = SnakeDirection.RIGHT;
                session.send("/app/direction" + user.getPlayerId(), direction);
            }


        });

    }

    public void initializeGame() {

        session = user.getSession();
        initializeArrowKeys();

        session.send("/app/playerId", "connect");
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
            gc.setFill(Color.RED);
            gc.setFont(new Font("", 50));
            gc.fillText("GAME OVER", 100, 250);
            return;
        }
        drawBackground();
        drawScore();
        drawSnake(playground.getSnake1(), Color.LIGHTGREEN, Color.GREEN);
        drawSnake(playground.getSnake2(), Color.LIGHTBLUE, Color.BLUE);
        drawFood();
    }

    public void drawSnake(Snake snake, Color colorBack, Color colorFront) {
        for (SnakeBodyPart c : snake.getSnakeBody()) {
            gc.setFill(colorBack);
            gc.fillRect(c.getPositionX() * playground.getSnakeBodySize(), c.getPositionY() * playground.getSnakeBodySize(), playground.getSnakeBodySize() - 1, playground.getSnakeBodySize() - 1);
            gc.setFill(colorFront);
            gc.fillRect(c.getPositionX() * playground.getSnakeBodySize(), c.getPositionY() * playground.getSnakeBodySize(), playground.getSnakeBodySize() - 2, playground.getSnakeBodySize() - 2);
            if (c.getColor() == 1) {
                gc.setFill(Color.PURPLE);
                gc.fillOval(c.getPositionX() * playground.getSnakeBodySize(), c.getPositionY() * playground.getSnakeBodySize(), playground.getSnakeBodySize() - 2, playground.getSnakeBodySize() - 2);
            }

            if (snake.isImmortal()) {
                gc.setFill(Color.YELLOW);
                gc.fillOval(c.getPositionX() * playground.getSnakeBodySize(), c.getPositionY() * playground.getSnakeBodySize(), playground.getSnakeBodySize() - 2, playground.getSnakeBodySize() - 2);
            }

        }
    }

    public void drawBackground() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, playground.getWidth() * playground.getSnakeBodySize(), playground.getHeight() * playground.getSnakeBodySize());
    }

    public void drawScreenText() {
        gc.setFill(Color.RED);
        gc.setFont(new Font("", 30));
        gc.fillText(screenText.getPlayerText(), 100, 250);
    }

    public void drawFood() {
        // random foodcolor
        Color cc = Color.WHITE;

        switch (playground.getFood().getFoodColor()) {
            case 0:
                cc = Color.PURPLE;
                break;
            case 1:
                cc = Color.LIGHTBLUE;
                break;
            case 2:
                cc = Color.YELLOW;
                break;
            case 3:
                cc = Color.PINK;
                break;
            case 4:
                cc = Color.ORANGE;
                break;
        }
        gc.setFill(cc);
        gc.fillOval(playground.getFood().getFoodPositionX() * playground.getSnakeBodySize(), playground.getFood().getFoodPositionY() * playground.getSnakeBodySize(), playground.getSnakeBodySize(), playground.getSnakeBodySize());


    }

    public void drawScore() {
        // score
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("", 30));
        //gc.fillText("Score: " + (speed - 6), 10, 30);
    }

    private Message getNewMessage() {
        Message msg = new Message();
        msg.setFrom(user.getName());
        msg.setText(chatMess.getText());
        return msg;
    }

    public Label getTestMessage() {
        return testMessage;
    }

    public void setTestMessage(Label testMessage) {
        this.testMessage = testMessage;
    }

    public void submitMessage() {
        session = user.getSession();
        session.send("/app/message", getNewMessage());
    }


}
