package de.snake.fxclient.service;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class SoundAndMusicService {
    // Musik
    private final String musicPath = "src/main/resources/sounds/483502__dominikbraun__let-me-see-ya-bounce-8-bit-music.mp3";
    // Essen
    private final String eatPath = "src/main/resources/sounds/342744__michael-kur95__increase-01.wav";
    // Runde vorbei
    private final String roundOverPath = "src/main/resources/sounds/365782__mattix__game-over-04.wav";
    // Spiel vorbei
    private final String gameOverPath = "src/main/resources/sounds/159408__noirenex__life-lost-game-over.wav";
    // Countdown
    private final String countdownPath = "src/main/resources/sounds/523762__matrixxx__select-granted-05.wav";
    // Game Start
    private final String gameStartPath = "src/main/resources/sounds/368691__fartbiscuit1700__8-bit-arcade-video-game-start-sound-effect-gun-reload-and-jump.wav";

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
            case "GameOver" -> {
                gameOverPlayer.stop();
                gameOverPlayer.play();
            }
            case "RoundOver" -> {
                roundOverPlayer.stop();
                roundOverPlayer.play();
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
