package day5;

import java.util.List;

import util.ReaderUtil;

public class HydrothermalVenture {
    public static void main(String[] args) {
        List<String> inputs = ReaderUtil.getInputsStr("2021/day5/input.txt");
        System.out.println(numberOfHorizontalAndVerticalOverlap(inputs));
        System.out.println(numberOfOverlap(inputs));
    }
    /**
     * --- Day 5: Hydrothermal Venture ---
     * You come across a field of hydrothermal vents on the ocean floor! These vents constantly produce large, opaque clouds, so it would be best to avoid them if possible.
     *
     * They tend to form in lines; the submarine helpfully produces a list of nearby lines of vents (your puzzle input) for you to review. For example:
     *
     * 0,9 -> 5,9
     * 8,0 -> 0,8
     * 9,4 -> 3,4
     * 2,2 -> 2,1
     * 7,0 -> 7,4
     * 6,4 -> 2,0
     * 0,9 -> 2,9
     * 3,4 -> 1,4
     * 0,0 -> 8,8
     * 5,5 -> 8,2
     * Each line of vents is given as a line segment in the format x1,y1 -> x2,y2 where x1,y1 are the coordinates of one end the line segment and x2,y2 are the coordinates of the other end. These line segments include the points at both ends. In other words:
     *
     * An entry like 1,1 -> 1,3 covers points 1,1, 1,2, and 1,3.
     * An entry like 9,7 -> 7,7 covers points 9,7, 8,7, and 7,7.
     * For now, only consider horizontal and vertical lines: lines where either x1 = x2 or y1 = y2.
     *
     * So, the horizontal and vertical lines from the above list would produce the following diagram:
     *
     * .......1..
     * ..1....1..
     * ..1....1..
     * .......1..
     * .112111211
     * ..........
     * ..........
     * ..........
     * ..........
     * 222111....
     * In this diagram, the top left corner is 0,0 and the bottom right corner is 9,9. Each position is shown as the number of lines which cover that point or . if no line covers that point. The top-left pair of 1s, for example, comes from 2,2 -> 2,1; the very bottom row is formed by the overlapping lines 0,9 -> 5,9 and 0,9 -> 2,9.
     *
     * To avoid the most dangerous areas, you need to determine the number of points where at least two lines overlap. In the above example, this is anywhere in the diagram with a 2 or larger - a total of 5 points.
     *
     * Consider only horizontal and vertical lines. At how many points do at least two lines overlap?
     */
    public static int numberOfHorizontalAndVerticalOverlap(List<String> inputs) {
        // setup
        int largestX = 0;
        int largestY = 0;
        int[][] parsedInputs = new int[inputs.size()][4];
        for (int i = 0; i < inputs.size(); i++) {
            String[] coordinates = inputs.get(i).split("->");
            String[] startCoordinate = coordinates[0].trim().split(",");
            String[] endCoordinate = coordinates[1].trim().split(",");
            parsedInputs[i][0] = Integer.parseInt(startCoordinate[0]);
            parsedInputs[i][1] = Integer.parseInt(startCoordinate[1]);
            parsedInputs[i][2] = Integer.parseInt(endCoordinate[0]);
            parsedInputs[i][3] = Integer.parseInt(endCoordinate[1]);

            largestX = Math.max(largestX, Math.max(parsedInputs[i][0], parsedInputs[i][2]));
            largestY = Math.max(largestY, Math.max(parsedInputs[i][1], parsedInputs[i][3]));
        }
        int[][] graph = new int[largestY + 1][largestX + 1];
        int count = 0;

        for (int i = 0; i < inputs.size(); i++) {
            if (parsedInputs[i][0] == parsedInputs[i][2]) { // same x-axis (vertical line)
                count += verticalLine(graph, parsedInputs[i]);
            } else if (parsedInputs[i][1] == parsedInputs[i][3]) { // same y-axis (horizontal line)
                count += horizontalLine(graph, parsedInputs[i]);
            }
        }
        return count;
    }


    /**
     * --- Part Two ---
     * Unfortunately, considering only horizontal and vertical lines doesn't give you the full picture; you need to also consider diagonal lines.
     *
     * Because of the limits of the hydrothermal vent mapping system, the lines in your list will only ever be horizontal, vertical, or a diagonal line at exactly 45 degrees. In other words:
     *
     * An entry like 1,1 -> 3,3 covers points 1,1, 2,2, and 3,3.
     * An entry like 9,7 -> 7,9 covers points 9,7, 8,8, and 7,9.
     * Considering all lines from the above example would now produce the following diagram:
     *
     * 1.1....11.
     * .111...2..
     * ..2.1.111.
     * ...1.2.2..
     * .112313211
     * ...1.2....
     * ..1...1...
     * .1.....1..
     * 1.......1.
     * 222111....
     * You still need to determine the number of points where at least two lines overlap. In the above example, this is still anywhere in the diagram with a 2 or larger - now a total of 12 points.
     *
     * Consider all of the lines. At how many points do at least two lines overlap?
     */
    public static int numberOfOverlap(List<String> inputs) {
        // setup
        int largestX = 0;
        int largestY = 0;
        int[][] parsedInputs = new int[inputs.size()][4];
        for (int i = 0; i < inputs.size(); i++) {
            String[] coordinates = inputs.get(i).split("->");
            String[] startCoordinate = coordinates[0].trim().split(",");
            String[] endCoordinate = coordinates[1].trim().split(",");
            parsedInputs[i][0] = Integer.parseInt(startCoordinate[0]);
            parsedInputs[i][1] = Integer.parseInt(startCoordinate[1]);
            parsedInputs[i][2] = Integer.parseInt(endCoordinate[0]);
            parsedInputs[i][3] = Integer.parseInt(endCoordinate[1]);

            largestX = Math.max(largestX, Math.max(parsedInputs[i][0], parsedInputs[i][2]));
            largestY = Math.max(largestY, Math.max(parsedInputs[i][1], parsedInputs[i][3]));
        }
        int[][] graph = new int[largestY + 1][largestX + 1];
        int count = 0;

        for (int i = 0; i < inputs.size(); i++) {
            if (parsedInputs[i][0] == parsedInputs[i][2]) { // same x-axis (vertical line)
                count += verticalLine(graph, parsedInputs[i]);
            } else if (parsedInputs[i][1] == parsedInputs[i][3]) { // same y-axis (horizontal line)
                count += horizontalLine(graph, parsedInputs[i]);
            } else {
                count += diagonalLine(graph, parsedInputs[i]);
            }
        }
        return count;
    }



    // helper functions
    public static int horizontalLine(int[][] graph, int[] coordinates) {
        int count = 0;
        int smallerX = Math.min(coordinates[0], coordinates[2]);
        int largerX = Math.max(coordinates[0], coordinates[2]);
        for (int i = smallerX; i <= largerX; i++) {
            graph[coordinates[1]][i] += 1;
            if (graph[coordinates[1]][i] == 2) {
                count += 1;
            }
        }
        return count;
    }

    public static int verticalLine(int[][] graph, int[] coordinates) {
        int count = 0;
        int smallerY = Math.min(coordinates[1], coordinates[3]);
        int largerY = Math.max(coordinates[1], coordinates[3]);
        for (int i = smallerY; i <= largerY; i++) {
            graph[i][coordinates[0]] += 1;
            if (graph[i][coordinates[0]] == 2) {
                count += 1;
            }
        }
        return count;
    }

    public static int diagonalLine(int[][] graph, int[] coordinates) {
        int count = 0;
        int incrementX = coordinates[0] < coordinates[2] ? 1 : -1;
        int incrementY = coordinates[1] < coordinates[3] ? 1 : -1;
        int currentX = coordinates[0];
        int currentY = coordinates[1];
        while (currentX != coordinates[2] + incrementX) {
         graph[currentY][currentX] += 1;
         if (graph[currentY][currentX] == 2) {
             count += 1;
         }
         currentX += incrementX;
         currentY += incrementY;
        }
        return count;
    }
}
