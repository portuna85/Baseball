package baseball;

final class GuessParserTest {
    private final GuessParser parser = new GuessParser();

    void run() {
        parsesCompactDigits();
        parsesZeroDigit();
        parsesSpaceSeparatedDigits();
        parsesLeadingAndTrailingSpaces();
        parsesCommaSeparatedDigits();
        parsesMixedSeparators();
        parsesTrailingComma();
        rejectsEmptyInput();
        rejectsTooFewDigits();
        rejectsTooManyDigits();
        rejectsDuplicateDigits();
        rejectsNonDigit();
        rejectsFullWidthDigits();
        rejectsMultiCharToken();
        rejectsNullInput();
    }

    private void parsesCompactDigits() {
        TestSupport.assertArrayEquals(new int[]{1, 2, 3}, parser.parse("123"));
    }

    private void parsesZeroDigit() {
        TestSupport.assertArrayEquals(new int[]{0, 1, 2}, parser.parse("012"));
    }

    private void parsesSpaceSeparatedDigits() {
        TestSupport.assertArrayEquals(new int[]{1, 2, 3}, parser.parse("1 2 3"));
    }

    private void parsesLeadingAndTrailingSpaces() {
        TestSupport.assertArrayEquals(new int[]{1, 2, 3}, parser.parse("  1 2 3  "));
    }

    private void parsesCommaSeparatedDigits() {
        TestSupport.assertArrayEquals(new int[]{1, 2, 3}, parser.parse("1,2,3"));
    }

    private void parsesMixedSeparators() {
        TestSupport.assertArrayEquals(new int[]{1, 2, 3}, parser.parse("1, 2 3"));
    }

    private void parsesTrailingComma() {
        TestSupport.assertArrayEquals(new int[]{1, 2, 3}, parser.parse("1,2,3,"));
    }

    private void rejectsEmptyInput() {
        TestSupport.assertThrows(IllegalArgumentException.class, () -> parser.parse(""));
    }

    private void rejectsTooFewDigits() {
        TestSupport.assertThrows(IllegalArgumentException.class, () -> parser.parse("12"));
    }

    private void rejectsTooManyDigits() {
        TestSupport.assertThrows(IllegalArgumentException.class, () -> parser.parse("1234"));
    }

    private void rejectsDuplicateDigits() {
        TestSupport.assertThrows(IllegalArgumentException.class, () -> parser.parse("112"));
    }

    private void rejectsNonDigit() {
        TestSupport.assertThrows(IllegalArgumentException.class, () -> parser.parse("1,a,3"));
    }

    private void rejectsFullWidthDigits() {
        TestSupport.assertThrows(IllegalArgumentException.class, () -> parser.parse("１ ２ ３"));
    }

    private void rejectsMultiCharToken() {
        TestSupport.assertThrows(IllegalArgumentException.class, () -> parser.parse("10 2 3"));
    }

    private void rejectsNullInput() {
        TestSupport.assertThrows(NullPointerException.class, () -> parser.parse(null));
    }
}
