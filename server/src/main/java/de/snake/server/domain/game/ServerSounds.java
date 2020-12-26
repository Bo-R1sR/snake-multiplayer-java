package de.snake.server.domain.game;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class ServerSounds implements Serializable {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
