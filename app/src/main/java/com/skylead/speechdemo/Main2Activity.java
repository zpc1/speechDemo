package com.skylead.speechdemo;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{
    private Button speechBtn = null;
    private TextView mShowText = null;
    private Speech myspeech = null;
    private static final String TAG = "Main2Activity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
                initialView();
                getVoice();

    }
    public static final int RESULT_CODE_STARTAUDIO = 1;
    private void getVoice(){
       // ------------------------------------------打开音频权限------------------------------------------------
        if (PackageManager.PERMISSION_GRANTED ==   ContextCompat.
                checkSelfPermission(getApplicationContext(), android.Manifest.permission.RECORD_AUDIO)) {
        }else{
            //提示用户开户权限音频
            String[] perms = {"android.permission.RECORD_AUDIO"};
            ActivityCompat.requestPermissions(this,perms, RESULT_CODE_STARTAUDIO);
        }

    }
    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){
        switch(permsRequestCode){
            case RESULT_CODE_STARTAUDIO:
                boolean albumAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                if(!albumAccepted){
                    Toast.makeText(this, "请开启应用录音权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void initialView(){
        this.speechBtn = (Button) this.findViewById(R.id.speech);
        this.speechBtn.setOnClickListener(this);
        this.mShowText = (TextView) this.findViewById(R.id.showText);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.speech:
                if (myspeech == null) {
                    myspeech = new Speech(getApplicationContext(), msgHandler);
                }
                myspeech.speech_Init();
                myspeech.startASR();
                break;

        }
    }

    public Handler msgHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            print(msg);
        }
    };
    private void print(Message msg) {
        String message = (String) msg.obj;
        if (message != null) {
            Log.w(TAG, message);
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            scrollLog(message);
        }
    }
    private void scrollLog(String message) {
        Spannable colorMessage = new SpannableString(message + "\n");
        colorMessage.setSpan(new ForegroundColorSpan(0xff0000ff), 0, message.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mShowText.append(colorMessage);
        Layout layout = mShowText.getLayout();
        if (layout != null) {
            int scrollAmount = layout.getLineTop(mShowText.getLineCount()) - mShowText.getHeight();
            if (scrollAmount > 0) {
                mShowText.scrollTo(0, scrollAmount + mShowText.getCompoundPaddingBottom());
            } else {
                mShowText.scrollTo(0, 0);
            }
        }
    }
}
