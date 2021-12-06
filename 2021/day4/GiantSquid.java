package day4;

import java.util.ArrayList;
import java.util.List;

import util.ReaderUtil;

public class GiantSquid {
    public static void main(String[] args) {
        List<String> inputs = ReaderUtil.getInputsStr("2021/day4/input.txt");
        System.out.println(bingoFirstScore(inputs));
        System.out.println(bingoLastScore(inputs));
    }

    /**
     * --- Day 4: Giant Squid ---
     * You're already almost 1.5km (almost a mile) below the surface of the ocean, already so deep that you can't see any sunlight. What you can see, however, is a giant squid that has attached itself to the outside of your submarine.
     * <p>
     * Maybe it wants to play bingo?
     * <p>
     * Bingo is played on a set of boards each consisting of a 5x5 grid of numbers. Numbers are chosen at random, and the chosen number is marked on all boards on which it appears. (Numbers may not appear on all boards.) If all numbers in any row or any column of a board are marked, that board wins. (Diagonals don't count.)
     * <p>
     * The submarine has a bingo subsystem to help passengers (currently, you and the giant squid) pass the time. It automatically generates a random order in which to draw numbers and a random set of boards (your puzzle input). For example:
     * <p>
     * 7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1
     * <p>
     * 22 13 17 11  0
     * 8  2 23  4 24
     * 21  9 14 16  7
     * 6 10  3 18  5
     * 1 12 20 15 19
     * <p>
     * 3 15  0  2 22
     * 9 18 13 17  5
     * 19  8  7 25 23
     * 20 11 10 24  4
     * 14 21 16 12  6
     * <p>
     * 14 21 17 24  4
     * 10 16 15  9 19
     * 18  8 23 26 20
     * 22 11 13  6  5
     * 2  0 12  3  7
     * After the first five numbers are drawn (7, 4, 9, 5, and 11), there are no winners, but the boards are marked as follows (shown here adjacent to each other to save space):
     * <p>
     * 22 13 17 11  0         3 15  0  2 22        14 21 17 24  4
     * 8  2 23  4 24         9 18 13 17  5        10 16 15  9 19
     * 21  9 14 16  7        19  8  7 25 23        18  8 23 26 20
     * 6 10  3 18  5        20 11 10 24  4        22 11 13  6  5
     * 1 12 20 15 19        14 21 16 12  6         2  0 12  3  7
     * After the next six numbers are drawn (17, 23, 2, 0, 14, and 21), there are still no winners:
     * <p>
     * 22 13 17 11  0         3 15  0  2 22        14 21 17 24  4
     * 8  2 23  4 24         9 18 13 17  5        10 16 15  9 19
     * 21  9 14 16  7        19  8  7 25 23        18  8 23 26 20
     * 6 10  3 18  5        20 11 10 24  4        22 11 13  6  5
     * 1 12 20 15 19        14 21 16 12  6         2  0 12  3  7
     * Finally, 24 is drawn:
     * <p>
     * 22 13 17 11  0         3 15  0  2 22        14 21 17 24  4
     * 8  2 23  4 24         9 18 13 17  5        10 16 15  9 19
     * 21  9 14 16  7        19  8  7 25 23        18  8 23 26 20
     * 6 10  3 18  5        20 11 10 24  4        22 11 13  6  5
     * 1 12 20 15 19        14 21 16 12  6         2  0 12  3  7
     * At this point, the third board wins because it has at least one complete row or column of marked numbers (in this case, the entire top row is marked: 14 21 17 24 4).
     * <p>
     * The score of the winning board can now be calculated. Start by finding the sum of all unmarked numbers on that board; in this case, the sum is 188. Then, multiply that sum by the number that was just called when the board won, 24, to get the final score, 188 * 24 = 4512.
     * <p>
     * To guarantee victory against the giant squid, figure out which board will win first. What will your final score be if you choose that board?
     */
    public static int bingoFirstScore(List<String> inputs) {
        // process data
        String[] numbersDrawn = inputs.get(0).split(",");
        int numberOfBoards = (inputs.size() - 1) / 6;
        ArrayList<ArrayList<String>> boards = new ArrayList<>(numberOfBoards);
        ArrayList<ArrayList<Integer>> boardScores = new ArrayList<>(numberOfBoards);
        for (int i = 0; i < numberOfBoards; i++) {
            ArrayList<String> board = new ArrayList<>(25);
            board.addAll(List.of(inputs.get(i * 6 + 2).trim().split("\\s+")));
            board.addAll(List.of(inputs.get(i * 6 + 3).trim().split("\\s+")));
            board.addAll(List.of(inputs.get(i * 6 + 4).trim().split("\\s+")));
            board.addAll(List.of(inputs.get(i * 6 + 5).trim().split("\\s+")));
            board.addAll(List.of(inputs.get(i * 6 + 6).trim().split("\\s+")));
            boards.add(board);
            // 5 rows and 5 columns
            boardScores.add(new ArrayList<>(List.of(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)));
        }

        for (String numberDrawn : numbersDrawn) {
            for (int j = 0; j < numberOfBoards; j++) {
                int index = boards.get(j).indexOf(numberDrawn);
                if (index >= 0) {
                    boards.get(j).set(index, "X");
                    int row = index / 5;
                    int column = index % 5;
                    int rowScore = boardScores.get(j).get(row) + 1;
                    int columnScore = boardScores.get(j).get(column + 5) + 1;

                    // BINGO
                    if (rowScore == 5 || columnScore == 5) {
                        return Integer.parseInt(boards.get(j).stream().reduce("0",
                                (x, y) -> String.valueOf(Integer.parseInt(x)
                                        + (y.equals("X") ? 0 : Integer.parseInt(y)))))
                                * Integer.parseInt(numberDrawn);

                    }

                    boardScores.get(j).set(row, rowScore);
                    boardScores.get(j).set(column + 5, columnScore);
                }
            }
        }

        return 0;
    }

    /**
     * --- Part Two ---
     * On the other hand, it might be wise to try a different strategy: let the giant squid win.
     *
     * You aren't sure how many bingo boards a giant squid could play at once, so rather than waste time counting its arms, the safe thing to do is to figure out which board will win last and choose that one. That way, no matter which boards it picks, it will win for sure.
     *
     * In the above example, the second board is the last to win, which happens after 13 is eventually called and its middle column is completely marked. If you were to keep playing until this point, the second board would have a sum of unmarked numbers equal to 148 for a final score of 148 * 13 = 1924.
     *
     * Figure out which board will win last. Once it wins, what would its final score be?
     */
    public static int bingoLastScore(List<String> inputs) {
        // process data
        String[] numbersDrawn = inputs.get(0).split(",");
        int numberOfBoards = (inputs.size() - 1) / 6;
        ArrayList<ArrayList<String>> boards = new ArrayList<>(numberOfBoards);
        ArrayList<ArrayList<Integer>> boardScores = new ArrayList<>(numberOfBoards);
        boolean[] isBoardBingo = new boolean[numberOfBoards];
        int boardsLeft = numberOfBoards;

        for (int i = 0; i < numberOfBoards; i++) {
            ArrayList<String> board = new ArrayList<>(25);
            board.addAll(List.of(inputs.get(i * 6 + 2).trim().split("\\s+")));
            board.addAll(List.of(inputs.get(i * 6 + 3).trim().split("\\s+")));
            board.addAll(List.of(inputs.get(i * 6 + 4).trim().split("\\s+")));
            board.addAll(List.of(inputs.get(i * 6 + 5).trim().split("\\s+")));
            board.addAll(List.of(inputs.get(i * 6 + 6).trim().split("\\s+")));
            boards.add(board);
            // 5 rows and 5 columns
            boardScores.add(new ArrayList<>(List.of(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)));
        }

        for (String numberDrawn : numbersDrawn) {
            for (int j = 0; j < numberOfBoards; j++) {
                int index = boards.get(j).indexOf(numberDrawn);
                if (index >= 0 && !isBoardBingo[j]) {
                    boards.get(j).set(index, "X");
                    int row = index / 5;
                    int column = index % 5;
                    int rowScore = boardScores.get(j).get(row) + 1;
                    int columnScore = boardScores.get(j).get(column + 5) + 1;

                    // BINGO
                    if (rowScore == 5 || columnScore == 5) {
                        boardsLeft -= 1;
                        isBoardBingo[j] = true;
                    }

                    // BINGO
                    if (boardsLeft == 0) {
                        return Integer.parseInt(boards.get(j).stream().reduce("0",
                                (x, y) -> String.valueOf(Integer.parseInt(x)
                                        + (y.equals("X") ? 0 : Integer.parseInt(y)))))
                                * Integer.parseInt(numberDrawn);
                    }

                    boardScores.get(j).set(row, rowScore);
                    boardScores.get(j).set(column + 5, columnScore);
                }
            }
        }

        return 0;
    }

}
