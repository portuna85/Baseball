package baseball;

import java.io.Console;
import java.nio.charset.Charset;
import java.util.Scanner;

public class Baseball {
    public static void main(String[] args) {
        BaseballGame game = new BaseballGame(new Scanner(System.in, inputCharset()));
        game.play();
    }

    private static Charset inputCharset() {
        Console console = System.console();
        if (console != null) {
            return console.charset();
        }

        String nativeEncoding = System.getProperty("native.encoding");
        if (nativeEncoding != null && !nativeEncoding.isBlank()) {
            return Charset.forName(nativeEncoding);
        }

        return Charset.defaultCharset();
    }
}
