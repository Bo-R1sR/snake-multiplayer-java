package de.snake.fxclient.domain;

public class GameHistoryTable {

    private String createdAt;

    private String enemyName;

    private int yourPoints;

    private int enemyPoints;

    private String decision;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getEnemyName() {
        return enemyName;
    }

    public void setEnemyName(String enemyName) {
        this.enemyName = enemyName;
    }

    public int getYourPoints() {
        return yourPoints;
    }

    public void setYourPoints(int yourPoints) {
        this.yourPoints = yourPoints;
    }

    public int getEnemyPoints() {
        return enemyPoints;
    }

    public void setEnemyPoints(int enemyPoints) {
        this.enemyPoints = enemyPoints;
    }


    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}
