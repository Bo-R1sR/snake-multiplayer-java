package de.snake.fxclient.game;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class ScreenText implements Serializable {

    private String playerText;

    public String getPlayerText() {
        return playerText;
    }

    public void setPlayerText(String playerText) {
        this.playerText = playerText;
    }
}
