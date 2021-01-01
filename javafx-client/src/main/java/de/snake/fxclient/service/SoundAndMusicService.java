package de.snake.fxclient.service;

import de.snake.fxclient.logger.MyLogger;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class SoundAndMusicService {

    private final MyLogger myLogger;
    // Musik
    private final String musicPath = "src/main/resources/sounds/483502__dominikbraun__let-me-see-ya-bounce-8-bit-music.mp3";
    final Media musicMedia = new Media(new File(musicPath).toURI().toString());
    // Essen
    private final String eatPath = "src/main/resources/sounds/342744__michael-kur95__increase-01.wav";
    final Media eatMedia = new Media(new File(eatPath).toURI().toString());
    // Runde vorbei
    private final String roundOverPath = "src/main/resources/sounds/365782__mattix__game-over-04.wav";
    final Media roundOverMedia = new Media(new File(roundOverPath).toURI().toString());
    // Spiel vorbei
    private final String gameOverPath = "src/main/resources/sounds/159408__noirenex__life-lost-game-over.wav";
    final Media gameOverMedia = new Media(new File(gameOverPath).toURI().toString());
    // Countdown
    private final String countdownPath = "src/main/resources/sounds/523762__matrixxx__select-granted-05.wav";
    final Media countdownMedia = new Media(new File(countdownPath).toURI().toString());
    // Game Start
    private final String gameStartPath = "src/main/resources/sounds/368691__fartbiscuit1700__8-bit-arcade-video-game-start-sound-effect-gun-reload-and-jump.wav";
    final Media gameStartMedia = new Media(new File(gameStartPath).toURI().toString());

    private final MediaPlayer musicPlayer = new MediaPlayer(musicMedia);

    public SoundAndMusicService(MyLogger myLogger) {
        this.myLogger = myLogger;
    }

    public void playServerSound(String serverSound) {
        switch (serverSound) {
            case "Food" -> {
                new MediaPlayer(eatMedia).play();
            }
            case "Countdown1", "Countdown2", "Countdown3" -> {
                new MediaPlayer(countdownMedia).play();
            }
            case "GameStart" -> {
                new MediaPlayer(gameStartMedia).play();
            }
            case "GameOver" -> {
                new MediaPlayer(gameOverMedia).play();
            }
            case "RoundOver" -> {
                new MediaPlayer(roundOverMedia).play();
            }
        }
    }

    public void playMusic() {
        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        musicPlayer.play();
        myLogger.log("Musik an");
    }

    public void pauseMusic() {
        musicPlayer.pause();
        myLogger.log("Musik aus");
    }

    public void changeVolume(double value) {
        musicPlayer.setVolume(value);
    }
}

