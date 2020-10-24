package de.snake.fxclient.websocket;

import de.snake.fxclient.controller.GameController;
import de.snake.fxclient.domain.User;
import de.snake.fxclient.game.Snake;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class CustomStompSessionHandler extends StompSessionHandlerAdapter {

    private final GameController gameController;
    private final User user;
    private final Logger logger = LogManager.getLogger(CustomStompSessionHandler.class);
    private final Snake snake1;
    private final Snake snake2;

    public CustomStompSessionHandler(GameController gameController, User user, Snake snake1, Snake snake2) {
        this.gameController = gameController;
        this.user = user;
        this.snake1 = snake1;
        this.snake2 = snake2;
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
                System.out.println("PlayerId " + payload);
                Integer returnId = (Integer) payload;

                Platform.runLater(() -> user.setPlayerId(returnId));
            }

        });

       session.send("/app/playerId", getSampleMessage());

        session.subscribe("/topic/messages", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Message.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Message msg = (Message) payload;
                logger.info("Received : " + msg.getText() + " from : " + msg.getFrom());
                Platform.runLater(() -> gameController.getTestMessage().setText(msg.getText()));
            }

        });


        session.subscribe("/topic/snake1", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Snake.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("snake arrived");
                Snake returnSnake = (Snake) payload;
                snake1.setHeight(returnSnake.getHeight());
                snake1.setWidth(returnSnake.getWidth());
                snake1.setSnakeBody(returnSnake.getSnakeBody());

            }

        });

        session.subscribe("/topic/snake2", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Snake.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("snake arrived");
                Snake returnSnake = (Snake) payload;
                snake2.setHeight(returnSnake.getHeight());
                snake2.setWidth(returnSnake.getWidth());
                snake2.setSnakeBody(returnSnake.getSnakeBody());

            }

        });

    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        logger.error("Got an exception", exception);
    }
    private Message getSampleMessage() {
        Message msg = new Message();
        msg.setText("Hello from here");
        return msg;
    }
}
