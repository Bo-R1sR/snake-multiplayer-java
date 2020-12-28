package de.snake.fxclient.service;

import de.snake.fxclient.controller.BackgroundController;
import de.snake.fxclient.domain.User;
import de.snake.fxclient.game.SnakeColor;
import de.snake.fxclient.game.SnakeDirection;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final BackgroundController backgroundController;
    private final User user;
    private final SnakeColor snakeColor;

    private SnakeDirection direction;

    public GameService(BackgroundController backgroundController, User user, SnakeColor snakeColor) {
        this.backgroundController = backgroundController;
        this.user = user;
        this.snakeColor = snakeColor;
    }

    public void registerArrowKeys() {
        // assign arrow keys for game control
        Scene scene = backgroundController.getViewHolder().getParent().getScene();
        scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            // only send key if it is arrow
            if (key.getCode() == KeyCode.UP) {
                direction = SnakeDirection.UP;
                key.consume();
                user.getSession().send("/app/direction" + user.getPlayerId(), direction);
            }
            if (key.getCode() == KeyCode.LEFT) {
                direction = SnakeDirection.LEFT;
                key.consume();
                user.getSession().send("/app/direction" + user.getPlayerId(), direction);
            }
            if (key.getCode() == KeyCode.DOWN) {
                direction = SnakeDirection.DOWN;
                key.consume();
                user.getSession().send("/app/direction" + user.getPlayerId(), direction);
            }
            if (key.getCode() == KeyCode.RIGHT) {
                direction = SnakeDirection.RIGHT;
                key.consume();
                user.getSession().send("/app/direction" + user.getPlayerId(), direction);
            }
        });

    }

    public void changeColor(String choosenColor) {
        switch (choosenColor) {
            case "Blau/Grün" -> {
                snakeColor.setColorSnake1(Color.BLUE);
                snakeColor.setColorSnake1Border(Color.LIGHTBLUE);
                snakeColor.setColorSnake2(Color.GREEN);
                snakeColor.setColorSnake2Border(Color.LIGHTGREEN);
            }
            case "Gelb/Rosa" -> {
                snakeColor.setColorSnake1(Color.YELLOW);
                snakeColor.setColorSnake1Border(Color.LIGHTYELLOW);
                snakeColor.setColorSnake2(Color.PINK);
                snakeColor.setColorSnake2Border(Color.LIGHTPINK);
            }
            case "Lila/Cyan" -> {
                snakeColor.setColorSnake1(Color.VIOLET);
                snakeColor.setColorSnake1Border(Color.PALEVIOLETRED);
                snakeColor.setColorSnake2(Color.CYAN);
                snakeColor.setColorSnake2Border(Color.LIGHTCYAN);
            }
        }
    }
}
