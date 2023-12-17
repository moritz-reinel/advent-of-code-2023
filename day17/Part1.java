import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class Part1
{
    private final static Coord[] MOVES = {
        new Coord(0, -1), new Coord(1, 0), new Coord(0, 1), new Coord(-1, 0)};

    private final static int MAX_MOVES_PER_DIRECTION = 3;

    record Coord(int x, int y) {}

    record Node(Coord pos, int dir, int dirCnt, int dist) implements Comparable<Node> {
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.dist, other.dist);
        }
    }

    public static void main(String[] args) throws IOException
    {
        String[] lines = Files.readString(Path.of("input.txt")).strip().split("\n");

        int rowCnt = lines.length;
        int colCnt = lines[0].length();
        int[][] grid = new int[rowCnt][colCnt];
        for (int y=0; y<rowCnt; y++) {
            for (int x=0; x<colCnt; x++) {
                grid[y][x] = Character.getNumericValue(lines[y].charAt(x));
            }
        }

        int result = calculateShortestPath(grid);
        
        System.out.println(result);
    }

    // Dijkstra's algorithm + modifications to accomodate for the maximum steps per direction
    static int calculateShortestPath(int[][] grid) {
        var start = new Coord(0, 0);
        var end   = new Coord(grid[0].length-1, grid.length-1);

        int rows = grid.length;
        int cols = grid[0].length;

        Map<String, Integer> visited = new HashMap<>();

        PriorityQueue<Node> minHeap = new PriorityQueue<>();
        minHeap.add(new Node(start, 0, 0, 0));
        minHeap.add(new Node(start, 1, 0, 0));

        int result = Integer.MAX_VALUE;
        Node currentNode;
        while ((currentNode = minHeap.poll()) != null) {
            int x = currentNode.pos().x();
            int y = currentNode.pos().y();
            int dir = currentNode.dir();
            int dirCnt = currentNode.dirCnt();
            int dist = currentNode.dist();

            String visitedKey = "%d-%d-%d-%d".formatted(x, y, dir, dirCnt);

            if (visited.containsKey(visitedKey)) continue;

            visited.put(visitedKey, dist);

            if (x == end.x() && y == end.y()) {
                result = Math.min(result, dist);
            }

            for (int newDir = 0; newDir < MOVES.length; newDir++) {
                int newX = x + MOVES[newDir].x();
                int newY = y + MOVES[newDir].y();

                if (newX < 0 || newX >= cols || newY < 0 || newY >= rows || (newDir + 2) % 4 == dir) {
                    continue;
                }

                int newDirCnt = (newDir == dir) ? dirCnt + 1 : 1;

                if (newDirCnt > MAX_MOVES_PER_DIRECTION) continue;

                int newDist = dist + grid[newY][newX];
                minHeap.add(new Node(new Coord(newX, newY), newDir, newDirCnt, newDist));
            }
        }
        return result;
    }
}
