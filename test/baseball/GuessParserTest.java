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
        rejectsTrailingComma();
        rejectsLeadingComma();
        rejectsRepeatedComma();
        rejectsRepeatedCommaWithSpace();
        rejectsEmptyInput();
        rejectsTooFewDigits();
        rejectsTooManyDigits();
        rejectsDuplicateDigits();
        rejectsNonDigitToken();
        rejectsCompactNonDigitWithDigitErrorMessage();
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

    private void rejectsTrailingComma() {
        assertIllegalArgMessage(BaseballMessages.INPUT_GUIDE, "1,2,3,");
    }

    private void rejectsLeadingComma() {
        assertIllegalArgMessage(BaseballMessages.INPUT_GUIDE, ",1,2,3");
    }

    private void rejectsRepeatedComma() {
        assertIllegalArgMessage(BaseballMessages.INPUT_GUIDE, "1,,2,3");
    }

    private void rejectsRepeatedCommaWithSpace() {
        assertIllegalArgMessage(BaseballMessages.INPUT_GUIDE, "1, ,2,3");
    }

    private void rejectsEmptyInput() {
        assertIllegalArgMessage(BaseballMessages.INPUT_GUIDE, "");
    }

    private void rejectsTooFewDigits() {
        assertIllegalArgMessage(BaseballMessages.INPUT_GUIDE, "12");
    }

    private void rejectsTooManyDigits() {
        assertIllegalArgMessage(BaseballMessages.INPUT_GUIDE, "1234");
    }

    private void rejectsDuplicateDigits() {
        assertIllegalArgMessage(BaseballMessages.DUPLICATE_ERROR, "112");
    }

    private void rejectsNonDigitToken() {
        assertIllegalArgMessage(BaseballMessages.DIGIT_ERROR, "1,a,3");
    }

    private void rejectsCompactNonDigitWithDigitErrorMessage() {
        assertIllegalArgMessage(BaseballMessages.DIGIT_ERROR, "12a");
    }

    private void rejectsFullWidthDigits() {
        assertIllegalArgMessage(BaseballMessages.DIGIT_ERROR, "\uFF11\uFF12\uFF13");
    }

    private void rejectsMultiCharToken() {
        assertIllegalArgMessage(BaseballMessages.DIGIT_ERROR, "10 2 3");
    }

    private void rejectsNullInput() {
        TestSupport.assertThrows(NullPointerException.class, () -> parser.parse(null));
    }

    private void assertIllegalArgMessage(String expectedMessage, String input) {
        try {
            parser.parse(input);
            throw new AssertionError("expected IllegalArgumentException for input=" + input);
        } catch (IllegalArgumentException e) {
            TestSupport.assertEquals(expectedMessage, e.getMessage());
        }
    }
}
