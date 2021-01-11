package de.snake.fxclient.websocket;

import de.snake.fxclient.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@Component
public class CustomStompClient {

    private final WebSocketStompClient webSocketStompClient;
    private final CustomStompSessionHandler sessionHandler;
    private final User user;
    @Value("${server.ip}")
    private String serverIp;

    public CustomStompClient(WebSocketStompClient webSocketStompClient, CustomStompSessionHandler sessionHandler, User user) {
        this.webSocketStompClient = webSocketStompClient;
        this.sessionHandler = sessionHandler;
        this.user = user;
    }

    public void connectToServerWS() {
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
        WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();
        handshakeHeaders.add("Authorization", user.getJsonWebToken());
        webSocketStompClient.connect("ws://snakeserver-env.eba-qsdrnpdw.eu-central-1.elasticbeanstalk.com/game", handshakeHeaders, sessionHandler);
    }


}
