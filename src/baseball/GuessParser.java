package baseball;

import java.util.Objects;
import java.util.regex.Pattern;

final class GuessParser {
    private static final Pattern COMMA_GAP_PATTERN = Pattern.compile(",\\s*,");
    private static final Pattern SPLIT_PATTERN = Pattern.compile("\\s*,\\s*|\\s+");
    private static final int INVALID_DIGIT = -1;

    int[] parse(String input) {
        String normalized = Objects.requireNonNull(input, "input must not be null").trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(BaseballMessages.INPUT_GUIDE);
        }

        if (!containsSeparator(normalized)) {
            if (normalized.length() != BaseballRules.DIGIT_COUNT) {
                throw new IllegalArgumentException(BaseballMessages.INPUT_GUIDE);
            }
            if (!isAsciiDigits(normalized)) {
                throw new IllegalArgumentException(BaseballMessages.DIGIT_ERROR);
            }
            return parseCompactDigits(normalized);
        }

        if (hasInvalidCommaPlacement(normalized)) {
            throw new IllegalArgumentException(BaseballMessages.INPUT_GUIDE);
        }

        String[] values = split(normalized);
        if (values.length != BaseballRules.DIGIT_COUNT) {
            throw new IllegalArgumentException(BaseballMessages.INPUT_GUIDE);
        }
        return parseDigits(values);
    }

    private static String[] split(String input) {
        return SPLIT_PATTERN.split(input);
    }

    private static boolean hasInvalidCommaPlacement(String input) {
        return input.charAt(0) == ','
                || input.charAt(input.length() - 1) == ','
                || COMMA_GAP_PATTERN.matcher(input).find();
    }

    private static boolean containsSeparator(String value) {
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if (ch == ',' || Character.isWhitespace(ch)) {
                return true;
            }
        }
        return false;
    }

    private static int[] parseDigits(String[] values) {
        int[] digits = new int[BaseballRules.DIGIT_COUNT];
        boolean[] used = new boolean[BaseballRules.DIGIT_RANGE];

        for (int i = 0; i < values.length; i++) {
            digits[i] = parseUniqueDigit(values[i], used);
        }

        return digits;
    }

    private static int[] parseCompactDigits(String input) {
        int[] digits = new int[BaseballRules.DIGIT_COUNT];
        boolean[] used = new boolean[BaseballRules.DIGIT_RANGE];

        for (int i = 0; i < BaseballRules.DIGIT_COUNT; i++) {
            digits[i] = parseUniqueDigit(input.charAt(i), used);
        }

        return digits;
    }

    private static int parseUniqueDigit(String value, boolean[] used) {
        int digit = parseDigit(value);
        if (used[digit]) {
            throw new IllegalArgumentException(BaseballMessages.DUPLICATE_ERROR);
        }
        used[digit] = true;
        return digit;
    }

    private static int parseUniqueDigit(char ch, boolean[] used) {
        int digit = toDigit(ch);
        if (digit == INVALID_DIGIT) {
            throw new IllegalArgumentException(BaseballMessages.DIGIT_ERROR);
        }
        if (used[digit]) {
            throw new IllegalArgumentException(BaseballMessages.DUPLICATE_ERROR);
        }
        used[digit] = true;
        return digit;
    }

    private static int parseDigit(String value) {
        if (value.length() != 1) {
            throw new IllegalArgumentException(BaseballMessages.DIGIT_ERROR);
        }

        int digit = toDigit(value.charAt(0));
        if (digit == INVALID_DIGIT) {
            throw new IllegalArgumentException(BaseballMessages.DIGIT_ERROR);
        }

        return digit;
    }

    private static int toDigit(char ch) {
        if (ch < '0' || ch > '9') {
            return INVALID_DIGIT;
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
