package de.snake.server.controller;

import de.snake.server.domain.game.*;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
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
    private Timer refreshTimer;
    private Timer immortalTimer;
    private Boolean player1active = false;
    private Boolean player2active = false;
    private int speed;
    private SnakeDirection direction1;
    private SnakeDirection direction2;
    private int numberOfConnections = 0;
    // todo delete after testing
    private int counter;

    public GameController(Playground playground, ScreenText screenText, SimpMessagingTemplate template) {
        this.playground = playground;
        this.screenText = screenText;
        this.template = template;
    }
    // count players and assign id 1 or 2 to player and send this id to client
    @MessageMapping("/playerId")
    @SendToUser(destinations = "/queue/playerId", broadcast = false)
    public Integer sendID() {
        return ++numberOfConnections;
    }

    // receive directions from player1
    @MessageMapping("/direction1")
    public void changeDirection1(SnakeDirection direction) {
        this.direction1 = direction;
    }
    // receive directions from player2
    @MessageMapping("/direction2")
    public void changeDirection2(SnakeDirection direction) {
        this.direction2 = direction;
    }

    // if clients request start of game
    // method has to be called by both players
    // todo: check if it can be combined with restarting
    @MessageMapping("/playerActive/{id}")
    public void waitForAllPlayers(@DestinationVariable int id) throws InterruptedException {
        if (id == 1) player1active = true;
        if (id == 2) player2active = true;
        //todo || auf &&
        if (player1active || player2active) {
            startCounter();
        } else {
            screenText.setPlayerText("waiting for another Player to join");
            // send screen text to client
            this.template.convertAndSend("/topic/screenText", screenText);
        }
    }

    // if clients request restart and continuing
    // method has to be called by both players
    // todo actual levels after restart
    @MessageMapping("/playerRestart/{id}")
    public void waitForAllPlayers2(@DestinationVariable int id) throws InterruptedException {
        if (id == 1) player1active = true;
        if (id == 2) player2active = true;
        //todo || auf &&
        if (player1active || player2active) {
            startCounter();
        } else {
            screenText.setPlayerText("waiting for other Player to restart");
            // send screen text to client
            this.template.convertAndSend("/topic/screenText", screenText);
        }
    }

    public void initializePlayground() {
        // values for game start
        playground.setGameOver(false);
        // refreshing speed
        speed = 500;
        // initial movement direction for snakes
        direction1 = SnakeDirection.LEFT;
        direction2 = SnakeDirection.RIGHT;
        // initialize snakes and food
        int width = playground.getWidth();
        int height = playground.getHeight();
        playground.setSnake1(new Snake(30, width / 2, height / 2 + 5));
        playground.setSnake2(new Snake(30, width / 2, height / 2 - 5));
        playground.setFood(new Food(width / 2, height / 2));
        // always same color at start, during game random
        playground.getFood().setFoodColor(0);
        // todo remove counter
        counter = 0;
    }

    // counter at the beginning of each game
    public void startCounter() throws InterruptedException {
        initializePlayground();
        // countdown from 3 to zero and send to client
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
        // refresh snake every fixed time
        refreshTimer.scheduleAtFixedRate(new SnakeUpdateTask(), 0, 100000 / (speed));
    }

    // update snake position
    public void updateSnake(Snake snake, SnakeDirection direction) {
        for (int i = snake.getSnakeBody().size() - 1; i >= 1; i--) {
            snake.getSnakeBody().get(i).setPositionX(snake.getSnakeBody().get(i - 1).getPositionX());
            snake.getSnakeBody().get(i).setPositionY(snake.getSnakeBody().get(i - 1).getPositionY());
        }
        // move snake head to new position and check if head is outside playground
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
    // in case of game over stop refreshing
    public void setGameOver() {
        playground.setGameOver(true);
        refreshTimer.cancel();
    }

    public class SnakeUpdateTask extends TimerTask {
        @Override
        public void run() {
            // todo : remove counter: just for movement of second snake
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

            updateSnake(playground.getSnake1(), direction1);
            updateSnake(playground.getSnake2(), direction2);
            // todo : maybe exclude to own threads per snake
            checkSnakeAgainstFood(playground.getSnake1());
            if (!playground.getSnake1().isImmortal()) {
               // checkSnakeAgainstSelf(playground.getSnake1());
               checkSnakeAgainstOther(playground.getSnake2(), playground.getSnake1());
            }
            // todo : maybe exclude to own threads per snake
            checkSnakeAgainstFood(playground.getSnake2());
            if (!playground.getSnake2().isImmortal()) {
               // checkSnakeAgainstSelf(playground.getSnake2());
               checkSnakeAgainstOther(playground.getSnake1(), playground.getSnake2());
            }
             // send modified playground to client
            template.convertAndSend("/topic/playground", playground);
        }

        // check if snake eats food
        public void checkSnakeAgainstFood(Snake snake) {
            // check if snake head is on same position as food
            if (playground.getFood().getFoodPositionX() == snake.getSnakeBody().get(0).getPositionX() &&
                    playground.getFood().getFoodPositionY() == snake.getSnakeBody().get(0).getPositionY()) {
                // todo: set food color for biting point here
                if (playground.getFood().getFoodColor() == 0) {
                    snake.getSnakeBody().add(new SnakeBodyPart(-1, -1, 1));
                } else {
                    snake.getSnakeBody().add(new SnakeBodyPart(-1, -1, 0));
                }
                // set last eaten food color
                // todo: check if needed in client
                snake.setLastFoodColor(playground.getFood().getFoodColor());
                // if snake eats yellow food, snake becomes immortal
                if (snake.getLastFoodColor() == 2) {
                    // reset timer and cancel running own (if exist)
                    // if not running there will be an exception - this is catched
                    try {
                        immortalTimer.cancel();
                    } catch (Exception e) {
                    }
                    snake.setImmortal(true);
                    // reset immortality after specified time
                    immortalTimer = new Timer();
                    immortalTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            snake.setImmortal(false);
                        }
                        // snake will be immortal for 20 seconds
                    }, 20000);
                }
                try {
                    createNewFood();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // check if snake bites other snake
        public void checkSnakeAgainstOther(Snake bitingSnake, Snake targetSnake) {
            for (int i = 0; i < targetSnake.getSnakeBody().size(); i++) {
                // check if snake head is on same position as other snake body
                if (bitingSnake.getSnakeBody().get(0).getPositionX() == targetSnake.getSnakeBody().get(i).getPositionX() &&
                        bitingSnake.getSnakeBody().get(0).getPositionY() == targetSnake.getSnakeBody().get(i).getPositionY()) {
                    // todo : condition when biting can happen (color of body?)
                    if (true) {
                        //divide target snake a bite point
                        List<SnakeBodyPart> sbp_tf = targetSnake.getSnakeBody().subList(0, i - 1);
                        List<SnakeBodyPart> sbp_tb = targetSnake.getSnakeBody().subList(i, targetSnake.getSnakeBody().size());
                        // create new snake for continuing with front part
                        Snake snakeFrontTargetSnake = new Snake(0, -1, -1);
                        // set body of front snake
                        snakeFrontTargetSnake.setSnakeBody(sbp_tf);
                        // copy front part back to bitten snake
                        BeanUtils.copyProperties(snakeFrontTargetSnake, targetSnake);
                        // add remaining body to biting snake
                        bitingSnake.getSnakeBody().addAll(1, sbp_tb);
                    } else {
                        setGameOver();
                    }
                }
            }
        }

        // check if snake bites itself
        public void checkSnakeAgainstSelf(Snake snake) {
            for (int i = 1; i < snake.getSnakeBody().size(); i++) {
                // check if snakeHead is on same position as a body part
                if (snake.getSnakeBody().get(0).getPositionX() == snake.getSnakeBody().get(i).getPositionX() &&
                        snake.getSnakeBody().get(0).getPositionY() == snake.getSnakeBody().get(i).getPositionY()) {
                    setGameOver();
                }
            }
        }


        public void createNewFood() throws InterruptedException {
            // create new snake to store body of snake1 and snake2
            Snake totalSnake = new Snake(0, -1, -1);
            // add bodies
            totalSnake.getSnakeBody().addAll(playground.getSnake1().getSnakeBody());
            totalSnake.getSnakeBody().addAll(playground.getSnake2().getSnakeBody());
            start:
            while (true) {
                // create random food position - but not directly at border
                int foodX = rand.nextInt(playground.getWidth()-5);
                int foodY = rand.nextInt(playground.getHeight()-5);
                // check if food position is inside snake
                for (SnakeBodyPart sbp : totalSnake.getSnakeBody()) {
                    if (sbp.getPositionX() == foodX && sbp.getPositionY() == foodY) {
                        continue start;
                    }
                }
                // set food at free position
                playground.setFood(new Food(foodX, foodY));
                // increase speed and restart timer with new speed
                speed += 10;
                refreshTimer.cancel();
                startRefreshingCanvas();
                break;
            }
        }
    }
}
