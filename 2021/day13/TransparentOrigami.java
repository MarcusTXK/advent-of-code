package day13;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.ReaderUtil;

public class TransparentOrigami {
    public static void main(String[] args) {
        // setup data
        List<String> inputs = ReaderUtil.getInputsStr("2021/day13/input.txt");
        int maxLength = -1;
        int maxHeight = -1;
        List<String[]> foldInstructions = new ArrayList<>();
        List<int[]> coordinates = new ArrayList<>();
        for (String input : inputs) {
            if (input.contains("fold along")) {
                foldInstructions.add(input.split("="));
            } else if (input.contains(",")) {
                int[] coordinate = Arrays.stream(input.split(",")).mapToInt(Integer::parseInt).toArray();
                maxLength = Math.max(maxLength, coordinate[0]);
                maxHeight = Math.max(maxHeight, coordinate[1]);
                coordinates.add(coordinate);
            }
        }
        boolean[][] paper = new boolean[maxHeight + 1][maxLength + 1];
        for (int[] coordinate : coordinates) {
            paper[coordinate[1]][coordinate[0]] = true;
        }
        List<String[]> onlyFirstInstruction = new ArrayList<>();
        onlyFirstInstruction.add(foldInstructions.get(0));

        System.out.println(countFoldedPaper(paper, onlyFirstInstruction));
        System.out.println(countFoldedPaper(paper, foldInstructions));
    }

    /**
     * --- Day 13: Transparent Origami ---
     * You reach another volcanically active part of the cave. It would be nice if you could do some kind of thermal imaging so you could tell ahead of time which caves are too hot to safely enter.
     *
     * Fortunately, the submarine seems to be equipped with a thermal camera! When you activate it, you are greeted with:
     *
     * Congratulations on your purchase! To activate this infrared thermal imaging
     * camera system, please enter the code found on page 1 of the manual.
     * Apparently, the Elves have never used this feature. To your surprise, you manage to find the manual; as you go to open it, page 1 falls out. It's a large sheet of transparent paper! The transparent paper is marked with random dots and includes instructions on how to fold it up (your puzzle input). For example:
     *
     * 6,10
     * 0,14
     * 9,10
     * 0,3
     * 10,4
     * 4,11
     * 6,0
     * 6,12
     * 4,1
     * 0,13
     * 10,12
     * 3,4
     * 3,0
     * 8,4
     * 1,10
     * 2,14
     * 8,10
     * 9,0
     *
     * fold along y=7
     * fold along x=5
     * The first section is a list of dots on the transparent paper. 0,0 represents the top-left coordinate. The first value, x, increases to the right. The second value, y, increases downward. So, the coordinate 3,0 is to the right of 0,0, and the coordinate 0,7 is below 0,0. The coordinates in this example form the following pattern, where # is a dot on the paper and . is an empty, unmarked position:
     *
     * ...#..#..#.
     * ....#......
     * ...........
     * #..........
     * ...#....#.#
     * ...........
     * ...........
     * ...........
     * ...........
     * ...........
     * .#....#.##.
     * ....#......
     * ......#...#
     * #..........
     * #.#........
     * Then, there is a list of fold instructions. Each instruction indicates a line on the transparent paper and wants you to fold the paper up (for horizontal y=... lines) or left (for vertical x=... lines). In this example, the first fold instruction is fold along y=7, which designates the line formed by all of the positions where y is 7 (marked here with -):
     *
     * ...#..#..#.
     * ....#......
     * ...........
     * #..........
     * ...#....#.#
     * ...........
     * ...........
     * -----------
     * ...........
     * ...........
     * .#....#.##.
     * ....#......
     * ......#...#
     * #..........
     * #.#........
     * Because this is a horizontal line, fold the bottom half up. Some of the dots might end up overlapping after the fold is complete, but dots will never appear exactly on a fold line. The result of doing this fold looks like this:
     *
     * #.##..#..#.
     * #...#......
     * ......#...#
     * #...#......
     * .#.#..#.###
     * ...........
     * ...........
     * Now, only 17 dots are visible.
     *
     * Notice, for example, the two dots in the bottom left corner before the transparent paper is folded; after the fold is complete, those dots appear in the top left corner (at 0,0 and 0,1). Because the paper is transparent, the dot just below them in the result (at 0,3) remains visible, as it can be seen through the transparent paper.
     *
     * Also notice that some dots can end up overlapping; in this case, the dots merge together and become a single dot.
     *
     * The second fold instruction is fold along x=5, which indicates this line:
     *
     * #.##.|#..#.
     * #...#|.....
     * .....|#...#
     * #...#|.....
     * .#.#.|#.###
     * .....|.....
     * .....|.....
     * Because this is a vertical line, fold left:
     *
     * #####
     * #...#
     * #...#
     * #...#
     * #####
     * .....
     * .....
     * The instructions made a square!
     *
     * The transparent paper is pretty big, so for now, focus on just completing the first fold. After the first fold in the example above, 17 dots are visible - dots that end up overlapping after the fold is completed count as a single dot.
     *
     * How many dots are visible after completing just the first fold instruction on your transparent paper?
     *
     * --- Part Two ---
     * Finish folding the transparent paper according to the instructions. The manual says the code is always eight capital letters.
     *
     * What code do you use to activate the infrared thermal imaging camera system?
     */
    public static int countFoldedPaper(boolean[][] original, List<String[]> instructions) {
        boolean[][] paper = original;
        for (String[] instruction: instructions) {
            if (instruction[0].contains("x")) {
                paper = foldVertical(paper, Integer.parseInt(instruction[1]));
            } else {
                paper = foldHorizontal(paper, Integer.parseInt(instruction[1]));
            }
        }
        return countMarksAndDisplay(paper);
    }

    public static boolean[][] foldHorizontal(boolean[][] original, int foldLine) {
        int newHeight = Math.max(foldLine - 1, original.length - foldLine - 1);
        boolean[][] folded = new boolean[newHeight][original[0].length];
        for (int i = 0; i < folded.length; i++) {
            for (int j = 0; j < folded[0].length; j++) {
                int currentLine = foldLine + i + 1;
                if (currentLine < original.length) {
                    folded[i][j] = original[foldLine + i + 1][j];
                }
                if (currentLine - (currentLine - foldLine) * 2 >= 0) {
                    folded[i][j] = folded[i][j] || original[currentLine - (currentLine - foldLine) * 2][j];
                }
            }
        }
        return folded;
    }

    public static boolean[][] foldVertical(boolean[][] original, int foldLine) {
        int newWidth = Math.max(foldLine - 1, original[0].length - foldLine - 1);
        boolean[][] folded = new boolean[original.length][newWidth];
        for (int i = 0; i < folded.length; i++) {
            for (int j = 0; j < folded[0].length; j++) {
                int currentLine = foldLine + j + 1;
                if (currentLine < original[0].length) {
                    folded[i][j] = original[i][foldLine + j + 1];
                }
                if (currentLine - (currentLine - foldLine) * 2 >= 0) {
                    folded[i][j] = folded[i][j] || original[i][currentLine - (currentLine - foldLine) * 2];
                }
            }
        }
        return folded;
    }

    public static int countMarksAndDisplay(boolean[][] paper) {
        int count = 0;
        for (int i = 0; i < paper.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < paper[0].length; j++) {
                if (paper[i][j]) {
                    count += 1;
                }
                sb.append(paper[i][j] ? "#" : " ");
            }
            System.out.println(sb.toString());
        }
        return count;
    }


}
