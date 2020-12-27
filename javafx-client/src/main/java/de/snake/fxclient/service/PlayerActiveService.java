package de.snake.fxclient.service;

import de.snake.fxclient.domain.User;
import org.springframework.stereotype.Service;

@Service
public class PlayerActiveService {

    private final User user;

    public PlayerActiveService(User user) {
        this.user = user;
    }

    // called from SessionHandler when ID for client is fixed
    public void sendReadyToServer() {
        // set readiness in client to make sure correct ready message is displayed for player 1 or 2
        user.setReadyToPlay(true);
        // send readiness to sever
        user.getSession().send("/app/playerActive/" + user.getPlayerId(), "connect");
    }

}
