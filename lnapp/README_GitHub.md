# 雷鸟电视AI助手

一个专为雷鸟电视设计的AI语音助手应用，支持语音输入和OpenAI API集成。

## 🚀 快速开始

### 自动编译APK

这个项目使用GitHub Actions自动编译APK：

1. **查看编译状态**：点击上方的 "Actions" 标签
2. **下载APK**：编译完成后，在Actions页面下载 `tv-ai-assistant-apk` 文件
3. **安装到电视**：将APK复制到U盘，在雷鸟电视上安装

### 手动触发编译

如果需要手动触发编译：
1. 进入 "Actions" 页面
2. 选择 "Build Android APK" 工作流
3. 点击 "Run workflow" 按钮

## 📱 功能特点

- 🎤 支持遥控器语音按钮和屏幕语音输入
- 🤖 集成OpenAI API，支持自定义API地址
- 🔊 语音输出，支持中文TTS
- ⚙️ 配置文件管理，无需复杂设置
- 📺 专为电视优化的界面设计

## 🔧 配置说明

应用首次运行会在 `/Android/data/com.tvai.assistant/files/config.properties` 创建配置文件：

```properties
# OpenAI API配置
openai_url=https://api.openai.com/v1/chat/completions
api_key=your-api-key-here
model=gpt-3.5-turbo
```

### 修改配置

1. 通过ADB或文件管理器访问配置文件
2. 修改 `openai_url` 为您的API地址
3. 修改 `api_key` 为您的API密钥
4. 重启应用生效

## 📖 使用方法

1. **语音输入**：按遥控器语音按钮或点击"语音输入"按钮
2. **文字输入**：在输入框中输入问题，按确定键发送
3. **查看回复**：AI回复会显示在下方，同时语音播放

## 🛠️ 技术架构

- **平台**：Android TV (API 21+)
- **语音识别**：Android SpeechRecognizer
- **语音合成**：Android TextToSpeech
- **网络请求**：OkHttp3
- **JSON处理**：Gson

## 📋 系统要求

- **设备**：雷鸟电视或支持Android TV的设备
- **系统**：Android 5.0 (API 21) 或更高版本
- **网络**：稳定的网络连接
- **权限**：麦克风权限

## 🔍 故障排除

**语音识别不工作**：
- 检查麦克风权限
- 确认网络连接正常
- 尝试重启应用

**API调用失败**：
- 检查配置文件中的API地址和密钥
- 确认网络可以访问API服务器
- 查看错误信息进行调试

**语音播放异常**：
- 检查电视音量设置
- 确认TTS引擎正常工作
- 尝试重启应用

## 📄 许可证

本项目采用 MIT 许可证。
