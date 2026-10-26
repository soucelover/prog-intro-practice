# PowerShell 7.0 is recommended

[CmdletBinding(PositionalBinding = $false)]
param (
    [switch]$ForDebug,
    [Parameter(ValueFromRemainingArguments)]
    [object[]]$Arguments = @("3839")
)

$ErrorActionPreference = "Stop"
Set-StrictMode -Version Latest
$PSNativeCommandUseErrorActionPreference = $true

$JarFile = .\scripts\build.ps1 -Name MarkupTest -ForDebug:$ForDebug
$Class = "intro.java.markup.MarkupListTest"

Write-Host "Launching the application...`n"

java -enableassertions --class-path $JarFile $Class @Arguments
