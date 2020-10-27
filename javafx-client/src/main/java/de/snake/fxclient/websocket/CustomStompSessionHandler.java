package de.snake.fxclient.websocket;

import de.snake.fxclient.controller.GameController;
import de.snake.fxclient.domain.User;
import de.snake.fxclient.game.Playground;
import de.snake.fxclient.game.ScreenText;
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
//    private final Snake snake1;
//    private final Snake snake2;
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
                System.out.println("PlayerId " + payload);
                Integer returnId = (Integer) payload;

                Platform.runLater(() -> {
                    user.setPlayerId(returnId);
                    gameController.startGame();
                });
            }

        });

//       session.send("/app/playerId", getSampleMessage());

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

        session.subscribe("/topic/screenText", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ScreenText.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                ScreenText msg = (ScreenText) payload;
                logger.info("Received : " + msg.getPlayerText());
//                Playground returnedPlayground = (Playground) payload;
                BeanUtils.copyProperties(payload, screenText);
                gameController.updateScreenText();
                //Platform.runLater(() -> playgroundController.getTestMessage().setText(msg.getText()));
            }

        });


//        session.subscribe("/topic/snake1", new StompFrameHandler() {
//
//            @Override
//            public Type getPayloadType(StompHeaders headers) {
//                return Snake.class;
//            }
//
//            @Override
//            public void handleFrame(StompHeaders headers, Object payload) {
//                System.out.println("snake arrived");
//                Snake returnSnake = (Snake) payload;
//                snake1.setHeight(returnSnake.getHeight());
//                snake1.setWidth(returnSnake.getWidth());
//                snake1.setSnakeBody(returnSnake.getSnakeBody());
//
//            }
//
//        });
//
//        session.subscribe("/topic/snake2", new StompFrameHandler() {
//
//            @Override
//            public Type getPayloadType(StompHeaders headers) {
//                return Snake.class;
//            }
//
//            @Override
//            public void handleFrame(StompHeaders headers, Object payload) {
//                System.out.println("snake arrived");
//                Snake returnSnake = (Snake) payload;
//                snake2.setHeight(returnSnake.getHeight());
//                snake2.setWidth(returnSnake.getWidth());
//                snake2.setSnakeBody(returnSnake.getSnakeBody());
//
//            }
//
//        });

        session.subscribe("/topic/playground", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Playground.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                logger.info("Playground arrived");
//                Playground returnedPlayground = (Playground) payload;
                BeanUtils.copyProperties(payload, playground);
                gameController.updatePlayground();
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
