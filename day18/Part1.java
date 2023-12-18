import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Part1
{
    record Coord(Integer x, Integer y) {}

    public static void main(String[] args) throws IOException
    {
        List<Coord> path = new ArrayList<>();
        path.add(new Coord(0, 0));

        int[] outline = {0};
        Files.readString(Path.of("input.txt")).lines().forEach(line -> {
            var txt = line.split(" ");
            int steps = Integer.parseInt(txt[1]);
            char dir = txt[0].charAt(0);
            int previousX = path.getLast().x();
            int previousY = path.getLast().y();
            if (dir == 'R') {
                    path.add(new Coord(previousX + steps, previousY));
            } else if (dir == 'L') {
                    path.add(new Coord(previousX - steps, previousY));
            } else if (dir == 'U') {
                    path.add(new Coord(previousX, previousY - steps));
            } else if (dir == 'D') {
                    path.add(new Coord(previousX, previousY + steps));
            }
            outline[0] += steps;
        });

        int result = polygonArea(path, outline[0]);
        System.out.println(result);
    }

    /* modified Shoelace formula */
    private static int polygonArea(List<Coord> polygon, int outlineLength) {
        int area = 0;
     
        int n = polygon.size();
        int j = n-1;
        for (int i=0; i<n; i++) {
            area += (polygon.get(j).x() + polygon.get(i).x()) * (polygon.get(j).y() - polygon.get(i).y());
            j = i;
        }
     
        return ((Math.abs(area) + outlineLength) / 2) + 1;
    }
}
