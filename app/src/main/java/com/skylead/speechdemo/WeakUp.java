package com.skylead.speechdemo;

import android.content.Context;
import android.util.AndroidRuntimeException;
import android.util.Log;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class WeakUp {
    Speech myspeech = null;
    private EventManager mWpEventManager;
    private static final String TAG = "ActivityWakeUp";
    private static Context context = null;

    public void init(final Context context) {
        this.context = context;
        // 唤醒功能打开步骤
        // 1) 创建唤醒事件管理器
        mWpEventManager = EventManagerFactory.create(context, "wp");

        Log.d(TAG, "weakup init");

        // 2) 注册唤醒事件监听器
        //        mWpEventManager.registerListener(this);
        mWpEventManager.registerListener(new EventListener() {
            @Override
            public void onEvent(String name, String params, byte[] data, int offset, int length) {
                Log.d(TAG, String.format("event: name=%s, params=%s", name, params));
                try {
                    JSONObject json = new JSONObject(params);
                    if ("wp.data".equals(name)) { // 每次唤醒成功, 将会回调name=wp.data的时间, 被激活的唤醒词在params的word字段

                        String word = json.getString("word");
                        if (myspeech == null) {
                            myspeech = new Speech(context);
                        }
                        myspeech.speech_Init();
                        myspeech.startASR();
                        //                        txtLog.append("唤醒成功, 唤醒词: " + word + "\r\n");
                    } else if ("wp.exit".equals(name)) {
                        //                        txtLog.append("唤醒已经停止: " + params + "\r\n");
                    }
                } catch (JSONException e) {
                    throw new AndroidRuntimeException(e);
                }
            }
        });


        //        txtLog.setText(DESC_TEXT);
    }

    public void start() {
        // 3) 通知唤醒管理器, 启动唤醒功能
        HashMap params = new HashMap();
        params.put("kws-file", "assets:///WakeUp.bin"); // 设置唤醒资源, 唤醒资源请到 http://yuyin.baidu.com/wake#m4 来评估和导出
        mWpEventManager.send("wp.start", new JSONObject(params).toString(), null, 0, 0);

    }

    public void stop() {
        // 停止唤醒监听
        mWpEventManager.send("wp.stop", null, null, 0, 0);
        myspeech = null;
    }

    //    @Override
    //    public void onEvent(String s, String params, byte[] bytes, int i, int i1) {
    //        Log.d(TAG, String.format("event: name=%s, params=%s", name, params));
    //        if (myspeech == null) {
    //            myspeech = new Speech(context);
    //        }
    //        myspeech.speech_Init();
    //        myspeech.startASR();
    //        try {
    //            JSONObject json = new JSONObject(params);
    //            if ("wp.data".equals(name)) { // 每次唤醒成功, 将会回调name=wp.data的时间, 被激活的唤醒词在params的word字段
    //
    //                String word = json.getString("word");
    //                Log.d(TAG, "唤醒成功, 唤醒词: " + word + "\r\n");
    //
    //                if (myspeech == null) {
    //                    myspeech = new Speech(context);
    //                }
    //                myspeech.speech_Init();
    //                myspeech.startASR();
    //
    //
    //                //                        txtLog.append("唤醒成功, 唤醒词: " + word + "\r\n");
    //            } else if ("wp.exit".equals(name)) {
    //                //                        txtLog.append("唤醒已经停止: " + params + "\r\n");
    //
    //            }
    //        } catch (JSONException e) {
    //            throw new AndroidRuntimeException(e);
    //        }
    //    }
}
