import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Part2
{
    record Coord(Long x, Long y) {}

    public static void main(String[] args) throws IOException
    {
        List<Coord> path = new ArrayList<>();
        path.add(new Coord(0L, 0L));

        long[] outline = {0};
        Files.readString(Path.of("input.txt")).lines().forEach(line -> {
            var txt = line.split(" ")[2];
            long steps = Long.parseLong(txt.substring(2, txt.length()-2), 16);
            int dir = Character.getNumericValue(txt.charAt(7));
            long previousX = path.getLast().x();
            long previousY = path.getLast().y();
            if (dir == 0) {
                path.add(new Coord(previousX + steps, previousY));
            } else if (dir == 2) {
                path.add(new Coord(previousX - steps, previousY));
            } else if (dir == 3) {
                path.add(new Coord(previousX, previousY - steps));
            } else if (dir == 1) {
                path.add(new Coord(previousX, previousY + steps));
            }
            outline[0] += steps;
        });

        long result = polygonArea(path, outline[0]);
        System.out.println(result);
    }

    /* modified Shoelace formula */
    private static long polygonArea(List<Coord> polygon, long outlineLength) {
        long area = 0;
     
        int n = polygon.size();
        int j = n-1;
        for (int i=0; i<n; i++) {
            area += (polygon.get(j).x() + polygon.get(i).x()) * (polygon.get(j).y() - polygon.get(i).y());
            j = i;
        }
     
        return ((Math.abs(area) + outlineLength) / 2) + 1;
    }
}
