# 순수 Java 숫자 야구 게임

## 개요
- 외부 프레임워크 없이 순수 Java로 구현한 콘솔 숫자 야구 게임입니다.
- 테스트도 JUnit 없이 커스텀 테스트 러너(`baseball.TestRunner`)로 실행합니다.

## 요구 사항
- 공식 기준 JDK: `17+`

## 게임 실행
Linux/macOS (bash):
```bash
javac -encoding UTF-8 -d out/production/ex01 $(find src -name '*.java')
java -cp out/production/ex01 baseball.Baseball
```

Windows (PowerShell):
```powershell
$srcFiles = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d out\production\ex01 $srcFiles
java -cp out\production\ex01 baseball.Baseball
```

## 테스트 실행
Linux/macOS:
```bash
./run-tests.sh
```

Windows:
```powershell
.\run-tests.ps1
```

예상 결과:
```text
PASSED: all tests
```

## 현재 동작 규칙 (코드 기준)
- 자리수 규칙: 서로 다른 숫자 `3개`를 입력해야 합니다.
- 허용 입력 예시:
  - `123`
  - `1 2 3`
  - `1,2,3`
  - `1, 2 3` (혼합 구분자)
  - `1,2,3,` (후행 쉼표 포함)
- 입력 앞뒤 공백은 제거(trim) 후 처리됩니다.
- `0`도 유효 숫자입니다. (예: `012`)
- 전각 숫자(예: `１`)는 허용되지 않습니다.
- 종료 명령:
  - `q`, `quit` (대소문자 무관)
  - 앞뒤 공백 포함 입력도 종료로 처리됨 (예: `" q "`)

## 출력/게임 흐름
- 시작 시 시작 문구와 입력 가이드를 출력합니다.
- 매 입력마다 `Input> ` 프롬프트를 출력합니다.
- 잘못된 입력은 오류 문구를 출력하고 시도 횟수를 증가시키지 않습니다.
- 정답이면 `3 strikes. You got it in N attempt(s).`를 출력하고 종료합니다.
- 종료 명령이면 `Game ended. The answer was XYZ.`를 출력하고 종료합니다.
- EOF 입력이면 예외 없이 종료합니다.

## 프로젝트 구조
- `src/baseball`: 운영 코드
- `test/baseball`: 커스텀 테스트 코드
- `docs`: 개선 문서
- `run-tests.ps1`, `run-tests.sh`: 테스트 스크립트

## 생성 산출물
- `out/` 디렉터리는 빌드/테스트 시 생성되는 산출물이며 Git 추적 대상이 아닙니다.

## 사용자 메시지 언어 정책
- 현재 사용자 대상 게임 메시지는 영어(English) 고정입니다.
