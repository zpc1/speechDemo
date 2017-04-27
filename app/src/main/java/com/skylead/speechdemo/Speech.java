package com.skylead.speechdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;

import com.baidu.speech.VoiceRecognitionService;
import com.skylead.speechdemo.Util.ConnectUtil;
import com.skylead.speechdemo.Util.UrltoGbk;
import com.skylead.speechdemo.entity.SpeechItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;

import static android.os.MessageQueue.OnFileDescriptorEventListener.EVENT_ERROR;

/**
 * Created by Administrator on 2017/3/30.
 */

public class Speech implements HttpListen {
    private SpeechRecognizer speechRecognizer;
    private Context context = null;
    private static final String TAG = "SpeechUtile";
    private Handler mHandler;
    private long num = 0;

    public Speech(Context context, Handler handler) {
        this.context = context;
        this.mHandler = handler;
        //        final Thread thread = new Thread(new Runnable() {
        //            @Override
        //            public void run() {
        //                mHandler.sendMessage(mHandler.obtainMessage(1));
        //            }
        //        });
    }

    public void speech_Init() {
        // 创建识别器
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context, new ComponentName(context, VoiceRecognitionService.class));
        // 注册监听器
        speechRecognizer.setRecognitionListener(mrecognitionListen);
    }

    // 开始识别
    void startASR() {
        Intent intent = new Intent();
        bindParams(intent);
        speechRecognizer.startListening(intent);
    }

    public void bindParams(Intent intent) {
        int prop = 10060;
        intent.putExtra("sound_start", 0);//说话开始的提示音
        intent.putExtra("sound_end", 0);//说话结束的提示音
        intent.putExtra("sound_success", 0);//识别成功的提示音
        intent.putExtra("sound_error", 0);//识别出错的提示音
        intent.putExtra("nlu", "enable");//开启语义解析

        intent.putExtra("prop", prop); // 地图
        if (prop == 10060) { // 地图
            intent.putExtra("lm-res-file-path", "/sdcard/s_2_Navi");
        } else if (prop == 20000) { // 输入法
            intent.putExtra("lm-res-file-path", "/sdcard/s_2_InputMethod");
        }
    }

    public RecognitionListener mrecognitionListen = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            mHandler.sendMessage(mHandler.obtainMessage(111, "// 准备就绪\r\n等待语音输入..."));
            Log.d(TAG, "// 准备就绪\r\n等待语音输入...");
        }

        @Override
        public void onBeginningOfSpeech() {
            Log.d(TAG, "// 开始说话处理");
        }


        @Override
        public void onRmsChanged(float v) {
            Log.d(TAG, "// 音量变化处理");
        }

        @Override
        public void onBufferReceived(byte[] bytes) {
            Log.d(TAG, "// 录音数据传出处理");
        }

        @Override
        public void onEndOfSpeech() {
            mHandler.sendMessage(mHandler.obtainMessage(111, "// 说话结束处理"));
            Log.d(TAG, "// 说话结束处理");
        }

        @Override
        public void onError(int error) {
            Log.d(TAG, "// 说话结束处理");
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
            mHandler.sendMessage(mHandler.obtainMessage(111, sb.toString()));
            Log.d(TAG, "识别失败：" + sb.toString());
        }

        private String domain;

        @Override
        public void onResults(Bundle res) {
            if (speechRecognizer == null)
                return;

            if (null != res) {
                Log.e(TAG, "----res不为空---");
                ArrayList<String> nbest = res.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                //                Log.d(TAG, "识别成功：" + Arrays.toString(nbest.toArray(new String[nbest.size()])));
                //                mHandler.sendMessage(mHandler.obtainMessage(111,Arrays.toString(nbest.toArray(new String[nbest.size()])).toString()));
                try {
                    Log.e(TAG, "----begin---");
                    String obj = "识别结果：" + Arrays.toString(nbest.toArray(new String[nbest.size()])).toString();
                    Log.e(TAG, "-------" + obj);
                    Message msg = mHandler.obtainMessage();
                    msg.what = 111;
                    msg.obj = obj;
                    mHandler.sendMessage(msg);
                    Log.e(TAG, "----end---");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "----1111111111---");
                String json_str = res.getString("origin_result");
                SpeechItem sitem = new SpeechItem();
                ConnectUtil util = new ConnectUtil(Speech.this);
                if (nbest != null)
                    util.sendRequestWithHttpClient(nbest.get(0));
                //                //解析
                //                List<SpeechItem> a = JsonParser.parseUnderstander(json_str);
                //                SpeechItem tmp = a.get(0);
                //                if (tmp.domain){
                //                    Log.d("ActivityWakeUp", "raw_text:"+tmp.text+" 领域:"+tmp.domain+" 意图:"+tmp.intent+" 匹配率:"+tmp.score)
                //                }else {
                //
                //                }
                //                try {
                //                    JSONObject origin_result = new JSONObject(json_str);
                //                    JSONObject content = origin_result.getJSONObject("content");
                //                    JSONObject result = origin_result.getJSONObject("result");
                //                    JSONArray item = content.getJSONArray("item");
                //                    String text = item.get(0).toString();
                //                    if (result.getInt("err_no")!=0){
                //                        Log.d(TAG,"语音识别sdk错误");
                //                    }else {
                //                        String str_res = content.getString("json_res");
                //                        JSONObject json_res = new JSONObject(str_res);
                //                        JSONArray results = json_res.getJSONArray("results");
                //                        JSONObject tmp = (JSONObject) results.get(0);
                //
                //                        sitem.domain = tmp.getString("domain");
                //                        sitem.intent = tmp.getString("intent");
                //                    }
                //                    if (sitem.domain == null || sitem.intent == null) {
                //                        ConnectUtil.sendRequestWithHttpClient(text);
                ////                        Log.d(TAG, "origin_result=\n" + new JSONObject(json_str).toString(4));
                //                    }
                //                } catch (Exception e) {
                //                    Log.d(TAG, "origin_result=[warning: bad json]\n" + json_str);
                //                }
            } else {

            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            ArrayList<String> nbest = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (nbest.size() > 0) {
                Log.d(TAG, "~临时识别结果：" + Arrays.toString(nbest.toArray(new String[0])));
                //            txtResult.setText(nbest.get(0));
            }
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            switch (eventType) {
                case EVENT_ERROR:
                    String reason = params.get("reason") + "";
                    mHandler.sendMessage(mHandler.obtainMessage(111, reason));
                    Log.d(TAG, "EVENT_ERROR, " + reason);
                    break;
                case VoiceRecognitionService.EVENT_ENGINE_SWITCH:
                    int type = params.getInt("engine_type");
                    mHandler.sendMessage(mHandler.obtainMessage(111, "*引擎切换至" + (type == 0 ? "在线" : "离线")));
                    Log.d(TAG, "*引擎切换至" + (type == 0 ? "在线" : "离线"));
                    break;
            }
        }
    };


    @Override
    public void onResult(String result) {
        if (result != null) {
            StringBuffer log = new StringBuffer();
            String tmp = getResult(result);
            log.append(tmp);
            log.append("\r\n=================");
            mHandler.sendMessage(mHandler.obtainMessage(111, "HTTP:\r\n" + log));
            Log.d(TAG, result);
        }
    }

    private String getResult(String original) {
        JSONObject json = null;
        StringBuffer result = new StringBuffer();
        try {
            json = new JSONObject(original);
            String type = json.has("type")?json.getString("type"):null;
            String id_text = json.has("id_text")?json.getString("id_text"):null;
            if (id_text!= null){
                id_text = URLDecoder.decode(id_text);
            }
            String domain = json.has("domain")?json.getString("domain"):null;
            String raw_text = json.has("raw_text")?json.getString("raw_text"):null;
            if (raw_text!= null){
                raw_text = UrltoGbk.decoder(raw_text);
            }
            String pinyin = json.has("pinyin")?json.getString("pinyin"):null;
            int id = json.has("id")?json.getInt("id"):0;
            String score = json.has("score")?json.getString("score"):null;

            return type+ " | " +id_text+ " | " + domain + " | "
                    + raw_text + " | " + pinyin + " | "
                    +id + " | " + score;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
