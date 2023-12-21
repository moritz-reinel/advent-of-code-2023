import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part1
{
    private final static Coord[] MOVES = {
        new Coord(0, -1), new Coord(1, 0), new Coord(0, 1), new Coord(-1, 0)};

    private final static int TARGET_STEPS = 64;

    record Coord(int x, int y) {
        Coord apply(Coord other) {
            return new Coord(this.x + other.x, this.y + other.y);
        }
    }

    private static List<List<Character>> map;

    public static void main(String[] args) throws IOException
    {
        map = Files.lines(Paths.get("input.txt"))
            .map(line -> line.chars().mapToObj(c -> (char) c).collect(Collectors.toList()))
            .collect(Collectors.toList());

        Set<Coord> lastMoves = new HashSet<>();
        lastMoves.add(findStart());

        for (int steps=0; steps<TARGET_STEPS; steps++) {
            var tmp = new HashSet<>(lastMoves);
            lastMoves.clear();

            for (var move : tmp) {
                for (var possibleMove : MOVES) {
                    var newCoord = move.apply(possibleMove);
                    if (newCoord.x() >= 0 && newCoord.x() < map.getFirst().size() && newCoord.y() >= 0 && newCoord.y() < map.size() && map.get(newCoord.y()).get(newCoord.x())!= '#') {
                        lastMoves.add(newCoord);
                    }
                }
            }
        }

        System.out.println(lastMoves.size());
    }

    private static Coord findStart() {
        for (int y=0; y<map.size(); y++) {
            for (int x=0; x<map.getFirst().size(); x++) {
                if (map.get(y).get(x) =='S') {
                    return new Coord(x, y);
                }
            }
        }
        return null;
    }
}
