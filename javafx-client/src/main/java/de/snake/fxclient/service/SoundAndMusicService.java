package de.snake.fxclient.service;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class SoundAndMusicService {
    // Musik
    private final String musicPath = "src/main/resources/sounds/test.mp3";
    // Essen
    private final String eatPath = "src/main/resources/sounds/test.mp3";
    // Runde vorbei
    private final String roundOverPath = "src/main/resources/sounds/test.mp3";
    // Spiel vorbei
    private final String gameOverPath = "src/main/resources/sounds/test.mp3";
    // Countdown
    private final String countdownPath = "src/main/resources/sounds/test.mp3";
    // Game Start
    private final String gameStartPath = "src/main/resources/sounds/test.mp3";
    Media musicMedia = new Media(new File(musicPath).toURI().toString());
    MediaPlayer musicPlayer = new MediaPlayer(musicMedia);
    Media eatMedia = new Media(new File(eatPath).toURI().toString());
    MediaPlayer eatPlayer = new MediaPlayer(eatMedia);
    Media roundOverMedia = new Media(new File(roundOverPath).toURI().toString());
    MediaPlayer roundOverPlayer = new MediaPlayer(roundOverMedia);
    Media gameOverMedia = new Media(new File(gameOverPath).toURI().toString());
    MediaPlayer gameOverPlayer = new MediaPlayer(gameOverMedia);
    Media countdownMedia = new Media(new File(countdownPath).toURI().toString());
    MediaPlayer countdownPlayer = new MediaPlayer(countdownMedia);
    Media gameStartMedia = new Media(new File(gameStartPath).toURI().toString());
    MediaPlayer gameStartPlayer = new MediaPlayer(gameStartMedia);

    public void playServerSound(String serverSound) {
        switch (serverSound) {
            case "Food" -> {
                eatPlayer.stop();
                eatPlayer.play();
            }
            case "Countdown" -> {
                countdownPlayer.stop();
                countdownPlayer.play();
            }
            case "GameStart" -> {
                gameStartPlayer.stop();
                gameStartPlayer.play();
            }
        }
    }

    public void playMusic() {
        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        musicPlayer.play();
        System.out.println("Musik an");
    }

    public void pauseMusic() {
        musicPlayer.pause();
        System.out.println("Musik aus");
    }

    public void changeVolume(double value) {
        musicPlayer.setVolume(value);
        System.out.println("Volume: " + value);
    }
}

