import java.io.IOException;
import java.nio.file.*;

public class Part1
{
    public static void main(String[] args) throws IOException
    {
        String[] blocks = Files.readString(Path.of("input.txt")).split("\n\n");

        // Part 1
        System.out.println(calculateSum(blocks, false));

        // Part 2
        System.out.println(calculateSum(blocks, true));
    }

    private static long calculateSum(String[] blocks, boolean tolerance) {
        long result = 0;
        
        for (String block : blocks) {
            String[] grid = block.strip().split("\n");
            result += getAxisScore(grid, tolerance);
        }

        return result;
    }

    private static long getAxisScore(String[] lines, boolean tolerance) {
        long verticalPos = 0;
        for (int mirror = 1; mirror < lines[0].length(); mirror++) {
            int closestBorder = Math.min(mirror, lines[0].length() - mirror);
            int left = mirror-1, right = mirror;

            long count = 0;
            for (int diff = 0; diff < closestBorder; diff++) {
                for (String line : lines) {
                    if (line.charAt(left - diff) == line.charAt(right + diff)) count++;
                }
            }

            long maxCount = closestBorder * lines.length;
            if (!tolerance && count == maxCount) {
                verticalPos = mirror;
            } else if (tolerance && count == (maxCount - 1)) {
                verticalPos = mirror;
            }
        }

        long horizontalPos = 0;
        for (int mirror = 1; mirror < lines.length; mirror++) {
            int closestBorder = Math.min(mirror, lines.length - mirror);
            int up = mirror-1, down = mirror;

            long count = 0;
            for (int diff = 0; diff < closestBorder; diff++) {
                String lineUp = lines[up - diff], lineDown = lines[down + diff];

                for (int x = 0; x < lineUp.length(); x++) {
                    if (lineUp.charAt(x) == lineDown.charAt(x)) count++;
                }
            }

            long maxCount = closestBorder * lines[0].length();
            if (!tolerance && count == maxCount) {
                horizontalPos = mirror;
            } else if (tolerance && count == (maxCount - 1)) {
                horizontalPos = mirror;
            }
        }

        return verticalPos + horizontalPos * 100;
    }
}
