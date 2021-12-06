package day6.initialSolution;

import java.util.ArrayList;

public class Fish {
    private static final int NEW_TIMER = 8;
    private static final int OLD_TIMER = 6;
    // keeps track of total number of fish
    private static int totalCount = 0;

    private int timer;
    private ArrayList<Fish> children;

    public Fish() {
        this.timer = NEW_TIMER;
        totalCount += 1;
        this.children = new ArrayList<>();
    }

    public Fish(int timer) {
        this.timer = timer;
        totalCount += 1;
        this.children = new ArrayList<>();
    }

    public void decreaseTimer() {
        timer -= 1;
        children.forEach(Fish::decreaseTimer);
        if (timer < 0) {
            this.children.add(new Fish());
            timer = OLD_TIMER;
        }
    }

    public static int getTotalFishCount() {
        return totalCount;
    }
}
