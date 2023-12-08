import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class Part1
{
    private final static Pattern INPUT_PATTERN = Pattern.compile("([A-Z]{3})\\s*=\\s*\\(([A-Z]{3}),\\s*([A-Z]{3})\\)");

    public static void main(String[] args) throws IOException
    {
        String[] content = Files.readString(Path.of("input.txt")).strip().split("\n\n");

        char[] instructions = content[0].toCharArray();

        Map<String, String> left = new HashMap<>();
        Map<String, String> right = new HashMap<>();

        for (String line : content[1].split("\n")) {
            Matcher matcher = INPUT_PATTERN.matcher(line);

            if (matcher.matches()) {
                String node = matcher.group(1);
                String leftNode = matcher.group(2);
                String rightNode = matcher.group(3);

                left.put(node, leftNode);
                right.put(node, rightNode);
            } else {
                System.err.println(line + ": no matches");
            }
        }

        int steps = 0;
        String current = "AAA";

        outer:
        while (!current.equals("ZZZ")) {
            for (char d : instructions) {
                if (d == 'L') {
                    current = left.get(current);
                } else {
                    current = right.get(current);
                }

                steps++;
                if (current.equals("ZZZ")) break outer;
            }
        }

        System.out.println(steps);
    }
}
