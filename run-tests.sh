#!/usr/bin/env bash
set -euo pipefail

cleanup() {
  rm -f .sources.tmp .tests.tmp
}
trap cleanup EXIT

rm -rf out
mkdir -p out/production/ex01 out/test/production

find src -name '*.java' > .sources.tmp
find test -name '*.java' > .tests.tmp

javac -encoding UTF-8 -d out/production/ex01 @.sources.tmp
javac -encoding UTF-8 -cp out/production/ex01 -d out/test/production @.tests.tmp

java -cp "out/production/ex01:out/test/production" baseball.TestRunner
