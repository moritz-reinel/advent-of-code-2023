import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part2
{
    record Point(int x, int y) {
        int distance(Point other) {
            return Math.abs(x-other.x) + Math.abs(y-other.y);
        }

        Point moveY(int dist) {
            return new Point(x, y + dist * GROW_FACTOR);
        }
    };

    private final static int GROW_FACTOR = 1_000_000 - 1;

    public static void main(String[] args) throws IOException
    {
        List<List<Character>> grid = Files.lines(Paths.get("input.txt"))
            .map(line -> line.chars().mapToObj(c -> (char) c).collect(Collectors.toList()))
            .collect(Collectors.toList());

        List<Point> points = new ArrayList<>();

        int colsExpanded = 0;
        for (int col=0; col<grid.getFirst().size(); col++) {
            boolean empty = true;
            for (int r=0; r<grid.size(); r++) {
                if (grid.get(r).get(col) == '#') {
                    empty = false;
                    break;
                }
            }
            
            for (int r=0; r<grid.size(); r++) {
                if (grid.get(r).get(col) == '#') {
                    points.add(new Point(col + colsExpanded * GROW_FACTOR, r));
                }
            }
            if (empty) colsExpanded++;
        }

        List<Point> newPoints = new ArrayList<>();

        int rowsExpanded = 0;
        for (int row=0; row<grid.size(); row++) {
            boolean empty = true;
            for (int c=0; c<grid.getFirst().size(); c++) {
                if (grid.get(row).get(c) == '#') {
                    empty = false;
                    break;
                }
            }
            
            for (int i=0; i<points.size(); i++) {
                if (points.get(i).y() == row) {
                    newPoints.add(points.get(i).moveY(rowsExpanded));
                }
            }
            if (empty) rowsExpanded++;
        }

        long sumDist = generatePointCombinations(newPoints).stream()
            .mapToLong(pair -> { return pair[0].distance(pair[1]); })
            .reduce(0L, (sum, dist) -> sum + dist);

        System.out.println(sumDist);
    }

    private static List<Point[]> generatePointCombinations(List<Point> points) {
        List<Point[]> combinations = new ArrayList<>();

        int n = points.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                combinations.add(new Point[] {points.get(i), points.get(j)});
            }
        }

        return combinations;
    }
}
