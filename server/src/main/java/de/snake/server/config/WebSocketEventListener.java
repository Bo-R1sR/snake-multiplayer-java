package de.snake.server.config;

import de.snake.server.controller.GameController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Component
public class WebSocketEventListener {

    private final GameController gameController;


    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEventListener.class);

    public WebSocketEventListener(GameController gameController) {
        this.gameController = gameController;
    }

    @EventListener
    public void handleWebSocketConnectedListener(SessionConnectedEvent event) {
        LOGGER.info(Objects.requireNonNull(event.getUser()).getName() + " has connected");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        LOGGER.info(Objects.requireNonNull(event.getUser()).getName() + " has disconnected");
        gameController.setNumberOfConnections(gameController.getNumberOfConnections()-1);
    }

}
