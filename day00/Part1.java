import java.io.*;
import java.util.*;

public class Part1
{
    public static void main(String[] args) throws FileNotFoundException
    {
        Scanner scanner = new Scanner(new File("input_test.txt"));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
        }

        scanner.close();
    }

    private record Data (){

    }
}
