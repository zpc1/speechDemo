package com.skylead.speechdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/5/3.
 */

public class MyTestView extends TextView{
    public MyTestView(Context context) {
        super(context);
    }

    public MyTestView(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    public MyTestView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
    }

    public boolean isFocused(){
        return true;
    }
}
