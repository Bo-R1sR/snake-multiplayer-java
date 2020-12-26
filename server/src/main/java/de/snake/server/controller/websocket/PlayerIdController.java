package de.snake.server.controller.websocket;

import de.snake.server.config.WebSocketEventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.HashMap;

@Controller
public class PlayerIdController {

    private final WebSocketEventListener webSocketEventListener;

    public PlayerIdController(WebSocketEventListener webSocketEventListener) {
        this.webSocketEventListener = webSocketEventListener;
    }

    // assign id 1 or 2 to player and send this id to client
    @MessageMapping("/playerId/{username}")
    @SendToUser(destinations = "/queue/playerId", broadcast = false)
    public Integer sendID(@DestinationVariable String username) {
        HashMap<String, Integer> players = webSocketEventListener.getPlayers();
        return players.get(username);
    }

}
