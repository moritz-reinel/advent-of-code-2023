import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Part1
{
    record Brick(int id, int x1, int x2, int y1, int y2, int z1, int z2, List<Integer> supporters) implements Comparable<Brick> {
        static Brick from(String line, int id) {
            var nums = line.replace("~", ",").split(",");
            int x1 = Integer.parseInt(nums[0]);
            int y1 = Integer.parseInt(nums[1]);
            int z1 = Integer.parseInt(nums[2]);
            int x2 = Integer.parseInt(nums[3]);
            int y2 = Integer.parseInt(nums[4]);
            int z2 = Integer.parseInt(nums[5]);

            return new Brick(id, x1, x2, y1, y2, z1, z2, new ArrayList<>());
        }

        @Override
        public String toString() {
            return "[%d] %d,%d,%d~%d,%d,%d".formatted(id, x1, y1, z1, x2, y2, z2);
        }

        @Override
        public int compareTo(Brick o) {
            return Integer.compare(this.z1, o.z1);
        }

        boolean supports(Brick other) {
            if (other.z1 != (this.z2 + 1)) return false;
            if (this.x2 < other.x1 || other.x2 < this.x1) return false;
            if (this.y2 < other.y1 || other.y2 < this.y1) return false;
            return true;
        }

        Brick dropByOne() {
            return new Brick(id, x1, x2, y1, y2, z1-1, z2-1, supporters);
        }
    }

    public static void main(String[] args) throws IOException
    {
        var lines = Files.readString(Path.of("input.txt")).split("\n");
        var bricks = IntStream.range(0, lines.length).mapToObj(i -> Brick.from(lines[i], i)).collect(Collectors.toList());

        dropBricks(bricks);

        computeSupporters(bricks);

        Set<Integer> singleSupporters = new HashSet<>();
        for (var b : bricks) {
            if (b.supporters().size() == 1) {
                singleSupporters.add(b.supporters().getFirst());
            }
        }

        System.out.println(bricks.size() - singleSupporters.size());
    }

    private static void dropBricks(List<Brick> bricks) {
        boolean moved = true;
        while (moved) {
            moved = false;
            bricks.sort(Brick::compareTo);
            
            for (int moveIdx=0; moveIdx<bricks.size(); moveIdx++) {
                var testMoving = bricks.get(moveIdx);
                if (testMoving.z1 == 1) continue;
                
                boolean canMove = true;
                for (int supportIdx=moveIdx; supportIdx>=0; supportIdx--) {
                    if (supportIdx == moveIdx) continue;

                    if (bricks.get(supportIdx).supports(testMoving)) {
                        canMove = false;
                        break;
                    }
                }
                if (canMove) {
                    bricks.set(moveIdx, testMoving.dropByOne());
                    moved = true;
                }
            }
        }
    }

    private static void computeSupporters(List<Brick> bricks) {
        for (var b : bricks) {
            for (var o : bricks) {
                if (o.supports(b)) {
                    b.supporters().add(o.id());
                }
            }
        }
    }
}
