package de.snake.fxclient.logger;

public class LoggerFactory {

    public static MyLogger createLogger(LogType type) {
        if (type.equals(LogType.CONSOLE)) {
            return new ConsoleLogger();
        } else if (type.equals(LogType.FILE)) {
            return new FileLogger();
        }
        return null;
    }

    ;

    public static MyLogger createLogger() {
        return createLogger(LogType.CONSOLE); // default logger
    }

    public enum LogType {CONSOLE, FILE}
}