package de.snake.fxclient.controller;

import de.snake.fxclient.domain.User;
import de.snake.fxclient.game.SnakeBodyPart;
import de.snake.fxclient.game.Snake;
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

import java.util.Random;

@Component
@FxmlView("game-stage.fxml")
public class GameController {

    static int speed = 5;
    static int foodcolor = 0;
    static int width = 20;
    static int height = 20;
    static int foodX = 0;
    static int foodY = 0;
    static int cornersize = 25;
    //static List<Snake.Corner> snake = new ArrayList<>();
    static Dir direction = Dir.left;
    static boolean gameOver = false;
    static Random rand = new Random();

    public enum Dir {
        left, right, up, down
    }

    private final BackgroundController backgroundController;
    private final User user;
    private final Snake snake;

    @FXML
    private Label testMessage;

    @FXML
    private TextField chatMess;

    @FXML
    private Canvas gameCanvas ;

    private GraphicsContext gc ;

    private static StompSession session;

    public GameController(BackgroundController backgroundController, User user, Snake snake) {
        this.backgroundController = backgroundController;
        this.user = user;

        this.snake = snake;
    }

    @FXML
    public void initialize() {

        Scene scene = backgroundController.getViewHolder().getParent().getScene();
        scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            if (key.getCode() == KeyCode.UP) {
                direction = Dir.up;
            }
            if (key.getCode() == KeyCode.LEFT) {
                direction = Dir.left;
            }
            if (key.getCode() == KeyCode.DOWN) {
                direction = Dir.down;
            }
            if (key.getCode() == KeyCode.RIGHT) {
                direction = Dir.right;
            }
            session.send("/app/direction", direction);

        });

        gc = gameCanvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width * cornersize, height * cornersize);
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
        session = user.getSession();

        session.send("/app/game", getSampleMessage());


        startGame();

    }


    public void startGame() {

        session.send("/app/snake2", getSampleMessage());
       // newFood();

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



//        snake.add(new Snake.Corner(width / 2, height / 2));
//        snake.add(new Snake.Corner(width / 2, height / 2));
//        snake.add(new Snake.Corner(width / 2, height / 2));

    }


    public void tick(GraphicsContext gc) {
        System.out.println("ticked");
       // session.send("/app/snake", getSampleMessage());
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("", 50));
            gc.fillText("GAME OVER", 100, 250);
            return;
        }



//        for (int i = snake.getSnakeBody().size() - 1; i >= 1; i--) {
//            snake.getSnakeBody().get(i).setX(snake.getSnakeBody().get(i - 1).getX());
//            snake.getSnakeBody().get(i).setY(snake.getSnakeBody().get(i - 1).getY());
//        }



//        // eat food
//        if (foodX == snake.get(0).x && foodY == snake.get(0).y) {
//            snake.add(new Snake.Corner(-1, -1));
//            newFood();
//        }
//
//        // self destroy
//        for (int i = 1; i < snake.size(); i++) {
//            if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
//                gameOver = true;
//            }
//        }

        // fill
        // background
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



//        // snake
        for (SnakeBodyPart c : snake.getSnakeBody()) {
            gc.setFill(Color.LIGHTGREEN);
            gc.fillRect(c.getX() * cornersize, c.getY() * cornersize, cornersize - 1, cornersize - 1);
            gc.setFill(Color.GREEN);
            gc.fillRect(c.getX() * cornersize, c.getY() * cornersize, cornersize - 2, cornersize - 2);

        }
    }
//    // food
//    public static void newFood() {
//        start: while (true) {
//            foodX = rand.nextInt(width);
//            foodY = rand.nextInt(height);
//
//            for (Snake.Corner c : snake) {
//                if (c.x == foodX && c.y == foodY) {
//                    continue start;
//                }
//            }
//            foodcolor = rand.nextInt(5);
//            speed++;
//            break;
//
//        }
//    }
private Message getSampleMessage() {
    Message msg = new Message();
    msg.setText(chatMess.getText());
    return msg;
}

}
