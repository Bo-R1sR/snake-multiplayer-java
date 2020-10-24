package de.snake.server.controller;

import de.snake.server.domain.SnakeDirection;
import de.snake.server.game.Snake;
import de.snake.server.game.SnakeBodyPart;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Timer;
import java.util.TimerTask;

@Controller
public class SnakeController {

    private final Snake snake1;
    private final Snake snake2;
    private final SimpMessagingTemplate template;
    private final int speed = 1;
    SnakeDirection direction1 = SnakeDirection.left;
    SnakeDirection direction2 = SnakeDirection.right;

    public SnakeController(Snake snake1, Snake snake2, SimpMessagingTemplate template) {
        this.snake1 = snake1;
        this.snake2 = snake2;
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


    @MessageMapping("/snake")
    public void changeSnake() throws InterruptedException {

        //peridod: delay between TastExecutions
        new Timer().scheduleAtFixedRate(new SnakeUpdateTask(), 0, 1000/(speed));
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
        this.template.convertAndSend("/topic/snake" + number, snake);
    }

    public class SnakeUpdateTask extends TimerTask {
        @Override
        public void run() {
            updateSnake(snake1, direction1, 1);
            updateSnake(snake2, direction2, 2);
            System.out.println("TEST");
        }
    }
}
