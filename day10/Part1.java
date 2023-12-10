import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part1
{
    enum Direction {
        TOP    (new int[] {0, -1}),
        RIGHT  (new int[] {1, 0}),
        BOTTOM (new int[] {0, 1}),
        LEFT   (new int[] {-1, 0});

        private final int[] coords;

        private Direction(int[] coords) {
            this.coords = coords;
        }

        int[] getCoords() {
            return coords;
        }
    }

    record Coord(int x, int y, Direction afterDirection){};

    private static List<List<Character>> grid;
    private static int[] lastPos = new int[] {-1, -1};
    private static int[] currentPos;
    private static Direction fromDirection = null;

    private static List<Character> progressChars = Arrays.asList('|', '-', 'L', 'J', '7', 'F');

    private static Map<Direction, Map<Character, Coord>> nextPossibleSteps = new HashMap<>();
    static {
        Map<Character, Coord> lMap = new HashMap<>();
        lMap.put('-', new Coord(1, 0, Direction.LEFT));
        lMap.put('J', new Coord(0, -1, Direction.BOTTOM));
        lMap.put('7', new Coord(0, 1, Direction.TOP));
        nextPossibleSteps.put(Direction.LEFT, lMap);

        Map<Character, Coord> tMap = new HashMap<>();
        tMap.put('|', new Coord(0, 1, Direction.TOP));
        tMap.put('L', new Coord(1, 0, Direction.LEFT));
        tMap.put('J', new Coord(-1, 0, Direction.RIGHT));
        nextPossibleSteps.put(Direction.TOP, tMap);

        Map<Character, Coord> rMap = new HashMap<>();
        rMap.put('-', new Coord(-1, 0, Direction.RIGHT));
        rMap.put('L', new Coord(0, -1, Direction.BOTTOM));
        rMap.put('F', new Coord(0, 1, Direction.TOP));
        nextPossibleSteps.put(Direction.RIGHT, rMap);

        Map<Character, Coord> bMap = new HashMap<>();
        bMap.put('|', new Coord(0, -1, Direction.BOTTOM));
        bMap.put('7', new Coord(-1, 0, Direction.RIGHT));
        bMap.put('F', new Coord(1, 0, Direction.LEFT));
        nextPossibleSteps.put(Direction.BOTTOM, bMap);
    }

    private static Map<Direction, List<Character>> validPointsAroundStart = new HashMap<>();
    static {
        validPointsAroundStart.put(Direction.TOP,    Arrays.asList('|', '7', 'F'));
        validPointsAroundStart.put(Direction.RIGHT,  Arrays.asList('-', '7', 'J'));
        validPointsAroundStart.put(Direction.BOTTOM, Arrays.asList('|', 'J', 'L'));
        validPointsAroundStart.put(Direction.LEFT,   Arrays.asList('-', 'F', 'L'));
    }

    public static void main(String[] args) throws IOException
    {
        grid = Arrays.stream(
            Files.readString(Path.of("/media/DATEN/Programmieren/advent-of-code/advent-of-code-2023/day10/input.txt")).strip().split("\n"))
                .map(line -> line.chars().mapToObj(c -> (char) c).collect(Collectors.toList()))
                .collect(Collectors.toList());

        currentPos = getStartingPosition();
        if (currentPos == null) {
            System.err.println("S not found");
        }

        int steps = 0;
        while (!nextStep()) {
            steps++;
        }

        System.out.println((steps%2) + (steps/2));
    }
            
    private static int[] getStartingPosition() {
        for (int y = 0; y < grid.size(); y++) {
            for (int x = 0; x < grid.getFirst().size(); x++) {
                if (grid.get(y).get(x) == 'S') {
                    return new int[]{x, y};
                }
            }
        }
        return null;
    }

    private static boolean nextStep() {
        if (fromDirection == null) {  // starting point
            for (Direction dir : Direction.values()) {
                try {
                    char surrounding = grid.get(currentPos[1] + dir.getCoords()[1]).get(currentPos[0] + dir.getCoords()[0]);
                    if (!validPointsAroundStart.get(dir).contains(surrounding)) continue;

                    if (progressChars.contains(
                        grid.get(currentPos[1] + dir.getCoords()[1]).get(currentPos[0] + dir.getCoords()[0]))
                    ) {
                        lastPos[0] = currentPos[0];
                        lastPos[1] = currentPos[1];
                        currentPos[0] = currentPos[0] + dir.getCoords()[0];
                        currentPos[1] = currentPos[1] + dir.getCoords()[1];
                        fromDirection = switch(dir) {
                            case TOP    -> Direction.BOTTOM;
                            case RIGHT  -> Direction.LEFT;
                            case BOTTOM -> Direction.TOP;
                            case LEFT   -> Direction.RIGHT;
                        };
                        return false;
                    }
                } catch (IndexOutOfBoundsException e) { continue; }
            }
            System.err.println("no point after start found");
            return true;
        } else {
            if (!nextStepAfterInit()) {
                System.err.println("could not find next");
                return true;
            }
        }
        return grid.get(currentPos[1]).get(currentPos[0]) == 'S';
    }

    private static boolean nextStepAfterInit() {
        // System.out.println("at " + currentPos[0] + " " + currentPos[1] + " : " + grid.get(currentPos[1]).get(currentPos[0]) + " from " + fromDirection);

        for (Map.Entry<Character, Coord> e : nextPossibleSteps.get(fromDirection).entrySet()) {
            try {
                if (grid.get(currentPos[1]).get(currentPos[0]).equals(e.getKey()) && !isPreviousStep(e.getValue())) {
                    // System.out.println("going from " + grid.get(currentPos[1]).get(currentPos[0]) + " " + " to " + grid.get(currentPos[1] + e.getValue().y()).get(currentPos[0] + e.getValue().x()));
                    lastPos[0] = currentPos[0];
                    lastPos[1] = currentPos[1];
                    currentPos[0] = currentPos[0] + e.getValue().x();
                    currentPos[1] = currentPos[1] + e.getValue().y();
                    fromDirection = e.getValue().afterDirection();
                    return true;
                }
            } catch (IndexOutOfBoundsException ex) { continue; }
        }
        return false;
    }

    private static boolean isPreviousStep(Coord c) {
        return (lastPos[0] == currentPos[0] + c.x() && lastPos[1] == currentPos[1] + c.y());
    }
}
