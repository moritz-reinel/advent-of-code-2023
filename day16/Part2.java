import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part2
{
    enum Direction {TOP, RIGHT, BOTTOM, LEFT};

    record Coord(int x, int y, Direction dir) {
        Coord continueStraight(Direction d) {
            return switch (d) {
                case BOTTOM -> new Coord(x, y-1, dir);
                case LEFT   -> new Coord(x+1, y, dir);
                case RIGHT  -> new Coord(x-1, y, dir);
                case TOP    -> new Coord(x, y+1, dir);
            };
        }
    };

    private static List<List<Character>> grid;
    private static Set<Coord> visited = new HashSet<>();

    public static void main(String[] args) throws IOException
    {
        grid = Files.lines(Paths.get("input.txt"))
            .map(line -> line.chars().mapToObj(c -> (char) c).collect(Collectors.toList()))
            .collect(Collectors.toList());

        long res = 0;

        for (int x=0; x<grid.getFirst().size(); x++) {
            // simulate all TOP beams
            Coord current = new Coord(x, 0, Direction.TOP);
            visit(current);
            long tmpRes = visited.stream().map(coord -> new Coord(coord.x(), coord.y(), null)).collect(Collectors.toSet()).size();
            res = Math.max(res, tmpRes);
            visited.clear();

            // simulate all BOTTOM beams
            current = new Coord(x, grid.size()-1, Direction.BOTTOM);
            visit(current);
            tmpRes = visited.stream().map(coord -> new Coord(coord.x(), coord.y(), null)).collect(Collectors.toSet()).size();
            res = Math.max(res, tmpRes);
            visited.clear();
        }

        for (int y=0; y<grid.size(); y++) {
            // simulate all LEFT beams
            Coord current = new Coord(0, y, Direction.LEFT);
            visit(current);
            long tmpRes = visited.stream().map(coord -> new Coord(coord.x(), coord.y(), null)).collect(Collectors.toSet()).size();
            res = Math.max(res, tmpRes);
            visited.clear();

            // simulate all RIGHT beams
            current = new Coord(grid.getFirst().size()-1, y, Direction.BOTTOM);
            visit(current);
            tmpRes = visited.stream().map(coord -> new Coord(coord.x(), coord.y(), null)).collect(Collectors.toSet()).size();
            res = Math.max(res, tmpRes);
            visited.clear();
        }

        System.out.println(res);
    }

    private static void visit(Coord co) {
        if (visited.contains(co) || co.x() < 0 || co.x() >= grid.getFirst().size() || co.y() < 0 || co.y() >= grid.size()) {
            return;
        }
        
        visited.add(co);

        char c = grid.get(co.y()).get(co.x());
        if (c == '.') {
            visit(co.continueStraight(co.dir()));
            return;
        } else if (c == '|') {
            switch (co.dir()) {
                case BOTTOM, TOP -> visit(co.continueStraight(co.dir()));
                case LEFT, RIGHT -> {
                    visit(new Coord(co.x(), co.y()-1, Direction.BOTTOM));
                    visit(new Coord(co.x(), co.y()+1, Direction.TOP));
                }
            }
            return;
        } else if (c == '-') {
            switch (co.dir()) {
                case BOTTOM, TOP -> {
                    visit(new Coord(co.x()+1, co.y(), Direction.LEFT));
                    visit(new Coord(co.x()-1, co.y(), Direction.RIGHT));
                }
                case LEFT, RIGHT -> visit(co.continueStraight(co.dir()));
            }
            return;
        } else if (c == '\\') {
            switch (co.dir()) {
                case BOTTOM -> visit(new Coord(co.x()-1, co.y(), Direction.RIGHT));
                case LEFT   -> visit(new Coord(co.x(), co.y()+1, Direction.TOP));
                case RIGHT  -> visit(new Coord(co.x(), co.y()-1, Direction.BOTTOM));
                case TOP    -> visit(new Coord(co.x()+1, co.y(), Direction.LEFT));
            }
            return;
        } else if (c == '/') {
            switch (co.dir()) {
                case BOTTOM -> visit(new Coord(co.x()+1, co.y(), Direction.LEFT));
                case LEFT   -> visit(new Coord(co.x(), co.y()-1, Direction.BOTTOM));
                case RIGHT  -> visit(new Coord(co.x(), co.y()+1, Direction.TOP));
                case TOP    -> visit(new Coord(co.x()-1, co.y(), Direction.RIGHT));
            }
            return;
        }
    }
}
