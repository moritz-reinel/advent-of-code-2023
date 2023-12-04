import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Part2
{
    private static List<Integer> winsPerCard = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException
    {
        Scanner scanner = new Scanner(new File("input.txt"));

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().split(": ")[1];

            List<Integer> winNumbers = Arrays.stream(line.split(" \\| ")[0].split(" "))
                .filter(v -> !"".equals(v))
                .map(v -> Integer.valueOf(v))
                .collect(Collectors.toList());

            int haveWins = (int) (long) Arrays.stream(line.split(" \\| ")[1].split(" "))
                .filter(v -> !"".equals(v))
                .map(v -> Integer.valueOf(v))
                .filter(v -> winNumbers.contains(Integer.valueOf(v)))
                .collect(Collectors.counting());
            
            winsPerCard.add(haveWins);
        }

        Stack<Integer> todo = new Stack<>();
        for (int i=0; i<winsPerCard.size(); i++) { todo.push(i); }

        int sum = 0;

        while (!todo.isEmpty()) {
            sum += 1;

            int cardIdx = todo.pop();
            int winsAtCard = winsPerCard.get(cardIdx);
            for (int i = cardIdx+1; i < cardIdx+1+winsAtCard; i++) { todo.push(i); }
        }

        System.out.println(sum);

        scanner.close();
    }
}
