package baseball;

final class BaseballMessages {
    static final String START = "Number baseball game starts.";
    static final String PROMPT = "Input> ";
    static final String QUIT_RESULT = "Game ended. The answer was %s.%n";
    static final String WIN_RESULT = "3 strikes. You got it in %d attempt(s).%n";
    static final String DIGIT_ERROR = "Each input value must be a single digit from 0 to 9.";
    static final String DUPLICATE_ERROR = "Digits must not be duplicated.";
    static final String SCORE_TEXT = "%d strike(s) %d ball(s)";

    private BaseballMessages() {
    }
}