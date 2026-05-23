package baseball;

import java.io.PrintStream;
import java.util.Objects;
import java.util.Scanner;

final class BaseballGame {
    private final Scanner scanner;
    private final PrintStream out;
    private final AnswerGenerator answerGenerator;
    private final GuessParser guessParser;

    BaseballGame(Scanner scanner) {
        this(scanner, System.out, new RandomAnswerGenerator(), new GuessParser());
    }

    BaseballGame(Scanner scanner, PrintStream out, AnswerGenerator answerGenerator, GuessParser guessParser) {
        this.scanner = Objects.requireNonNull(scanner, "scanner must not be null");
        this.out = Objects.requireNonNull(out, "out must not be null");
        this.answerGenerator = Objects.requireNonNull(answerGenerator, "answerGenerator must not be null");
        this.guessParser = Objects.requireNonNull(guessParser, "guessParser must not be null");
    }

    void play() {
        int[] answer = answerGenerator.create();
        int attempts = 0;

        out.println(BaseballMessages.START);
        out.println(BaseballRules.INPUT_GUIDE);

        while (true) {
            out.print(BaseballMessages.PROMPT);

            if (!scanner.hasNextLine()) {
                out.println();
                return;
            }

            String input = scanner.nextLine().trim();
            if (BaseballCommands.isQuit(input)) {
                out.printf(BaseballMessages.QUIT_RESULT, formatDigits(answer));
                return;
            }

            int[] guess;
            try {
                guess = guessParser.parse(input);
            } catch (IllegalArgumentException e) {
                out.println(e.getMessage());
                continue;
            }

            attempts++;
            Score result = Score.of(answer, guess);

            if (result.isWin()) {
                out.printf(BaseballMessages.WIN_RESULT, attempts);
                return;
            }

            out.println(result);
        }
    }

    private static String formatDigits(int[] digits) {
        StringBuilder builder = new StringBuilder(digits.length);

        for (int digit : digits) {
            builder.append(digit);
        }

        return builder.toString();
    }
}