package baseball;

record Score(int strike, int ball) {
    static Score of(int[] answer, int[] guess) {
        return new ScoreEvaluator(answer).evaluate(guess);
    }

    static void validateDigits(String name, int[] digits) {
        if (digits == null) {
            throw new IllegalArgumentException(name + " must not be null");
        }
        if (digits.length != BaseballRules.DIGIT_COUNT) {
            throw new IllegalArgumentException(name + " must have length " + BaseballRules.DIGIT_COUNT);
        }

        boolean[] used = new boolean[BaseballRules.DIGIT_RANGE];
        for (int digit : digits) {
            if (digit < 0 || digit >= BaseballRules.DIGIT_RANGE) {
                throw new IllegalArgumentException(name + " contains out-of-range digit: " + digit);
            }
            if (used[digit]) {
                throw new IllegalArgumentException(name + " contains duplicate digit: " + digit);
            }
            used[digit] = true;
        }
    }

    Score {
        if (strike < 0 || ball < 0) {
            throw new IllegalArgumentException("strike and ball must be non-negative");
        }
        if (strike + ball > BaseballRules.DIGIT_COUNT) {
            throw new IllegalArgumentException("sum of strike and ball must not exceed digit count");
        }
    }

    boolean isWin() {
        return strike == BaseballRules.DIGIT_COUNT;
    }

    @Override
    public String toString() {
        return BaseballMessages.SCORE_TEXT.formatted(strike, ball);
    }
}
