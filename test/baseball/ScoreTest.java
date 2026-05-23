package baseball;

final class ScoreTest {
    void run() {
        calculatesThreeStrikeZeroBall();
        calculatesZeroStrikeThreeBall();
        calculatesOneStrikeTwoBall();
        calculatesZeroStrikeZeroBall();
        rejectsNullAnswer();
        rejectsNullGuess();
        rejectsWrongLength();
        rejectsOutOfRangeDigit();
        rejectsDuplicateDigit();
    }

    private void calculatesThreeStrikeZeroBall() {
        Score score = Score.of(new int[]{1, 2, 3}, new int[]{1, 2, 3});
        TestSupport.assertEquals(3, score.strike());
        TestSupport.assertEquals(0, score.ball());
    }

    private void calculatesZeroStrikeThreeBall() {
        Score score = Score.of(new int[]{1, 2, 3}, new int[]{2, 3, 1});
        TestSupport.assertEquals(0, score.strike());
        TestSupport.assertEquals(3, score.ball());
    }

    private void calculatesOneStrikeTwoBall() {
        Score score = Score.of(new int[]{1, 2, 3}, new int[]{1, 3, 2});
        TestSupport.assertEquals(1, score.strike());
        TestSupport.assertEquals(2, score.ball());
    }

    private void calculatesZeroStrikeZeroBall() {
        Score score = Score.of(new int[]{1, 2, 3}, new int[]{4, 5, 6});
        TestSupport.assertEquals(0, score.strike());
        TestSupport.assertEquals(0, score.ball());
    }

    private void rejectsNullAnswer() {
        TestSupport.assertThrows(IllegalArgumentException.class, () -> Score.of(null, new int[]{1, 2, 3}));
    }

    private void rejectsNullGuess() {
        TestSupport.assertThrows(IllegalArgumentException.class, () -> Score.of(new int[]{1, 2, 3}, null));
    }

    private void rejectsWrongLength() {
        TestSupport.assertThrows(IllegalArgumentException.class, () -> Score.of(new int[]{1, 2}, new int[]{1, 2, 3}));
        TestSupport.assertThrows(IllegalArgumentException.class, () -> Score.of(new int[]{1, 2, 3}, new int[]{1, 2}));
    }

    private void rejectsOutOfRangeDigit() {
        TestSupport.assertThrows(IllegalArgumentException.class, () -> Score.of(new int[]{1, 2, 10}, new int[]{1, 2, 3}));
        TestSupport.assertThrows(IllegalArgumentException.class, () -> Score.of(new int[]{1, 2, 3}, new int[]{-1, 2, 3}));
    }

    private void rejectsDuplicateDigit() {
        TestSupport.assertThrows(IllegalArgumentException.class, () -> Score.of(new int[]{1, 1, 3}, new int[]{1, 2, 3}));
        TestSupport.assertThrows(IllegalArgumentException.class, () -> Score.of(new int[]{1, 2, 3}, new int[]{1, 1, 3}));
    }
}
