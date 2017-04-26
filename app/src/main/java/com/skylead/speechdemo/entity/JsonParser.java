package com.skylead.speechdemo.entity;

import android.speech.SpeechRecognizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/6.
 */

public class JsonParser {

    public static List<SpeechItem> parseUnderstander(String json_str) {
        if (json_str == null || json_str.equals(""))
            return null;

        List<SpeechItem> itemList = new ArrayList<SpeechItem>();
        SpeechItem sitem = new SpeechItem();
        try {

            JSONObject origin_result = new JSONObject(json_str);
            JSONObject content = origin_result.getJSONObject("content");
            JSONObject result = origin_result.getJSONObject("result");

            if (content == null || result == null)
                return null;

//            JSONArray item = content.getJSONArray("item");
//            sitem.text = item.get(0).toString();
            sitem.error_code = result.getInt("err_no");
            if (sitem.error_code != 0) {
                sitem.error_message = getError(sitem.error_code);
                itemList.add(sitem);
            } else {
                String str_res = content.getString("json_res");
                JSONObject json_res = new JSONObject(str_res);
                JSONArray results = json_res.getJSONArray("results");
                JSONObject tmp = (JSONObject) results.get(0);

                sitem.domain = tmp.getString("domain");
                sitem.intent = tmp.getString("intent");
                sitem.object = getSlots(sitem.domain,sitem.intent,tmp.getJSONObject("object"));
                itemList.add(sitem);
            }

            return itemList;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static SpeechResults getSlots(String service, String operation, JSONObject jsonObject) {
        if (jsonObject == null)
            return null;
        try {
            SpeechResults slots = new SpeechResults();
            if (service.equals(SpeechCommonDefination.service_map)){
                SpeechMap map = new SpeechMap();
                if (operation.equals(SpeechCommonDefination.operation_nearby)){
                    map.centre = jsonObject.has("centre")?jsonObject.getString("centre"):null;
                    map.keywords = jsonObject.has("keywords")?jsonObject.getString("keywords"):null;
                    map.lab_tag = jsonObject.has("lbs_tag")?jsonObject.getString("lbs_tag"):null;
                }else if (operation.equals(SpeechCommonDefination.operation_poi)){
                    map.centre = jsonObject.has("centre")?jsonObject.getString("centre"):null;
                }else if (operation.equals(SpeechCommonDefination.operation_route)){
                    map.start = jsonObject.has("start")?jsonObject.getString("start"):null;
                    map.arrival = jsonObject.has("arrival")?jsonObject.getString("arrival"):null;
                    map.route_sort = jsonObject.has("route_sort")?jsonObject.getString("route_sort"):null;
                    map.drive_sort = jsonObject.has("drive_sort")?jsonObject.getString("drive_sort"):null;
                    map.route_type = jsonObject.has("route_type")?jsonObject.getString("route_type"):null;
                }
                slots.mMap = map;
                return slots;
            }

            JSONObject json = new JSONObject("");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    private static String getError(int error) {
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
        return sb.toString();
    }
}
