package de.snake.server.controller;

import de.snake.server.domain.SnakeDirection;
import de.snake.server.game.Snake;
import de.snake.server.game.SnakeBodyPart;
import javafx.animation.AnimationTimer;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Timer;
import java.util.TimerTask;

@Controller
public class GameController {

    private final Snake snake;
    private final SimpMessagingTemplate template;

    private int speed = 5;

    public GameController(Snake snake, SimpMessagingTemplate template) {
        this.snake = snake;
        this.template = template;
    }



    public void tick() {
        Snake finalSnake = get();
        System.out.println(finalSnake.toString());
        this.template.convertAndSend("/topic/snake", finalSnake);
    }

    SnakeDirection direction = SnakeDirection.left;

        @MessageMapping("/snake2")
        @SendTo("/topic/snake")
        public void submit() throws InterruptedException {
            new Timer().scheduleAtFixedRate(new NewsletterTask(), 0, 1000);

            for (int i = 0; i < 3; i++) {
                Thread.sleep(1000);
            }
        }

    public class NewsletterTask extends TimerTask {
        @Override
        public void run() {
            tick();

        }
    }

    @MessageMapping("/snake")
    @SendTo("/topic/snake")
    public Snake get() {

        for (int i = snake.getSnakeBody().size() - 1; i >= 1; i--) {
            snake.getSnakeBody().get(i).setX(snake.getSnakeBody().get(i - 1).getX());
            snake.getSnakeBody().get(i).setY(snake.getSnakeBody().get(i - 1).getY());
        }
        Snake movedSnake = calculateNewPosition(direction);

        return movedSnake;

    }


    @MessageMapping("/direction")
    @SendTo("/topic/snakeDirection")
    public void changeDirection(SnakeDirection direction) {
        this.direction = direction;
    }


    public Snake calculateNewPosition(SnakeDirection direction) {
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
        return snake;

    }

}
