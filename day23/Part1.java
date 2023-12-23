import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part1
{
    record Coord(int x, int y) {
        List<Coord> neighbours() {
            return Arrays.asList(new Coord(x - 1, y), new Coord(x + 1, y), new Coord(x, y - 1), new Coord(x, y + 1));
        }

        List<Coord> validNeighbours() {
            return this.neighbours().stream()
                .filter(neighbour -> neighbour.y >= 0 && neighbour.y < grid.size() && neighbour.x >= 0 && neighbour.x < grid.getFirst().size() && grid.get(neighbour.y).get(neighbour.x) != '#')
                .toList();
        }
    }

    private static List<List<Character>> grid;
    private static Coord start;
    private static Coord end;
    private static boolean[][] visited;

    /*
     * had to run with `java -Xss4m Part1` to avoid stack overflow
     */
    public static void main(String[] args) throws IOException {
        grid = Files.lines(Paths.get("input.txt"))
            .map(line -> line.chars().mapToObj(c -> (char) c).collect(Collectors.toList()))
            .collect(Collectors.toList());

        start = new Coord(1, 0);
        end = new Coord(grid.size() - 2, grid.getFirst().size() - 1);
        visited = new boolean[grid.size()][grid.get(0).size()];

        int result = findMax(start, visited, 0);
        System.out.println(result);
    }

    private static int findMax(Coord current, boolean[][] visited, int distance) {
        if (current.equals(end)) {
            return distance;
        }

        visited[current.y][current.x] = true;

        char currentChar = grid.get(current.y).get(current.x);
        var neighbours = switch (currentChar) {
            case '>' -> List.of(Map.entry(new Coord(current.x + 1, current.y), 1));
            case '<' -> List.of(Map.entry(new Coord(current.x - 1, current.y), 1));
            case 'v' -> List.of(Map.entry(new Coord(current.x, current.y + 1), 1));
            case '^' -> List.of(Map.entry(new Coord(current.x, current.y - 1), 1));
            default  -> current.validNeighbours().stream().map(item -> Map.entry(item, 1)).toList();
        };

        int max = neighbours.stream()
            .filter(neighbour -> !visited[neighbour.getKey().y][neighbour.getKey().x])
            .mapToInt(neighbour -> findMax(neighbour.getKey(), visited, distance + neighbour.getValue()))
            .max()
            .orElse(0);
        visited[current.y][current.x] = false;

        return max;
    }
}
