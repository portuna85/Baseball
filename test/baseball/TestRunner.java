package baseball;

import java.util.ArrayList;
import java.util.List;

public final class TestRunner {
    private TestRunner() {
    }

    public static void main(String[] args) {
        List<String> failures = new ArrayList<>();

        run("GuessParserTest", () -> new GuessParserTest().run(), failures);
        run("ScoreTest", () -> new ScoreTest().run(), failures);
        run("BaseballRulesTest", () -> new BaseballRulesTest().run(), failures);
        run("BaseballCommandsTest", () -> new BaseballCommandsTest().run(), failures);
        run("BaseballMessagesTest", () -> new BaseballMessagesTest().run(), failures);
        run("RandomAnswerGeneratorTest", () -> new RandomAnswerGeneratorTest().run(), failures);
        run("BaseballGameTest", () -> new BaseballGameTest().run(), failures);

        if (!failures.isEmpty()) {
            System.err.println("FAILED: " + failures.size());
            for (String failure : failures) {
                System.err.println(" - " + failure);
            }
            System.exit(1);
        }

        System.out.println("PASSED: all tests");
    }

    private static void run(String name, TestSupport.ThrowingRunnable test, List<String> failures) {
        try {
            test.run();
            System.out.println("[PASS] " + name);
        } catch (Throwable t) {
            failures.add(name + " -> " + t.getClass().getSimpleName() + ": " + t.getMessage());
            System.err.println("[FAIL] " + name);
        }
    }
}
