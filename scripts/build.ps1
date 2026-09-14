# PowerShell 7.0 is recommended

[CmdletBinding(PositionalBinding = $false)]
param (
    [String]$Name = "HelloWorld",
    [String]$Source,
    [String]$Destination,
)

$ErrorActionPreference = "Stop"
Set-StrictMode -Version Latest
$PSNativeCommandUseErrorActionPreference = $true

if ($Source -eq "") {
    $Source = "$(Get-Location)\src\"
}

if ($Destination -eq "") {
    $Destination = "$(Get-Location)\bin\"
}

Remove-Item -Path "$Destination\*" -Recurse -Force -ErrorAction SilentlyContinue

# Compilation
$SourceFiles = Get-ChildItem -Path $Source -Filter "*.java" -Recurse -File

javac -d bin $SourceFiles
Copy-Item "$Source\MANIFEST.MF" $Destination

# Building JAR
Push-Location $Destination

try {
    $ClassFiles = Get-ChildItem -Filter "*.class" -Recurse -File | Resolve-Path -Relative
    
    jar cvfm .\$Name.jar .\MANIFEST.MF $ClassFiles
}
finally {
    Pop-Location
}
