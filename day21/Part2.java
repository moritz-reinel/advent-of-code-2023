import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/*
 * DISCLAIMER: I only got part 2 working with the help of a friend of mine, who is studying pysics.
 *             The diamond shape and unobstructed "view" to the edges in every cardinal direction
 *             from the starting point made me realise that there had to be a cylce.
 *             My friend helped me by seeing a polynomial formula for the number of matches and also
 *             the hint to solving it with the Lagrange interpolation.
 */
public class Part2
{
    private final static Coord[] MOVES = {
        new Coord(0, -1), new Coord(1, 0), new Coord(0, 1), new Coord(-1, 0)};

    private final static int TARGET_X = 26_501_365;
    
    private final static List<Integer> POLYNOMIAL_STEPS = List.of(65, 65 + 131, 65 + 2 * 131);

    record Coord(int x, int y) {
        Coord apply(Coord other) {
            return new Coord(this.x + other.x, this.y + other.y);
        }

        Coord fitToBounds(int width, int height) {
            return new Coord(((this.x % width) + width) % width, ((this.y % height) + height) % height);
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

        Map<Integer, Integer> polyStepsY = POLYNOMIAL_STEPS.stream().collect(Collectors.toMap(n -> n, n -> 0));

        for (int steps = 1; steps <= POLYNOMIAL_STEPS.getLast(); steps++) {
            var tmp = new HashSet<>(lastMoves);
            lastMoves.clear();

            Map<Coord, Boolean> visited = new HashMap<>();

            for (var move : tmp) {
                for (var possibleMove : MOVES) {
                    var newCoord = move.apply(possibleMove);
                    var checkCoord = newCoord.fitToBounds(map.getFirst().size(), map.size());
                    if (map.get(checkCoord.y()).get(checkCoord.x()) != '#' && !visited.containsKey(newCoord)) {
                        lastMoves.add(newCoord);
                        visited.put(newCoord, true);
                    }
                }
            }

            if (POLYNOMIAL_STEPS.contains(steps)) {
                polyStepsY.put(steps, lastMoves.size());
            }
        }

        long result = LagrangeInterpolation.lagrangeInterpolation(polyStepsY, TARGET_X);

        System.out.println(result);
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
