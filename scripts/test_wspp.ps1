# PowerShell 7.0 is recommended

[CmdletBinding(PositionalBinding = $false)]
param (
    [switch]$ForDebug,
    [Parameter(ValueFromRemainingArguments)]
    [object[]]$Arguments = @("Base")
)

$ErrorActionPreference = "Stop"
Set-StrictMode -Version Latest
$PSNativeCommandUseErrorActionPreference = $true

$JarFile = .\scripts\build.ps1 -Name WsppTest -ForDebug:$ForDebug
$Class = "intro.java.wspp.WsppTest"

Write-Host "Launching the application...`n"

java -enableassertions --class-path $JarFile $Class @Arguments
