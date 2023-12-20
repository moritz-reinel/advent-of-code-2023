import java.util.*;

public class MyMath
{
    public static long lcm(List<Long> numbers)
    {
        Set<Long> primeFactors = new HashSet<>();

        for (long number : numbers) {
            for (long i = 2; i <= number; i++) {
                if (number % i == 0) {
                    primeFactors.add(i);
                    number /= i;
                }
            }
        }

        return primeFactors.stream().reduce(1L, (lcm, x) -> (lcm * x));
    }    
}
