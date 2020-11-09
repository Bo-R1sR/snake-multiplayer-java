package de.snake.fxclient.websocket;

import de.snake.fxclient.controller.GameController;
import de.snake.fxclient.domain.User;
import de.snake.fxclient.game.Playground;
import de.snake.fxclient.game.ScreenText;
import de.snake.fxclient.game.message.InputMessage;
import de.snake.fxclient.game.message.Message;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class CustomStompSessionHandler extends StompSessionHandlerAdapter {

    private final GameController gameController;
    private final ScreenText screenText;
    private final User user;
    private final Logger logger = LogManager.getLogger(CustomStompSessionHandler.class);
    private final Playground playground;

    public CustomStompSessionHandler(GameController gameController, ScreenText screenText, User user, Playground playground) {
        this.gameController = gameController;
        this.screenText = screenText;
        this.user = user;
        this.playground = playground;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        user.setSession(session);
        logger.info("New session established : " + session.getSessionId());
        session.subscribe("/user/queue/playerId", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Integer.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                logger.info("PlayerId " + payload);

                Platform.runLater(() -> {
                    user.setPlayerId((Integer) payload);
                    gameController.startGame();
                });
            }
        });

        session.subscribe("/topic/screenText", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ScreenText.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                //logger.info("Received : screenText");
                BeanUtils.copyProperties(payload, screenText);
                if (!user.isReadyToPlay()) {
                    screenText.setPlayerText("der andere Spieler ist bereit");
                }
                gameController.updateScreenText();
            }
        });

        session.subscribe("/topic/playground", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Playground.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                //logger.info("Playground arrived");
                BeanUtils.copyProperties(payload, playground);
                gameController.updatePlayground();
            }
        });

        session.subscribe("/topic/messages", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return InputMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                InputMessage msg = (InputMessage) payload;

                String outputFormat = System.lineSeparator() + "<" + msg.getTime() + " " + msg.getFrom() + "> " + msg.getText();
                logger.info("Received message");
                //Platform.runLater(() -> gameController.getTestMessage().setText(outputFormat));
                Platform.runLater(() -> gameController.appendChatMessage(outputFormat, msg.getColor()));
            }
        });

        Message joinMessage = new Message();
        joinMessage.setFrom("SYSTEM");
        joinMessage.setText("Spieler " + user.getName() + " ist hinzugekommen");
        joinMessage.setColor(1);
        session.send("/app/message", joinMessage);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        logger.error("An exception occurred: ", exception);
    }
}
