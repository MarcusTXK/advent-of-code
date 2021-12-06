package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ReaderUtil {

    public static List<String> getInputsStr(String path) {
        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            ArrayList<String> input = new ArrayList<>();
            String line = br.readLine();

            while (line != null) {
                input.add(line);
                line = br.readLine();
            }
            return input;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static List<Integer> getInputsInt(String path) {
        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            ArrayList<Integer> input = new ArrayList<>();
            String line = br.readLine();

            while (line != null) {
                input.add(Integer.parseInt(line));
                line = br.readLine();
            }
            return input;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
