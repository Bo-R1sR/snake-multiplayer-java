package de.snake.fxclient.logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConsoleLogger implements MyLogger {

    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    final LocalDateTime now = LocalDateTime.now();

    @Override
    public void log(String message) {
        System.out.println(dtf.format(now) + " " + message);
    }
}