package day12;

import java.util.ArrayList;
import java.util.List;

public class Cave {

    public static final String START = "start";
    private static final String END = "end";

    private String name;
    private boolean isLarge;
    private List<Cave> adjacentCaves;
    private boolean isCaveLarge;

    public Cave(String name) {
        this.name = name;
        isCaveLarge = name.toUpperCase().equals(name);
        adjacentCaves = new ArrayList<>();
    }

    public boolean isLarge() {
        return isCaveLarge;
    }

    public boolean isStart() {
        return this.name.equals(START);
    }

    public boolean isEnd() {
        return this.name.equals(END);
    }

    public List<Cave> getAdjacentCaves() {
        return adjacentCaves;
    }

    public void addAdjacentCaves(Cave cave) {
        adjacentCaves.add(cave);
    }
}
