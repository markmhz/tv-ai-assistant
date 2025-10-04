# GitHub在线编译设置指南

## 📋 上传到GitHub的步骤

### 1. 创建GitHub仓库

1. 访问 [GitHub.com](https://github.com)
2. 点击右上角的 "+" 号，选择 "New repository"
3. 填写仓库信息：
   - **Repository name**: `tv-ai-assistant`
   - **Description**: `雷鸟电视AI语音助手`
   - **Visibility**: 选择 "Public"（公开）
   - **不要**勾选 "Add a README file"（我们已经有了）
4. 点击 "Create repository"

### 2. 上传项目文件

#### 方法一：使用GitHub网页界面（推荐）

1. 在新建的仓库页面，点击 "uploading an existing file"
2. 将整个 `lnapp` 文件夹的内容拖拽到上传区域
3. 在底部填写提交信息：
   - **Commit message**: `Initial commit: TV AI Assistant`
4. 点击 "Commit changes"

#### 方法二：使用Git命令行

```bash
# 在项目目录中执行
git init
git add .
git commit -m "Initial commit: TV AI Assistant"
git branch -M main
git remote add origin https://github.com/你的用户名/tv-ai-assistant.git
git push -u origin main
```

### 3. 触发自动编译

上传完成后，GitHub Actions会自动开始编译：

1. 点击仓库页面的 "Actions" 标签
2. 查看 "Build Android APK" 工作流
3. 等待编译完成（通常需要5-10分钟）

### 4. 下载APK

编译完成后：

1. 在Actions页面找到成功的构建
2. 点击进入构建详情
3. 在 "Artifacts" 部分下载 `tv-ai-assistant-apk`
4. 解压下载的文件，找到 `app-debug.apk`

### 5. 安装到雷鸟电视

1. 将 `app-debug.apk` 复制到U盘
2. 在雷鸟电视上插入U盘
3. 通过文件管理器安装APK
4. 首次运行需要授权麦克风权限

## 🔄 重新编译

如果需要重新编译：

1. 进入仓库的 "Actions" 页面
2. 选择 "Build Android APK" 工作流
3. 点击 "Run workflow" 按钮
4. 等待编译完成并下载新的APK

## ⚠️ 注意事项

- 确保仓库设为公开，否则GitHub Actions可能无法正常工作
- 编译过程需要网络连接，请耐心等待
- 如果编译失败，检查Actions页面的错误日志
- APK文件会在30天后自动删除，请及时下载

## 🆘 遇到问题？

如果遇到问题：

1. 检查GitHub Actions的构建日志
2. 确认所有文件都已正确上传
3. 检查仓库是否为公开状态
4. 尝试手动触发编译

## 📞 支持

如有问题，请查看：
- 项目README文件
- GitHub Actions构建日志
- 雷鸟电视使用说明
