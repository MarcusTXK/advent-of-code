package day14;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.ReaderUtil;

public class ExtendedPolymerization {
    public static void main(String[] args) {
        List<String> inputs = ReaderUtil.getInputsStr("2021/day14/input.txt");
        String polymer = inputs.get(0);
        String[][] instructions = new String[Math.max(inputs.size() - 2, 0)][2];
        for (int i = 2; i < inputs.size(); i++) {
            instructions[i - 2] = inputs.get(i).split(" -> ");
        }

        System.out.println(pairInsertion(polymer, instructions, 10));
        System.out.println(pairInsertion(polymer, instructions, 40));
    }

    /**
     * --- Day 14: Extended Polymerization ---
     * The incredible pressures at this depth are starting to put a strain on your submarine. The submarine has polymerization equipment that would produce suitable materials to reinforce the submarine, and the nearby volcanically-active caves should even have the necessary input elements in sufficient quantities.
     *
     * The submarine manual contains instructions for finding the optimal polymer formula; specifically, it offers a polymer template and a list of pair insertion rules (your puzzle input). You just need to work out what polymer would result after repeating the pair insertion process a few times.
     *
     * For example:
     *
     * NNCB
     *
     * CH -> B
     * HH -> N
     * CB -> H
     * NH -> C
     * HB -> C
     * HC -> B
     * HN -> C
     * NN -> C
     * BH -> H
     * NC -> B
     * NB -> B
     * BN -> B
     * BB -> N
     * BC -> B
     * CC -> N
     * CN -> C
     * The first line is the polymer template - this is the starting point of the process.
     *
     * The following section defines the pair insertion rules. A rule like AB -> C means that when elements A and B are immediately adjacent, element C should be inserted between them. These insertions all happen simultaneously.
     *
     * So, starting with the polymer template NNCB, the first step simultaneously considers all three pairs:
     *
     * The first pair (NN) matches the rule NN -> C, so element C is inserted between the first N and the second N.
     * The second pair (NC) matches the rule NC -> B, so element B is inserted between the N and the C.
     * The third pair (CB) matches the rule CB -> H, so element H is inserted between the C and the B.
     * Note that these pairs overlap: the second element of one pair is the first element of the next pair. Also, because all pairs are considered simultaneously, inserted elements are not considered to be part of a pair until the next step.
     *
     * After the first step of this process, the polymer becomes NCNBCHB.
     *
     * Here are the results of a few steps using the above rules:
     *
     * Template:     NNCB
     * After step 1: NCNBCHB
     * After step 2: NBCCNBBBCBHCB
     * After step 3: NBBBCNCCNBBNBNBBCHBHHBCHB
     * After step 4: NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB
     * This polymer grows quickly. After step 5, it has length 97; After step 10, it has length 3073. After step 10, B occurs 1749 times, C occurs 298 times, H occurs 161 times, and N occurs 865 times; taking the quantity of the most common element (B, 1749) and subtracting the quantity of the least common element (H, 161) produces 1749 - 161 = 1588.
     *
     * Apply 10 steps of pair insertion to the polymer template and find the most and least common elements in the result. What do you get if you take the quantity of the most common element and subtract the quantity of the least common element?
     *
     * --- Part Two ---
     * The resulting polymer isn't nearly strong enough to reinforce the submarine. You'll need to run more steps of the pair insertion process; a total of 40 steps should do it.
     *
     * In the above example, the most common element is B (occurring 2192039569602 times) and the least common element is H (occurring 3849876073 times); subtracting these produces 2188189693529.
     *
     * Apply 40 steps of pair insertion to the polymer template and find the most and least common elements in the result. What do you get if you take the quantity of the most common element and subtract the quantity of the least common element?
     */
    public static long pairInsertion(String template, String[][] instructions, int steps) {
        HashMap<String, Long> pairMap = new HashMap<>();
        for (int i = 0; i < template.length() - 1; i++) {
            String key = template.substring(i, i + 2);
            pairMap.computeIfPresent(key, (k, v) -> v + 1);
            pairMap.putIfAbsent(key, 1L);
        }
        while (steps > 0) {
            HashMap<String, Long> newPairMap = (HashMap<String, Long>) pairMap.clone();
            for (String[] instruction : instructions) {
                if (pairMap.containsKey(instruction[0])) {
                    long count = pairMap.get(instruction[0]);
                    newPairMap.compute(instruction[0], (k, v) -> v - count);
                    // AB  insert C  ->  AC and CB
                    String comboOne = instruction[0].charAt(0) + instruction[1];
                    newPairMap.computeIfPresent(comboOne, (k, v) -> v + count);
                    newPairMap.putIfAbsent(comboOne, count);

                    String comboTwo = instruction[1] + instruction[0].charAt(1);
                    newPairMap.computeIfPresent(comboTwo, (k, v) -> v + count);
                    newPairMap.putIfAbsent(comboTwo, count);
                }
            }
            steps -= 1;
            pairMap = newPairMap;
        }

        // calculate difference between most common and least common elements
        HashMap<Character, Long> elementMap = new HashMap<>();
        for (Map.Entry<String, Long> entry : pairMap.entrySet()) {
            elementMap.computeIfPresent(entry.getKey().charAt(0), (k, v) -> v + entry.getValue());
            elementMap.putIfAbsent(entry.getKey().charAt(0), entry.getValue());
            elementMap.computeIfPresent(entry.getKey().charAt(1), (k, v) -> v + entry.getValue());
            elementMap.putIfAbsent(entry.getKey().charAt(1), entry.getValue());
        }

        long max = Long.MIN_VALUE;
        long min = Long.MAX_VALUE;
        for (Long value : elementMap.values()) {
            max = Math.max(max, (value + 1) / 2);
            min = Math.min(min, (value + 1) / 2);
        }
        return max - min;
    }
}
