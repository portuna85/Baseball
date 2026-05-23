package baseball;

final class BaseballMessagesTest {
    void run() {
        formatsQuitResult();
        formatsWinResult();
        formatsScoreText();
    }

    private void formatsQuitResult() {
        String message = String.format(BaseballMessages.QUIT_RESULT, "123");
        TestSupport.assertEquals("Game ended. The answer was 123.\n", normalizeLineSeparators(message));
    }

    private void formatsWinResult() {
        String message = String.format(BaseballMessages.WIN_RESULT, 2);
        TestSupport.assertEquals("3 strikes. You got it in 2 attempt(s).\n", normalizeLineSeparators(message));
    }

    private void formatsScoreText() {
        String message = String.format(BaseballMessages.SCORE_TEXT, 2, 1);
        TestSupport.assertEquals("2 strike(s) 1 ball(s)", message);
    }

    private static String normalizeLineSeparators(String value) {
        return value.replace("\r\n", "\n");
    }
}
