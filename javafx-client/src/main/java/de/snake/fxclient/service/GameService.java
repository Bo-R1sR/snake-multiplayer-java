package de.snake.fxclient.service;

import de.snake.fxclient.controller.BackgroundController;
import de.snake.fxclient.domain.User;
import de.snake.fxclient.game.SnakeDirectionEnum;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final BackgroundController backgroundController;
    private final User user;

    private SnakeDirectionEnum direction;

    public GameService(BackgroundController backgroundController, User user) {
        this.backgroundController = backgroundController;
        this.user = user;
    }

    public void registerArrowKeys() {
        // assign arrow keys for game control
        Scene scene = backgroundController.getViewHolder().getParent().getScene();
        scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            // only send key if it is arrow
            if (key.getCode() == KeyCode.UP) {
                direction = SnakeDirectionEnum.UP;
                key.consume();
                user.getSession().send("/app/direction" + user.getPlayerId(), direction);
            }
            if (key.getCode() == KeyCode.LEFT) {
                direction = SnakeDirectionEnum.LEFT;
                key.consume();
                user.getSession().send("/app/direction" + user.getPlayerId(), direction);
            }
            if (key.getCode() == KeyCode.DOWN) {
                direction = SnakeDirectionEnum.DOWN;
                key.consume();
                user.getSession().send("/app/direction" + user.getPlayerId(), direction);
            }
            if (key.getCode() == KeyCode.RIGHT) {
                direction = SnakeDirectionEnum.RIGHT;
                key.consume();
                user.getSession().send("/app/direction" + user.getPlayerId(), direction);
            }
        });

    }
}
