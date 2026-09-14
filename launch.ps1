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

.\scripts\build.ps1 -Name $Name

Write-Host "`nLaunching the application..."

if ($Class -eq "") {
    java @JavaArgs -jar .\bin\$Name.jar @Arguments
} else {
    java @JavaArgs --class-path .\bin\$Name.jar $Class @Arguments
}
