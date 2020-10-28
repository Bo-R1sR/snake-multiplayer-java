package de.snake.server.controller;

import de.snake.server.game.*;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@Controller
public class GameController {

    private final Playground playground;
    private final ScreenText screenText;
    private final SimpMessagingTemplate template;
    private final Random rand = new Random();
    private Boolean player1active = false;
    private Boolean player2active = false;
    private int speed = 500;
    private SnakeDirection direction1 = SnakeDirection.LEFT;
    private SnakeDirection direction2 = SnakeDirection.RIGHT;
    private Timer timer;


    public GameController(Playground playground, ScreenText screenText, SimpMessagingTemplate template) {
        this.playground = playground;
        this.screenText = screenText;
        this.template = template;
    }

    @MessageMapping("/direction1")
    public void changeDirection1(SnakeDirection direction) {
        this.direction1 = direction;
    }

    @MessageMapping("/direction2")
    public void changeDirection2(SnakeDirection direction) {
        this.direction2 = direction;
    }

    @MessageMapping("/playerActive/{id}")
    public void waitForAllPlayers(@DestinationVariable int id) throws InterruptedException {
        if (id == 1) player1active = true;
        if (id == 2) player2active = true;

        //todo || auf &&
        if (player1active || player2active) {
            startCounter();
        } else {
            screenText.setPlayerText("waiting for another Player to join");

            this.template.convertAndSend("/topic/screenText", screenText);
        }
    }

    public void startCounter() throws InterruptedException {
        for (int i = 3; i > 0; i--) {
            screenText.setPlayerText("" + i);
            this.template.convertAndSend("/topic/screenText", screenText);
            Thread.sleep(1000);
        }
        screenText.setPlayerText("GO");
        this.template.convertAndSend("/topic/screenText", screenText);
        Thread.sleep(1000);
        screenText.setPlayerText("");
        this.template.convertAndSend("/topic/screenText", screenText);
        startRefreshingCanvas();
    }

    public void startRefreshingCanvas() throws InterruptedException {
        timer = new Timer();
        timer.scheduleAtFixedRate(new SnakeUpdateTask(), 0, 100000 / (speed));

//        new AnimationTimer() {
//            long lastTick = 0;
//
//            public void handle(long now) {
//                if (lastTick == 0) {
//                    lastTick = now;
//                    tick(gc);
//                    return;
//                }
//
//                if (now - lastTick > 1000000000 / speed) {
//                    lastTick = now;
//                    tick(gc);
//                }
//            }


    }

    public void updateSnake(Snake snake, SnakeDirection direction) {
        for (int i = snake.getSnakeBody().size() - 1; i >= 1; i--) {
            snake.getSnakeBody().get(i).setPositionX(snake.getSnakeBody().get(i - 1).getPositionX());
            snake.getSnakeBody().get(i).setPositionY(snake.getSnakeBody().get(i - 1).getPositionY());
        }

        SnakeBodyPart snakeHead = snake.getSnakeBody().get(0);
        switch (direction) {
            case UP:
                snakeHead.decreaseY();
//                if (snake.get(0).y < 0) {
//                    gameOver = true;
//                }
                break;
            case DOWN:
                snakeHead.increaseY();
//                if (snake.get(0).y > height) {
//                    gameOver = true;
//                }
                break;
            case LEFT:
                snakeHead.decreaseX();
//                if (snake.get(0).x < 0) {
//                    gameOver = true;
//                }
                break;
            case RIGHT:
                snakeHead.increaseX();
//                if (snake.get(0).x > width) {
//                    gameOver = true;
//                }
                break;
        }
    }

    public class SnakeUpdateTask extends TimerTask {
        @Override
        public void run() {
            updateSnake(playground.getSnake1(), direction1);
            updateSnake(playground.getSnake2(), direction2);

            checkSnakeAgainstFood(playground.getSnake1());
            checkSnakeAgainstFood(playground.getSnake2());

            checkSnakeAgainstSelf(playground.getSnake1());
            checkSnakeAgainstSelf(playground.getSnake2());

            template.convertAndSend("/topic/playground", playground);
        }

        public void checkSnakeAgainstFood(Snake snake) {
            if (playground.getFood().getFoodPositionX() == snake.getSnakeBody().get(0).getPositionX() &&
                    playground.getFood().getFoodPositionY() == snake.getSnakeBody().get(0).getPositionY()) {
                snake.getSnakeBody().add(new SnakeBodyPart(-1, -1));
                try {
                    createNewFood();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void checkSnakeAgainstSelf(Snake snake) {
            for (int i = 1; i < snake.getSnakeBody().size(); i++) {
                if (snake.getSnakeBody().get(0).getPositionX() == snake.getSnakeBody().get(i).getPositionX() &&
                        snake.getSnakeBody().get(0).getPositionY() == snake.getSnakeBody().get(i).getPositionY()) {
                    playground.setGameOver(true);
                    timer.cancel();
                }
            }
        }

        public void createNewFood() throws InterruptedException {
            Snake totalSnake = new Snake(0, -1, -1);

            totalSnake.getSnakeBody().addAll(playground.getSnake1().getSnakeBody());
            totalSnake.getSnakeBody().addAll(playground.getSnake2().getSnakeBody());

            start:
            while (true) {
                int foodX = rand.nextInt(playground.getWidth());
                int foodY = rand.nextInt(playground.getHeight());

                for (SnakeBodyPart sbp : totalSnake.getSnakeBody()) {

                    if (sbp.getPositionX() == foodX && sbp.getPositionY() == foodY) {
                        continue start;
                    }
                }
                speed += 5;
                timer.cancel();

                playground.getFood().setFoodColor(rand.nextInt(5));
                playground.getFood().setFoodPositionX(foodX);
                playground.getFood().setFoodPositionY(foodY);
                startRefreshingCanvas();
                break;
            }
        }
    }
}
