package baseball;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;

final class BaseballGameTest {
    void run() {
        endsOnFirstCorrectGuess();
        endsAfterMultipleAttempts();
        invalidInputDoesNotIncreaseAttempts();
        quitsWithQOrQuit();
        quitsWithTrimmedMixedCaseCommand();
        endsGracefullyOnEof();
        rejectsNullDependencies();
        rejectsNullIoDependencies();
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
                BaseballMessages.INPUT_GUIDE,
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

    private void rejectsNullIoDependencies() {
        GameInput input = new ScriptedInput("123");
        GameOutput output = new BufferingOutput();
        AnswerGenerator answerGenerator = () -> new int[]{1, 2, 3};
        GuessParser parser = new GuessParser();

        TestSupport.assertThrows(NullPointerException.class, () -> new BaseballGame(null, output, answerGenerator, parser));
        TestSupport.assertThrows(NullPointerException.class, () -> new BaseballGame(input, null, answerGenerator, parser));
    }

    private void quitsWithTrimmedMixedCaseCommand() {
        String output = runGameWithIo("  QuIt  ");
        assertContainsInOrder(
                output,
                "Number baseball game starts.",
                "Input> ",
                "Game ended. The answer was 123."
        );
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

    private String runGameWithIo(String... lines) {
        BufferingOutput output = new BufferingOutput();
        BaseballGame game = new BaseballGame(new ScriptedInput(lines), output, () -> new int[]{1, 2, 3}, new GuessParser());
        game.play();
        return output.asText();
    }

    private static final class ScriptedInput implements GameInput {
        private final Deque<String> lines;

        private ScriptedInput(String... values) {
            this.lines = new ArrayDeque<>(List.of(values));
        }

        @Override
        public boolean hasNextLine() {
            return !lines.isEmpty();
        }

        @Override
        public String nextLine() {
            return lines.removeFirst();
        }
    }

    private static final class BufferingOutput implements GameOutput {
        private final List<String> chunks = new ArrayList<>();

        @Override
        public void print(String text) {
            chunks.add(text);
        }

        @Override
        public void println(String text) {
            chunks.add(text + System.lineSeparator());
        }

        @Override
        public void printf(String format, Object... args) {
            chunks.add(String.format(format, args));
        }

        String asText() {
            StringBuilder builder = new StringBuilder();
            for (String chunk : chunks) {
                builder.append(chunk);
            }
            return builder.toString();
        }
    }
}
