$ErrorActionPreference = "Stop"
Set-StrictMode -Version Latest
$PSNativeCommandUseErrorActionPreference = $true

Remove-Item -Path ".\bin\*" -Recurse -Force

javac -d bin (Get-ChildItem -Path ".\src" -Filter "*.java" -Recurse -File)
Copy-Item ".\src\manifest.txt" ".\bin\"

$old_location = (Get-Location)

Set-Location ".\bin"
jar cvfm .\HelloWorld.jar .\manifest.txt (Get-ChildItem -Filter "*.class" -Recurse -File | Resolve-Path -Relative)

Set-Location $old_location

Write-Host "Now, you can hello your world with 'java -jar .\bin\HelloWorld.jar'"
