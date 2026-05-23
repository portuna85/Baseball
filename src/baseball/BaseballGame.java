package baseball;

import java.io.PrintStream;
import java.util.Objects;
import java.util.Scanner;

final class BaseballGame {
    private final GameInput input;
    private final GameOutput output;
    private final AnswerGenerator answerGenerator;
    private final GuessParser guessParser;

    BaseballGame(Scanner scanner) {
        this(new ScannerGameInput(scanner), new PrintStreamGameOutput(System.out), new RandomAnswerGenerator(), new GuessParser());
    }

    BaseballGame(Scanner scanner, PrintStream out, AnswerGenerator answerGenerator, GuessParser guessParser) {
        this(new ScannerGameInput(scanner), new PrintStreamGameOutput(out), answerGenerator, guessParser);
    }

    BaseballGame(GameInput input, GameOutput output, AnswerGenerator answerGenerator, GuessParser guessParser) {
        BaseballRules.validate();
        this.input = Objects.requireNonNull(input, "input must not be null");
        this.output = Objects.requireNonNull(output, "output must not be null");
        this.answerGenerator = Objects.requireNonNull(answerGenerator, "answerGenerator must not be null");
        this.guessParser = Objects.requireNonNull(guessParser, "guessParser must not be null");
    }

    void play() {
        int[] answer = answerGenerator.create();
        ScoreEvaluator scoreEvaluator = new ScoreEvaluator(answer);
        int attempts = 0;

        output.println(BaseballMessages.START);
        output.println(BaseballMessages.INPUT_GUIDE);

        while (true) {
            output.print(BaseballMessages.PROMPT);

            if (!input.hasNextLine()) {
                output.println("");
                return;
            }

            String line = input.nextLine().trim();
            if (BaseballCommands.isQuit(line)) {
                output.printf(BaseballMessages.QUIT_RESULT, formatDigits(answer));
                return;
            }

            int[] guess;
            try {
                guess = guessParser.parse(line);
            } catch (IllegalArgumentException e) {
                output.println(e.getMessage());
                continue;
            }

            attempts++;
            Score result = scoreEvaluator.evaluate(guess);

            if (result.isWin()) {
                output.printf(BaseballMessages.WIN_RESULT, BaseballRules.DIGIT_COUNT, attempts);
                return;
            }

            output.println(result.toString());
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
