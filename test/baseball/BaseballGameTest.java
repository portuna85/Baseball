package baseball;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

final class BaseballGameTest {
    void run() {
        endsOnFirstCorrectGuess();
        endsAfterMultipleAttempts();
        invalidInputDoesNotIncreaseAttempts();
        quitsWithQOrQuit();
        endsGracefullyOnEof();
        rejectsNullDependencies();
    }

    private void endsOnFirstCorrectGuess() {
        String output = runGame("123\n");
        assertContainsInOrder(
                output,
                "Number baseball game starts.",
                "Input> ",
                "3 strikes. You got it in 1 attempt(s)."
        );
    }

    private void endsAfterMultipleAttempts() {
        String output = runGame("456\n789\n123\n");
        assertContainsInOrder(
                output,
                "Number baseball game starts.",
                "Input> ",
                "0 strike(s) 0 ball(s)",
                "Input> ",
                "0 strike(s) 0 ball(s)",
                "Input> ",
                "3 strikes. You got it in 3 attempt(s)."
        );
    }

    private void invalidInputDoesNotIncreaseAttempts() {
        String output = runGame("11\n123\n");
        assertContainsInOrder(
                output,
                "Number baseball game starts.",
                "Input> ",
                BaseballRules.INPUT_GUIDE,
                "Input> ",
                "3 strikes. You got it in 1 attempt(s)."
        );
    }

    private void quitsWithQOrQuit() {
        String outputQ = runGame("q\n");
        assertContainsInOrder(
                outputQ,
                "Number baseball game starts.",
                "Input> ",
                "Game ended. The answer was 123."
        );

        String outputQuit = runGame("quit\n");
        assertContainsInOrder(
                outputQuit,
                "Number baseball game starts.",
                "Input> ",
                "Game ended. The answer was 123."
        );
    }

    private void endsGracefullyOnEof() {
        String output = runGame("");
        TestSupport.assertTrue(!output.contains("Exception"), "eof should not throw");
    }

    private void rejectsNullDependencies() {
        Scanner scanner = new Scanner(new ByteArrayInputStream(new byte[0]), StandardCharsets.UTF_8);
        PrintStream out = new PrintStream(new ByteArrayOutputStream(), true, StandardCharsets.UTF_8);
        AnswerGenerator answerGenerator = () -> new int[]{1, 2, 3};
        GuessParser parser = new GuessParser();

        TestSupport.assertThrows(NullPointerException.class, () -> new BaseballGame(null, out, answerGenerator, parser));
        TestSupport.assertThrows(NullPointerException.class, () -> new BaseballGame(scanner, null, answerGenerator, parser));
        TestSupport.assertThrows(NullPointerException.class, () -> new BaseballGame(scanner, out, null, parser));
        TestSupport.assertThrows(NullPointerException.class, () -> new BaseballGame(scanner, out, answerGenerator, null));

        scanner.close();
        out.close();
    }

    private String runGame(String input) {
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);
        Scanner scanner = new Scanner(in, StandardCharsets.UTF_8);

        BaseballGame game = new BaseballGame(scanner, out, () -> new int[]{1, 2, 3}, new GuessParser());
        game.play();

        scanner.close();
        out.close();
        return outBuffer.toString(StandardCharsets.UTF_8);
    }

    private static void assertContainsInOrder(String output, String... expectedParts) {
        int index = 0;
        for (String expected : expectedParts) {
            int nextIndex = output.indexOf(expected, index);
            if (nextIndex < 0) {
                throw new AssertionError("missing expected output: " + expected);
            }
            index = nextIndex + expected.length();
        }
    }
}
