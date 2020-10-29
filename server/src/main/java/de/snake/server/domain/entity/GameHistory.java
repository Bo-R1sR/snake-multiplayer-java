package de.snake.server.domain.entity;

import org.hibernate.annotations.Nationalized;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class GameHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CreatedDate
    private LocalDateTime createdAt;

    @Nationalized
    private String username1;

    @Nationalized
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