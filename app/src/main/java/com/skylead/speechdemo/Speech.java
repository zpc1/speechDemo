package com.skylead.speechdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;

import com.baidu.speech.VoiceRecognitionService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static android.os.MessageQueue.OnFileDescriptorEventListener.EVENT_ERROR;

/**
 * Created by Administrator on 2017/3/30.
 */

public class Speech  implements RecognitionListener{
    private SpeechRecognizer speechRecognizer;
    private Context context = null;
    private static final String TAG = "SpeechUtile";

    public Speech(Context context) {
        this.context = context;
    }



    public void speech_Init(){
        // 创建识别器
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context, new ComponentName(context, VoiceRecognitionService.class));
        // 注册监听器
        speechRecognizer.setRecognitionListener(this);
    }

    // 开始识别
    void startASR() {
        Intent intent = new Intent();
        bindParams(intent);
        speechRecognizer.startListening(intent);
    }
    public void bindParams(Intent intent){
        int prop = 10060;
        intent.putExtra("sound_start", 0);//说话开始的提示音
        intent.putExtra("sound_end", 0);//说话结束的提示音
        intent.putExtra("sound_success", 0);//识别成功的提示音
        intent.putExtra("sound_error", 0);//识别出错的提示音
        intent.putExtra("nlu", "enable");//开启语义解析
        intent.putExtra("nlu", "enable");//开启语义解析
        intent.putExtra("prop", prop); // 地图
        intent.putExtra("asr-base-file-path", "/sdcard/s_1");
        if (prop == 10060) { // 地图
            intent.putExtra("lm-res-file-path", "/sdcard/s_2_Navi");
        } else if (prop == 20000) { // 输入法
            intent.putExtra("lm-res-file-path", "/sdcard/s_2_InputMethod");
        }
    }
    @Override
    public void onReadyForSpeech(Bundle bundle) {
        Log.d(TAG,"// 准备就绪");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d(TAG,"// 开始说话处理");
    }

    @Override
    public void onRmsChanged(float v) {
        Log.d(TAG,"// 音量变化处理");
    }

    @Override
    public void onBufferReceived(byte[] bytes) {
        Log.d(TAG,"// 录音数据传出处理");
    }

    @Override
    public void onEndOfSpeech() {
        Log.d(TAG,"// 说话结束处理");
    }

    @Override
    public void onError(int error) {
        Log.d(TAG,"// 说话结束处理");
        StringBuilder sb = new StringBuilder();
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:
                sb.append("音频问题");
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                sb.append("没有语音输入");
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                sb.append("其它客户端错误");
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                sb.append("权限不足");
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                sb.append("网络问题");
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                sb.append("没有匹配的识别结果");
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                sb.append("引擎忙");
                break;
            case SpeechRecognizer.ERROR_SERVER:
                sb.append("服务端错误");
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                sb.append("连接超时");
                break;
        }
        sb.append(":" + error);
        Log.d(TAG, "识别失败：" + sb.toString());
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> nbest = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        Log.d(TAG, "识别成功：" + Arrays.toString(nbest.toArray(new String[nbest.size()])));
        String json_res = results.getString("origin_result");
        try {
            Log.d(TAG, "origin_result=\n" + new JSONObject(json_res).toString(4));
        } catch (Exception e) {
            Log.d(TAG, "origin_result=[warning: bad json]\n" + json_res);
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        ArrayList<String> nbest = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (nbest.size() > 0) {
            Log.d(TAG,"~临时识别结果：" + Arrays.toString(nbest.toArray(new String[0])));
//            txtResult.setText(nbest.get(0));
        }
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        switch (eventType) {
            case EVENT_ERROR:
                String reason = params.get("reason") + "";
                Log.d(TAG,"EVENT_ERROR, " + reason);
                break;
            case VoiceRecognitionService.EVENT_ENGINE_SWITCH:
                int type = params.getInt("engine_type");
                Log.d(TAG,"*引擎切换至" + (type == 0 ? "在线" : "离线"));
                break;
        }
    }
}
