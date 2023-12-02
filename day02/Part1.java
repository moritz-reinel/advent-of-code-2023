import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Part1 {

    private final static Pattern patternGame = Pattern.compile("Game (\\d+)");
    private final static Pattern patternRed = Pattern.compile("(\\d+) red");
    private final static Pattern patternBlue = Pattern.compile("(\\d+) blue");
    private final static Pattern patternGreen = Pattern.compile("(\\d+) green");

    public static void main(String[] args) throws FileNotFoundException {

        int possible = 0;

        Scanner scanner = new Scanner(new File("input.txt"));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            
            Matcher matcher = patternGame.matcher(line);
            if (matcher.find()) {
                int gameId = Integer.parseInt(matcher.group(1));

                List<Integer> reds = getNumbersFromPattern(patternRed, line);
                List<Integer> greens = getNumbersFromPattern(patternGreen, line);
                List<Integer> blues = getNumbersFromPattern(patternBlue, line);

                int maxRed = getMax(reds);
                int maxGreen = getMax(greens);
                int maxBlue = getMax(blues);

                if (maxRed <= 12 && maxGreen <= 13 && maxBlue <= 14)
                    possible += gameId;
            }
        }
        scanner.close();

        System.out.println(possible);
    }

    private static List<Integer> getNumbersFromPattern(Pattern pattern, String line) {
        Matcher matcher = pattern.matcher(line);
        List<Integer> numbers = new ArrayList<>();
        while (matcher.find()) {
            numbers.add(Integer.parseInt(matcher.group(1)));
        }
        return numbers;
    }

    private static int getMax(List<Integer> numbers) {
        return numbers.stream().mapToInt(Integer::intValue).max().orElse(0);
    }
}
