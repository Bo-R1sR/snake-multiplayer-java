package de.snake.fxclient.game;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Level {

    private List<List<SnakeBodyPart>> allLevels = new ArrayList<>();

    public Level() {
        List<SnakeBodyPart> level0 = new ArrayList<>();

        List<SnakeBodyPart> level1 = new ArrayList<>();
        for (int ii = 2; ii <= 9; ii++) {
            level1.add(new SnakeBodyPart(2, ii, 0));
            level1.add(new SnakeBodyPart(ii, 2, 0));
        }
        for (int ii = 15; ii <= 22; ii++) {
            level1.add(new SnakeBodyPart(ii, 22, 0));
            level1.add(new SnakeBodyPart(22, ii, 0));
        }
        for (int ii = 18; ii <= 22; ii++) {
            level1.add(new SnakeBodyPart(6, ii, 0));
            level1.add(new SnakeBodyPart(ii, 6, 0));
        }
        for (int ii = 2; ii <= 6; ii++) {
            level1.add(new SnakeBodyPart(ii, 18, 0));
            level1.add(new SnakeBodyPart(18, ii, 0));
        }

        List<SnakeBodyPart> level2 = new ArrayList<>(level1);
        for (int ii = 4; ii <= 8; ii++) {
            level2.add(new SnakeBodyPart(4, ii, 0));
            level2.add(new SnakeBodyPart(ii, 4, 0));
        }
        for (int ii = 16; ii <= 20; ii++) {
            level2.add(new SnakeBodyPart(20, ii, 0));
            level2.add(new SnakeBodyPart(ii, 20, 0));
        }
        for (int ii = 7; ii <= 17; ii++) {
            level2.add(new SnakeBodyPart(ii, 10, 0));
            level2.add(new SnakeBodyPart(ii, 14, 0));
        }
        for (int ii = 2; ii <= 3; ii++) {
            level2.add(new SnakeBodyPart(ii, 21, 0));
            level2.add(new SnakeBodyPart(ii, 22, 0));
        }
        for (int ii = 21; ii <= 22; ii++) {
            level2.add(new SnakeBodyPart(ii, 2, 0));
            level2.add(new SnakeBodyPart(ii, 3, 0));
        }

        List<SnakeBodyPart> level3 = new ArrayList<>(level2);
        for (int ii = 0; ii <= 7; ii++) {
            level3.add(new SnakeBodyPart(12, ii, 0));
        }
        for (int ii = 17; ii <= 24; ii++) {
            level3.add(new SnakeBodyPart(12, ii, 0));
        }
        for (int ii = 12; ii <= 13; ii++) {
            level3.add(new SnakeBodyPart(7, ii, 0));
        }
        for (int ii = 11; ii <= 12; ii++) {
            level3.add(new SnakeBodyPart(17, ii, 0));
        }
        for (int ii = 12; ii <= 15; ii++) {
            level3.add(new SnakeBodyPart(2, ii, 0));
        }
        for (int ii = 9; ii <= 12; ii++) {
            level3.add(new SnakeBodyPart(22, ii, 0));
        }
        for (int ii = 2; ii <= 4; ii++) {
            level3.add(new SnakeBodyPart(ii, 12, 0));
            level3.add(new SnakeBodyPart(ii, 15, 0));
        }
        for (int ii = 20; ii <= 22; ii++) {
            level3.add(new SnakeBodyPart(ii, 12, 0));
            level3.add(new SnakeBodyPart(ii, 9, 0));
        }

        List<SnakeBodyPart> level4 = new ArrayList<>(level3);
        for (int ii = 2; ii <= 18; ii++) {
            level4.add(new SnakeBodyPart(2, ii, 0));
        }
        for (int ii = 6; ii <= 22; ii++) {
            level4.add(new SnakeBodyPart(22, ii, 0));
        }
        for (int ii = 7; ii <= 11; ii++) {
            level4.add(new SnakeBodyPart(ii, 7, 0));
        }
        for (int ii = 13; ii <= 17; ii++) {
            level4.add(new SnakeBodyPart(ii, 17, 0));
        }
        for (int ii = 11; ii <= 12; ii++) {
            level4.add(new SnakeBodyPart(10, ii, 0));
        }
        for (int ii = 12; ii <= 13; ii++) {
            level4.add(new SnakeBodyPart(14, ii, 0));
        }
        for (int ii = 5; ii <= 7; ii++) {
            level4.add(new SnakeBodyPart(14, ii, 0));
        }
        for (int ii = 18; ii <= 20; ii++) {
            level4.add(new SnakeBodyPart(10, ii, 0));
        }
        for (int ii = 8; ii <= 10; ii++) {
            level4.add(new SnakeBodyPart(ii, 18, 0));
        }
        for (int ii = 14; ii <= 16; ii++) {
            level4.add(new SnakeBodyPart(ii, 7, 0));
        }
        for (int ii = 8; ii <= 11; ii++) {
            level4.add(new SnakeBodyPart(ii, 22, 0));
        }
        for (int ii = 13; ii <= 16; ii++) {
            level4.add(new SnakeBodyPart(ii, 2, 0));
        }

        allLevels.add(level0);
        allLevels.add(level1);
        allLevels.add(level2);
        allLevels.add(level3);
        allLevels.add(level4);
    }

    public List<List<SnakeBodyPart>> getAllLevels() {
        return allLevels;
    }

    public void setAllLevels(List<List<SnakeBodyPart>> allLevels) {
        this.allLevels = allLevels;
    }
}

