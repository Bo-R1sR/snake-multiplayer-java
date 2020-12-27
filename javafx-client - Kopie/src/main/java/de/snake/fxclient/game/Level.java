package de.snake.fxclient.game;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Level {

    private List<List<SnakeBodyPart>> allLevels = new ArrayList<>();

    public Level() {
        List<SnakeBodyPart> level1 = new ArrayList<>();
        level1.add(new SnakeBodyPart(14, 5, 0));
        level1.add(new SnakeBodyPart(14, 6, 0));
        level1.add(new SnakeBodyPart(13, 5, 0));

        List<SnakeBodyPart> level2 = new ArrayList<>();
        level2.add(new SnakeBodyPart(20, 5, 0));
        level2.add(new SnakeBodyPart(20, 6, 0));
        level2.add(new SnakeBodyPart(21, 5, 0));

        List<SnakeBodyPart> level3 = new ArrayList<>();
        level3.add(new SnakeBodyPart(14, 5, 0));
        level3.add(new SnakeBodyPart(14, 6, 0));
        level3.add(new SnakeBodyPart(13, 5, 0));

        List<SnakeBodyPart> level4 = new ArrayList<>();
        level4.add(new SnakeBodyPart(20, 5, 0));
        level4.add(new SnakeBodyPart(20, 6, 0));
        level4.add(new SnakeBodyPart(21, 5, 0));

        List<SnakeBodyPart> level5 = new ArrayList<>();
        level5.add(new SnakeBodyPart(14, 5, 0));
        level5.add(new SnakeBodyPart(14, 6, 0));
        level5.add(new SnakeBodyPart(13, 5, 0));

        allLevels.add(level1);
        allLevels.add(level2);
        allLevels.add(level3);
        allLevels.add(level4);
        allLevels.add(level5);

    }

    public List<List<SnakeBodyPart>> getAllLevels() {
        return allLevels;
    }

    public void setAllLevels(List<List<SnakeBodyPart>> allLevels) {
        this.allLevels = allLevels;
    }
}

