package day9;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import util.ReaderUtil;

public class SmokeBasin {
    public static void main(String[] args) {
        List<String> inputs = ReaderUtil.getInputsStr("2021/day9/input.txt");
        System.out.println(sumOfRiskLevels(inputs));
        System.out.println(multiplicationOfBasinRiskLevels(inputs));
    }

    /**
     * --- Day 9: Smoke Basin ---
     * These caves seem to be lava tubes. Parts are even still volcanically active; small hydrothermal vents release smoke into the caves that slowly settles like rain.
     * <p>
     * If you can model how the smoke flows through the caves, you might be able to avoid it and be that much safer. The submarine generates a heightmap of the floor of the nearby caves for you (your puzzle input).
     * <p>
     * Smoke flows to the lowest point of the area it's in. For example, consider the following heightmap:
     * <p>
     * 2199943210
     * 3987894921
     * 9856789892
     * 8767896789
     * 9899965678
     * Each number corresponds to the height of a particular location, where 9 is the highest and 0 is the lowest a location can be.
     * <p>
     * Your first goal is to find the low points - the locations that are lower than any of its adjacent locations. Most locations have four adjacent locations (up, down, left, and right); locations on the edge or corner of the map have three or two adjacent locations, respectively. (Diagonal locations do not count as adjacent.)
     * <p>
     * In the above example, there are four low points, all highlighted: two are in the first row (a 1 and a 0), one is in the third row (a 5), and one is in the bottom row (also a 5). All other locations on the heightmap have some lower adjacent location, and so are not low points.
     * <p>
     * The risk level of a low point is 1 plus its height. In the above example, the risk levels of the low points are 2, 1, 6, and 6. The sum of the risk levels of all low points in the heightmap is therefore 15.
     * <p>
     * Find all of the low points on your heightmap. What is the sum of the risk levels of all low points on your heightmap?
     */
    public static int sumOfRiskLevels(List<String> inputs) {
        // process data
        int[][] heightMap = new int[inputs.size()][inputs.get(0).length()];
        for (int i = 0; i < inputs.size(); i++) {
            heightMap[i] = Arrays.stream(inputs.get(i).trim().split("|")).mapToInt(Integer::parseInt).toArray();
        }

        int sum = 0;
        for (int i = 0; i < heightMap.length; i++) {
            for (int j = 0; j < heightMap[0].length; j++) {
                boolean isTopLarger = i - 1 < 0 || heightMap[i][j] < heightMap[i - 1][j];
                boolean isBottomLarger = i + 1 >= heightMap.length || heightMap[i][j] < heightMap[i + 1][j];
                boolean isLeftLarger = j - 1 < 0 || heightMap[i][j] < heightMap[i][j - 1];
                boolean isRightLarger = j + 1 >= heightMap[0].length || heightMap[i][j] < heightMap[i][j + 1];
                if (isTopLarger && isBottomLarger && isLeftLarger && isRightLarger) {
                    sum += heightMap[i][j] + 1;
                }
            }
        }
        return sum;
    }

    /**
     * --- Part Two ---
     * Next, you need to find the largest basins so you know what areas are most important to avoid.
     *
     * A basin is all locations that eventually flow downward to a single low point. Therefore, every low point has a basin, although some basins are very small. Locations of height 9 do not count as being in any basin, and all other locations will always be part of exactly one basin.
     *
     * The size of a basin is the number of locations within the basin, including the low point. The example above has four basins.
     *
     * The top-left basin, size 3:
     *
     * 2199943210
     * 3987894921
     * 9856789892
     * 8767896789
     * 9899965678
     * The top-right basin, size 9:
     *
     * 2199943210
     * 3987894921
     * 9856789892
     * 8767896789
     * 9899965678
     * The middle basin, size 14:
     *
     * 2199943210
     * 3987894921
     * 9856789892
     * 8767896789
     * 9899965678
     * The bottom-right basin, size 9:
     *
     * 2199943210
     * 3987894921
     * 9856789892
     * 8767896789
     * 9899965678
     * Find the three largest basins and multiply their sizes together. In the above example, this is 9 * 14 * 9 = 1134.
     *
     * What do you get if you multiply together the sizes of the three largest basins?
     */
    public static int multiplicationOfBasinRiskLevels(List<String> inputs) {
        // process data
        int[][] heightMap = new int[inputs.size()][inputs.get(0).length()];
        for (int i = 0; i < inputs.size(); i++) {
            heightMap[i] = Arrays.stream(inputs.get(i).trim().split("|")).mapToInt(Integer::parseInt).toArray();
        }

        int[][] basinMap = new int[inputs.size()][inputs.get(0).length()];
        int basinCount = 1; // starts from 1
        HashMap<Integer, Integer> basinSizeMap = new HashMap<>();
        for (int i = 0; i < heightMap.length; i++) {
            for (int j = 0; j < heightMap[0].length; j++) {
                boolean isTopLarger = i - 1 < 0 || heightMap[i][j] < heightMap[i - 1][j];
                boolean isBottomLarger = i + 1 >= heightMap.length || heightMap[i][j] < heightMap[i + 1][j];
                boolean isLeftLarger = j - 1 < 0 || heightMap[i][j] < heightMap[i][j - 1];
                boolean isRightLarger = j + 1 >= heightMap[0].length || heightMap[i][j] < heightMap[i][j + 1];
                if (isTopLarger && isBottomLarger && isLeftLarger && isRightLarger) {
                    basinMap[i][j] = basinCount;
                    basinSizeMap.put(basinCount, 1);
                    setBasinMap(i, j, basinCount, heightMap, basinMap, basinSizeMap);
                    basinCount++;
                }
            }
        }

        int riskLevel = 1;
        int largestN = 3;
        Integer[] largestNArr = getNLargestMapValues(largestN, basinSizeMap);
        for (int i = 0; i < largestN; i++) {
            riskLevel *= basinSizeMap.get(largestNArr[i]);
        }
        return riskLevel;
    }


    public static void setBasinMap(int y, int x, int basin, int[][] heightMap, int[][] basinMap, HashMap<Integer, Integer> basinSizeMap) {
        basinMap[y][x] = basin;
        if (y - 1 >= 0 && heightMap[y][x] < heightMap[y - 1][x] && heightMap[y - 1][x] != 9 && basinMap[y - 1][x] == 0) {
            basinSizeMap.computeIfPresent(basin, (key, value) -> value += 1);
            setBasinMap(y - 1, x, basin, heightMap, basinMap, basinSizeMap);
        }
        if (y + 1 < heightMap.length && heightMap[y][x] < heightMap[y + 1][x] && heightMap[y + 1][x] != 9 && basinMap[y + 1][x] == 0) {
            basinSizeMap.computeIfPresent(basin, (key, value) -> value += 1);
            setBasinMap(y + 1, x, basin, heightMap, basinMap, basinSizeMap);
        }
        if (x - 1 >= 0 && heightMap[y][x] < heightMap[y][x - 1] && heightMap[y][x - 1] != 9 && basinMap[y][x - 1] == 0) {
            basinSizeMap.computeIfPresent(basin, (key, value) -> value += 1);
            setBasinMap(y, x - 1, basin, heightMap, basinMap, basinSizeMap);
        }
        if (x + 1 < heightMap[0].length && heightMap[y][x] < heightMap[y][x + 1] && heightMap[y][x + 1] != 9 && basinMap[y][x + 1] == 0) {
            basinSizeMap.computeIfPresent(basin, (key, value) -> value += 1);
            setBasinMap(y, x + 1, basin, heightMap, basinMap, basinSizeMap);
        }
    }

    public static Integer[] getNLargestMapValues(int n, Map<Integer, Integer> map) {
        PriorityQueue<Integer> topN = new PriorityQueue<>(n, Comparator.comparingInt(map::get));
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (topN.size() < n)
                topN.add(entry.getKey());
            else if (map.get(topN.peek()) < entry.getValue()) {
                topN.poll();
                topN.add(entry.getKey());
            }
        }
        return topN.toArray(Integer[]::new);
    }
}
