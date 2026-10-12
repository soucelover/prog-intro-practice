# PowerShell 7.0 is recommended

[CmdletBinding(PositionalBinding = $false)]
param (
    [switch]$ForDebug,
    [Parameter(ValueFromRemainingArguments)]
    [object[]]$Arguments
)

$ErrorActionPreference = "Stop"
Set-StrictMode -Version Latest
$PSNativeCommandUseErrorActionPreference = $true

$JarFile = .\scripts\build.ps1 -Name FastReverse -ForDebug:$ForDebug

Write-Host "Launching the application...`n"

$Class = "intro.java.reverse.FastReverseTest"

if ($null -eq $Arguments) {
    java -enableassertions --class-path $JarFile $Class 3839
} else {
    java -enableassertions --class-path $JarFile $Class @Arguments
}
