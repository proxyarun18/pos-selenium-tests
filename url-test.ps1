# URL Test Script - Find the correct URL for your POS application
$baseIP = "4.213.224.7"
$commonPorts = @(80, 3000, 8080, 8000, 9000, 5000, 8081, 3001, 4200, 5173)

Write-Host "Testing common URLs for POS application..." -ForegroundColor Green
Write-Host "Base IP: $baseIP" -ForegroundColor Yellow
Write-Host ""

foreach ($port in $commonPorts) {
    $url = "http://${baseIP}:${port}/"
    if ($port -eq 80) {
        $url = "http://${baseIP}/"
    }
    
    Write-Host "Testing: $url" -NoNewline
    
    try {
        $response = Invoke-WebRequest -Uri $url -TimeoutSec 10 -ErrorAction Stop
        Write-Host " ✓ ACCESSIBLE (Status: $($response.StatusCode))" -ForegroundColor Green
        Write-Host "   Title: $($response.ParsedHtml.title)" -ForegroundColor Cyan
        Write-Host ""
    }
    catch {
        Write-Host " ✗ Not accessible" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "Update your config.properties with the working URL!" -ForegroundColor Yellow
