package baseball;

final class ScoreEvaluatorTest {
    void run() {
        evaluatesScore();
        copiesAnswerDefensively();
        rejectsInvalidAnswer();
        rejectsInvalidGuess();
    }

    private void evaluatesScore() {
        ScoreEvaluator evaluator = new ScoreEvaluator(new int[]{1, 2, 3});
        Score score = evaluator.evaluate(new int[]{1, 3, 2});

        TestSupport.assertEquals(1, score.strike());
        TestSupport.assertEquals(2, score.ball());
    }

    private void copiesAnswerDefensively() {
        int[] answer = new int[]{1, 2, 3};
        ScoreEvaluator evaluator = new ScoreEvaluator(answer);
        answer[0] = 9;

        Score score = evaluator.evaluate(new int[]{1, 2, 3});
        TestSupport.assertEquals(3, score.strike());
        TestSupport.assertEquals(0, score.ball());
    }

    private void rejectsInvalidAnswer() {
        TestSupport.assertThrows(IllegalArgumentException.class, () -> new ScoreEvaluator(new int[]{1, 1, 3}));
    }

    private void rejectsInvalidGuess() {
        ScoreEvaluator evaluator = new ScoreEvaluator(new int[]{1, 2, 3});
        TestSupport.assertThrows(IllegalArgumentException.class, () -> evaluator.evaluate(new int[]{1, 1, 3}));
    }
}
