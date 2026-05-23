package baseball;

import java.util.Objects;

final class GuessParser {
    int[] parse(String input) {
        String normalized = Objects.requireNonNull(input, "input must not be null").trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(BaseballRules.INPUT_GUIDE);
        }

        String[] values = split(normalized);
        if (values.length != BaseballRules.DIGIT_COUNT) {
            throw new IllegalArgumentException(BaseballRules.INPUT_GUIDE);
        }

        return parseDigits(values);
    }

    private static String[] split(String input) {
        if (input.length() == BaseballRules.DIGIT_COUNT && isAsciiDigits(input)) {
            String[] values = new String[BaseballRules.DIGIT_COUNT];

            for (int i = 0; i < values.length; i++) {
                values[i] = String.valueOf(input.charAt(i));
            }

            return values;
        }

        return input.split("[,\\s]+");
    }

    private static int[] parseDigits(String[] values) {
        int[] digits = new int[BaseballRules.DIGIT_COUNT];
        boolean[] used = new boolean[BaseballRules.DIGIT_RANGE];

        for (int i = 0; i < values.length; i++) {
            int digit = parseDigit(values[i]);

            if (used[digit]) {
                throw new IllegalArgumentException(BaseballMessages.DUPLICATE_ERROR);
            }

            digits[i] = digit;
            used[digit] = true;
        }

        return digits;
    }

    private static int parseDigit(String value) {
        if (value.length() != 1) {
            throw new IllegalArgumentException(BaseballMessages.DIGIT_ERROR);
        }

        char ch = value.charAt(0);
        if (ch < '0' || ch > '9') {
            throw new IllegalArgumentException(BaseballMessages.DIGIT_ERROR);
        }

        return ch - '0';
    }

    private static boolean isAsciiDigits(String value) {
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if (ch < '0' || ch > '9') {
                return false;
            }
        }

        return true;
    }
}
