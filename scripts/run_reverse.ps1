# PowerShell 7.0 is recommended

[CmdletBinding(PositionalBinding = $false)]
param (
    [Parameter(ValueFromRemainingArguments)]
    [object[]]$Arguments
)

$ErrorActionPreference = "Stop"
Set-StrictMode -Version Latest
$PSNativeCommandUseErrorActionPreference = $true

$JarFile = .\scripts\build.ps1 -Name Reverse

Write-Host "Launching the application...`n"

$Class = "intro.java.reverse.ReverseTest"

if ($null -eq $Arguments) {
    java -enableassertions --class-path $JarFile $Class Base
} else {
    java -enableassertions --class-path $JarFile $Class @Arguments
}
