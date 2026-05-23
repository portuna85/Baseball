$ErrorActionPreference = "Stop"

Remove-Item -Recurse -Force out -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force out\production\ex01 | Out-Null
New-Item -ItemType Directory -Force out\test\production | Out-Null

$srcFiles = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
$testFiles = Get-ChildItem -Path test -Recurse -Filter *.java | ForEach-Object { $_.FullName }

javac -encoding UTF-8 -d out\production\ex01 $srcFiles
javac -encoding UTF-8 -cp out\production\ex01 -d out\test\production $testFiles

java -cp "out\production\ex01;out\test\production" baseball.TestRunner
