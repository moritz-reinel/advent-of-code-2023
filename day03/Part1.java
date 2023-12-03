import java.io.*;
import java.util.*;

public class Part1
{
    public static void main(String[] args) throws FileNotFoundException
    {
        Scanner scanner = new Scanner(new File("input.txt"));
        List<char[]> chars = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            chars.add(line.toCharArray());
        }

        int sum = 0;
        int number = 0;
        boolean isAtSymbol = false;
        
        for (int r = 0; r < chars.size(); r++) {
            for (int c = 0; c < chars.get(r).length; c++) {
                char x = chars.get(r)[c];

                if (!Character.isDigit(x)) {
                    if (isAtSymbol) {
                        sum += number;
                        isAtSymbol = false;
                    }
                    number = 0;
                    continue;
                }

                if (Character.isDigit(chars.get(r)[c])) {
                    if (number != 0) {
                        number *= 10;
                    }
                    number += x - '0';

                    char left, right, top, bottom, topLeft, topRight, bottomLeft, bottomRight;
                    try {
                        left = chars.get(r)[c-1];
                    } catch (IndexOutOfBoundsException e) {
                        left = '.';
                    }
                    try {
                        right = chars.get(r)[c+1];
                    } catch (IndexOutOfBoundsException e) {
                        right = '.';
                    }
                    try {
                        top = chars.get(r-1)[c];
                    } catch (IndexOutOfBoundsException e) {
                        top = '.';
                    }
                    try {
                        bottom = chars.get(r+1)[c];
                    } catch (IndexOutOfBoundsException e) {
                        bottom = '.';
                    }
                    try {
                        topLeft = chars.get(r-1)[c-1];
                    } catch (IndexOutOfBoundsException e) {
                        topLeft = '.';
                    }
                    try {
                        topRight = chars.get(r-1)[c+1];
                    } catch (IndexOutOfBoundsException e) {
                        topRight = '.';
                    }
                    try {
                        bottomLeft = chars.get(r+1)[c-1];
                    } catch (IndexOutOfBoundsException e) {
                        bottomLeft = '.';
                    }
                    try {
                        bottomRight = chars.get(r+1)[c+1];
                    } catch (IndexOutOfBoundsException e) {
                        bottomRight = '.';
                    }

                    for (char check : new char[]{left, right, top, bottom, topLeft, topRight, bottomLeft, bottomRight}) {
                        if ((!Character.isDigit(check)) && check != '.') {
                            isAtSymbol = true;
                            break;
                        }
                    }
                }
            }
        }

        System.out.println(sum);

        scanner.close();
    }
}
