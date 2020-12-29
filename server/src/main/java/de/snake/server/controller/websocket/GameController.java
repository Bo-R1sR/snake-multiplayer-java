package de.snake.server.controller.websocket;

import de.snake.server.config.WebSocketEventListener;
import de.snake.server.domain.game.Playground;
import de.snake.server.domain.game.Snake;
import de.snake.server.service.GameService;
import de.snake.server.service.HistoryService;
import de.snake.server.service.SnakeUpdateService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Controller
public class GameController {

    private final Playground playground;
    private final SimpMessagingTemplate template;
    private final GameService gameService;
    private final SnakeUpdateService snakeUpdateService;
    private final HistoryService historyService;
    private final WebSocketEventListener webSocketEventListener;
    private Timer refreshTimer;

    public GameController(Playground playground, SimpMessagingTemplate template, GameService gameService, SnakeUpdateService snakeUpdateService, HistoryService historyService, WebSocketEventListener webSocketEventListener) {
        this.playground = playground;
        this.template = template;
        this.gameService = gameService;
        this.snakeUpdateService = snakeUpdateService;
        this.historyService = historyService;
        this.webSocketEventListener = webSocketEventListener;
    }

    // entry point for each game
    public void startCounter() throws InterruptedException {
        gameService.initializePlayground();
        gameService.startCountdown();
        refreshTimer = new Timer();
        // refresh snake every fixed time at 30 frames per second
        refreshTimer.scheduleAtFixedRate(new SnakeUpdateTask(), 0, 33);
    }

    // in case of game over stop refreshing
    public void setGameOver() {
        playground.setGameOver(true);
        playground.setPlayer1active(false);
        playground.setPlayer2active(false);
        playground.setRunning(false);
        refreshTimer.cancel();

        if (playground.getLevelNumber() == 4) {
            playground.setDuringLevel(false);
            playground.setLevelFinish(true);
            historyService.save(playground.getSnake1().getPoints(), playground.getSnake2().getPoints(), webSocketEventListener.getUsername1(), webSocketEventListener.getUsername2());
        }
    }

    public class SnakeUpdateTask extends TimerTask {
        @Override
        public void run() {
            List<Snake> bothSnakes = new ArrayList<>();
            Snake snake1 = playground.getSnake1();
            Snake snake2 = playground.getSnake2();
            bothSnakes.add(snake1);
            bothSnakes.add(snake2);
            // speed control: counter starts with 0 and is increased every time this task is executed
            // speed starts with 10
            // when counter equals speed execute
            for (Snake snake : bothSnakes) {
                if (snake.getCounter() == snake.getSpeed()) {
                    snake.resetCounter();
                    // move snake head forward
                    //snakeUpdateService.updateSnakePosition(snake, snakeDirections.getDirection1());
                    snakeUpdateService.updateSnakePosition(snake, snake.getSnakeDirectionEnum());
                    // check snake head versus playground
                    if (snakeUpdateService.checkSnakeAgainstPlayground(snake)) {
                        setGameOver();
                    }
                    // check snake head versus obstacles
                    if (snakeUpdateService.checkSnakeAgainstWall(snake)) {
                        setGameOver();
                    }
                    // check if snake bites itself
                    if (!snake.isImmortal()) {
                        if (snakeUpdateService.checkSnakeAgainstSelf(snake)) {
                            setGameOver();
                        }
                    }
                    // check if snake can eat food
                    snakeUpdateService.checkSnakeAgainstFood(snake);
                } else snake.increaseCounter();
            }
            // wait for both snakes to update position before checking against each other
            if (!snake1.isImmortal()) {
                if (snakeUpdateService.checkSnakeAgainstOther(snake2, snake1)) {
                    setGameOver();
                }
            }
            if (!snake2.isImmortal()) {
                if (snakeUpdateService.checkSnakeAgainstOther(snake1, snake2)) {
                    setGameOver();
                }
            }
            if (snakeUpdateService.checkSnakeLength(snake1, snake2)) {
                setGameOver();
            }
            template.convertAndSend("/topic/playground", playground);
        }
    }
}

