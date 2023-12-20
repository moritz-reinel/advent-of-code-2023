import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry;

public class Part2
{
    enum Type {BROADCAST, CONJUNCTION, NORMAL};

    static class FlipFlop {
        Type type = Type.NORMAL;
        List<String> before = null;
        String name;
        List<String> after;
        boolean on = false;
        boolean lastSent = false;

        @Override
        public String toString() {
            return "{%s,%s,%s,%s}".formatted(name, type, on, lastSent);
        }

        FlipFlop (String s) {
            var leftRight = s.split(" -> ");
            if (leftRight[0].startsWith("%")) {
                name = leftRight[0].substring(1);
                type = Type.NORMAL;
            } else if (leftRight[0].startsWith("&")) {
                name = leftRight[0].substring(1);
                type = Type.CONJUNCTION;
                before = new ArrayList<>();
            } else {
                name = leftRight[0];
                type = Type.BROADCAST;
            }
            after = Arrays.asList(leftRight[1].split(", "));
        }

        void addBefore(String s) {
            before.add(s);
        }

        List<Entry<String, Boolean>> sendPulse(boolean high) {
            List<Entry<String, Boolean>> updates = new ArrayList<>();
            switch (type) {
                case BROADCAST -> {
                    for (var a : after) {
                        updates.add(Map.entry(a, high));
                    }
                    lastSent = high;
                }
                case CONJUNCTION -> {
                    // before all high pulses
                    if (before.stream().map(b -> ffs.get(b).lastSent).allMatch(sent -> (sent == true))) {
                        for (var a : after) {
                            updates.add(Map.entry(a, false));
                        }
                        lastSent = false;
                    } else {
                        for (var a : after) {
                            updates.add(Map.entry(a, true));
                        }
                        lastSent = true;
                    }
                }
                case NORMAL -> {
                    if (!high) {
                        on = !on;
                        if (on) {
                            for (var a : after) {
                                updates.add(Map.entry(a, true));
                            }
                            lastSent = true;
                        } else {
                            for (var a : after) {
                                updates.add(Map.entry(a, false));
                            }
                            lastSent = false;
                        }
                    }
                }
            }
            return updates;
        }
    }

    private static Map<String, FlipFlop> ffs = new HashMap<>();

    public static void main(String[] args) throws IOException
    {
        Files.readString(Path.of("input.txt")).lines().forEach(line -> {
            var f = new FlipFlop(line);
            ffs.put(f.name, f);
        });

        // manually add the "finish" flip-flop
        ffs.put("rx", new FlipFlop("&rx -> , "));

        // add before flip-flops to each conjunction
        ffs.values().stream().filter(ff -> (ff.type == Type.CONJUNCTION)).forEach(ff -> {
            ffs.values().stream().forEach(f -> {
                if (f.after.contains(ff.name)) {
                    ff.addBefore(f.name);
                }
            });
        });

        List<Entry<String, Boolean>> updates = new ArrayList<>();
        List<Entry<String, Boolean>> tmp     = new ArrayList<>();

        Map<String, List<Integer>> endJunctionOccurences = ffs.get(ffs.get("rx").before.getFirst()).before.stream().collect(Collectors.toMap(entry -> entry, entry -> new ArrayList<>()));

        boolean firstCycleForEveryEndJunction = false;
        for (int buttonCount=0; !firstCycleForEveryEndJunction; buttonCount++) {
            final int currentHumanCount = buttonCount + 1;
            updates.add(Map.entry("broadcaster", false));
            while (!updates.isEmpty()) {
                tmp.clear();
                for (var u : updates) {
                    // end reached
                    if (!ffs.containsKey(u.getKey())) continue;

                    var newUpdates = ffs.get(u.getKey()).sendPulse(u.getValue());
                    tmp.addAll(newUpdates);

                    // look, if pulse sent to one of the "end junctions"
                    endJunctionOccurences.entrySet().stream().forEach(entry -> {
                        tmp.stream().forEach(e -> {
                            if (entry.getKey().equals(e.getKey()) && e.getValue() == false) {
                                entry.getValue().add(currentHumanCount);
                            }
                        });
                    });
                }

                updates = new ArrayList<>(tmp);
                firstCycleForEveryEndJunction = !endJunctionOccurences.values().stream().anyMatch(list -> list.size() == 0);
            }
        }

        var firstOccurencesOfEndJunctions = endJunctionOccurences.values().stream().mapToLong(list -> list.getFirst()).boxed().collect(Collectors.toList());

        System.out.println(MyMath.lcm(firstOccurencesOfEndJunctions));
    }
}
