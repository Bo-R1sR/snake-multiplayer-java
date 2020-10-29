package de.snake.server.controller;

import de.snake.server.game.*;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@Controller
public class GameController {

    private final Playground playground;
    private final ScreenText screenText;
    private final SimpMessagingTemplate template;
    private final Random rand = new Random();
    boolean immortal = false;
    private Timer refreshTimer;
    private Timer immortalTimer;
    private Boolean player1active = false;
    private Boolean player2active = false;
    private int speed = 500;
    private SnakeDirection direction1 = SnakeDirection.LEFT;
    private SnakeDirection direction2 = SnakeDirection.RIGHT;

    private int counter;


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

    @MessageMapping("/playerRestart/{id}")
    public void waitForAllPlayers2(@DestinationVariable int id) throws InterruptedException {
        if (id == 1) player1active = true;
        if (id == 2) player2active = true;

        //todo || auf &&
        if (player1active || player2active) {
            speed = 500;
            direction1 = SnakeDirection.LEFT;
            direction2 = SnakeDirection.RIGHT;
            counter = 0;
            int width = playground.getWidth();
            int height = playground.getHeight();
            playground.setGameOver(false);
            playground.setSnake1(new Snake(30, width / 2, height / 2 + 5));
            playground.setSnake2(new Snake(30, width / 2, height / 2 - 5));
            playground.setFood(new Food(width / 2, height / 2));


            startCounter();
        } else {
            screenText.setPlayerText("waiting for other Player to restart");

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
        refreshTimer = new Timer();
        refreshTimer.scheduleAtFixedRate(new SnakeUpdateTask(), 0, 100000 / (speed));

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
                if (snakeHead.getPositionY() < 0) {
                    setGameOver();
                }
                break;
            case DOWN:
                snakeHead.increaseY();
                if (snakeHead.getPositionY() > playground.getHeight() - 1) {
                    setGameOver();
                }
                break;
            case LEFT:
                snakeHead.decreaseX();
                if (snakeHead.getPositionX() < 0) {
                    setGameOver();
                }
                break;
            case RIGHT:
                snakeHead.increaseX();
                if (snakeHead.getPositionX() > playground.getWidth() - 1) {
                    setGameOver();
                }
                break;
        }
    }

    public void setGameOver() {
        playground.setGameOver(true);
        refreshTimer.cancel();
    }

    public class SnakeUpdateTask extends TimerTask {

        @Override
        public void run() {


            updateSnake(playground.getSnake1(), direction1);

            counter++;
            if (counter < 10) {
                direction2 = SnakeDirection.RIGHT;
            } else if (counter < 20) {
                direction2 = SnakeDirection.UP;
            } else if (counter < 30) {
                direction2 = SnakeDirection.LEFT;
            } else if (counter < 40) {
                direction2 = SnakeDirection.DOWN;
            } else {
                counter = 0;
            }


            updateSnake(playground.getSnake2(), direction2);

            checkSnakeAgainstFood(playground.getSnake1());
            checkSnakeAgainstFood(playground.getSnake2());

            if (!playground.getSnake1().isImmortal()) {
                checkSnakeAgainstSelf(playground.getSnake1());
            }
            if (!playground.getSnake2().isImmortal()) {
                checkSnakeAgainstSelf(playground.getSnake2());
            }
            if (!playground.getSnake2().isImmortal()) {
                checkSnakeAgainstOther(playground.getSnake1(), playground.getSnake2());
            }
            if (!playground.getSnake1().isImmortal()) {
                checkSnakeAgainstOther(playground.getSnake2(), playground.getSnake1());
            }


            template.convertAndSend("/topic/playground", playground);
        }

        public void checkSnakeAgainstFood(Snake snake) {
            if (playground.getFood().getFoodPositionX() == snake.getSnakeBody().get(snake.getSnakeBody().size() - 1).getPositionX() &&
                    playground.getFood().getFoodPositionY() == snake.getSnakeBody().get(snake.getSnakeBody().size() - 1).getPositionY()) {


                if (playground.getFood().getFoodColor() == 0) {
                    snake.getSnakeBody().add(new SnakeBodyPart(-1, -1, 1));
                } else {
                    snake.getSnakeBody().add(new SnakeBodyPart(-1, -1, 0));
                }


                snake.setLastFoodColor(playground.getFood().getFoodColor());
                if (snake.getLastFoodColor() == 2) {

                    try {
                        immortalTimer.cancel();
                    } catch (Exception e) {
                    }

                    snake.setImmortal(true);
                    immortalTimer = new Timer();
                    immortalTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            snake.setImmortal(false);
                        }
                    }, 20000);
                }

                try {
                    createNewFood();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

        // snake 2 is target
        public void checkSnakeAgainstOther(Snake hittingSnake, Snake targetSnake) {
            Snake snakeFrontTargetSnake = new Snake(0, -1, -1);
            //Snake snakeBackTargetSnake = new Snake(0, -1, -1);
            //Snake snakeFrontHittingSnake = new Snake(0, -1, -1);
            //Snake snakeBackHittingSnake = new Snake(0, -1, -1);

            int lastIndexTargetSnake = targetSnake.getSnakeBody().size();
            //int lastIndexHittingSnake = hittingSnake.getSnakeBody().size();

            for (int i = 0; i < targetSnake.getSnakeBody().size(); i++) {
                if (hittingSnake.getSnakeBody().get(0).getPositionX() == targetSnake.getSnakeBody().get(i).getPositionX() &&
                        hittingSnake.getSnakeBody().get(0).getPositionY() == targetSnake.getSnakeBody().get(i).getPositionY()) {
                    //   if (snake.getSnakeBody().get(i).getColor() == 1) {
                    if (true) {

                        List<SnakeBodyPart> sbp_tf = targetSnake.getSnakeBody().subList(0, i - 1);
                        List<SnakeBodyPart> sbp_tb = targetSnake.getSnakeBody().subList(i, lastIndexTargetSnake);
                        //List<SnakeBodyPart> sbp_hf = hittingSnake.getSnakeBody().subList(0, 1);
                        //List<SnakeBodyPart> sbp_hb = hittingSnake.getSnakeBody().subList(1, lastIndexHittingSnake);

                        //snakeFrontHittingSnake.setSnakeBody(sbp_hf);
                        //snakeBackHittingSnake.setSnakeBody(sbp_hb);
                        snakeFrontTargetSnake.setSnakeBody(sbp_tf);
                        //snakeBackTargetSnake.setSnakeBody(sbp_tb);
                        BeanUtils.copyProperties(snakeFrontTargetSnake, targetSnake);
                        hittingSnake.getSnakeBody().addAll(1, sbp_tb);


                    } else {
                        setGameOver();
                    }
                }
            }


        }

        public void checkSnakeAgainstSelf(Snake snake) {
            for (int i = 1; i < snake.getSnakeBody().size(); i++) {
                if (snake.getSnakeBody().get(0).getPositionX() == snake.getSnakeBody().get(i).getPositionX() &&
                        snake.getSnakeBody().get(0).getPositionY() == snake.getSnakeBody().get(i).getPositionY()) {
                    setGameOver();
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
                speed += 10;
                refreshTimer.cancel();

                playground.getFood().setFoodColor(rand.nextInt(2));
                playground.getFood().setFoodPositionX(foodX);
                playground.getFood().setFoodPositionY(foodY);
                startRefreshingCanvas();
                break;
            }
        }
    }
}
