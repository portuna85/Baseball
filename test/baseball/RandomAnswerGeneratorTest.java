package baseball;

final class RandomAnswerGeneratorTest {
    void run() {
        generatedAnswersKeepInvariants();
    }

    private void generatedAnswersKeepInvariants() {
        RandomAnswerGenerator generator = new RandomAnswerGenerator();

        for (int i = 0; i < 1000; i++) {
            int[] answer = generator.create();
            TestSupport.assertEquals(BaseballRules.DIGIT_COUNT, answer.length);

            boolean[] used = new boolean[BaseballRules.DIGIT_RANGE];
            for (int digit : answer) {
                TestSupport.assertTrue(digit >= 0 && digit < BaseballRules.DIGIT_RANGE, "out-of-range digit: " + digit);
                TestSupport.assertTrue(!used[digit], "duplicate digit: " + digit);
                used[digit] = true;
            }
        }
    }
}
