package de.snake.fxclient.websocket;

import de.snake.fxclient.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;

@Component
public class CustomStompClient {

    @Value("${server.ip}")
    private String serverIp;

    private final WebSocketStompClient webSocketStompClient;
    private final CustomStompSessionHandler sessionHandler;
    private final User user;

    public CustomStompClient(WebSocketStompClient webSocketStompClient, CustomStompSessionHandler sessionHandler, User user) {
        this.webSocketStompClient = webSocketStompClient;
        this.sessionHandler = sessionHandler;
        this.user = user;
    }

    public void connectToServerWS() throws DeploymentException, IOException, URISyntaxException {
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
        WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();
        handshakeHeaders.add("Authorization", user.getJsonWebToken());
        webSocketStompClient.connect("ws://" + serverIp + ":8080/game", handshakeHeaders, sessionHandler);
    }
}
