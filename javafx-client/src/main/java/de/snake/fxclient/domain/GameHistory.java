package de.snake.fxclient.domain;

import java.time.LocalDateTime;

public class GameHistory {

    private long id;

    private LocalDateTime createdAt;

    private String username1;

    private String username2;

    private int pointsUser1;

    private int pointsUser2;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUsername1() {
        return username1;
    }

    public void setUsername1(String username1) {
        this.username1 = username1;
    }

    public String getUsername2() {
        return username2;
    }

    public void setUsername2(String username2) {
        this.username2 = username2;
    }

    public int getPointsUser1() {
        return pointsUser1;
    }

    public void setPointsUser1(int pointsUser1) {
        this.pointsUser1 = pointsUser1;
    }

    public int getPointsUser2() {
        return pointsUser2;
    }

    public void setPointsUser2(int pointsUser2) {
        this.pointsUser2 = pointsUser2;
    }
}