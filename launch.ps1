# PowerShell 7.0 is recommended

[CmdletBinding(PositionalBinding = $false)]
param (
    [String]$Name = "HelloWorld",
    [String]$Class,
    [String[]]$JavaArgs,

    [Parameter(ValueFromRemainingArguments)]
    [object[]]$Arguments
)

$ErrorActionPreference = "Stop"
Set-StrictMode -Version Latest
$PSNativeCommandUseErrorActionPreference = $true

$JarFile = .\scripts\build.ps1 -Name $Name

Write-Host "`nLaunching the application..."

if ($Class -eq "") {
    java @JavaArgs -jar $JarFile @Arguments
} else {
    java @JavaArgs --class-path $JarFile $Class @Arguments
}
