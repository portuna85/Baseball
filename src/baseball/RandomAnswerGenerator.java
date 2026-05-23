package baseball;

import java.util.concurrent.ThreadLocalRandom;

final class RandomAnswerGenerator implements AnswerGenerator {
    @Override
    public int[] create() {
        int[] answer = new int[BaseballRules.DIGIT_COUNT];
        boolean[] used = new boolean[BaseballRules.DIGIT_RANGE];
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < answer.length; i++) {
            int digit;
            do {
                digit = random.nextInt(BaseballRules.DIGIT_RANGE);
            } while (used[digit]);

            answer[i] = digit;
            used[digit] = true;
        }

        return answer;
    }
}
