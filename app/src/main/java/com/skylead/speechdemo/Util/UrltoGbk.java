package com.skylead.speechdemo.Util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2017/4/26.
 */

//解决url中中文问题
public class UrltoGbk {
    public static String decoder(String s){
        String rtn =null;
        try {
            rtn = URLDecoder.decode(s,"GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return rtn;
    }

    public static String encoder(String s){
        String rtn =null;
        try {
            rtn = URLEncoder.encode(s,"GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return rtn;
    }
}
