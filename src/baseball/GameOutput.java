package baseball;

interface GameOutput {
    void print(String text);

    void println(String text);

    void printf(String format, Object... args);
}
