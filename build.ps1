# PowerShell 7.0 is recommended

param (
    [String]$Name = "HelloWorld"
)

$ErrorActionPreference = "Stop"
Set-StrictMode -Version Latest
$PSNativeCommandUseErrorActionPreference = $true

Remove-Item -Path ".\bin\*" -Recurse -Force -ErrorAction SilentlyContinue

# Compilation
javac -d bin (Get-ChildItem -Path ".\src" -Filter "*.java" -Recurse -File)
Copy-Item ".\src\MANIFEST.MF" ".\bin\"

# Building JAR
Push-Location ".\bin"
jar cvfm .\$Name.jar .\MANIFEST.MF (Get-ChildItem -Filter "*.class" -Recurse -File | Resolve-Path -Relative)
Pop-Location

Write-Host "`nLaunching the application..."
java -jar .\bin\$Name.jar
