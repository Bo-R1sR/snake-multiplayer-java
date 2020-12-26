package de.snake.server.controller;

import de.snake.server.domain.game.*;
import org.springframework.beans.BeanUtils;
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
    private final SnakeDirections snakeDirections;
    private final Level level;
    private Timer refreshTimer;
    private Timer immortalTimer;

    private int counter;

    public GameController(Playground playground, ScreenText screenText, SimpMessagingTemplate template, SnakeDirections snakeDirections, Level level) {
        this.playground = playground;
        this.screenText = screenText;
        this.template = template;
        this.snakeDirections = snakeDirections;
        this.level = level;
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
        screenText.setPlayerText("LOS GEHTS");
        this.template.convertAndSend("/topic/screenText", screenText);
        Thread.sleep(1000);
        screenText.setPlayerText("");
        this.template.convertAndSend("/topic/screenText", screenText);
        playground.setLevelNumber(rand.nextInt(5));
        startRefreshingCanvas();
    }

    public void initializePlayground() {
        // values for game start
        playground.setGameOver(false);
        // refreshing speed
        // initial movement direction for snakes
        snakeDirections.setDirection1(SnakeDirectionEnum.LEFT); //direction1 = SnakeDirection.LEFT;
        snakeDirections.setDirection2(SnakeDirectionEnum.RIGHT);//direction2 = SnakeDirection.RIGHT;
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
        // always same color at start, during game random
        playground.getFood().setFoodColor(0);
        // todo remove counter
        counter = 0;
    }


    public void startRefreshingCanvas() throws InterruptedException {
        refreshTimer = new Timer();
        // refresh snake every fixed time at 30 frames per second
        refreshTimer.scheduleAtFixedRate(new SnakeUpdateTask(), 0, 33);
    }

    // update snake position
    public void updateSnake(Snake snake, SnakeDirectionEnum direction) {
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
                    snake.setPoints(snake.getPoints() + 1);
                    setGameOver();
                }
                break;
            case DOWN:
                snakeHead.increaseY();
                if (snakeHead.getPositionY() > playground.getHeight() - 1) {
                    snake.setPoints(snake.getPoints() + 1);
                    setGameOver();
                }
                break;
            case LEFT:
                snakeHead.decreaseX();
                if (snakeHead.getPositionX() < 0) {
                    snake.setPoints(snake.getPoints() + 1);
                    setGameOver();
                }
                break;
            case RIGHT:
                snakeHead.increaseX();
                if (snakeHead.getPositionX() > playground.getWidth() - 1) {
                    snake.setPoints(snake.getPoints() + 1);
                    setGameOver();
                }
                break;
        }
    }

    // in case of game over stop refreshing
    public void setGameOver() {
        playground.setGameOver(true);
        playground.setPlayer1active(false);
        playground.setPlayer2active(false);
        refreshTimer.cancel();
        playground.setRunning(false);
        //todo hier die history speichern bzw. updaten
    }

    // check if snake bites other snake
    public void checkSnakeAgainstOther(Snake bitingSnake, Snake targetSnake) {
        for (int i = 0; i < targetSnake.getSnakeBody().size(); i++) {
            // check if snake head is on same position as other snake body
            if (bitingSnake.getSnakeBody().get(0).getPositionX() == targetSnake.getSnakeBody().get(i).getPositionX() &&
                    bitingSnake.getSnakeBody().get(0).getPositionY() == targetSnake.getSnakeBody().get(i).getPositionY()) {
                // only bite at red fields
                if (targetSnake.getSnakeBody().get(i).getColor() == 3) {
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
                    bitingSnake.setPoints(bitingSnake.getPoints() + 1);
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
                snake.setPoints(snake.getPoints() + 1);
                setGameOver();
            }
        }
    }

    public void checkSnakeAgainstWall(Snake snake) {
        for (int i = 0; i < level.getAllLevels().get(playground.getLevelNumber()).size(); i++) {
            if (snake.getSnakeBody().get(0).getPositionX() == level.getAllLevels().get(playground.getLevelNumber()).get(i).getPositionX() &&
                    snake.getSnakeBody().get(0).getPositionY() == level.getAllLevels().get(playground.getLevelNumber()).get(i).getPositionY()) {
                snake.setPoints(snake.getPoints() + 1);
                setGameOver();
            }

        }
    }


    public void createNewFood() {

        // create new snake to store body of snake1 and snake2
        Snake totalSnake = new Snake(0, -1, -1);
        // add bodies
        totalSnake.getSnakeBody().addAll(playground.getSnake1().getSnakeBody());
        totalSnake.getSnakeBody().addAll(playground.getSnake2().getSnakeBody());
        start:
        while (true) {
            // create random food position - but not directly at border
            int foodX = rand.nextInt(playground.getWidth() - 5);
            int foodY = rand.nextInt(playground.getHeight() - 5);
            // check if food position is inside snake
            for (SnakeBodyPart sbp : totalSnake.getSnakeBody()) {
                if (sbp.getPositionX() == foodX && sbp.getPositionY() == foodY) {
                    continue start;
                }
            }
            // set food at free position
            playground.setFood(new Food(foodX, foodY));
            break;
        }
    }

    public class SnakeUpdateTask extends TimerTask {
        @Override
        public void run() {

            // todo : remove counter: just for movement of second snake
            // every 20 frames
            if (playground.getSnake1().getCounter() % playground.getSnake1().getSpeed() == 0) {
                playground.getSnake1().setCounter(0);
                updateSnake(playground.getSnake1(), snakeDirections.getDirection1());
            }
            if (playground.getSnake2().getCounter() % playground.getSnake2().getSpeed() == 0) {
                playground.getSnake2().setCounter(0);
                counter++;
                if (counter < 5) {
                    snakeDirections.setDirection2(SnakeDirectionEnum.RIGHT);
                } else if (counter < 10) {
                    snakeDirections.setDirection2(SnakeDirectionEnum.UP);
                } else if (counter < 15) {
                    snakeDirections.setDirection2(SnakeDirectionEnum.LEFT);
                } else if (counter < 20) {
                    snakeDirections.setDirection2(SnakeDirectionEnum.DOWN);
                } else {
                    counter = 0;
                }
                updateSnake(playground.getSnake2(), snakeDirections.getDirection2());
            }

            checkSnakeAgainstWall(playground.getSnake1());
            checkSnakeAgainstWall(playground.getSnake2());

            checkSnakeLength(playground.getSnake1(), playground.getSnake2());

            checkSnakeAgainstFood(playground.getSnake1());
            if (!playground.getSnake1().isImmortal()) {
                checkSnakeAgainstSelf(playground.getSnake1());
                checkSnakeAgainstOther(playground.getSnake2(), playground.getSnake1());
            }

            checkSnakeAgainstFood(playground.getSnake2());
            if (!playground.getSnake2().isImmortal()) {
                checkSnakeAgainstSelf(playground.getSnake2());
                checkSnakeAgainstOther(playground.getSnake1(), playground.getSnake2());
            }
            // update speed counter
            playground.getSnake1().setCounter(playground.getSnake1().getCounter() + 1);
            playground.getSnake2().setCounter(playground.getSnake2().getCounter() + 1);
            // send modified playground to client
            template.convertAndSend("/topic/playground", playground);
        }

        public void checkSnakeLength(Snake snake1, Snake snake2) {
            // todo
            if (snake1.getSnakeBody().size() >= 5 * snake2.getSnakeBody().size()) {
                snake2.setPoints(snake2.getPoints() + 1);
                setGameOver();
            } else if (snake2.getSnakeBody().size() >= 5 * snake1.getSnakeBody().size()) {
                snake1.setPoints(snake1.getPoints() + 1);
                setGameOver();
            }
        }

        // check if snake eats food
        public void checkSnakeAgainstFood(Snake snake) {
            // check if snake head is on same position as food
            if (playground.getFood().getFoodPositionX() == snake.getSnakeBody().get(0).getPositionX() &&
                    playground.getFood().getFoodPositionY() == snake.getSnakeBody().get(0).getPositionY()) {
                // Color.PURPLE - add part
                if (playground.getFood().getFoodColor() == 0) {
                    snake.getSnakeBody().add(new SnakeBodyPart(-1, -1, 0));
                    // shift biting mark backwards
                    for (SnakeBodyPart sbp : snake.getSnakeBody()) {
                        // find first red part, set it to default, set last part
                        if (sbp.getColor() == 3) {
                            sbp.setColor(0);
                            snake.getSnakeBody().get(snake.getSnakeBody().size() - 1).setColor(3);
                            break;
                        }
//                    for (int i = snake.getSnakeBody().size()-2; i>= 3; i--) {
//                        if (snake.getSnakeBody().get(i).getColor() == 3) {
//                            snake.getSnakeBody().get(i).setColor(0);
//                            snake.getSnakeBody().get(i+1).setColor(3);
//                        }
                    }
                } // Color.LIGHTBLUE - remove last part
                else if (playground.getFood().getFoodColor() == 1) {
                    if (snake.getSnakeBody().size() > 5) {
                        snake.getSnakeBody().remove(snake.getSnakeBody().size() - 1);
                    }
                } // Color.YELLOW - set immortality
                else if (playground.getFood().getFoodColor() == 2) {
                    try {
                        immortalTimer.cancel();
                    } catch (Exception ignored) {
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
                } // Color.RED - position for biting
                else if (playground.getFood().getFoodColor() == 3) {
                    // first set color back to default
                    for (SnakeBodyPart sbp : snake.getSnakeBody()) {
                        sbp.setColor(0);
                    }
                    // assign red color according to length of snake
                    int sizeSnake = snake.getSnakeBody().size();
                    if (sizeSnake == 5 || sizeSnake == 6) {
                        snake.getSnakeBody().get(sizeSnake - 1).setColor(3);
                    } else if (sizeSnake > 5 && sizeSnake < 9) {
                        snake.getSnakeBody().get(sizeSnake - 1).setColor(3);
                        snake.getSnakeBody().get(sizeSnake - 2).setColor(3);
                    } else if (sizeSnake > 5 && sizeSnake < 14) {
                        snake.getSnakeBody().get(sizeSnake - 1).setColor(3);
                        snake.getSnakeBody().get(sizeSnake - 2).setColor(3);
                        snake.getSnakeBody().get(sizeSnake - 3).setColor(3);
                    } else if (sizeSnake > 5) {
                        snake.getSnakeBody().get(sizeSnake - 1).setColor(3);
                        snake.getSnakeBody().get(sizeSnake - 2).setColor(3);
                        snake.getSnakeBody().get(sizeSnake - 3).setColor(3);
                        snake.getSnakeBody().get(sizeSnake - 4).setColor(3);
                    }
                    snake.getSnakeBody().add(new SnakeBodyPart(-1, -1, 3));
                } // Color.PINK - snake faster
                else if (playground.getFood().getFoodColor() == 4) {
                    if (snake.getSpeed() > 4) {
                        snake.setSpeed(snake.getSpeed() - 2);
                        snake.setCounter(0);
                    }
                } // Color.GREEN - snake slower
                else if (playground.getFood().getFoodColor() == 5) {
                    if (snake.getSpeed() < 16) {
                        snake.setSpeed(snake.getSpeed() + 2);
                        snake.setCounter(0);
                    }
                }

                // if method not quick enough, create food for sending so client
                playground.setFood(new Food(-1, -1));
                // create Food in own Thread because it can take awhile when screen is already full
                Runnable runnable =
                        GameController.this::createNewFood;
                Thread thread = new Thread(runnable);
                thread.start();
            }
        }
    }
}

