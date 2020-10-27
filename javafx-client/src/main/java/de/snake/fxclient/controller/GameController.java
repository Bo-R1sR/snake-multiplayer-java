package de.snake.fxclient.controller;

import de.snake.fxclient.domain.User;
import de.snake.fxclient.game.Playground;
import de.snake.fxclient.game.ScreenText;
import de.snake.fxclient.game.SnakeBodyPart;
import de.snake.fxclient.websocket.Message;
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

    static int cornersize = 25;

    static Dir direction;
    public enum Dir {
        left, right, up, down
    }

    private final BackgroundController backgroundController;
    private final User user;
    private final Playground playground;
    private final ScreenText screenText;

    @FXML
    private Label testMessage;

    @FXML
    private TextField chatMess;

    @FXML
    private Canvas gameCanvas ;

    private static GraphicsContext gc ;

    private static StompSession session;

    public GameController(BackgroundController backgroundController, User user, Playground playground, ScreenText screenText) {
        this.backgroundController = backgroundController;
        this.user = user;
        this.playground = playground;
        this.screenText = screenText;
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
            session.send("/app/direction" + user.getPlayerId(), direction);

        });

        gc = gameCanvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, playground.getWidth() * playground.getSnakeBodySize(), playground.getHeight() * playground.getSnakeBodySize());

    }



    public void back() {
        backgroundController.changeView(MainController.class);
    }

    public void restartGame() {

    }



      public void begin() {
          int id = user.getPlayerId();
          session.send("/app/snakeNeg/"+id, getSampleMessage());
      }

    public void playGame() {
        session = user.getSession();
        session.send("/app/playerId", getSampleMessage());
        //session.send("/app/snake", getSampleMessage());

        // newFood();


//
    }

    public void updateScreenText() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, playground.getWidth() * playground.getSnakeBodySize(), playground.getHeight() * playground.getSnakeBodySize());


        gc.setFill(Color.RED);
        gc.setFont(new Font("", 30));
        gc.fillText(screenText.getPlayerText(), 100, 250);
    }

    public void updatePlayground() {
    //    public void update(GraphicsContext gc) {
       // session.send("/app/snake", getSampleMessage());
        if (playground.isGameOver()) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("", 50));
            gc.fillText("GAME OVER", 100, 250);
            return;
        }


//
//

        // background
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, playground.getWidth() * playground.getSnakeBodySize(), playground.getHeight() * playground.getSnakeBodySize());

        // score
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("", 30));
        //gc.fillText("Score: " + (speed - 6), 10, 30);

        // random foodcolor
        Color cc = Color.WHITE;

        switch (playground.getFood().getFoodColor()) {
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
        gc.fillOval(playground.getFood().getFoodX() * playground.getSnakeBodySize(), playground.getFood().getFoodY() * playground.getSnakeBodySize(), playground.getSnakeBodySize(), playground.getSnakeBodySize());



//        // snake
        for (SnakeBodyPart c : playground.getSnake1().getSnakeBody()) {
            gc.setFill(Color.LIGHTGREEN);
            gc.fillRect(c.getX() * cornersize, c.getY() * cornersize, cornersize - 1, cornersize - 1);
            gc.setFill(Color.GREEN);
            gc.fillRect(c.getX() * cornersize, c.getY() * cornersize, cornersize - 2, cornersize - 2);

        }
        for (SnakeBodyPart c : playground.getSnake2().getSnakeBody()) {
            gc.setFill(Color.LIGHTBLUE);
            gc.fillRect(c.getX() * cornersize, c.getY() * cornersize, cornersize - 1, cornersize - 1);
            gc.setFill(Color.BLUE);
            gc.fillRect(c.getX() * cornersize, c.getY() * cornersize, cornersize - 2, cornersize - 2);

        }
    }

private Message getSampleMessage() {
    Message msg = new Message();
    msg.setText(chatMess.getText());
    return msg;
}

    public Label getTestMessage() {
        return testMessage;
    }

    public void setTestMessage(Label testMessage) {
        this.testMessage = testMessage;
    }

    public void submitMessage() {
        session = user.getSession();
        session.send("/app/message", getSampleMessage());
    }
}
