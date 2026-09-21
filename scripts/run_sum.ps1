# PowerShell 7.0 is recommended

[CmdletBinding(PositionalBinding = $false)]
param (
    [Parameter(ValueFromRemainingArguments)]
    [object[]]$Arguments
)

$ErrorActionPreference = "Stop"
Set-StrictMode -Version Latest
$PSNativeCommandUseErrorActionPreference = $true

$JarFile = .\scripts\build.ps1 -Name Sum

Write-Host "Launching the application...`n"

$Class = "intro.java.sum.SumTest"

if ($null -eq $Arguments) {
    java -enableassertions --class-path $JarFile $Class 3839
} else {
    java -enableassertions --class-path $JarFile $Class @Arguments
}
