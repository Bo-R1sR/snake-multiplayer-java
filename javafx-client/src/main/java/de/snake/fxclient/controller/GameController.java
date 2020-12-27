package de.snake.fxclient.controller;

import de.snake.fxclient.domain.User;
import de.snake.fxclient.game.Playground;
import de.snake.fxclient.game.composite.Shape;
import de.snake.fxclient.game.composite.Square;
import de.snake.fxclient.game.message.Message;
import de.snake.fxclient.service.GameService;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("game-stage.fxml")
public class GameController {

    private final BackgroundController backgroundController;
    private final User user;
    private final Playground playground;
    private final GameService gameService;

    private GraphicsContext gc;

    @FXML
    private TextArea chatArea;
    @FXML
    private TextField chatMess;
    @FXML
    private Canvas gameCanvas;

    public GameController(BackgroundController backgroundController, User user, Playground playground, GameService gameService) {
        this.backgroundController = backgroundController;
        this.user = user;
        this.playground = playground;
        this.gameService = gameService;
    }

    @FXML
    public void initialize() {
        gc = gameCanvas.getGraphicsContext2D();
        Shape background = new Square(gc, Color.BLACK, new Point2D(0, 0), playground.getWidth() * playground.getSnakeBodySize(), playground.getHeight() * playground.getSnakeBodySize());
        background.draw();
    }

    public GraphicsContext getGC() {
        return gc;
    }

    // button for back to main menu - disconnect ws session
    public void back() {
        if (user.getSession() != null) {
            user.getSession().disconnect();
        }
        backgroundController.changeView(MainController.class);
    }

    // executed after Spiel starten is pressed
    public void initializeGame() {
        // send username to server
        // afterwards player id is assigned and returned to client
        // this callback will then execute method sendReadyToServer in PlayerAcitveController
        user.getSession().send("/app/playerId/" + user.getName(), "connect");
        // assign arrow keys for game control
        gameService.registerArrowKeys();
    }

    public void appendChatMessage(String message) {
        chatArea.appendText(message);
    }

    public void submitMessage() {
        user.getSession().send("/app/message", getNewMessage());
        chatMess.setText("");
    }

    private Message getNewMessage() {
        Message msg = new Message();
        msg.setFrom(user.getName());
        msg.setText(chatMess.getText());
        return msg;
    }
}
