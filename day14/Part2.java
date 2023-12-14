import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

public class Part2
{
    private final static int CYCLES = 1_000_000_000;

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException
    {
        List<List<Character>> grid = Files.lines(Paths.get("input.txt"))
            .map(line -> line.chars().mapToObj(c -> (char) c).collect(Collectors.toList()))
            .collect(Collectors.toList());

        Map<String, Integer> stateAtCycle = new HashMap<>();

        List<Integer> free = new ArrayList<>();
        for (int cycle=0; cycle<CYCLES; cycle++) {
            // NORTH roll
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
            free.clear();

            // WEST roll
            for (int row=0; row<grid.size(); row++) {
                for (int col=0; col<grid.getFirst().size(); col++) {
                    if (grid.get(row).get(col) == '.') {
                        free.add(col);
                    } else if (grid.get(row).get(col) == 'O') {
                        if (!free.isEmpty()) {
                            grid.get(row).set(free.removeFirst(), 'O');
                            grid.get(row).set(col, '.');
                            free.add(col);
                        }
                    } else if (grid.get(row).get(col) == '#') {
                        free.clear();
                    }
                }
                free.clear();
            }
            free.clear();

            // SOUTH roll
            for (int col=0; col<grid.getFirst().size(); col++) {
                for (int row=grid.size()-1; row>=0; row--) {
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
            free.clear();

            // EAST roll
            for (int row=0; row<grid.size(); row++) {
                for (int col=grid.getFirst().size()-1; col>=0; col--) {
                    if (grid.get(row).get(col) == '.') {
                        free.add(col);
                    } else if (grid.get(row).get(col) == 'O') {
                        if (!free.isEmpty()) {
                            grid.get(row).set(free.removeFirst(), 'O');
                            grid.get(row).set(col, '.');
                            free.add(col);
                        }
                    } else if (grid.get(row).get(col) == '#') {
                        free.clear();
                    }
                }
                free.clear();
            }
            free.clear();

            String hash = hashGrid(grid);
            // detect a full cycle and "fast-forward"
            if (stateAtCycle.containsKey(hash)) {
                int delta = cycle - stateAtCycle.get(hash);
                cycle += delta * ((CYCLES - cycle) / delta);
            }
            
            stateAtCycle.put(hash, cycle);
        }

        long sum = 0;
        for (int y=0; y<grid.size(); y++) {
            long rocks = grid.get(y).stream().filter(c -> (c == 'O')).count();
            sum += (grid.size() - y) * rocks;
        }

        System.out.println(sum);
    }

    private static String hashGrid(List<List<Character>> grid) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        grid.stream().flatMap(List::stream).forEach(c -> digest.update((byte) (char) c));

        return Base64.getEncoder().encodeToString(digest.digest());
    }

    @SuppressWarnings("unused")
    private static void printGrid(List<List<Character>> grid) {
        for (int y=0; y<grid.size(); y++) {
            System.out.println(
                grid.get(y).stream().map(String::valueOf).collect(Collectors.joining()));
        }
    }
}
