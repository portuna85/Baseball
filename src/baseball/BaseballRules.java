package baseball;

final class BaseballRules {
    static final int DIGIT_COUNT = 3;
    static final int DIGIT_RANGE = 10;

    static void validate() {
        if (DIGIT_COUNT <= 0) {
            throw new IllegalStateException("DIGIT_COUNT must be positive");
        }
        if (DIGIT_RANGE <= 0) {
            throw new IllegalStateException("DIGIT_RANGE must be positive");
        }
        if (DIGIT_COUNT > DIGIT_RANGE) {
            throw new IllegalStateException("DIGIT_COUNT must not exceed DIGIT_RANGE");
        }
    }

    private BaseballRules() {
    }
}
