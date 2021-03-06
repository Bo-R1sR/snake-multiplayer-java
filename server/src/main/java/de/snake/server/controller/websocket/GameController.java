package de.snake.server.controller.websocket;

import de.snake.server.domain.OutputMessage;
import de.snake.server.domain.game.Playground;
import de.snake.server.domain.game.ServerSounds;
import de.snake.server.domain.game.Snake;
import de.snake.server.service.GameService;
import de.snake.server.service.HistoryService;
import de.snake.server.service.SnakeUpdateService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class GameController {

    private final Playground playground;
    private final SimpMessagingTemplate template;
    private final GameService gameService;
    private final SnakeUpdateService snakeUpdateService;
    private final ServerSounds serverSounds;
    private final HistoryService historyService;
    private Timer refreshTimer;

    public GameController(Playground playground, SimpMessagingTemplate template, GameService gameService, SnakeUpdateService snakeUpdateService, ServerSounds serverSounds, HistoryService historyService) {
        this.playground = playground;
        this.template = template;
        this.gameService = gameService;
        this.snakeUpdateService = snakeUpdateService;
        this.serverSounds = serverSounds;
        this.historyService = historyService;
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
        if (!playground.isGameOver()) {
            playground.setGameOver(true);
            playground.setPlayer1active(false);
            playground.setPlayer2active(false);
            playground.setRunning(false);
            refreshTimer.cancel();


            if (playground.getLevelNumber() == 4) {
                playground.setDuringLevel(false);
                playground.setLevelFinish(true);
                serverSounds.setText("RoundOver");
                historyService.saveGame();
            } else {
                serverSounds.setText("GameOver");
            }
            template.convertAndSend("/topic/serverSounds", serverSounds);
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
                    snakeUpdateService.updateSnakePosition(snake, snake.getSnakeDirection());
                    // check snake head versus playground
                    if (snakeUpdateService.checkSnakeAgainstPlayground(snake)) {
                        template.convertAndSend("/topic/messages",
                                new OutputMessage("SYSTEM", "Spieler " + snake.getUsername() + " hat Spielfeldrand berührt.", new SimpleDateFormat("HH:mm").format(new Date())));
                        setGameOver();
                    }
                    // check snake head versus obstacles
                    if (snakeUpdateService.checkSnakeAgainstWall(snake)) {
                        template.convertAndSend("/topic/messages",
                                new OutputMessage("SYSTEM", "Spieler " + snake.getUsername() + " hat Hindernis berührt.", new SimpleDateFormat("HH:mm").format(new Date())));

                        setGameOver();
                    }
                    // check if snake bites itself
                        if (snakeUpdateService.checkSnakeAgainstSelf(snake)) {
                            template.convertAndSend("/topic/messages",
                                    new OutputMessage("SYSTEM", "Spieler " + snake.getUsername() + " hat sich selbst gebissen.", new SimpleDateFormat("HH:mm").format(new Date())));

                            setGameOver();
                        }
                    // check if snake can eat food
                    snakeUpdateService.checkSnakeAgainstFood(snake);
                } else snake.increaseCounter();
            }
            // wait for both snakes to update position before checking against each other
            if (snakeUpdateService.checkSnakeAgainstOther(snake2, snake1)) {
                setGameOver();
            }

            if (snakeUpdateService.checkSnakeAgainstOther(snake1, snake2)) {
                setGameOver();
            }

            if (snakeUpdateService.checkSnakeLength(snake1, snake2)) {
                setGameOver();
            }
            template.convertAndSend("/topic/playground", playground);
        }
    }
}
