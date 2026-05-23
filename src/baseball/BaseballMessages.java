package baseball;

final class BaseballMessages {
    static final String START = "Number baseball game starts.";
    static final String INPUT_GUIDE = "Enter three distinct digits. Examples: 123, 1 2 3, 1,2,3 / Quit: q, quit";
    static final String PROMPT = "Input> ";
    static final String QUIT_RESULT = "Game ended. The answer was %s.%n";
    static final String WIN_RESULT = "%d strikes. You got it in %d attempt(s).%n";
    static final String DIGIT_ERROR = "Each input value must be a single digit from 0 to 9.";
    static final String DUPLICATE_ERROR = "Digits must not be duplicated.";
    static final String SCORE_TEXT = "%d strike(s) %d ball(s)";

    private BaseballMessages() {
    }
}
