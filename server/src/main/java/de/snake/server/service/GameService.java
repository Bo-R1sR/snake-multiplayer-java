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
    private final Random rand = new Random();
    private final ServerSounds serverSounds;

    public GameService(ScreenText screenText, SimpMessagingTemplate template, Playground playground, ServerSounds serverSounds) {
        this.screenText = screenText;
        this.template = template;
        this.playground = playground;
        this.serverSounds = serverSounds;
    }


    public void startCountdown() throws InterruptedException {
        // countdown from 3 to zero and send to client
        for (int i = 3; i > 0; i--) {
            screenText.setPlayerText("" + i);
            this.template.convertAndSend("/topic/screenText", screenText);
            serverSounds.setText("Countdown");
            template.convertAndSend("/topic/serverSounds", serverSounds);
            Thread.sleep(1000);
        }
        screenText.setPlayerText("LOS GEHTS");
        this.template.convertAndSend("/topic/screenText", screenText);
        serverSounds.setText("GameStart");
        template.convertAndSend("/topic/serverSounds", serverSounds);
        Thread.sleep(1000);
        screenText.setPlayerText("");
        this.template.convertAndSend("/topic/screenText", screenText);
        playground.setLevelNumber(rand.nextInt(5));
    }

    public void initializePlayground() {
        // values for game start
        playground.setGameOver(false);
        // refreshing speed

        // initialize snakes and food
        int width = playground.getWidth();
        int height = playground.getHeight();
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
        playground.setSnake1(new Snake(3, width / 2, height / 2 + 5));
        playground.setSnake2(new Snake(3, width / 2, height / 2 - 5));
        playground.setFood(new Food(width / 2, height / 2));
        playground.getSnake1().setPoints(points1);
        playground.getSnake2().setPoints(points2);
        // initial movement direction for snakes
        playground.getSnake1().setSnakeDirectionEnum(SnakeDirectionEnum.LEFT); //direction1 = SnakeDirection.LEFT;
        playground.getSnake2().setSnakeDirectionEnum(SnakeDirectionEnum.RIGHT);//direction2 = SnakeDirection.RIGHT;
        // always same color at start, during game random
        playground.getFood().setFoodColor(0);
    }
}
