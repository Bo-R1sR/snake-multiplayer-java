package de.snake.server.config;

import de.snake.server.controller.ChatController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Component
public class WebSocketEventListener {


    private final ChatController chatController;

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEventListener.class);

    public WebSocketEventListener(ChatController chatController) {
        this.chatController = chatController;
    }

    @EventListener
    public void handleWebSocketConnectedListener(SessionConnectedEvent event) {
        LOGGER.info(Objects.requireNonNull(event.getUser()).getName() + " has connected");
        //countConnections++;
        //String destination = "/user/" + event.getUser().getName() + "/queue/playerId";
        //this.template.convertAndSendToUser(event.getUser().getName(), "/user/queue/playerId", countConnections);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        LOGGER.info(Objects.requireNonNull(event.getUser()).getName() + " has disconnected");
        chatController.setNumberOfConnetion(chatController.getNumberOfConnetion()-1);

    }

}
