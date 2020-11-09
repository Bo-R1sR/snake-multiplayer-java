package de.snake.fxclient.game;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Level {

    private List<SnakeBodyPart> level1 = new ArrayList<>();
    private List<SnakeBodyPart> level2 = new ArrayList<>();
    private List<SnakeBodyPart> level3 = new ArrayList<>();
    private List<SnakeBodyPart> level4 = new ArrayList<>();
    private List<SnakeBodyPart> level5 = new ArrayList<>();

    private List<List<SnakeBodyPart>> allLevels = new ArrayList<>();

    public Level() {
        level1.add(new SnakeBodyPart(14, 5, 0));
        level1.add(new SnakeBodyPart(14, 6, 0));
        level1.add(new SnakeBodyPart(13, 5, 0));

        level2.add(new SnakeBodyPart(20, 5, 0));
        level2.add(new SnakeBodyPart(20, 6, 0));
        level2.add(new SnakeBodyPart(21, 5, 0));

        level3.add(new SnakeBodyPart(14, 5, 0));
        level3.add(new SnakeBodyPart(14, 6, 0));
        level3.add(new SnakeBodyPart(13, 5, 0));

        level4.add(new SnakeBodyPart(20, 5, 0));
        level4.add(new SnakeBodyPart(20, 6, 0));
        level4.add(new SnakeBodyPart(21, 5, 0));

        level5.add(new SnakeBodyPart(14, 5, 0));
        level5.add(new SnakeBodyPart(14, 6, 0));
        level5.add(new SnakeBodyPart(13, 5, 0));

        allLevels.add(level1);
        allLevels.add(level2);
        allLevels.add(level3);
        allLevels.add(level4);
        allLevels.add(level5);

    }

    public List<SnakeBodyPart> getLevel1() {
        return level1;
    }

    public void setLevel1(List<SnakeBodyPart> level1) {
        this.level1 = level1;
    }

    public List<SnakeBodyPart> getLevel2() {
        return level2;
    }

    public void setLevel2(List<SnakeBodyPart> level2) {
        this.level2 = level2;
    }

    public List<SnakeBodyPart> getLevel3() {
        return level3;
    }

    public void setLevel3(List<SnakeBodyPart> level3) {
        this.level3 = level3;
    }

    public List<List<SnakeBodyPart>> getAllLevels() {
        return allLevels;
    }

    public void setAllLevels(List<List<SnakeBodyPart>> allLevels) {
        this.allLevels = allLevels;
    }

    public List<SnakeBodyPart> getLevel4() {
        return level4;
    }

    public void setLevel4(List<SnakeBodyPart> level4) {
        this.level4 = level4;
    }
}

