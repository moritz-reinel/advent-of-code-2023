import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part1
{
    public static void main(String[] args) throws FileNotFoundException
    {
        Scanner scanner = new Scanner(new File("input.txt"));

        int sum = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().split(": ")[1];

            List<Integer> wins = Arrays.stream(line.split(" \\| ")[0].split(" "))
                .filter(v -> !"".equals(v))
                .map(v -> Integer.valueOf(v))
                .collect(Collectors.toList());

            long matches = Arrays.stream(line.split(" \\| ")[1].split(" "))
                .filter(v -> !"".equals(v))
                .filter(v -> wins.contains(Integer.valueOf(v)))
                .collect(Collectors.counting());

            if (matches > 0) {
                sum += Math.pow(2, matches-1);
            }
        }

        System.out.println(sum);

        scanner.close();
    }
}
