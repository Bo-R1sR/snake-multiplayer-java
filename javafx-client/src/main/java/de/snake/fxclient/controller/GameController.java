package de.snake.fxclient.controller;

import de.snake.fxclient.domain.User;
import de.snake.fxclient.websocket.Message;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@FxmlView("game-stage.fxml")
public class GameController {

    private final BackgroundController backgroundController;
    private final User user;

    static Scene scene;

//    public Scene getScene() {
//        return scene;
//    }
//
//    public void setScene(Scene scene) {
//        this.scene = scene;
//    }

    @FXML
    private Label testMessage;

    @FXML
    private TextField chatMess;

    @FXML
    private Canvas gameCanvas ;

    GraphicsContext gc ;

    public GameController(BackgroundController backgroundController, User user) {
        this.backgroundController = backgroundController;
        this.user = user;
    }

    @FXML
    public void initialize() {
        scene = backgroundController.getViewHolder().getParent().getScene();


        gc = gameCanvas.getGraphicsContext2D();
//        gc.setFill(Color.BLACK);
//        System.out.println("color set to black");
//        gc.fillRect(50, 50, 100, 100);
//        System.out.println("draw rectangle");
    }

    public Label getTestMessage() {
        return testMessage;
    }

    public void setTestMessage(Label testMessage) {
        this.testMessage = testMessage;
    }

    public void back() {
        backgroundController.changeView(MainController.class);
    }


    public void submitMessage() {
        startGame();
//        gc.setFill(Color.AQUA);
//        gc.fillRect(10,10,100,100);
        StompSession session = user.getSession();
        session.send("/app/game", getSampleMessage());
    }

    private Message getSampleMessage() {
        Message msg = new Message();
        msg.setText(chatMess.getText());
        return msg;
    }

    static int speed = 5;
    static int foodcolor = 0;
    static int width = 20;
    static int height = 20;
    static int foodX = 0;
    static int foodY = 0;
    static int cornersize = 25;
    static List<Snake.Corner> snake = new ArrayList<>();
    static Snake.Dir direction = Snake.Dir.left;
    static boolean gameOver = false;
    static Random rand = new Random();

    public static class Corner {
        int x;
        int y;

        public Corner(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }

    public void startGame() {
        newFood();

        new AnimationTimer() {
            long lastTick = 0;

            public void handle(long now) {
                if (lastTick == 0) {
                    lastTick = now;
                    tick(gc);
                    return;
                }

                if (now - lastTick > 1000000000 / speed) {
                    lastTick = now;
                    tick(gc);
                }
            }

        }.start();

        snake.add(new Snake.Corner(width / 2, height / 2));
        snake.add(new Snake.Corner(width / 2, height / 2));
        snake.add(new Snake.Corner(width / 2, height / 2));

    }


    public static void tick(GraphicsContext gc) {
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("", 50));
            gc.fillText("GAME OVER", 100, 250);
            return;
        }

        for (int i = snake.size() - 1; i >= 1; i--) {
            snake.get(i).x = snake.get(i - 1).x;
            snake.get(i).y = snake.get(i - 1).y;
        }


       // viewHolder.getParent().getScene();
//        // control

        scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            if (key.getCode() == KeyCode.UP) {
                direction = Snake.Dir.up;
            }
            if (key.getCode() == KeyCode.LEFT) {
                direction = Snake.Dir.left;
            }
            if (key.getCode() == KeyCode.DOWN) {
                direction = Snake.Dir.down;
            }
            if (key.getCode() == KeyCode.RIGHT) {
                direction = Snake.Dir.right;
            }

        });


        switch (direction) {
            case up:
                snake.get(0).y--;
                if (snake.get(0).y < 0) {
                    gameOver = true;
                }
                break;
            case down:
                snake.get(0).y++;
                if (snake.get(0).y > height) {
                    gameOver = true;
                }
                break;
            case left:
                snake.get(0).x--;
                if (snake.get(0).x < 0) {
                    gameOver = true;
                }
                break;
            case right:
                snake.get(0).x++;
                if (snake.get(0).x > width) {
                    gameOver = true;
                }
                break;

        }

        // eat food
        if (foodX == snake.get(0).x && foodY == snake.get(0).y) {
            snake.add(new Snake.Corner(-1, -1));
            newFood();
        }

        // self destroy
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
                gameOver = true;
            }
        }

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width * cornersize, height * cornersize);

        // score
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("", 30));
        gc.fillText("Score: " + (speed - 6), 10, 30);

        // random foodcolor
        Color cc = Color.WHITE;

        switch (foodcolor) {
            case 0:
                cc = Color.PURPLE;
                break;
            case 1:
                cc = Color.LIGHTBLUE;
                break;
            case 2:
                cc = Color.YELLOW;
                break;
            case 3:
                cc = Color.PINK;
                break;
            case 4:
                cc = Color.ORANGE;
                break;
        }
        gc.setFill(cc);
        gc.fillOval(foodX * cornersize, foodY * cornersize, cornersize, cornersize);



        // snake
        for (Snake.Corner c : snake) {
            gc.setFill(Color.LIGHTGREEN);
            gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 1, cornersize - 1);
            gc.setFill(Color.GREEN);
            gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 2, cornersize - 2);

        }
    }
    // food
    public static void newFood() {
        start: while (true) {
            foodX = rand.nextInt(width);
            foodY = rand.nextInt(height);

            for (Snake.Corner c : snake) {
                if (c.x == foodX && c.y == foodY) {
                    continue start;
                }
            }
            foodcolor = rand.nextInt(5);
            speed++;
            break;

        }
    }
}
