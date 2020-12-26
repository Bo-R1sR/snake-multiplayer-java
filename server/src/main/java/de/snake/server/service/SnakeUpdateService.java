package de.snake.server.service;

import de.snake.server.domain.game.*;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class SnakeUpdateService {
    private final Playground playground;
    private final Level level;
    private final Random rand = new Random();
    private Timer immortalTimer;
    private final SimpMessagingTemplate template;
    private final ServerSounds serverSounds;

    public SnakeUpdateService(Playground playground, Level level, SimpMessagingTemplate template, ServerSounds serverSounds) {
        this.playground = playground;
        this.level = level;
        this.template = template;
        this.serverSounds = serverSounds;
    }

    public void updateSnakePosition(Snake snake, SnakeDirectionEnum direction) {
        for (int i = snake.getSnakeBody().size() - 1; i >= 1; i--) {
            snake.getSnakeBody().get(i).setPositionX(snake.getSnakeBody().get(i - 1).getPositionX());
            snake.getSnakeBody().get(i).setPositionY(snake.getSnakeBody().get(i - 1).getPositionY());
        }
        SnakeBodyPart snakeHead = snake.getSnakeBody().get(0);
        switch (direction) {
            case UP:
                snakeHead.decreaseY();
                break;
            case DOWN:
                snakeHead.increaseY();
                break;
            case LEFT:
                snakeHead.decreaseX();
                break;
            case RIGHT:
                snakeHead.increaseX();
                break;
        }
    }

    public boolean checkSnakeAgainstPlayground(Snake snake) {
        SnakeBodyPart snakeHead = snake.getHead();
        if (snakeHead.getPositionY() < 0 || snakeHead.getPositionX() < 0 ||
                snakeHead.getPositionY() > playground.getHeight() - 1 || snakeHead.getPositionX() > playground.getWidth() - 1) {
            snake.increasePoints();
            return true;
        } else return false;
    }

    public boolean checkSnakeAgainstWall(Snake snake) {
        for (int i = 0; i < level.getAllLevels().get(playground.getLevelNumber()).size(); i++) {
            if (snake.getSnakeBody().get(0).getPositionX() == level.getAllLevels().get(playground.getLevelNumber()).get(i).getPositionX() &&
                    snake.getSnakeBody().get(0).getPositionY() == level.getAllLevels().get(playground.getLevelNumber()).get(i).getPositionY()) {
                snake.setPoints(snake.getPoints() + 1);
                return true;
            }
        }
        return false;
    }

    public boolean checkSnakeLength(Snake snake1, Snake snake2) {
        if (snake1.getSnakeBody().size() >= 5 * snake2.getSnakeBody().size()) {
            snake2.setPoints(snake2.getPoints() + 1);
            return true;
        } else if (snake2.getSnakeBody().size() >= 5 * snake1.getSnakeBody().size()) {
            snake1.setPoints(snake1.getPoints() + 1);
            return true;
        }
        return false;
    }

    public void checkSnakeAgainstFood(Snake snake) {
        // check if snake head is on same position as food
        if (playground.getFood().getFoodPositionX() == snake.getHead().getPositionX() &&
                playground.getFood().getFoodPositionY() == snake.getHead().getPositionY()) {
            serverSounds.setText("Food");
            template.convertAndSend("/topic/serverSounds", serverSounds);
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
                    SnakeUpdateService.this::createNewFood;
            Thread thread = new Thread(runnable);
            thread.start();
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
            // create random food position
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

    // check if snake bites itself
    public boolean checkSnakeAgainstSelf(Snake snake) {
        for (int i = 1; i < snake.getSnakeBody().size(); i++) {
            // check if snakeHead is on same position as a body part
            if (snake.getHead().getPositionX() == snake.getSnakeBody().get(i).getPositionX() &&
                    snake.getHead().getPositionY() == snake.getSnakeBody().get(i).getPositionY()) {
                snake.increasePoints();
                return true;
            }
        }
        return false;
    }

    // check if snake bites other snake
    public boolean checkSnakeAgainstOther(Snake bitingSnake, Snake targetSnake) {
        for (int i = 0; i < targetSnake.getSnakeBody().size(); i++) {
            // check if snake head is on same position as other snake body
            if (bitingSnake.getHead().getPositionX() == targetSnake.getSnakeBody().get(i).getPositionX() &&
                    bitingSnake.getHead().getPositionY() == targetSnake.getSnakeBody().get(i).getPositionY()) {
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
                    bitingSnake.increasePoints();
                    return true;
                }
            }
        }
        return false;
    }
}
