import java.io.*;
import java.util.*;

public class Part2
{
    private static String[] digitsAsWord = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};

    public static void main(String[] args) throws FileNotFoundException
    {
        Scanner scanner = new Scanner(new File("input.txt"));
        int result = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            int one = Integer.MIN_VALUE, two = 0;
            int idx;

            for (idx=0; idx<line.length(); idx++) {
                if (Character.isDigit(line.charAt(idx))) {
                    if (one == Integer.MIN_VALUE) {
                        one = line.charAt(idx) - '0';
                        two = one;
                    } else {
                        two = line.charAt(idx) - '0';
                    }
                } else {
                    String fromI = line.substring(idx);

                    for (int d=0; d<digitsAsWord.length; d++) {
                        if (fromI.startsWith(digitsAsWord[d])) {
                            if (one == Integer.MIN_VALUE) {
                                one = d;
                                two = one;
                            } else {
                                two = d;
                            }
                        }
                    }
                }
            }

            result += one * 10 + two;
        }
        
        System.out.println(result);
        scanner.close();
    }
}
