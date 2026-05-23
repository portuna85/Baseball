package baseball;

import java.io.PrintStream;
import java.util.Objects;

final class PrintStreamGameOutput implements GameOutput {
    private final PrintStream out;

    PrintStreamGameOutput(PrintStream out) {
        this.out = Objects.requireNonNull(out, "out must not be null");
    }

    @Override
    public void print(String text) {
        out.print(text);
    }

    @Override
    public void println(String text) {
        out.println(text);
    }

    @Override
    public void printf(String format, Object... args) {
        out.printf(format, args);
    }
}
