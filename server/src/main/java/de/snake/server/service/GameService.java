package de.snake.server.service;

import de.snake.server.domain.game.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GameService {

    private final ScreenText screenText;
    private final SimpMessagingTemplate template;
    private final Playground playground;

    public GameService(ScreenText screenText, SimpMessagingTemplate template, Playground playground) {
        this.screenText = screenText;
        this.template = template;
        this.playground = playground;
    }

    public void startCountdown() throws InterruptedException {
        // countdown from 3 to zero and send to client
        for (int i = 3; i > 0; i--) {
            screenText.setPlayerText("" + i);
            this.template.convertAndSend("/topic/screenText", screenText);
            Thread.sleep(1000);
        }
        screenText.setPlayerText("LOS GEHTS");
        this.template.convertAndSend("/topic/screenText", screenText);
        Thread.sleep(1000);
        screenText.setPlayerText("");
        this.template.convertAndSend("/topic/screenText", screenText);
    }

    public void initializePlayground() {
        // values for game start
        playground.setGameOver(false);

        // todo points in history auslagern
        // todo make sure after restart snake is correctly assigned depending who clicks first
        int points1;
        int points2;
        if (playground.getSnake1() != null) {
            points1 = playground.getSnake1().getPoints();
        } else {
            points1 = 0;
        }
        if (playground.getSnake1() != null) {
            points2 = playground.getSnake2().getPoints();
        } else {
            points2 = 0;
        }
        playground.setSnake1(new Snake(3, 12, 15));
        playground.setSnake2(new Snake(3, 12, 9));

        playground.setFood(new Food(12, 12));
        // always same color at start, during game random
        playground.getFood().setFoodColor(0);
        // initial movement direction for snakes
        playground.getSnake1().setSnakeDirectionEnum(SnakeDirection.LEFT);
        playground.getSnake2().setSnakeDirectionEnum(SnakeDirection.RIGHT);

        if (!playground.isDuringLevel()) {
            playground.setLevelNumber(0);
            playground.setDuringLevel(true);
            playground.setLevelFinish(false);
        } else {
            playground.increaseLevelNumber();
            playground.getSnake1().setPoints(points1);
            playground.getSnake2().setPoints(points2);
        }
    }
}
