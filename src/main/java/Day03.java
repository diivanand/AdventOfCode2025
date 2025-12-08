import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Day03 {

    public static void main(String[] args) {
        try {
            // Assuming file input fits in memory and input format is valid so skipping validation of input.
            final String input = Files.readString(Paths.get("src/main/resources/day3/input.txt"));
            final List<BatteryBank> batteryBanks = parseInput(input);

            // Part 1
            final Instant start = Instant.now();
            final long totalOutputJoltage = computePart1TotalOutputJoltage(batteryBanks);
            final Duration timeTaken = Duration.between(start, Instant.now());
            System.out.println("Part 1: totalOutputJoltage=" + totalOutputJoltage); // Answer: 16927
            System.out.println("Part 1: timeTaken=" + timeTaken.toMillis() + " ms");

            System.out.println();

            // Part 2
            final Instant start2 = Instant.now();
            final BigInteger totalOutputJoltage2 = computePart2TotalOutputJoltage(batteryBanks);
            final Duration timeTaken2 = Duration.between(start2, Instant.now());
            System.out.println("Part 2: totalOutputJoltage2=" + totalOutputJoltage2); // Answer:
            System.out.println("Part 2: timeTaken2=" + timeTaken2.toMillis() + " ms");

        } catch (Exception e) {
            System.err.println("Error reading input file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static long computePart1TotalOutputJoltage(List<BatteryBank> batteryBanks) {
        if (batteryBanks == null || batteryBanks.isEmpty()) {
            return 0L;
        }
        return batteryBanks.stream().mapToLong(Day03::computePart1BankJoltage).sum();
    }

    private static BigInteger computePart2TotalOutputJoltage(List<BatteryBank> batteryBanks) {
        if (batteryBanks == null || batteryBanks.isEmpty()) {
            return BigInteger.ZERO;
        }
        return batteryBanks.stream().map(Day03::computePart2BankJoltage).reduce(BigInteger.ZERO, BigInteger::add);
    }

    private static int computePart1BankJoltage(BatteryBank batteryBank) {
        if (batteryBank == null) {
            return 0;
        }
        final List<Integer> batteries = batteryBank.batteries();
        int indexOfLargestFirstDigit = 0;
        // Find index of largest First Digit via linear scan
        for (int index = 0; index < batteries.size() - 1; index++) {
            if (batteries.get(index) > batteries.get(indexOfLargestFirstDigit)) {
                indexOfLargestFirstDigit = index;
            }
        }
        int indexOfLargestSecondDigit = indexOfLargestFirstDigit + 1;
        // Find index of largest second Digit via linear scan starting at index right after index of largest first digit
        for (int index = indexOfLargestSecondDigit; index < batteries.size(); index++) {
            if (batteries.get(index) > batteries.get(indexOfLargestSecondDigit)) {
                indexOfLargestSecondDigit = index;
            }
        }
        final String joltageAsString = batteries.get(indexOfLargestFirstDigit).toString()
                + batteries.get(indexOfLargestSecondDigit).toString();
        //System.out.println("joltageAsString=" + joltageAsString);
        return Integer.parseInt(joltageAsString);
    }

    private static BigInteger computePart2BankJoltage(BatteryBank batteryBank) {
        final int NUM_DIGITS = 12;
        if (batteryBank == null) {
            return BigInteger.ZERO;
        }
        if (batteryBank.batteries().size() < NUM_DIGITS) {
            throw new IllegalArgumentException("batteryBank.batteries().size() < NUM_DIGITS");
        }
        final List<Integer> batteries = batteryBank.batteries();
        final int[] largestDigitIndexForEachPlaceholder = new int[NUM_DIGITS];

        for (int i = 0; i < largestDigitIndexForEachPlaceholder.length; i++) {
            int currentDigitIndexOfLargestDigit;
            if (i == 0) {
                currentDigitIndexOfLargestDigit = 0;
                for (int index = 0; index < batteries.size() - (NUM_DIGITS - 1); index++) {
                    if (batteries.get(index) > batteries.get(currentDigitIndexOfLargestDigit)) {
                        currentDigitIndexOfLargestDigit = index;
                    }
                }
            } else {
                currentDigitIndexOfLargestDigit = largestDigitIndexForEachPlaceholder[i - 1] + 1;
                for (int index = largestDigitIndexForEachPlaceholder[i - 1] + 1; index < batteries.size() - (11 - i); index++) {
                    if (batteries.get(index) > batteries.get(currentDigitIndexOfLargestDigit)) {
                        currentDigitIndexOfLargestDigit = index;
                    }
                }
            }
            largestDigitIndexForEachPlaceholder[i] = currentDigitIndexOfLargestDigit;
        }

        final StringBuilder builder = new StringBuilder();
        for (int index : largestDigitIndexForEachPlaceholder) {
            builder.append(batteries.get(index));
        }
        final BigInteger joltage = new BigInteger(builder.toString());
        //System.out.println("joltage=" + joltage);
        return joltage;
    }

    private static List<BatteryBank> parseInput(String input) {
        final List<BatteryBank> result = new ArrayList<>();
        for (String line : input.split("\n")) {
            final List<Integer> batteries = new ArrayList<>();
            for (String characterAsString : line.split("")) {
                batteries.add(Integer.parseInt(characterAsString));
            }
            result.add(new BatteryBank(batteries));
        }
        return result;
    }

    private record BatteryBank(List<Integer> batteries) {
        public BatteryBank {
            if (batteries == null || batteries.size() < 2) {
                throw new IllegalArgumentException("BatteryBank must contain at least two batteries");
            }
            batteries = List.copyOf(batteries);
        }
    }
}
