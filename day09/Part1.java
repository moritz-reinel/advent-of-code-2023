import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part1
{
    public static void main(String[] args) throws IOException
    {
        String[] lines = Files.readString(Path.of("input.txt")).strip().split("\n");

        long result = 0;
        List<Integer> row;

        for (var line : lines) {
            row = new ArrayList<>();
            row.addAll(Arrays.stream(line.split(" "))
                          .map(val -> Integer.parseInt(val))
                          .collect(Collectors.toList()));

            long lastValuesPerLineAdded = row.getLast();
            while (!isAllZero(row)) {
                List<Integer> next = new ArrayList<>();
                for (int i=0; i<row.size()-1; i++) {
                    next.add(row.get(i+1) - row.get(i));
                }
                
                row = next;
                lastValuesPerLineAdded += next.getLast();
            }

            result += lastValuesPerLineAdded;
        }

        System.out.println(result);
    }

    private static boolean isAllZero(List<Integer> row) {
        return row.stream().allMatch(val -> val == 0);
    }
}
