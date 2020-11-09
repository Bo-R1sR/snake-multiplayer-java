package de.snake.server.domain;

public class OutputMessage {

    private String from;
    private String text;
    private String time;
    private int color;

    public OutputMessage(String from, String text, String time, int color) {
        this.from = from;
        this.text = text;
        this.time = time;
        this.color = color;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
