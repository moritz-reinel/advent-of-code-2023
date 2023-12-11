import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part1
{
    record Point(int x, int y) {
        int distance(Point other) {
            return Math.abs(x-other.x) + Math.abs(y-other.y);
        }
    };

    public static void main(String[] args) throws IOException
    {
        List<List<Character>> grid = Files.lines(Paths.get("input.txt"))
            .map(line -> line.chars().mapToObj(c -> (char) c).collect(Collectors.toList()))
            .collect(Collectors.toList());

        int colsExpanded = 0;
        int initWidth = grid.getFirst().size();
        for (int col=0; col<initWidth; col++) {
            boolean empty = true;
            for (int r=0; r<grid.size(); r++) {
                if (grid.get(r).get(col+colsExpanded) == '#') {
                    empty = false;
                    break;
                }
            }

            if (empty) {
                for (int r=0; r<grid.size(); r++) {
                    grid.get(r).add(col+colsExpanded, '.');
                }
                colsExpanded++;
            }
        }
        
        int rowsExpanded = 0;
        int initHeight = grid.size();
        for (int row=0; row<initHeight; row++) {
            boolean empty = true;
            for (int c=0; c<grid.getFirst().size(); c++) {
                if (grid.get(row+rowsExpanded).get(c) == '#') {
                    empty = false;
                    break;
                }
            }

            if (empty) {
                grid.add(row+rowsExpanded, new ArrayList<>());
                for (int c=0; c<grid.getFirst().size(); c++) {
                    grid.get(row+rowsExpanded).add('.');
                }
                rowsExpanded++;
            }
        }

        List<Point> points = new ArrayList<>();
        for (int y=0; y<grid.size(); y++) {
            for (int x=0; x<grid.getFirst().size(); x++) {
                if (grid.get(y).get(x) == '#') {
                    points.add(new Point(x, y));
                }
            }
        }
        System.out.println(points);

        int sumDist = generatePointCombinations(points).stream()
            .map(pair -> { return pair[0].distance(pair[1]); })
            .reduce(0, (sum, dist) -> sum + dist);

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
