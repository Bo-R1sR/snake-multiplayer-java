package de.snake.fxclient.controller;

import de.snake.fxclient.domain.User;
import de.snake.fxclient.websocket.Message;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@FxmlView("game-stage.fxml")
public class GameController {

    private final BackgroundController backgroundController;
    private final User user;

    @FXML
    private Label testMessage;

    @FXML
    private TextField chatMess;

    public GameController(BackgroundController backgroundController, User user) {
        this.backgroundController = backgroundController;
        this.user = user;
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
        StompSession session = user.getSession();
        session.send("/app/game", getSampleMessage());
    }

    private Message getSampleMessage() {
        Message msg = new Message();
        msg.setText(chatMess.getText());
        return msg;
    }

}
