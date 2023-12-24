import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Part1
{
    record Hailstone(long x, long y, long z, long vx, long vy, long vz, double slope, double t) {}

    private final static long[] CHECK_AREA = {200_000_000_000_000L, 400_000_000_000_000L};

    public static void main(String[] args) throws IOException
    {
        List<Hailstone> hailstones = Files.readString(Path.of("input.txt")).lines()
            .map(line -> {
                var nums = Arrays.stream(line.replace(" @ ", ", ").replaceAll("\\s+", " ").split(", ")).map(s -> Long.parseLong(s)).toList();

                double slope = (double) nums.get(4) / nums.get(3);
                double intercept = nums.get(1) - slope * nums.get(0);

                return new Hailstone(nums.get(0), nums.get(1), nums.get(2), nums.get(3), nums.get(4), nums.get(5), slope, intercept);
            })
            .toList();

        int matches = 0;

        // generate combinations: 2 out of n
        int n = hailstones.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                var h1 = hailstones.get(i);
                var h2 = hailstones.get(j);

                // parallel
                if (h1.slope() == h2.slope()) continue;

                double ix = (h2.t() - h1.t()) / (h1.slope() - h2.slope());
                double iy = h1.slope() * ix + h1.t();
                double t1 = (ix - h1.x()) / h1.vx();
                double t2 = (ix - h2.x()) / h2.vx();

                // behind starting points
                if (t1 < 0 || t2 < 0) continue;

                if (CHECK_AREA[0] <= ix && ix <= CHECK_AREA[1] && CHECK_AREA[0] <= iy && iy <= CHECK_AREA[1])
                    matches++;
            }
        }

        System.out.println(matches);
    }
}
