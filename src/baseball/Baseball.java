package baseball;

import java.util.Scanner;

public class Baseball {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            BaseballGame game = new BaseballGame(scanner);
            game.play();
        }
    }
}
