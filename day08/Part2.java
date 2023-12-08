import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class Part2
{
    private final static Pattern INPUT_PATTERN = Pattern.compile("([A-Z0-9]{3})\\s*=\\s*\\(([A-Z0-9]{3}),\\s*([A-Z0-9]{3})\\)");

    public static void main(String[] args) throws IOException
    {
        String[] content = Files.readString(Path.of("input.txt")).strip().split("\n\n");

        char[] instructions = content[0].toCharArray();

        Map<String, String> left = new HashMap<>();
        Map<String, String> right = new HashMap<>();

        List<String> as = new ArrayList<>();

        for (String line : content[1].split("\n")) {
            Matcher matcher = INPUT_PATTERN.matcher(line);

            if (matcher.matches()) {
                String node = matcher.group(1);
                String leftNode = matcher.group(2);
                String rightNode = matcher.group(3);

                left.put(node, leftNode);
                right.put(node, rightNode);

                if (node.endsWith("A")) {
                    as.add(node);
                }
            } else {
                System.err.println(line + ": no matches");
            }
        }

        long lcm = 1;

        for (int i=0; i<as.size(); i++) {
            int steps = 0, instructionIdx = 0;

            while (!as.get(i).endsWith("Z")) {
                if (instructionIdx == instructions.length) instructionIdx = 0;

                if (instructions[instructionIdx] == 'L') {
                    as.set(i, left.get(as.get(i)));
                } else {
                    as.set(i, right.get(as.get(i)));
                }

                steps++;
                instructionIdx++;
            }

            lcm = leastCommonMultiplier(lcm, steps);
        }

        System.out.println(lcm);
    }

    private static long leastCommonMultiplier(long number1, long number2) {
        if (number1 == 0 || number2 == 0) return 0;

        long abs1 = Math.abs(number1);
        long abs2 = Math.abs(number2);
        long absHigher = Math.max(abs1, abs2);
        long absLower = Math.min(abs1, abs2);
        long lcm = absHigher;
        while (lcm % absLower != 0) {
            lcm += absHigher;
        }
        return lcm;
    }
}
