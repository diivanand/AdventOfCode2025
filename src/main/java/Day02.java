import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Day02 {

    public static void main(String[] args) {
        try {
            // Assuming file input fits in memory and input format is valid so skipping validation of input.
            final String input = Files.readString(Paths.get("src/main/resources/day2/input.txt"));
            final List<Range> ranges = parseInput(input);

            // Part 1 Brute Force
            final Instant start = Instant.now();
            final BigInteger sumOfInvalidIds = computeSumOfInvalidIdsPart1BruteForce(ranges);
            final Duration timeTaken = Duration.between(start, Instant.now());
            System.out.println("Part 1 Brute Force: sumOfInvalidIds=" + sumOfInvalidIds); // Answer: 54641809925
            System.out.println("Part 1 Brute Force: timeTaken=" + timeTaken.toMillis() + " ms");

            System.out.println();

            // Part 2 Brute Force
            final Instant start2 = Instant.now();
            final BigInteger sumOfInvalidIds2 = computeSumOfInvalidIdsPart2BruteForce(ranges);
            final Duration timeTaken2 = Duration.between(start2, Instant.now());
            System.out.println("Part 2 Brute Force: sumOfInvalidIds2=" + sumOfInvalidIds2); // Answer:
            System.out.println("Part 2 Brute Force: timeTaken=" + timeTaken2.toMillis() + " ms");

        } catch (Exception e) {
            System.err.println("Error reading input file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // There is prob a smarter way by sorting the ranges by start, and merging ranges to create distinct largest possible non-overlapping ranges
    // And then from that you can generate number X such that the number XX (X concatenated with itself) would be invalid and can
    // binary search the non-overlapping ranges to see if that number is in one of the ranges to be counted.
    // The generation of such numbers X can probably be done in some smart nerd way starting from
    // smallest number of digits and working upwards but I don't have time to try to think of how that would work.
    private static BigInteger computeSumOfInvalidIdsPart1BruteForce(final List<Range> origInputRanges) {
        BigInteger sum = BigInteger.ZERO;
        for (Range range : origInputRanges) {
            BigInteger i = range.lower;
            while (i.compareTo(range.upper) <= 0) {
                if (isInvalidIdPart1(i)) {
                    sum = sum.add(i);
                }
                i = i.add(BigInteger.ONE);
            }
        }
        return sum;
    }

    // As explained above in Part 1 Brute Force there is a better way than iterating though every number in the ranges
    // But this worked for the inputs given so lets stick with this
    private static BigInteger computeSumOfInvalidIdsPart2BruteForce(final List<Range> origInputRanges) {
        BigInteger sum = BigInteger.ZERO;
        for (Range range : origInputRanges) {
            BigInteger i = range.lower;
            while (i.compareTo(range.upper) <= 0) {
                if (isInvalidIdPart2(i)) {
                    sum = sum.add(i);
                }
                i = i.add(BigInteger.ONE);
            }
        }
        return sum;
    }

    private static boolean isInvalidIdPart1(BigInteger id) {
        final String idAsStr = String.valueOf(id);
        final int numberOfDigits = idAsStr.length();
        return numberOfDigits % 2 == 0
                && idAsStr.regionMatches(0, idAsStr, numberOfDigits / 2, numberOfDigits / 2);
    }

    private static boolean isInvalidIdPart2(BigInteger id) {
        final String idAsStr = String.valueOf(id);
        final int numberOfDigits = idAsStr.length();
        if (numberOfDigits < 2) {
            return false;
        }
        final String idAsStrDoubled = idAsStr + idAsStr;
        final String middleSubstring = idAsStrDoubled.substring(1, idAsStrDoubled.length() - 1);
        // If idAsStr is built by repeating some smaller block, it will show up here
        return middleSubstring.contains(idAsStr);
    }

    private static List<Range> parseInput(final String fileInput) {
        final String[] rangesAsStrings = fileInput.split(",");
        final List<Range> ranges = new ArrayList<>();
        for (String rangeStr : rangesAsStrings) {
            String[] rangeComponents = rangeStr.split("-");
            ranges.add(new Range(new BigInteger(rangeComponents[0]), new BigInteger(rangeComponents[1])));
        }
        return ranges;
    }

    private record Range(BigInteger lower, BigInteger upper) {}
}
