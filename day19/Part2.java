import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/* again, rewrite of my crappy logic to be able solve part 2 */
public class Part2
{
    record WorkflowStep(char letter, char operator, int number, String next) {
        static WorkflowStep from(String s) {
            var parts = s.split("[<>]");
            if (parts.length == 1) {
                return new WorkflowStep(' ', ' ', 0, s);
            }
            String after = parts[parts.length-1].split(":")[1];
            char criteria = s.contains(">") ? '>' : '<';
            return new WorkflowStep(parts[0].charAt(0), criteria, Integer.parseInt(parts[1].split(":")[0]), after);
        }
    }

    record Condition(long min, long max) {
        Condition newMax(long newMax) {
            return new Condition(min, Math.min(this.max, newMax));
        }

        Condition newMin(long newMin) {
            return new Condition(Math.max(this.min, newMin), max);
        }

        long countValid() {
            return max-min-1;
        }
    };

    public static void main(String[] args) throws IOException
    {
        String[] content = Files.readString(Path.of("input.txt")).split("\n\n");

        Map<String, List<WorkflowStep>> workflows = content[0].lines()
            .map(line -> line.split("\\{"))
            .collect(Collectors.toMap(
                e -> e[0],
                e -> Arrays.stream(e[1].replace("}", "").split(",")).map(s -> WorkflowStep.from(s)).collect(Collectors.toList())
            ));

        Map<Character, Condition> conditions = new HashMap<>();
        for (char c : "xmas".toCharArray()) conditions.put(c, new Condition(0, 4001));

        long result = countAcceptedCombinations(workflows, conditions, "in");
        System.out.println(result);
    }

    private static long countAcceptedCombinations(Map<String, List<WorkflowStep>> workflows, Map<Character, Condition> conditions, String workflow) {
        long sum = 0;
        for (var step : workflows.get(workflow)) {
            if (step.letter() == ' ') {
                sum += checkNext(workflows, conditions, step);
                continue;
            }
            
            var newConditions = new HashMap<>(conditions);
            var condition = conditions.get(step.letter());
            if (step.operator() == '<') {
                newConditions.put(step.letter(), condition.newMax(step.number()));
                conditions.put(step.letter(), condition.newMin(step.number() - 1));
            } else {
                newConditions.put(step.letter(), condition.newMin(step.number()));
                conditions.put(step.letter(), condition.newMax(step.number() + 1));
            }
            sum += checkNext(workflows, newConditions, step);
        }
        return sum;
    }

    private static long checkNext(Map<String, List<WorkflowStep>> workflows, Map<Character, Condition> constraints, WorkflowStep step) {
        if (step.next().equals("R"))
            return 0;

        if (step.next().equals("A"))
            return constraints.values().stream().mapToLong(Condition::countValid).reduce(1, (product, c) -> product * c);

        return countAcceptedCombinations(workflows, constraints, step.next());
    }
}
