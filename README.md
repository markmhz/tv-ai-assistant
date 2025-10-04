# 雷鸟电视AI助手

一个专为雷鸟电视设计的简单AI语音助手应用，支持语音输入和OpenAI API集成。

## 项目结构

```
lnapp/
├── app/
│   ├── build.gradle
│   ├── proguard-rules.pro
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           ├── java/com/tvai/assistant/
│           │   └── MainActivity.java
│           └── res/
│               ├── layout/
│               │   └── activity_main.xml
│               ├── values/
│               │   ├── strings.xml
│               │   ├── colors.xml
│               │   └── themes.xml
│               ├── mipmap-hdpi/
│               └── xml/
│                   ├── data_extraction_rules.xml
│                   └── backup_rules.xml
├── build.gradle
├── settings.gradle
├── gradle.properties
└── README.md
```

## 功能特点

- 🎤 支持遥控器语音按钮和屏幕语音输入
- 🤖 集成OpenAI API，支持自定义API地址
- 🔊 语音输出，支持中文TTS
- ⚙️ 配置文件管理，无需复杂设置
- 📺 专为电视优化的界面设计

## 编译和安装

### 1. 编译APK
```bash
cd lnapp
./gradlew assembleDebug
```

生成的APK文件位置：`app/build/outputs/apk/debug/app-debug.apk`

### 2. 安装到雷鸟电视

**方法一：ADB安装**
```bash
# 连接电视（确保电视开启开发者模式）
adb connect [电视IP地址]
adb install app/build/outputs/apk/debug/app-debug.apk
```

**方法二：U盘安装**
1. 将APK文件复制到U盘
2. 在电视上插入U盘
3. 通过文件管理器安装APK

## 配置说明

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

## 使用方法

1. **语音输入**：按遥控器语音按钮或点击"语音输入"按钮
2. **文字输入**：在输入框中输入问题，按确定键发送
3. **查看回复**：AI回复会显示在下方，同时语音播放

## 技术架构

- **平台**：Android TV (API 21+)
- **语音识别**：Android SpeechRecognizer
- **语音合成**：Android TextToSpeech
- **网络请求**：OkHttp3
- **JSON处理**：Gson

## 权限说明

- `RECORD_AUDIO`：语音识别功能
- `INTERNET`：网络请求
- `WRITE_EXTERNAL_STORAGE`：配置文件存储

## 注意事项

1. 确保电视连接到网络
2. 首次使用需要授权麦克风权限
3. 语音识别需要网络连接
4. 建议使用稳定的网络环境

## 故障排除

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
