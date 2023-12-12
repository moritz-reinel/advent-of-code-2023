import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/* complete logic refactor, as my initial logic was way too inefficient */
public class Part2
{
    public static void main(String[] args) throws IOException
    {
        String[] lines = Files.readString(Path.of("input.txt")).split("\n");

        long result = 0;
        for (var line : lines) {
            var groups = Collections.nCopies(5, Arrays.stream(line.split(" ")[1]
                    .split(","))
                    .map(n -> Integer.parseInt(n))
                    .collect(Collectors.toList()))
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

            // add a trailing dot so that nothing at the end matches falsely
            String springs = (line.split(" ")[0] + "?").repeat(5).replaceAll("\\?$", "") + ".";

            result += countCombinations(springs, groups, 0, 0, 0, new HashMap<String, Long>());
        }
    
        System.out.println(result);
    }

    private static long countCombinations(String springs, List<Integer> groups, int pos, int insideGroupPos, int groupCount, Map<String, Long> cache) {
        String cacheResultKey = "%d-%d-%d".formatted(pos, insideGroupPos, groupCount);

        if (cache.containsKey(cacheResultKey)) {
            return cache.get(cacheResultKey);
        }

        long result = 0;
        // at the end and no. of found groups equals no. of given groups -> result is 1 else 0
        if (pos == springs.length()) {
            result = (groups.size() == groupCount) ? 1 : 0;
        }
        // at # -> move pos cursor and insideGroupCursor +1
        else if (springs.charAt(pos) == '#') {
            result = countCombinations(springs, groups, pos + 1, insideGroupPos + 1, groupCount, cache);
        }
        // at . or number of given groups reached
        else if (springs.charAt(pos) == '.' || groupCount == groups.size()) {
            // previous step was not inside a group -> move on
            if (insideGroupPos == 0) {
                result = countCombinations(springs, groups, pos + 1, 0, groupCount, cache);
            }
            // not all groups found and previous group length matches the given one -> continue
            else if (groupCount < groups.size() && insideGroupPos == groups.get(groupCount)) {
                result = countCombinations(springs, groups, pos + 1, 0, groupCount + 1, cache);
            }
            // no more matches possible -> 0 possibilities
            else {
                result = 0;
            }
        }
        // at ? -> calculate combinations for # and .
        else {
            long matchesWithHash = countCombinations(springs, groups, pos + 1, insideGroupPos + 1, groupCount, cache);
            
            long matchesWithDot = 0;
            if (insideGroupPos == 0) {
                matchesWithDot = countCombinations(springs, groups, pos + 1, 0, groupCount, cache);
            } else if (insideGroupPos == groups.get(groupCount)) {
                matchesWithDot = countCombinations(springs, groups, pos + 1, 0, groupCount + 1, cache);
            }
            
            result = matchesWithHash + matchesWithDot;
        }

        cache.put(cacheResultKey, result);
        return result;
    }
}
