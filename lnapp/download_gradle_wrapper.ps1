# 下载gradle-wrapper.jar文件
$url = "https://github.com/gradle/gradle/raw/v8.0.0/gradle/wrapper/gradle-wrapper.jar"
$output = "gradle\wrapper\gradle-wrapper.jar"

# 创建目录
New-Item -ItemType Directory -Force -Path "gradle\wrapper"

# 下载文件
Write-Host "正在下载 gradle-wrapper.jar..."
Invoke-WebRequest -Uri $url -OutFile $output

if (Test-Path $output) {
    Write-Host "gradle-wrapper.jar 下载成功！"
} else {
    Write-Host "下载失败，请检查网络连接"
}
