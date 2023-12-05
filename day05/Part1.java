import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part1
{
    public static void main(String[] args) throws IOException
    {
        /* I moved the seed numers to a newline because parsing drove me nuts */
        String content = Files.readString(Path.of("input.txt")).strip();
        List<List<List<Long>>> sections = Arrays.stream(content.split("\n\n"))
                .map(section -> Arrays.stream(section.strip().split("\n"))
                        .filter(line -> !line.trim().endsWith(":"))
                        .map(line -> Arrays.stream(line.strip().split("\\s"))
                                .map(Long::parseLong)
                                .collect(Collectors.toList()))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        long min = Long.MAX_VALUE;
        for (long seed : sections.get(0).get(0)) {
            long loc = seed;
            for (int i=1; i<sections.size(); i++) {
                for (List<Long> ln : sections.get(i)) {
                    if (loc >= ln.get(1) && loc <= ln.get(1)+ln.get(2)-1) {
                        loc = loc - ln.get(1) + ln.get(0);
                        break;
                    }
                }
            }

            min = Math.min(loc, min);
        }

        System.out.println(min);
    }
}
