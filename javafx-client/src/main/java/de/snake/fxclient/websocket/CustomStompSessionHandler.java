package de.snake.fxclient.websocket;

import de.snake.fxclient.controller.GameController;
import de.snake.fxclient.domain.User;
import de.snake.fxclient.game.Snake;
import de.snake.fxclient.game.SnakeBodyPart;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;

@Component
public class CustomStompSessionHandler extends StompSessionHandlerAdapter {

    private final GameController gameController;
    private final User user;

    @Autowired
    Snake snake;

    private final Logger logger = LogManager.getLogger(CustomStompSessionHandler.class);

    public CustomStompSessionHandler(GameController gameController, User user) {
        this.gameController = gameController;
        this.user = user;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        user.setSession(session);
        logger.info("New session established : " + session.getSessionId());
        //session.subscribe("/topic/messages", this);

        session.subscribe("/topic/messages", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Message.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Message msg = (Message) payload;
                logger.info("Received : " + msg.getText() + " from : " + msg.getFrom());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        gameController.getTestMessage().setText(msg.getText());
                    }
                });
            }

        });


        session.subscribe("/topic/snake", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Snake.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("snake arrived");
                Snake returnSnake = (Snake) payload;
                snake.setHeight(returnSnake.getHeight());
                snake.setWidth(returnSnake.getWidth());
                snake.setSnakeBody(returnSnake.getSnakeBody());
                // ...
            }

        });

        session.subscribe("/topic/snakeHead", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return SnakeBodyPart.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                SnakeBodyPart snakeHeadReturn = (SnakeBodyPart) payload;
                SnakeBodyPart snakeHead = snake.getSnakeBody().get(0);
                snakeHead.setX(snakeHeadReturn.getX());
                snakeHead.setY(snakeHeadReturn.getY());
            }
        });

    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        logger.error("Got an exception", exception);
    }

//    @Override
//    public Type getPayloadType(StompHeaders headers) {
//        return Message.class;
//    }
//
//    @Override
//    public void handleFrame(StompHeaders headers, Object payload) {
//        Message msg = (Message) payload;
//        logger.info("Received : " + msg.getText() + " from : " + msg.getFrom());
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                gameController.getTestMessage().setText(msg.getText());
//            }
//        });
//    }

//    session.subscribe("/topic/something", new StompFrameHandler() {
//
//        @Override
//        public Type getPayloadType(StompHeaders headers) {
//            return String.class;
//        }
//
//        @Override
//        public void handleFrame(StompHeaders headers, Object payload) {
//            // ...
//        }
//
//    });

}
