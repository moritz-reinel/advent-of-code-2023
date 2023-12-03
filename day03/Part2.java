import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Part2
{
    private static List<char[]> chars = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException
    {
        Scanner scanner = new Scanner(new File("input.txt"));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            chars.add(line.toCharArray());
        }

        int sum = 0;
        
        for (int r = 0; r < chars.size(); r++) {
            for (int c = 0; c < chars.get(r).length; c++) {
                char x = chars.get(r)[c];

                if (x == '*') {
                    List<Integer> nums = getSurroundingNumbers(r, c);
                    if (nums.size() == 2) {
                        sum += nums.get(0) * nums.get(1);
                    }
                }
            }
        }

        System.out.println(sum);

        scanner.close();
    }

    private static List<Integer> getSurroundingNumbers(int r, int c) {
        List<Integer> out = new ArrayList<>();

        Pattern leftNum = Pattern.compile("(\\d+)$");
        Pattern rightNum = Pattern.compile("^(\\d+)");
        Pattern rightNumWithLeadingDot = Pattern.compile("^\\.?(\\d+)");
        
        String left = new String(chars.get(r)).substring(0, c);
        Matcher m1 = leftNum.matcher(left);
        if (m1.find()) {
            out.add(Integer.valueOf(m1.group()));
        }

        String right = new String(chars.get(r)).substring(c+1);
        Matcher m2 = rightNum.matcher(right);
        if (m2.find()) {
            out.add(Integer.valueOf(m2.group()));
        }

        String above = "";
        try {
            String topLeft = new String(chars.get(r-1)).substring(0, c);
            Matcher m3 = leftNum.matcher(topLeft);
            if (m3.find()) {
                above += m3.group();
            }
        } catch (IndexOutOfBoundsException e) {}
        try {
            String topRight = new String(chars.get(r-1)).substring(c);
            Matcher m4 = rightNumWithLeadingDot.matcher(topRight);
            if (m4.find()) {
                above += m4.group();
            }
        } catch (IndexOutOfBoundsException e) {}

        Arrays.stream(above.split("\\.")).forEach(p -> {
            if (!"".equals(p)) {
                out.add(Integer.valueOf(p));
            }
        });

        String below = "";
        try {
            String topLeft = new String(chars.get(r+1)).substring(0, c);
            Matcher m3 = leftNum.matcher(topLeft);
            if (m3.find()) {
                below += m3.group();
            }
        } catch (IndexOutOfBoundsException e) {}
        try {
            String topRight = new String(chars.get(r+1)).substring(c);
            Matcher m4 = rightNumWithLeadingDot.matcher(topRight);
            if (m4.find()) {
                below += m4.group();
            }
        } catch (IndexOutOfBoundsException e) {}

        Arrays.stream(below.split("\\.")).forEach(p -> {
            if (!"".equals(p)) {
                out.add(Integer.valueOf(p));
            }
        });

        return out;
    }
}
