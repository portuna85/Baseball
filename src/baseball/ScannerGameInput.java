package baseball;

import java.util.Objects;
import java.util.Scanner;

final class ScannerGameInput implements GameInput {
    private final Scanner scanner;

    ScannerGameInput(Scanner scanner) {
        this.scanner = Objects.requireNonNull(scanner, "scanner must not be null");
    }

    @Override
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    @Override
    public String nextLine() {
        return scanner.nextLine();
    }
}
