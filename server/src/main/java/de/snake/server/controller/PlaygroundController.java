package de.snake.server.controller;

import de.snake.server.game.SnakeDirection;
import de.snake.server.game.Playground;
import de.snake.server.game.ScreenText;
import de.snake.server.game.Snake;
import de.snake.server.game.SnakeBodyPart;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@Controller
public class PlaygroundController {

    private static Boolean player1active = false;
    private static Boolean player2active = false;
    private final Playground playground;
    private final ScreenText screenText;
    private final SimpMessagingTemplate template;
    private int speed = 10;
    private SnakeDirection direction1 = SnakeDirection.left;
    private SnakeDirection direction2 = SnakeDirection.right;
    private Timer timer;
    private final Random rand = new Random();


    public PlaygroundController(Playground playground, ScreenText screenText, SimpMessagingTemplate template) {
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



    //@MessageMapping("/snake")
    public void startRefreshingCanvas() throws InterruptedException {
        timer = new Timer();
        timer.scheduleAtFixedRate(new SnakeUpdateTask(), 0, 10000 / (speed));
    }

    public void updateSnake(Snake snake, SnakeDirection direction, Integer number) {
        for (int i = snake.getSnakeBody().size() - 1; i >= 1; i--) {
            snake.getSnakeBody().get(i).setX(snake.getSnakeBody().get(i - 1).getX());
            snake.getSnakeBody().get(i).setY(snake.getSnakeBody().get(i - 1).getY());
        }

        System.out.println(direction);
        SnakeBodyPart snakeHead = snake.getSnakeBody().get(0);
        switch (direction) {
            case up:
                snakeHead.decreaseY();
//                if (snake.get(0).y < 0) {
//                    gameOver = true;
//                }
                break;
            case down:
                snakeHead.increaseY();
//                if (snake.get(0).y > height) {
//                    gameOver = true;
//                }
                break;
            case left:
                snakeHead.decreaseX();
//                if (snake.get(0).x < 0) {
//                    gameOver = true;
//                }
                break;
            case right:
                snakeHead.increaseX();
//                if (snake.get(0).x > width) {
//                    gameOver = true;
//                }
                break;
        }
        //this.template.convertAndSend("/topic/snake" + number, snake);
    }

    public class SnakeUpdateTask extends TimerTask {


//        public SnakeUpdateTask(SimpMessagingTemplate template) {
//            this.template = template;
//        }

        @Override
        public void run() {
            updateSnake(playground.getSnake1(), direction1, 1);
            updateSnake(playground.getSnake2(), direction2, 2);


            if (playground.getFood().getFoodX() == playground.getSnake1().getSnakeBody().get(0).getX() &&
                    playground.getFood().getFoodY() == playground.getSnake1().getSnakeBody().get(0).getY()) {
                playground.getSnake1().getSnakeBody().add(new SnakeBodyPart(-1, -1));
                int width = 20;
                int height = 20;
                //playground.getFood().setFoodX(rand.nextInt(width));
                //playground.getFood().setFoodY(rand.nextInt(height));
                try {
                    createNewFood();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (playground.getFood().getFoodX() == playground.getSnake2().getSnakeBody().get(0).getX() &&
                    playground.getFood().getFoodY() == playground.getSnake2().getSnakeBody().get(0).getY()) {
                playground.getSnake2().getSnakeBody().add(new SnakeBodyPart(-1, -1));
                int width = 20;
                int height = 20;
                //playground.getFood().setFoodX(rand.nextInt(width));
                //playground.getFood().setFoodY(rand.nextInt(height));
                try {
                    createNewFood();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // self destroy
        for (int i = 1; i < playground.getSnake1().getSnakeBody().size(); i++) {
            if (playground.getSnake1().getSnakeBody().get(0).getX() == playground.getSnake1().getSnakeBody().get(i).getX() &&
                    playground.getSnake1().getSnakeBody().get(0).getY() == playground.getSnake1().getSnakeBody().get(i).getY()) {
                playground.setGameOver(true);
                timer.cancel();
            }
        }


            System.out.println("TEST");
            template.convertAndSend("/topic/playground", playground);
        }

        public void createNewFood() throws InterruptedException {
            Snake totalSnake = new Snake(-1);
            totalSnake.getSnakeBody().clear();

            totalSnake.getSnakeBody().addAll(playground.getSnake1().getSnakeBody());
            totalSnake.getSnakeBody().addAll(playground.getSnake2().getSnakeBody());
            int width = 20;
            int height = 20;

            start:
            while (true) {
                int foodX = rand.nextInt(width);
                int foodY = rand.nextInt(height);

                for (SnakeBodyPart sbp : totalSnake.getSnakeBody()) {

                    if (sbp.getX() == foodX && sbp.getY() == foodY) {
                        continue start;
                    }
                }
                speed += 5;
                timer.cancel();

                playground.getFood().setFoodColor(rand.nextInt(5));
                playground.getFood().setFoodX(foodX);
                playground.getFood().setFoodY(foodY);
                startRefreshingCanvas();
                break;

            }


        }
    }
}
