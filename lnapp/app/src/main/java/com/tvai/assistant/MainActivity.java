package com.tvai.assistant;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;
import okhttp3.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final int SPEECH_REQUEST_CODE = 0;
    
    private EditText inputText;
    private TextView resultText;
    private Button voiceButton;
    private Button sendButton;
    private TextToSpeech tts;
    private SpeechRecognizer speechRecognizer;
    private Properties config;
    private String configPath;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initViews();
        initConfig();
        initTTS();
        initSpeechRecognizer();
        checkPermissions();
    }
    
    private void initViews() {
        inputText = findViewById(R.id.inputText);
        resultText = findViewById(R.id.resultText);
        voiceButton = findViewById(R.id.voiceButton);
        sendButton = findViewById(R.id.sendButton);
        
        voiceButton.setOnClickListener(v -> startVoiceInput());
        sendButton.setOnClickListener(v -> sendMessage());
        
        // 监听遥控器按键
        inputText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || 
                    keyCode == KeyEvent.KEYCODE_ENTER) {
                    sendMessage();
                    return true;
                }
            }
            return false;
        });
    }
    
    private void initConfig() {
        config = new Properties();
        configPath = getExternalFilesDir(null) + "/config.properties";
        loadConfig();
    }
    
    private void loadConfig() {
        try {
            File configFile = new File(configPath);
            if (configFile.exists()) {
                config.load(new FileInputStream(configFile));
            } else {
                // 创建默认配置
                config.setProperty("openai_url", "https://api.openai.com/v1/chat/completions");
                config.setProperty("api_key", "your-api-key-here");
                config.setProperty("model", "gpt-3.5-turbo");
                saveConfig();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void saveConfig() {
        try {
            config.store(new FileOutputStream(configPath), "TV AI Assistant Config");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void initTTS() {
        tts = new TextToSpeech(this, this);
    }
    
    private void initSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
    }
    
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) 
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                new String[]{Manifest.permission.RECORD_AUDIO}, 
                REQUEST_RECORD_AUDIO_PERMISSION);
        }
    }
    
    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.CHINESE);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "请说话...");
        
        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(this, "语音识别不可用", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) {
                inputText.setText(results.get(0));
                sendMessage();
            }
        }
    }
    
    private void sendMessage() {
        String message = inputText.getText().toString().trim();
        if (message.isEmpty()) {
            Toast.makeText(this, "请输入问题", Toast.LENGTH_SHORT).show();
            return;
        }
        
        resultText.setText("正在思考...");
        sendButton.setEnabled(false);
        
        new Thread(() -> {
            try {
                String response = callOpenAI(message);
                runOnUiThread(() -> {
                    resultText.setText(response);
                    speakText(response);
                    sendButton.setEnabled(true);
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    resultText.setText("错误: " + e.getMessage());
                    sendButton.setEnabled(true);
                });
            }
        }).start();
    }
    
    private String callOpenAI(String message) throws IOException {
        OkHttpClient client = new OkHttpClient();
        
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", config.getProperty("model", "gpt-3.5-turbo"));
        
        JsonObject messageObj = new JsonObject();
        messageObj.addProperty("role", "user");
        messageObj.addProperty("content", message);
        
        JsonObject[] messages = {messageObj};
        requestBody.add("messages", new Gson().toJsonTree(messages));
        requestBody.addProperty("max_tokens", 1000);
        
        RequestBody body = RequestBody.create(
            requestBody.toString(), 
            MediaType.parse("application/json")
        );
        
        Request request = new Request.Builder()
            .url(config.getProperty("openai_url"))
            .addHeader("Authorization", "Bearer " + config.getProperty("api_key"))
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("API调用失败: " + response.code());
            }
            
            String responseBody = response.body().string();
            JsonObject jsonResponse = new Gson().fromJson(responseBody, JsonObject.class);
            return jsonResponse.getAsJsonArray("choices")
                .get(0).getAsJsonObject()
                .getAsJsonObject("message")
                .get("content").getAsString();
        }
    }
    
    private void speakText(String text) {
        if (tts != null) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }
    
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.CHINESE);
            if (result == TextToSpeech.LANG_MISSING_DATA || 
                result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "中文语音不支持", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 处理遥控器语音按钮
        if (keyCode == KeyEvent.KEYCODE_VOICE_ASSIST) {
            startVoiceInput();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        super.onDestroy();
    }
}
