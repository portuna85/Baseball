package baseball;

final class BaseballCommandsTest {
    void run() {
        recognizesQuitCommands();
        recognizesQuitCommandsWithOuterSpaces();
        rejectsNonQuitCommands();
        rejectsNullInput();
    }

    private void recognizesQuitCommands() {
        TestSupport.assertTrue(BaseballCommands.isQuit("q"), "q should quit");
        TestSupport.assertTrue(BaseballCommands.isQuit("Q"), "Q should quit");
        TestSupport.assertTrue(BaseballCommands.isQuit("quit"), "quit should quit");
        TestSupport.assertTrue(BaseballCommands.isQuit("QUIT"), "QUIT should quit");
    }

    private void rejectsNonQuitCommands() {
        TestSupport.assertTrue(!BaseballCommands.isQuit(""), "empty should not quit");
        TestSupport.assertTrue(!BaseballCommands.isQuit("123"), "guess should not quit");
    }

    private void recognizesQuitCommandsWithOuterSpaces() {
        TestSupport.assertTrue(BaseballCommands.isQuit(" q "), "spaced q should quit");
        TestSupport.assertTrue(BaseballCommands.isQuit(" QUIT "), "spaced quit should quit");
    }

    private void rejectsNullInput() {
        TestSupport.assertTrue(!BaseballCommands.isQuit(null), "null should not quit");
    }
}
