package day8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.ReaderUtil;

public class SevenSegmentSearch {
    public static void main(String[] args) {
        List<String> inputs = ReaderUtil.getInputsStr("2021/day8/input.txt");
        System.out.println(partialDigitCounter(inputs));
        System.out.println(fullDigitCounter(inputs));
    }

    /**
     * --- Day 8: Seven Segment Search ---
     * You barely reach the safety of the cave when the whale smashes into the cave mouth, collapsing it. Sensors indicate another exit to this cave at a much greater depth, so you have no choice but to press on.
     * <p>
     * As your submarine slowly makes its way through the cave system, you notice that the four-digit seven-segment displays in your submarine are malfunctioning; they must have been damaged during the escape. You'll be in a lot of trouble without them, so you'd better figure out what's wrong.
     * <p>
     * Each digit of a seven-segment display is rendered by turning on or off any of seven segments named a through g:
     * <p>
     * 0:      1:      2:      3:      4:
     * aaaa    ....    aaaa    aaaa    ....
     * b    c  .    c  .    c  .    c  b    c
     * b    c  .    c  .    c  .    c  b    c
     * ....    ....    dddd    dddd    dddd
     * e    f  .    f  e    .  .    f  .    f
     * e    f  .    f  e    .  .    f  .    f
     * gggg    ....    gggg    gggg    ....
     * <p>
     * 5:      6:      7:      8:      9:
     * aaaa    aaaa    aaaa    aaaa    aaaa
     * b    .  b    .  .    c  b    c  b    c
     * b    .  b    .  .    c  b    c  b    c
     * dddd    dddd    ....    dddd    dddd
     * .    f  e    f  .    f  e    f  .    f
     * .    f  e    f  .    f  e    f  .    f
     * gggg    gggg    ....    gggg    gggg
     * So, to render a 1, only segments c and f would be turned on; the rest would be off. To render a 7, only segments a, c, and f would be turned on.
     * <p>
     * The problem is that the signals which control the segments have been mixed up on each display. The submarine is still trying to display numbers by producing output on signal wires a through g, but those wires are connected to segments randomly. Worse, the wire/segment connections are mixed up separately for each four-digit display! (All of the digits within a display use the same connections, though.)
     * <p>
     * So, you might know that only signal wires b and g are turned on, but that doesn't mean segments b and g are turned on: the only digit that uses two segments is 1, so it must mean segments c and f are meant to be on. With just that information, you still can't tell which wire (b/g) goes to which segment (c/f). For that, you'll need to collect more information.
     * <p>
     * For each display, you watch the changing signals for a while, make a note of all ten unique signal patterns you see, and then write down a single four digit output value (your puzzle input). Using the signal patterns, you should be able to work out which pattern corresponds to which digit.
     * <p>
     * For example, here is what you might see in a single entry in your notes:
     * <p>
     * acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab |
     * cdfeb fcadb cdfeb cdbaf
     * (The entry is wrapped here to two lines so it fits; in your notes, it will all be on a single line.)
     * <p>
     * Each entry consists of ten unique signal patterns, a | delimiter, and finally the four digit output value. Within an entry, the same wire/segment connections are used (but you don't know what the connections actually are). The unique signal patterns correspond to the ten different ways the submarine tries to render a digit using the current wire/segment connections. Because 7 is the only digit that uses three segments, dab in the above example means that to render a 7, signal lines d, a, and b are on. Because 4 is the only digit that uses four segments, eafb means that to render a 4, signal lines e, a, f, and b are on.
     * <p>
     * Using this information, you should be able to work out which combination of signal wires corresponds to each of the ten digits. Then, you can decode the four digit output value. Unfortunately, in the above example, all of the digits in the output value (cdfeb fcadb cdfeb cdbaf) use five segments and are more difficult to deduce.
     * <p>
     * For now, focus on the easy digits. Consider this larger example:
     * <p>
     * be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb |
     * fdgacbe cefdb cefbgd gcbe
     * edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec |
     * fcgedb cgb dgebacf gc
     * fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef |
     * cg cg fdcagb cbg
     * fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega |
     * efabcd cedba gadfec cb
     * aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga |
     * gecf egdcabf bgf bfgea
     * fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf |
     * gebdcfa ecba ca fadegcb
     * dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf |
     * cefg dcbef fcge gbcadfe
     * bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd |
     * ed bcgafe cdgba cbgef
     * egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg |
     * gbdfcae bgc cg cgb
     * gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc |
     * fgae cfgab fg bagce
     * Because the digits 1, 4, 7, and 8 each use a unique number of segments, you should be able to tell which combinations of signals correspond to those digits. Counting only digits in the output values (the part after | on each line), in the above example, there are 26 instances of digits that use a unique number of segments (highlighted above).
     * <p>
     * In the output values, how many times do digits 1, 4, 7, or 8 appear?
     */
    public static int partialDigitCounter(List<String> inputs) {
        int count = 0;
        for (int i = 0; i < inputs.size(); i++) {
            String[] digits = inputs.get(i).split("\\|")[1].trim().split(" ");
            for (String digit : digits) {
                count = paritalDigitDecoder(digit) != -1 ? count + 1 : count;
            }
        }
        return count;
    }

    public static int paritalDigitDecoder(String value) {
        switch (value.length()) {
        case 2:
            return 1;
        case 4:
            return 4;
        case 3:
            return 7;
        case 7:
            return 8;
        default:
            return -1;
        }
    }

    /**
     * --- Part Two ---
     * Through a little deduction, you should now be able to determine the remaining digits. Consider again the first example above:
     * <p>
     * acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab |
     * cdfeb fcadb cdfeb cdbaf
     * After some careful analysis, the mapping between signal wires and segments only make sense in the following configuration:
     * <p>
     * dddd
     * e    a
     * e    a
     * ffff
     * g    b
     * g    b
     * cccc
     * So, the unique signal patterns would correspond to the following digits:
     * <p>
     * acedgfb: 8
     * cdfbe: 5
     * gcdfa: 2
     * fbcad: 3
     * dab: 7
     * cefabd: 9
     * cdfgeb: 6
     * eafb: 4
     * cagedb: 0
     * ab: 1
     * Then, the four digits of the output value can be decoded:
     * <p>
     * cdfeb: 5
     * fcadb: 3
     * cdfeb: 5
     * cdbaf: 3
     * Therefore, the output value for this entry is 5353.
     * <p>
     * Following this same process for each entry in the second, larger example above, the output value of each entry can be determined:
     * <p>
     * fdgacbe cefdb cefbgd gcbe: 8394
     * fcgedb cgb dgebacf gc: 9781
     * cg cg fdcagb cbg: 1197
     * efabcd cedba gadfec cb: 9361
     * gecf egdcabf bgf bfgea: 4873
     * gebdcfa ecba ca fadegcb: 8418
     * cefg dcbef fcge gbcadfe: 4548
     * ed bcgafe cdgba cbgef: 1625
     * gbdfcae bgc cg cgb: 8717
     * fgae cfgab fg bagce: 4315
     * Adding all of the output values in this larger example produces 61229.
     * <p>
     * For each entry, determine all of the wire/segment connections and decode the four-digit output values. What do you get if you add up all of the output values?
     */
    public static int fullDigitCounter(List<String> inputs) {
        int count = 0;
        for (String input : inputs) {
            String[] entry = input.split("\\|");
            Map<Set<Character>, Integer> decoded = signalDecoder(entry[0].trim());
            String[] digits = entry[1].trim().split(" ");
            int number = 0;
            for (String digit : digits) {
                number = number * 10 + decoded.get(stringToSet(digit));
            }
            count += number;
        }
        return count;
    }

    public static Map<Set<Character>, Integer> signalDecoder(String signal) {
        HashMap<Set<Character>, Integer> setToDigit = new HashMap<>();
        HashMap<Integer, Set<Character>> digitToSet = new HashMap<>(); // need 1 and 4 to differentiate others later
        String[] numbers = signal.split(" ");
        // Decode digits in this order 1,4,5,7 (obvious ones with unique segment numbers)
        // 2,3,5 (requires 5 segment)
        List<Set<Character>> fiveSegments = new ArrayList<>();
        // 9,6,0 (requires 6 segments)
        List<Set<Character>> sixSegments = new ArrayList<>();
        for (String number : numbers) {
            switch (number.length()) {
            case 2:
                setToDigit.put(stringToSet(number), 1);
                digitToSet.put(1, stringToSet(number));
                break;
            case 3:
                setToDigit.put(stringToSet(number), 7);
                break;
            case 4:
                setToDigit.put(stringToSet(number), 4);
                digitToSet.put(4, stringToSet(number));
                break;
            case 5:
                fiveSegments.add(stringToSet(number));
                break;
            case 6:
                sixSegments.add(stringToSet(number));
                break;
            case 7:
                setToDigit.put(stringToSet(number), 8);
                break;
            }
        }

        // Differentiate between five segments
        // 3 -> contains 1
        // 5 -> only 2 segments left after removing 4
        // 2 -> only 3 segments left after removing 4
        for (Set<Character> fiveSegment : fiveSegments) {
            if (fiveSegment.containsAll(digitToSet.get(1))) {
                setToDigit.put(fiveSegment, 3);
            } else {
                Set<Character> copy = new HashSet<>(fiveSegment);
                copy.removeAll(digitToSet.get(4));
                if (copy.size() == 2) {
                    setToDigit.put(fiveSegment, 5);
                } else {
                    setToDigit.put(fiveSegment, 2);
                }
            }
        }

        // Differentiate between six segments
        // 9 -> contains 4
        // 0 -> contains 1 but not 4
        // 6 -> contains neither 1 or 4
        for (Set<Character> sixSegment : sixSegments) {
            if (sixSegment.containsAll(digitToSet.get(4))) {
                setToDigit.put(sixSegment, 9);
            } else if (!sixSegment.containsAll(digitToSet.get(4)) && sixSegment.containsAll(digitToSet.get(1))) {
                setToDigit.put(sixSegment, 0);
            } else {
                setToDigit.put(sixSegment, 6);
            }
        }

        return setToDigit;
    }

    public static Set<Character> stringToSet(String str) {
        HashSet<Character> set = new HashSet<>();
        for (char c : str.toCharArray()) {
            set.add(c);
        }
        return set;
    }
}
