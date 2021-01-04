package de.snake.server.service;

import de.snake.server.config.WebSocketEventListener;
import de.snake.server.domain.game.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final ScreenText screenText;
    private final SimpMessagingTemplate template;
    private final Playground playground;
    private final ServerSounds serverSounds;
    private final WebSocketEventListener webSocketEventListener;

    public GameService(ScreenText screenText, SimpMessagingTemplate template, Playground playground, ServerSounds serverSounds, WebSocketEventListener webSocketEventListener) {
        this.screenText = screenText;
        this.template = template;
        this.playground = playground;
        this.serverSounds = serverSounds;
        this.webSocketEventListener = webSocketEventListener;
    }

    public void startCountdown() throws InterruptedException {
        // countdown from 3 to zero and send to client
        for (int i = 3; i > 0; i--) {
            serverSounds.setText("Countdown" + i);
            template.convertAndSend("/topic/serverSounds", serverSounds);
            screenText.setPlayerText("" + i);
            this.template.convertAndSend("/topic/screenText", screenText);

            Thread.sleep(1000);
        }
        screenText.setPlayerText("LOS GEHTS");
        this.template.convertAndSend("/topic/screenText", screenText);
        serverSounds.setText("GameStart");
        template.convertAndSend("/topic/serverSounds", serverSounds);
        Thread.sleep(1000);
        screenText.setPlayerText("");
        this.template.convertAndSend("/topic/screenText", screenText);
    }

    public void initializePlayground() {
        // values for game start
        playground.setGameOver(false);

        int points1 = 0;
        int points2 = 0;
        if (playground.getSnake1() != null) {
            points1 = playground.getSnake1().getPoints();
        }
        if (playground.getSnake1() != null) {
            points2 = playground.getSnake2().getPoints();
        }

        playground.setSnake1(new Snake(3, 12, 15));
        playground.setSnake2(new Snake(3, 12, 9));

        playground.setFood(new Food(12, 12));
        // always same color at start, during game random
        playground.getFood().setFoodColor(0);
        // initial movement direction for snakes
        playground.getSnake1().setSnakeDirection(SnakeDirection.LEFT);
        playground.getSnake2().setSnakeDirection(SnakeDirection.RIGHT);
        playground.getSnake1().setMoveDirection(SnakeDirection.LEFT);
        playground.getSnake2().setMoveDirection(SnakeDirection.RIGHT);

        playground.getSnake2().setUsername(webSocketEventListener.getUsername1());
        playground.getSnake1().setUsername(webSocketEventListener.getUsername2());

//        // ab hier für TEST beißen
//
//        playground.setSnake2(new Snake(8, 12, 15));
//        playground.getSnake2().setPossibleToBite(true);
//        playground.getSnake2().setSnakeDirection(SnakeDirection.RIGHT);
//        playground.getSnake2().setMoveDirection(SnakeDirection.RIGHT);
//
//        List<SnakeBodyPart> sbp2 = playground.getSnake2().getSnakeBody();
//        sbp2.get(7).setColor(3);
//        sbp2.get(6).setColor(3);
//        sbp2.get(5).setColor(3);
//
//        int jj = 8;
//        for(SnakeBodyPart sbp: sbp2) {
//            sbp.setPositionX(jj);
//            jj += 1;
//            sbp.setPositionY(6);
//        }
//
//        // hier für Test Single Player
//        playground.getSnake2().setCounter(1000000);


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
