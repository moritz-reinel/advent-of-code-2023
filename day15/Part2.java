import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.IntStream;

public class Part2
{
    record Lens(String label, int focalLength) {};

    private static int hash(String step) {
        return step.chars().reduce(0, (current, c) -> (current + c) * 17 % 256);
    }

    public static void main(String[] args) throws IOException
    {
        Map<Integer, List<Lens>> boxes = new HashMap<>();
        IntStream.range(0, 256).forEach(idx -> boxes.put(idx, new ArrayList<>()));
        
        Arrays.stream(Files.readString(Path.of("input.txt")).strip().split(",")).forEach(step -> {
            if (step.contains("-")) {
                String label = step.split("-")[0];
                boxes.get(hash(label)).removeIf(box -> box.label().equals(label));
                return;
            }

            // equals sign
            String label = step.split("=")[0];
            Lens newLens = new Lens(label, Integer.parseInt(step.split("=")[1]));
            var lensesInBox = boxes.get(hash(label));
            for (int idx=0; idx<lensesInBox.size(); idx++) {
                if (lensesInBox.get(idx).label().equals(label)) {
                    lensesInBox.set(idx, newLens);
                    return;
                }
            }
            boxes.get(hash(label)).add(newLens);
        });

        long sum = 0;
        for (int box=0; box<boxes.size(); box++) {
            for (int idx=0; idx<boxes.get(box).size(); idx++) {
                int power = (box+1) * (idx+1) * boxes.get(box).get(idx).focalLength();
                sum += power;
            }
        }

        System.out.println(sum);
    }
}
