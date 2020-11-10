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
    HashMap<String, Integer> players = new HashMap<>();

    public WebSocketEventListener(SimpMessagingTemplate template, Playground playground) {
        this.template = template;
        this.playground = playground;
    }

    public HashMap<String, Integer> getPlayers() {
        return players;
    }

    public void setPlayers(HashMap<String, Integer> players) {
        this.players = players;
    }

    @EventListener
    public void handleWebSocketConnectedListener(SessionConnectedEvent event) {
        LOGGER.info(Objects.requireNonNull(event.getUser()).getName() + " has connected");
        players.put(event.getUser().getName(), ids.get(0));
        ids.remove(0);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        LOGGER.info(Objects.requireNonNull(event.getUser()).getName() + " has disconnected");
        ids.add(players.get(event.getUser().getName()));
        players.remove(event.getUser().getName());
        playground.setPlayer1active(false);
        playground.setPlayer2active(false);
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        OutputMessage leavePlayer = new OutputMessage("SYSTEM", "Spieler " + event.getUser().getName() + " ist gegangen", time);
        this.template.convertAndSend("/topic/messages", leavePlayer);
    }

}
