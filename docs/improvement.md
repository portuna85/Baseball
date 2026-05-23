# Pure Java Number Baseball Project - Code Review Report

## 1. Review Scope

This report analyzes the uploaded pure Java project `ex01(3).zip` as a console-based Number Baseball game. The review focuses on:

- Project structure
- Domain design
- Input parsing and validation
- Game flow
- Test quality
- Build/run reproducibility
- Maintainability and extensibility
- Objective evaluation

The project intentionally avoids external frameworks and uses only standard Java APIs.

---

## 2. Executive Summary

The project is a well-structured pure Java implementation of a Number Baseball game. It demonstrates stronger-than-basic separation of concerns: parsing, score evaluation, random answer generation, command handling, I/O abstraction, and game orchestration are each separated into dedicated classes.

The code compiles cleanly, the custom test suite passes, and the design is testable because dependencies such as input, output, answer generation, and parsing can be injected.

The main improvement areas are not basic correctness issues. They are mostly engineering-quality refinements:

1. Clarify and tighten input grammar.
2. Remove or document implicit parser behavior.
3. Improve build/test reproducibility.
4. Avoid distributing `.git` metadata inside the submission ZIP.
5. Reduce small-project over-engineering or document why the layered structure exists.
6. Consider separating value objects from calculation services more cleanly.
7. Add more behavioral tests for ambiguous input cases.

Overall, this is a solid pure Java console project with good testability and reasonable object decomposition.

---

## 3. Verified Execution Results

| Check | Result |
|---|---:|
| Production compilation | Passed |
| Test compilation | Passed |
| Custom test runner | Passed |
| `javac -Xlint:all` | No warnings observed |
| `bash run-tests.sh` | Passed |
| `./run-tests.sh` | Failed because the script is not executable |

Test output:

```text
[PASS] GuessParserTest
[PASS] ScoreTest
[PASS] ScoreEvaluatorTest
[PASS] BaseballRulesTest
[PASS] BaseballCommandsTest
[PASS] BaseballMessagesTest
[PASS] RandomAnswerGeneratorTest
[PASS] BaseballGameTest
PASSED: all tests
```

Operational issue:

```text
./run-tests.sh: Permission denied
```

Reason: `run-tests.sh` is present with `-rw-r--r--` permissions instead of executable permissions.

Recommended fix:

```bash
chmod +x run-tests.sh
git update-index --chmod=+x run-tests.sh
```

---

## 4. Project Structure

```text
src/baseball/
  AnswerGenerator.java
  Baseball.java
  BaseballCommands.java
  BaseballGame.java
  BaseballMessages.java
  BaseballRules.java
  GameInput.java
  GameOutput.java
  GuessParser.java
  PrintStreamGameOutput.java
  RandomAnswerGenerator.java
  ScannerGameInput.java
  Score.java
  ScoreEvaluator.java

test/baseball/
  BaseballCommandsTest.java
  BaseballGameTest.java
  BaseballMessagesTest.java
  BaseballRulesTest.java
  GuessParserTest.java
  RandomAnswerGeneratorTest.java
  ScoreEvaluatorTest.java
  ScoreTest.java
  TestRunner.java
  TestSupport.java
```

Approximate size:

| Area | Lines |
|---|---:|
| Production Java code | 426 |
| Test Java code | 555 |
| Total Java code | 981 |

The test code is larger than the production code. For a small console project, this is a positive signal: important behavior is being exercised rather than only manually checked.

---

## 5. Functional Behavior Summary

The game currently implements the following behavior:

- Generates a random answer of three distinct digits.
- Allows `0` as a valid digit.
- Accepts compact input such as `123`.
- Accepts separated input such as `1 2 3`, `1,2,3`, and `1, 2 3`.
- Accepts quit commands: `q` and `quit`, case-insensitive.
- Ignores leading/trailing whitespace.
- Rejects duplicate digits.
- Rejects non-ASCII digits such as full-width digits.
- Does not increment attempt count for invalid input.
- Exits gracefully on EOF.
- Prints the answer when the user quits.

The functionality is coherent and the README accurately describes most of the current behavior.

---

## 6. Design Analysis

### 6.1 Entry Point: `Baseball`

`Baseball` is minimal and correctly delegates actual game behavior to `BaseballGame`.

Strengths:

- Keeps `main` small.
- Uses try-with-resources for `Scanner`.
- Avoids embedding game logic in the entry point.

Potential improvement:

- If the game becomes configurable, command-line argument parsing should remain outside `BaseballGame` and be handled in or near `Baseball`.

Current design is appropriate.

---

### 6.2 Game Orchestration: `BaseballGame`

`BaseballGame` is the central application service. It coordinates:

- Answer generation
- Prompt output
- Input reading
- Quit command detection
- Guess parsing
- Score evaluation
- Attempt counting
- Win/quit/EOF termination

Strengths:

- Dependency injection is used effectively.
- `GameInput` and `GameOutput` make game-flow testing easy.
- Invalid input does not increase attempts.
- EOF is handled without throwing an exception.
- Answer formatting is localized to one method.

Issues and refinements:

1. `BaseballGame` is doing several orchestration responsibilities.
   - This is acceptable for the current size.
   - If features such as replay, difficulty levels, or statistics are added, this class may become too large.

2. EOF handling prints an empty line:

   ```java
   output.println("");
   return;
   ```

   This is probably intended to close the prompt line. That is reasonable for interactive console UX, but it should be documented or wrapped in a clearer method such as `output.println()` if `GameOutput` supports it.

3. `formatDigits` assumes the answer is a digit array. That matches current rules, but if `DIGIT_RANGE` or token format changes, this method will need revision.

Recommended direction:

- Keep `BaseballGame` as-is for the current scope.
- If the project grows, introduce a `GameSession` or `GameResult` model rather than expanding `BaseballGame` indefinitely.

---

### 6.3 Input Abstraction: `GameInput`, `ScannerGameInput`

The input abstraction is a good design choice. It allows the game to be tested without relying on `System.in`.

Strengths:

- Simple interface.
- Easy fake implementation in tests.
- Avoids hard dependency on `Scanner` in the core game loop.

Potential improvement:

- `GameInput` could be documented as a line-based input abstraction.
- If the project remains small, this abstraction may be more structure than strictly necessary, but it is justified by the testability benefit.

---

### 6.4 Output Abstraction: `GameOutput`, `PrintStreamGameOutput`

The output abstraction is also useful for testability.

Strengths:

- Makes console output testable.
- Avoids direct `System.out` calls inside game logic.
- Keeps formatting behavior centralized.

Potential improvement:

- Add a no-argument `println()` method if blank-line output is intentional.
- Consider using `Locale.ROOT` for formatted output if future messages include locale-sensitive formatting. Current output is numeric and simple, so this is not urgent.

---

### 6.5 Command Handling: `BaseballCommands`

`BaseballCommands.isQuit` is simple and correct.

Strengths:

- Handles `null` defensively.
- Trims outer whitespace.
- Supports case-insensitive quit commands.
- Has focused tests.

Potential improvement:

- Current command handling is sufficient.
- If more commands are added, replace the boolean method with a command parser that returns an enum such as `QUIT`, `GUESS`, `HELP`, or `UNKNOWN`.

---

### 6.6 Rule Configuration: `BaseballRules`

`BaseballRules` centralizes game constants:

```java
static final int DIGIT_COUNT = 3;
static final int DIGIT_RANGE = 10;
```

Strengths:

- The core constants are not duplicated throughout the code.
- `validate()` protects against invalid internal rule combinations.

Main issue:

The code looks configurable, but it is not fully generalized.

Examples:

- `INPUT_GUIDE` says `Enter three distinct digits`, so the message is fixed to three digits.
- `GuessParser` assumes each token is a single ASCII digit from `0` to `9`.
- `DIGIT_RANGE > 10` would not work as a multi-symbol game without parser changes.
- The term `digit` itself limits the model to decimal single-character values.

Recommended options:

Option A: Keep the rules fixed and make that explicit.

```java
static final int DIGIT_COUNT = 3;
static final int DIGIT_RANGE = 10;
```

This is perfectly acceptable for a simple console assignment.

Option B: Make the game genuinely configurable.

```java
record BaseballConfig(int digitCount, int digitRange, boolean allowZero) {
    BaseballConfig {
        if (digitCount <= 0) throw new IllegalArgumentException("digitCount must be positive");
        if (digitRange <= 0) throw new IllegalArgumentException("digitRange must be positive");
        if (digitCount > digitRange) throw new IllegalArgumentException("digitCount must not exceed digitRange");
    }
}
```

For this project, Option A is more practical unless configurability is a requirement.

---

## 7. Parser Analysis: `GuessParser`

`GuessParser` is the most important class for correctness because user input is inherently messy.

### 7.1 Current Accepted Formats

The parser accepts:

```text
123
012
1 2 3
1,2,3
1, 2 3
1,2,3,
```

It also accepts some formats not explicitly emphasized in the README:

```text
1,,2,3
1,2,,3
1   2   3
```

The reason is this separator pattern:

```java
private static final Pattern SEPARATOR_PATTERN = Pattern.compile("[,\\s]+");
```

Because the pattern uses `+`, consecutive separators are treated as a single separator. Also, Java's default `Pattern.split(input)` behavior discards trailing empty strings, so `1,2,3,` is accepted.

This behavior may be acceptable, but it should be intentional and documented.

### 7.2 Strengths

- Rejects `null` through `Objects.requireNonNull`.
- Rejects empty input.
- Rejects duplicates.
- Rejects full-width digits.
- Rejects multi-character tokens such as `10`.
- Handles compact and separated forms separately.
- Uses ASCII digit validation instead of `Character.isDigit`, which avoids unexpected Unicode digit acceptance.

### 7.3 Weaknesses

#### Issue 1: Input grammar is permissive in implicit ways

`1,,2,3` and `1,2,,3` are accepted. This may surprise users because repeated commas often indicate a missing value.

Recommended decision:

- If permissive input is intended, document it in README and tests.
- If strict input is intended, reject repeated separators and trailing separators.

#### Issue 2: Error messages are not always specific

Example:

```text
12a
```

Current result:

```text
Enter three distinct digits. Examples: 123, 1 2 3, 1,2,3 / Quit: q, quit
```

This happens because the compact path is only used when the input length equals `DIGIT_COUNT` and all characters are ASCII digits. Since `12a` is not all digits, it falls into separated parsing and becomes one invalid token count.

A more specific message would be:

```text
Each input value must be a single digit from 0 to 9.
```

Recommended improvement:

- First classify the input as compact-like or separated-like.
- If the input has no separators and contains non-digits, return the digit error instead of the generic guide.

#### Issue 3: Parser policy and README policy should stay synchronized

The README currently documents many accepted formats, which is good. However, the exact grammar should be clearer if this is being submitted for evaluation.

Recommended README clarification:

```text
Allowed separators: comma and whitespace.
Multiple adjacent separators are treated as one separator.
A trailing separator is currently accepted.
```

Or, if strict behavior is preferred:

```text
A missing value, repeated comma, or trailing comma is invalid.
```

---

## 8. Scoring Design: `Score` and `ScoreEvaluator`

### 8.1 `Score`

`Score` is implemented as a Java record:

```java
record Score(int strike, int ball)
```

Strengths:

- A record is appropriate for an immutable value object.
- Constructor validation prevents impossible score states.
- `isWin()` is clear and expressive.
- `toString()` centralizes score message formatting.

Potential design issue:

`Score.of(answer, guess)` creates a `ScoreEvaluator`:

```java
static Score of(int[] answer, int[] guess) {
    return new ScoreEvaluator(answer).evaluate(guess);
}
```

This makes the value object depend on the calculation service. That is convenient, but it slightly blurs responsibilities.

Cleaner alternatives:

1. Keep all evaluation in `ScoreEvaluator`.
2. Move `Score.of` to a utility/factory class.
3. Rename it to make the service dependency clear.

For the current project, this is a minor design concern, not a correctness problem.

### 8.2 `ScoreEvaluator`

`ScoreEvaluator` is efficient and clean.

Strengths:

- Clones the answer defensively.
- Validates answer and guess arrays.
- Uses a position lookup array for O(n) evaluation.
- Avoids nested loops.

Current algorithm:

1. Build `answerPositionByDigit`.
2. For each guessed digit:
   - If the stored answer position equals the guess index, count strike.
   - If the digit exists elsewhere, count ball.

This is efficient and appropriate.

Potential improvement:

- If `DIGIT_RANGE` remains 10, the lookup array is ideal.
- If the domain becomes more general, consider a `Map<Integer, Integer>` or a dedicated `Digits` value object.

---

## 9. Random Answer Generation: `RandomAnswerGenerator`

`RandomAnswerGenerator` uses `ThreadLocalRandom` and a boolean `used` array.

Strengths:

- Simple and efficient for a 3-digit answer.
- Guarantees no duplicate digits.
- Respects `BaseballRules.DIGIT_RANGE`.
- Covered by a 1,000-iteration invariant test.

Potential improvement:

- For fairness and predictability, the current rejection sampling is fine for 3 unique digits from 10 digits.
- If `DIGIT_COUNT` approaches `DIGIT_RANGE`, a shuffle-based approach may be more predictable in runtime, but this is unnecessary for the current game.

Example alternative:

```java
List<Integer> digits = IntStream.range(0, BaseballRules.DIGIT_RANGE)
        .boxed()
        .collect(Collectors.toCollection(ArrayList::new));
Collections.shuffle(digits);
return digits.stream().limit(BaseballRules.DIGIT_COUNT).mapToInt(Integer::intValue).toArray();
```

This is more verbose and not clearly better for the current scope.

---

## 10. Message Management: `BaseballMessages`

The project centralizes user-facing messages in `BaseballMessages`.

Strengths:

- Avoids repeated string literals.
- Makes output tests stable.
- Helps future localization.

Potential improvements:

1. Keep all user-facing strings together.
   - `INPUT_GUIDE` currently lives in `BaseballRules`, not `BaseballMessages`.
   - This is defensible because the guide describes rules, but it mixes configuration and presentation.

2. Consider a `Messages` or `GameText` class if future localization is needed.

3. If messages remain English-only, the README correctly states that policy.

---

## 11. Test Suite Analysis

The custom test suite is one of the stronger parts of the project.

### 11.1 Strengths

- Does not require JUnit or external dependencies.
- Tests parser behavior extensively.
- Tests score calculation cases.
- Tests defensive validation.
- Tests game flow with fixed answers.
- Tests output ordering.
- Tests EOF handling.
- Tests invalid input not increasing attempt count.
- Tests random answer invariants.

### 11.2 Areas to Improve

#### Add tests for ambiguous separator behavior

Current parser behavior accepts repeated separators. Add tests that explicitly lock this policy down.

If accepting repeated separators is intended:

```java
private void parsesRepeatedSeparators() {
    TestSupport.assertArrayEquals(new int[]{1, 2, 3}, parser.parse("1,,2,3"));
    TestSupport.assertArrayEquals(new int[]{1, 2, 3}, parser.parse("1,2,,3"));
}
```

If rejecting repeated separators is intended:

```java
private void rejectsRepeatedSeparators() {
    TestSupport.assertThrows(IllegalArgumentException.class, () -> parser.parse("1,,2,3"));
    TestSupport.assertThrows(IllegalArgumentException.class, () -> parser.parse("1,2,,3"));
}
```

#### Add tests for specific error messages

The tests currently check that exceptions occur, but not always which message is returned.

Useful cases:

- `12a`
- `1,a,3`
- `10 2 3`
- duplicate input
- empty input

#### Add tests for answer generator invalid configurations only if rules become configurable

Current static rules make this unnecessary.

#### Add test runner summary counts

Current output is readable. It could be slightly improved by printing counts:

```text
PASSED: 8 test class(es), 0 failed
```

This is optional.

---

## 12. Build and Repository Hygiene

### 12.1 Current State

The project intentionally avoids Gradle/Maven and uses shell scripts:

- `run-tests.sh`
- `run-tests.ps1`

This is appropriate for a pure Java assignment.

### 12.2 Issues

#### Issue 1: Shell script is not executable

Fix:

```bash
chmod +x run-tests.sh
git update-index --chmod=+x run-tests.sh
```

#### Issue 2: `.git` directory is included in the ZIP

The uploaded ZIP includes `.git/` metadata. For a submission package, this is usually unnecessary and can expose repository metadata.

Recommended packaging command:

```bash
git archive --format=zip --output ex01.zip HEAD
```

Or:

```bash
zip -r ex01.zip . -x ".git/*" "out/*"
```

#### Issue 3: `docs/` exists but appears empty

README mentions `docs` as an improvement-document location. If the directory is intentionally reserved, that is fine. Otherwise, either add documentation or remove the reference.

#### Issue 4: No CI configuration

Not required for a small project, but a minimal GitHub Actions workflow would make the test status reproducible.

Example direction:

```yaml
name: Java CI
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '17'
      - run: bash run-tests.sh
```

---

## 13. Maintainability Assessment

### 13.1 Positive Maintainability Signals

- Small focused classes.
- Package-private classes reduce public API surface.
- Constructor dependency injection improves testability.
- Validation is present at boundaries.
- Score evaluation is efficient and isolated.
- README documents execution and behavior.
- Tests are easy to run without external tools.

### 13.2 Maintainability Risks

- The project may be slightly over-structured for a very small console game.
- Some rules are centralized but not truly configurable.
- Parser behavior is more permissive than it may look at first glance.
- `Score` mixes value representation with evaluation convenience.
- Error-message policy is not fully consistent.
- Build scripts are not fully reproducible due to file permission issue.

---

## 14. Recommended Improvement Plan

### Priority 1 - Must Fix

| Item | Reason | Suggested Action |
|---|---|---|
| Make `run-tests.sh` executable | `./run-tests.sh` currently fails | `chmod +x run-tests.sh` and commit file mode |
| Exclude `.git` from distribution ZIP | Avoid leaking repository metadata | Use `git archive` or ZIP exclude rule |
| Clarify parser grammar | Current behavior accepts repeated/trailing separators | Document or reject such inputs |

### Priority 2 - Should Improve

| Item | Reason | Suggested Action |
|---|---|---|
| Add parser edge-case tests | Prevent accidental grammar drift | Test `1,,2,3`, `1,2,,3`, `12a`, leading comma |
| Improve error specificity | Better user feedback | Return digit error for compact non-digit input |
| Align `INPUT_GUIDE` location | Cleaner separation | Move to `BaseballMessages` or keep with clear rationale |
| Add test count summary | Better test-run visibility | Print passed/failed class counts |

### Priority 3 - Optional Refactoring

| Item | Reason | Suggested Action |
|---|---|---|
| Introduce `Digits` value object | Centralize validation/formatting | Replace raw `int[]` across parser/evaluator |
| Remove `Score.of` or move it | Keep value object pure | Use `ScoreEvaluator` directly |
| Add `BaseballConfig` | True configurability | Replace static rules if needed |
| Add CI | Reproducible verification | GitHub Actions or equivalent |

---

## 15. Suggested Refactoring Direction

For the current project size, avoid large rewrites. The best improvement path is incremental:

1. Fix packaging and script permission.
2. Decide strict vs permissive parser policy.
3. Add tests for that policy.
4. Improve error messages.
5. Keep the current architecture unless new features are added.

A full domain-model refactor is not necessary right now.

If future requirements include replay, difficulty levels, custom digit count, scoring history, or GUI support, then the following structure would be useful:

```text
baseball/
  app/
    BaseballGame.java
    GameSession.java
  domain/
    Digits.java
    Score.java
    ScoreEvaluator.java
    BaseballConfig.java
  io/
    GameInput.java
    GameOutput.java
    ScannerGameInput.java
    PrintStreamGameOutput.java
  parser/
    GuessParser.java
  random/
    AnswerGenerator.java
    RandomAnswerGenerator.java
```

For the current assignment, this package split may be unnecessary overhead.

---

## 16. Objective Evaluation in Korean

### 총평

순수 Java만으로 작성한 콘솔 숫자 야구 게임으로서 완성도가 높습니다. 단순 동작 구현에 그치지 않고 입력 처리, 점수 계산, 정답 생성, 게임 진행, 입출력 추상화까지 분리되어 있어 테스트 용이성과 유지보수성이 좋습니다.

다만 작은 콘솔 과제 기준에서는 구조가 다소 세분화되어 과설계로 보일 여지가 있습니다. 또한 입력 파서가 `1,,2,3`, `1,2,,3`, `1,2,3,` 같은 형식을 허용하는 점은 의도된 정책인지 명확히 할 필요가 있습니다. 제출 ZIP에 `.git` 디렉터리가 포함된 점과 `run-tests.sh` 실행 권한 누락은 실무 제출 완성도 측면에서 감점 요소입니다.

### 점수 평가

| 항목 | 점수 | 평가 |
|---|---:|---|
| 기능 완성도 | 18 / 20 | 기본 게임 흐름, 종료, EOF, 유효성 검증이 잘 구현됨 |
| 객체지향 설계 | 17 / 20 | 역할 분리가 좋지만 과제 규모 대비 다소 과도한 분리 |
| 코드 품질 | 18 / 20 | 가독성, 방어적 복사, 검증 로직이 양호함 |
| 테스트 품질 | 17 / 20 | 커스텀 테스트가 충실하나 파서 경계 케이스 보강 여지 있음 |
| 실행/배포 완성도 | 7 / 10 | 스크립트 권한 문제와 `.git` 포함 이슈 존재 |
| 문서화 | 8 / 10 | README는 좋지만 `docs/` 참조와 실제 내용 일치성 보완 필요 |

**총점: 85 / 100**

### 등급

**중급 초입 수준**입니다.

초급 과제로 보면 매우 잘 작성된 편이고, 중급 코드 리뷰 기준에서도 구조와 테스트는 긍정적으로 평가할 수 있습니다. 다만 실무 제출물 관점에서는 입력 정책의 명확성, 배포 패키지 정리, 스크립트 실행 권한 같은 마감 품질을 보완할 필요가 있습니다.

### 단기 개선 우선순위

1. `run-tests.sh` 실행 권한 부여
2. 제출 ZIP에서 `.git` 디렉터리 제외
3. 입력 허용 정책 명확화
4. 반복 구분자 및 잘못된 문자 입력 테스트 추가
5. `Score.of()` 책임 위치 재검토
6. `BaseballRules`를 고정 규칙으로 둘지, 확장 가능한 설정으로 만들지 방향 결정

### 최종 진단

현재 코드는 순수 콘솔 게임 과제 수준을 넘어 테스트 가능성과 유지보수를 고려한 구조를 갖추고 있습니다. 큰 결함은 없으며, 주요 개선점은 기능 오류보다는 완성도와 명확성입니다. 위 우선순위 항목만 정리해도 전체 품질은 더 높아질 수 있습니다.
