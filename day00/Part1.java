import java.io.*;
import java.util.*;

public class Part1
{
    public static void main(String[] args)
    {
        try (Scanner scanner = new Scanner(new File("input_test.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private record Data (){

    }
}
