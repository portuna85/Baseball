package baseball;

final class ScoreEvaluator {
    private final int[] answer;
    private final int[] answerPositionByDigit;

    ScoreEvaluator(int[] answer) {
        Score.validateDigits("answer", answer);
        this.answer = answer.clone();
        this.answerPositionByDigit = createAnswerPositionByDigit(this.answer);
    }

    Score evaluate(int[] guess) {
        Score.validateDigits("guess", guess);

        int strike = 0;
        int ball = 0;

        for (int i = 0; i < guess.length; i++) {
            int answerPosition = answerPositionByDigit[guess[i]];

            if (answerPosition == i + 1) {
                strike++;
            } else if (answerPosition != 0) {
                ball++;
            }
        }

        return new Score(strike, ball);
    }

    private static int[] createAnswerPositionByDigit(int[] answerDigits) {
        int[] positionByDigit = new int[BaseballRules.DIGIT_RANGE];

        for (int i = 0; i < answerDigits.length; i++) {
            positionByDigit[answerDigits[i]] = i + 1;
        }

        return positionByDigit;
    }
}
