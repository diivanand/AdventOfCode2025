import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Problem Description: <a href="https://adventofcode.com/2025/day/1">...</a>
 */
public class Day01 {

    private static final String ROTATION_REGEX = "([LR])(\\d+)";
    private static final Pattern ROTATION_PATTERN = Pattern.compile(ROTATION_REGEX);
    private static final long DIAL_STARTING_VALUE = 50L;
    private static final long DIAL_MODULUS = 100L;

    public static void main(String[] args) {
        try {
            // Read input from file, assume file is small enough to fit in memory
            final List<String> lines = Files.readAllLines(Paths.get("src/main/resources/day1/input.txt"));

            System.out.println("BEGIN PART1");

            // Part 1
            final long passwordPart1 = computePasswordPart1(lines);
            System.out.println("passwordPart1: " + passwordPart1); // Output for input.txt is 1177

            System.out.println("END PART1");

            System.out.println();

            System.out.println("BEGIN PART2");

            // Part 2
            final long passwordPart2 = computePasswordPart2(lines);
            System.out.println("passwordPart2: " + passwordPart2); // Output for input.txt is 6768

            System.out.println("END PART2");
        } catch (Exception e) {
            System.err.println("Error reading input file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Used in PART 1
    private static long computePasswordPart1(final List<String> lines) {
        long currentValue = DIAL_STARTING_VALUE;
        long currentPassword = 0;
        System.out.println("The numbers of the dial range from 0 to " + DIAL_MODULUS);
        System.out.println("The dial starts by pointing at " + DIAL_STARTING_VALUE);
        for (String line : lines) {
            final Matcher matcher = ROTATION_PATTERN.matcher(line);
            if (!matcher.find()){
                throw new IllegalArgumentException("Invalid line: " + line);
            }
            // NOTE: We don't need to validate these two further since the regex matched
            final String fullString = matcher.group();
            final char direction = matcher.group(1).charAt(0); // Guaranteed to be either "L" or "R"
            final long rotationAmount = Long.parseLong(matcher.group(2)); // Guaranteed to be a number string containing only digits 0-9
            currentValue = switch (direction) {
                case 'L' -> ((currentValue - rotationAmount) % DIAL_MODULUS + DIAL_MODULUS) % DIAL_MODULUS;
                case 'R' -> ((currentValue + rotationAmount) % DIAL_MODULUS + DIAL_MODULUS) % DIAL_MODULUS;
                default -> throw new IllegalArgumentException("Invalid direction: " + direction);
            };
            System.out.println("The dial is rotated " + fullString + " to point at " + currentValue + ".");
            if (currentValue == 0) {
                currentPassword++;
            }
        }

        System.out.println();
        return currentPassword;
    }

    // Used in PART 2
    private static long computePasswordPart2(final List<String> lines) {
        long currentValue = DIAL_STARTING_VALUE;
        long currentPassword = 0;
        System.out.println("The numbers of the dial range from 0 to " + (DIAL_MODULUS - 1));
        System.out.println("The dial starts by pointing at " + DIAL_STARTING_VALUE);
        for (String line : lines) {
            final Matcher matcher = ROTATION_PATTERN.matcher(line);
            if (!matcher.find()){
                throw new IllegalArgumentException("Invalid line: " + line);
            }
            // NOTE: We don't need to validate these two further since the regex matched
            final String fullString = matcher.group();
            final char direction = matcher.group(1).charAt(0); // Guaranteed to be either "L" or "R"
            final long rotationAmount = Long.parseLong(matcher.group(2)); // Guaranteed to be a number string containing only digits 0-9
            long numTimesPassesZero;
            currentValue = switch (direction) {
                case 'L' -> {
                    long newValueBeforeMod = currentValue - rotationAmount;
                    if (currentValue == 0) {
                        // Starting at 0, we don't cross it initially
                        numTimesPassesZero = rotationAmount / DIAL_MODULUS;
                    } else if (newValueBeforeMod >= 0) {
                        // No wraparound occurred
                        numTimesPassesZero = (newValueBeforeMod == 0 ? 1 : 0);
                    } else {
                        // Wraparound occurred: So we hit 0 once at currentValue, and every DIAL_MODULUS
                        numTimesPassesZero = 1 + Math.abs(newValueBeforeMod) / DIAL_MODULUS;
                    }
                    yield ((newValueBeforeMod) % DIAL_MODULUS + DIAL_MODULUS) % DIAL_MODULUS;
                }
                case 'R' -> {
                    long newValueBeforeMod = currentValue + rotationAmount;
                    numTimesPassesZero = newValueBeforeMod / DIAL_MODULUS;
                    yield newValueBeforeMod % DIAL_MODULUS;
                }
                default -> throw new IllegalArgumentException("Invalid direction: " + direction);
            };
            System.out.print("The dial is rotated " + fullString + " to point at " + currentValue);
            if (numTimesPassesZero > 0) {
                System.out.print("; during this rotation it points at 0 exactly " + numTimesPassesZero + " times.\n");
                currentPassword += numTimesPassesZero;
            } else {
                System.out.print("\n");
            }
        }

        System.out.println();
        return currentPassword;
    }
}
