import java.util.*;

public class LagrangeInterpolation
{
    static long lagrangeInterpolation(Map<Integer, Integer> values, int targetX)
    {
        long result = 0;

        for (var entry : values.entrySet()) {
            long term = entry.getValue();
            for (var otherEntry : values.entrySet()) {
                if (!entry.equals(otherEntry)) {
                    term *= (targetX - otherEntry.getKey()) / (entry.getKey() - otherEntry.getKey());
                }
            }
            result += term;
        }

        return result;
    }
}
