import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Part1
{
    public static void main(String[] args) throws IOException
    {
        int result = Arrays.stream(Files.readString(Path.of("input.txt")).strip().split(","))
                        .map(step -> step.chars().reduce(0, (current, c) -> (current + c) * 17 % 256))
                        .reduce(0, (sum, hash) -> (sum + hash));

        System.out.println(result);
    }
}
