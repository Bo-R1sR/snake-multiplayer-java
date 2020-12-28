package de.snake.fxclient.game;

import org.springframework.stereotype.Component;

@Component
public class SoundSetting {
    private boolean isSoundMuted = true;

    public boolean isSoundMuted() {
        return isSoundMuted;
    }

    public void setSoundMuted(boolean soundMuted) {
        isSoundMuted = soundMuted;
    }
}
