package de.snake.server.config;

import de.snake.server.domain.OutputMessage;
import de.snake.server.domain.game.Playground;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class WebSocketEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEventListener.class);
    private final SimpMessagingTemplate template;
    private final Playground playground;

    List<Integer> ids = new ArrayList<>(Arrays.asList(1, 2));
    HashMap<String, Integer> connectedPlayers = new HashMap<>();

    public WebSocketEventListener(SimpMessagingTemplate template, Playground playground) {
        this.template = template;
        this.playground = playground;
    }

    public HashMap<String, Integer> getConnectedPlayers() {
        return connectedPlayers;
    }

    public void setConnectedPlayers(HashMap<String, Integer> connectedPlayers) {
        this.connectedPlayers = connectedPlayers;
    }

    @EventListener
    // executed when new websocket connection is established
    public void handleWebSocketConnectedListener(SessionConnectedEvent event) {
        LOGGER.info(Objects.requireNonNull(event.getUser()).getName() + " has connected");
        // add first user with username and id 1
        connectedPlayers.put(event.getUser().getName(), ids.get(0));
        // remove id 1
        ids.remove(0);
        // when second user connects, assign remaining id 2 to username
    }

    @EventListener
    // executed when existing websocket connection is closed
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        LOGGER.info(Objects.requireNonNull(event.getUser()).getName() + " has disconnected");
        // when user disconnects add his id as back as available
        ids.add(connectedPlayers.get(event.getUser().getName()));
        // remove from map of connected users
        connectedPlayers.remove(event.getUser().getName());
        // set both players as not active so a restart of game is not possible
        playground.setPlayer1active(false);
        playground.setPlayer2active(false);
        // send disconnection to chat window
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        OutputMessage leavePlayer = new OutputMessage("SYSTEM", "Spieler " + event.getUser().getName() + " ist gegangen", time);
        this.template.convertAndSend("/topic/messages", leavePlayer);
    }

}
