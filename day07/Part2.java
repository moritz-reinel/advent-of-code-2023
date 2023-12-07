import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Part2 implements Comparable<Part2>
{
    enum HandType {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIR,
        THREE_OF_KIND,
        FULL_HOUSE,
        FOUR_OF_KIND,
        FIVE_OF_KIND
    };

    // move J to front so it is always treated equally
    private static List<String> customCardOrder = Arrays.asList("J", "2", "3", "4", "5", "6", "7", "8", "9", "T", "Q", "K", "A");

    private String itemName;
    private HandType handType;

    Part2(String itemName, HandType handType) {
        this.itemName = itemName;
        this.handType = handType;
    }

    String getItemName() {
        return itemName;
    }

    HandType getHandType() {
        return handType;
    }

    @Override
    public int compareTo(Part2 other) {
        int enumComparison = Integer.compare(this.handType.ordinal(), other.handType.ordinal());
        if (enumComparison != 0) {
            return enumComparison;
        }

        for (int i=0; i<this.itemName.length(); i++) {
            int comp = Integer.compare(customCardOrder.indexOf(this.itemName.substring(i, i+1)), customCardOrder.indexOf(other.itemName.substring(i, i+1)));
            if (comp != 0) return comp;
        }

        return 0;
    }


    public static void main(String[] args) throws IOException
    {
        Map<String, HandType> handTypes = new HashMap<>();
        Map<String, Integer> bids = new HashMap<>();
        List<Part2> test = new LinkedList<>();

        Arrays.stream(Files.readString(Path.of("input.txt")).strip().split("\n"))
            .forEach(line -> {
                String[] cols = line.split(" ");
                handTypes.put(cols[0], getMaxHandType(cols[0]));
                bids.put(cols[0], Integer.valueOf(cols[1]));
                test.add(new Part2(cols[0], getMaxHandType(cols[0])));
            });

        Collections.sort(test);

        long sum = 0;
        for (int i=0; i<test.size(); i++) {
            int bid = bids.get(test.get(i).getItemName());
            // System.out.println("%d: %s %s, %d".formatted(i+1, test.get(i).getItemName(), handTypes.get(test.get(i).getItemName()), bid));
            sum += ((i+1) * bid);
        }

        System.out.println(sum);
    }

    private static HandType getMaxHandType(String str) {
        HandType h = getHandType(str);
        for (String s : generateCombinations(str)) {
            if (getHandType(s).ordinal() > h.ordinal()) h = getHandType(s);
        }
        return h;
    }

    public static List<String> generateCombinations(String input) {
        List<String> combinations = new ArrayList<>();
        generateCombinationsRec(input.toCharArray(), 0, combinations);
        return combinations;
    }

    private static void generateCombinationsRec(char[] chars, int index, List<String> combinations) {
        if (index == chars.length) {
            combinations.add(new String(chars));
            return;
        }

        char currentChar = chars[index];

        if (chars[index] == 'J') {
            for (String c : customCardOrder) {
                chars[index] = c.charAt(0);
                generateCombinationsRec(chars, index + 1, combinations);    
            }
        } else {
            generateCombinationsRec(chars, index + 1, combinations);
        }

        chars[index] = currentChar;
    }

    private static HandType getHandType(String str) {
        Map<Character, Integer> charCount = new HashMap<>();

        for (char ch : str.toCharArray()) {
            charCount.put(ch, charCount.getOrDefault(ch, 0) + 1);
        }

        if (charCount.size() == 1) return HandType.FIVE_OF_KIND;
        if (charCount.containsValue(4)) return HandType.FOUR_OF_KIND;
        if (charCount.containsValue(3) && charCount.size() == 2) return HandType.FULL_HOUSE;
        if (charCount.containsValue(3) && charCount.size() == 3) return HandType.THREE_OF_KIND;
        if (charCount.containsValue(2) && charCount.size() == 4) return HandType.ONE_PAIR;
        if (charCount.size() == 5) return HandType.HIGH_CARD;
        return HandType.TWO_PAIR;
    }
}
