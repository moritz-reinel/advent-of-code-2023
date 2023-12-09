import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part2
{
    public static void main(String[] args) throws IOException
    {
        String[] lines = Files.readString(Path.of("input.txt")).strip().split("\n");

        long result = 0;
        List<Integer> row;
        List<Integer> firstNumsPerLine;

        for (var line : lines) {
            row = new ArrayList<>();
            firstNumsPerLine  = new ArrayList<>();

            row.addAll(Arrays.stream(line.split(" "))
                          .map(val -> Integer.parseInt(val))
                          .collect(Collectors.toList()));

            while (!isAllZero(row)) {
                firstNumsPerLine.add(row.getFirst());
                List<Integer> next = new ArrayList<>();
                for (int i=0; i<row.size()-1; i++) {
                    next.add(row.get(i+1) - row.get(i));
                }
                
                row = next;
            }
            firstNumsPerLine.add(0);
            
            // backwards extrapolation
            for (int i=firstNumsPerLine.size()-2; i!=-1; i--) {
                firstNumsPerLine.set(i, firstNumsPerLine.get(i) - firstNumsPerLine.get(i+1));
            }

            result += firstNumsPerLine.getFirst();
        }

        System.out.println(result);
    }

    private static boolean isAllZero(List<Integer> row) {
        return row.stream().allMatch(val -> val == 0);
    }
}
