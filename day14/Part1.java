import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part1
{
    public static void main(String[] args) throws IOException
    {
        List<List<Character>> grid = Files.lines(Paths.get("input.txt"))
            .map(line -> line.chars().mapToObj(c -> (char) c).collect(Collectors.toList()))
            .collect(Collectors.toList());

        // roll up
        List<Integer> free = new ArrayList<>();
        for (int col=0; col<grid.getFirst().size(); col++) {
            for (int row=0; row<grid.size(); row++) {
                if (grid.get(row).get(col) == '.') {
                    free.add(row);
                } else if (grid.get(row).get(col) == 'O') {
                    if (!free.isEmpty()) {
                        grid.get(free.removeFirst()).set(col, 'O');
                        grid.get(row).set(col, '.');
                        free.add(row);
                    }
                } else if (grid.get(row).get(col) == '#') {
                    free.clear();
                }
            }
            free.clear();
        }

        long sum = 0;
        for (int y=0; y<grid.size(); y++) {
            long rocks = grid.get(y).stream().filter(c -> (c == 'O')).count();
            sum += (grid.size() - y) * rocks;
        }

        System.out.println(sum);
    }
}
