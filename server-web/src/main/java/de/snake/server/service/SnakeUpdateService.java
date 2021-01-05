package de.snake.server.service;

import de.snake.server.domain.OutputMessage;
import de.snake.server.domain.game.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class SnakeUpdateService {
    private final Playground playground;
    private final Level level;
    private final Random rand = new Random();
    private final SimpMessagingTemplate template;
    private final ServerSounds serverSounds;
    private Timer immortalTimer1;
    private Timer bitingTimer1;
    private Timer immortalTimer2;
    private Timer bitingTimer2;

    public SnakeUpdateService(Playground playground, Level level, SimpMessagingTemplate template, ServerSounds serverSounds) {
        this.playground = playground;
        this.level = level;
        this.template = template;
        this.serverSounds = serverSounds;
    }

    public void updateSnakePosition(Snake snake, SnakeDirection direction) {
        for (int i = snake.getSnakeBody().size() - 1; i >= 1; i--) {
            snake.getSnakeBody().get(i).setPositionX(snake.getSnakeBody().get(i - 1).getPositionX());
            snake.getSnakeBody().get(i).setPositionY(snake.getSnakeBody().get(i - 1).getPositionY());
        }
        SnakeBodyPart snakeHead = snake.getSnakeBody().get(0);
        switch (direction) {
            case UP:
                snakeHead.decreaseY();
                snake.setMoveDirection(direction);
                break;
            case DOWN:
                snakeHead.increaseY();
                snake.setMoveDirection(direction);
                break;
            case LEFT:
                snakeHead.decreaseX();
                snake.setMoveDirection(direction);
                break;
            case RIGHT:
                snakeHead.increaseX();
                snake.setMoveDirection(direction);
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
                snake.increasePoints();
                return true;
            }
        }
        return false;
    }

    public boolean checkSnakeLength(Snake snake1, Snake snake2) {
        // if snake is 10 elements longer than other snake
        if (snake1.getSnakeBody().size() >= snake2.getSnakeBody().size() + 5) {
            snake2.increasePoints();
            template.convertAndSend("/topic/messages",
                    new OutputMessage("SYSTEM", "Spieler " + snake2.getUsername() + " ist 10 Felder kürzer als Gegner.", new SimpleDateFormat("HH:mm").format(new Date())));

            return true;
        }
        if (snake2.getSnakeBody().size() >= snake1.getSnakeBody().size() + 5) {
            snake1.increasePoints();
            template.convertAndSend("/topic/messages",
                    new OutputMessage("SYSTEM", "Spieler " + snake1.getUsername() + " ist 10 Felder kürzer als Gegner.", new SimpleDateFormat("HH:mm").format(new Date())));

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
                if (snake.getSnakeBody().size() > 3) {
                    snake.getSnakeBody().remove(snake.getSnakeBody().size() - 1);
                }
            } // Color.ORANGE - set immortality
            else if (playground.getFood().getFoodColor() == 2) {
                try {
                    if (snake == playground.getSnake1()) immortalTimer1.cancel();
                    if (snake == playground.getSnake2()) immortalTimer2.cancel();
                } catch (Exception ignored) {
                }

                snake.setImmortal(true);
                // reset immortality after specified time
                if (snake == playground.getSnake1()) {
                    immortalTimer1 = new Timer();
                    immortalTimer1.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            snake.setImmortal(false);
                        }
                        // snake will be immortal for 30 seconds
                    }, 30000);
                }
                if (snake == playground.getSnake2()) {
                    immortalTimer2 = new Timer();
                    immortalTimer2.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            snake.setImmortal(false);
                        }
                        // snake will be immortal for 30 seconds
                    }, 30000);
                }

            } // Color.RED - position for biting
            else if (playground.getFood().getFoodColor() == 3) {
                try {
                    if (snake == playground.getSnake1()) bitingTimer1.cancel();
                    if (snake == playground.getSnake2()) bitingTimer2.cancel();
                } catch (Exception ignored) {
                }

                snake.setPossibleToBite(true);
                // reset after specified time
                if (snake == playground.getSnake1()) {

                    bitingTimer1 = new Timer();
                    bitingTimer1.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            snake.setPossibleToBite(false);
                            for (SnakeBodyPart sbp : snake.getSnakeBody()) {
                                sbp.setColor(0);
                            }
                        }
                        // snake can be bitten for 30 seconds
                    }, 30000);
                }
                if (snake == playground.getSnake2()) {

                    bitingTimer2 = new Timer();
                    bitingTimer2.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            snake.setPossibleToBite(false);
                            for (SnakeBodyPart sbp : snake.getSnakeBody()) {
                                sbp.setColor(0);
                            }
                        }
                        // snake can be bitten for 30 seconds
                    }, 30000);
                }

                // first set color back to default
                for (SnakeBodyPart sbp : snake.getSnakeBody()) {
                    sbp.setColor(0);
                }
                // assign red color according to length of snake
                int sizeSnake = snake.getSnakeBody().size();
                if (sizeSnake == 3) {

                } else if (sizeSnake == 4 || sizeSnake == 5) {
                    snake.getSnakeBody().get(sizeSnake - 1).setColor(3);
                } else if (sizeSnake <= 7) {
                    snake.getSnakeBody().get(sizeSnake - 1).setColor(3);
                    snake.getSnakeBody().get(sizeSnake - 2).setColor(3);
                } else if (sizeSnake <= 9) {
                    snake.getSnakeBody().get(sizeSnake - 1).setColor(3);
                    snake.getSnakeBody().get(sizeSnake - 2).setColor(3);
                    snake.getSnakeBody().get(sizeSnake - 3).setColor(3);
                } else if (sizeSnake <= 14) {
                    snake.getSnakeBody().get(sizeSnake - 1).setColor(3);
                    snake.getSnakeBody().get(sizeSnake - 2).setColor(3);
                    snake.getSnakeBody().get(sizeSnake - 3).setColor(3);
                    snake.getSnakeBody().get(sizeSnake - 4).setColor(3);
                } else {
                    snake.getSnakeBody().get(sizeSnake - 1).setColor(3);
                    snake.getSnakeBody().get(sizeSnake - 2).setColor(3);
                    snake.getSnakeBody().get(sizeSnake - 3).setColor(3);
                    snake.getSnakeBody().get(sizeSnake - 4).setColor(3);
                    snake.getSnakeBody().get(sizeSnake - 5).setColor(3);
                }

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
        totalSnake.getSnakeBody().addAll(level.getAllLevels().get(playground.getLevelNumber()));

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
        int numberOfParts = 0;
        int original_size = targetSnake.getSnakeBody().size();
        for (int i = 0; i < targetSnake.getSnakeBody().size(); i++) {
            // check if snake head is on same position as other snake body
            if (bitingSnake.getHead().getPositionX() == targetSnake.getSnakeBody().get(i).getPositionX() &&
                    bitingSnake.getHead().getPositionY() == targetSnake.getSnakeBody().get(i).getPositionY()) {
                // only bite if possible
                if (targetSnake.isPossibleToBite() && !targetSnake.isImmortal()) {
                    for (SnakeBodyPart sbp : targetSnake.getSnakeBody()) {
                        if (sbp.getColor() == 3) {
                            numberOfParts += 1;
                        }
                    }
                    if (numberOfParts > 0) {
                        for (int ii = original_size - 1; ii >= original_size - numberOfParts; ii--) {
                            targetSnake.getSnakeBody().remove(ii);
                            bitingSnake.getSnakeBody().add(new SnakeBodyPart(-1, -1, 0));
                        }
                        serverSounds.setText("Biting");
                        template.convertAndSend("/topic/serverSounds", serverSounds);
                    }
                } else {
                    bitingSnake.increasePoints();
                    template.convertAndSend("/topic/messages",
                            new OutputMessage("SYSTEM", "Spieler " + bitingSnake.getUsername() + " hat Gegner gebissen.", new SimpleDateFormat("HH:mm").format(new Date())));

                    return true;
                }
            }
        }
        return false;
    }
}
