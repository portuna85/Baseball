package baseball;

final class BaseballCommands {
    private BaseballCommands() {
    }

    static boolean isQuit(String input) {
        if (input == null) {
            return false;
        }

        String normalized = input.trim();
        return normalized.equalsIgnoreCase("q") || normalized.equalsIgnoreCase("quit");
    }
}
