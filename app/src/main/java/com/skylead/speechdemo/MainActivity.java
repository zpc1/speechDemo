package com.skylead.speechdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button start = null;
    Button stop = null;
    TextView tv = null;
    private Button mSpeak, mPause, mResume, mStop;
    private tts mytts = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = (Button) findViewById(R.id.button);
        stop = (Button) findViewById(R.id.button2);
        tv = (TextView) findViewById(R.id.tv);
        this.mSpeak = (Button) this.findViewById(R.id.speak);
        this.mSpeak.setOnClickListener(this);
        this.mPause = (Button) this.findViewById(R.id.pause);
        this.mPause.setOnClickListener(this);
        this.mResume = (Button) this.findViewById(R.id.resume);
        this.mResume.setOnClickListener(this);
        this.mStop = (Button) this.findViewById(R.id.stop);
        this.mStop.setOnClickListener(this);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        //tts
        if (mytts == null)
            mytts = new tts(getApplicationContext());
        mytts.init_tts();

        //        Intent intent = new Intent(this, MyService.class);
//        startActivity(intent);

    }

    private Handler msgHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.speak:
                mytts.speak("hello,语音合成测试成功");
                break;
            case R.id.pause:
                mytts.pause();
                break;
            case R.id.resume:
                mytts.resume();
                break;
            case R.id.stop:
                mytts.stop();
                break;
            case R.id.button:
                Intent startservice = new Intent(getApplicationContext(), MyService.class);
                startService(startservice);
                break;
            case R.id.button2:
                Intent stopservice = new Intent(getApplicationContext(), MyService.class);
                stopService(stopservice);
                break;
        }
    }
}
