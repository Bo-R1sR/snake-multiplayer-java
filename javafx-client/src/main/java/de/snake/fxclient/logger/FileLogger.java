package de.snake.fxclient.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileLogger implements MyLogger {

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    BufferedWriter writer;

    @Override
    public void log(String message) {
        try {
            if (writer == null) {
                writer = new BufferedWriter(new FileWriter("log.txt", true));
            }
            writer.append(dtf.format(now)).append(" ").append(message).append("\n");
            writer.close();
        } catch (IOException ignored) {
        }
    }
}