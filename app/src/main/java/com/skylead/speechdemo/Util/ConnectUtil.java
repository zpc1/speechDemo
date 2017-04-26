package com.skylead.speechdemo.Util;

import android.util.Log;


import com.skylead.speechdemo.HttpListen;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.methods.HttpGet;


/**
 * Created by Administrator on 2017/4/20.
 */

public class ConnectUtil {
    HttpListen listen = null;
    public ConnectUtil(HttpListen listen) {
        if (this.listen == null){
            this.listen = listen;
        }
    }

    public  void sendRequestWithHttpClient(final String cmd) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    // 指定访问的服务器地址是电脑本机
                    //					HttpGet httpGet = new HttpGet("http://10.0.2.2/get_data.json");
                    String url = "http://139.224.237.6:8081/?name="+cmd;
                    HttpGet httpGet = new HttpGet(url);
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        // 请求和响应都成功了
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity, "utf-8");
                        Log.d("MainActivity", response);
                        listen.onResult(response);
                        //						parseJSONWithGSON(response);
                        // parseJSONWithJSONObject(response);
                        // parseXMLWithPull(response);
                        // parseXMLWithSAX(response);
                        // Message message = new Message();
                        // message.what = SHOW_RESPONSE;
                        // // 将服务器返回的结果存放到Message中
                        // message.obj = response.toString();
                        // handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
