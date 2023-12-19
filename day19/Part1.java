import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class Part1
{
    public static void main(String[] args) throws IOException
    {
        String[] content = Files.readString(Path.of("input.txt")).split("\n\n");

        Map<String, List<String>> workflows = new HashMap<>();
        content[0].lines().forEach(line -> {
            String[] tmp = line.split("\\{");

            workflows.put(tmp[0], Arrays.asList(tmp[1].replace("}", "").split(",")));
        });

        Pattern regex = Pattern.compile("=(\\d+)");
        int result = content[1].lines().map(line -> {
            Matcher matcher = regex.matcher(line);
            matcher.find();
            int x = Integer.parseInt(matcher.group(1));
            matcher.find();
            int m = Integer.parseInt(matcher.group(1));
            matcher.find();
            int a = Integer.parseInt(matcher.group(1));
            matcher.find();
            int s = Integer.parseInt(matcher.group(1));

            String key = "in";
            int sum;
            outer:
            while (true) {
                for (String order : workflows.get(key)) {
                    if (order.equals("A")) {
                        sum = x + m + a + s;
                        break outer;
                    } else if (order.equals("R")) {
                        sum = 0;
                        break outer;
                    } else {
                        var o = order.split("[<>]");
                        if (o.length == 1) {
                            key = order;
                            break;
                        }
                        var res = o[1].split(":");
                        int num = 0;
                        if (o[0].equals("x")) {
                            num = x;
                        } else if (o[0].equals("m")) {
                            num = m;
                        } else if (o[0].equals("a")) {
                            num = a;
                        } else if (o[0].equals("s")) {
                            num = s;
                        }

                        if (order.contains(">")) {
                            if (num > Integer.parseInt(res[0])) {
                                if (res[1].equals("A")) {
                                    sum = x + m + a + s;
                                    break outer;
                                } else if (res[1].equals("R")) {
                                    sum = 0;
                                    break outer;
                                } else {
                                    key = res[1];
                                    break;
                                }
                            } else {
                                continue;
                            }
                        } else {
                            if (num < Integer.parseInt(res[0])) {
                                if (res[1].equals("A")) {
                                    sum = x + m + a + s;
                                    break outer;
                                } else if (res[1].equals("R")) {
                                    sum = 0;
                                    break outer;
                                } else {
                                    key = res[1];
                                    break;
                                }
                            } else {
                                continue;
                            }
                        }
                    }
                }
            }
            return sum;
        })
        .reduce((res, x) -> (res + x))
        .orElse(0);

        System.out.println(result);
    }
}
