import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part1
{
    public static void main(String[] args) throws IOException
    {
        String[] lines = Files.readString(Path.of("input.txt")).strip().split("\n");

        long combinations = 0;

        for (var line : lines) {
            List<Integer> nums = Arrays.stream(line.split(" ")[1]
                .split(","))
                .map(n -> Integer.parseInt(n))
                .collect(Collectors.toList());
            combinations += generateCombinations(line.split(" ")[0], 0, new StringBuilder()).stream()
                .filter(combi -> validateSprings(combi, nums) == true)
                .count();
        }

        System.out.println(combinations);
    }

    private static boolean validateSprings(String springs, List<Integer> groups) {
        var foundGroups = Arrays.stream(springs.split("\\."))
            .filter(elem -> !"".equals(elem))
            .collect(Collectors.toList());

        if (foundGroups.size() != groups.size()) return false;

        for (int i=0; i<groups.size(); i++) {
            if (!(i < foundGroups.size() && foundGroups.get(i).length() == groups.get(i))) return false;
        }

        return true;
    }

    private static List<String> generateCombinations(String springs, int index, StringBuilder current) {
        List<String> result = new ArrayList<>();

        if (index == springs.length()) {
            result.add(current.toString());
            return result;
        }

        char currentChar = springs.charAt(index);
        if (currentChar == '?') {
            current.append('.');
            result.addAll(generateCombinations(springs, index + 1, new StringBuilder(current)));
            current.deleteCharAt(current.length() - 1);

            current.append('#');
            result.addAll(generateCombinations(springs, index + 1, new StringBuilder(current)));
            current.deleteCharAt(current.length() - 1);
        } else {
            current.append(currentChar);
            result.addAll(generateCombinations(springs, index + 1, new StringBuilder(current)));
            current.deleteCharAt(current.length() - 1);
        }

        return result;
    }
}
