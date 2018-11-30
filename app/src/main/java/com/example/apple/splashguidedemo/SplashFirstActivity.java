package com.example.apple.splashguidedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.example.apple.splashguidedemo.help.BaseTimeTask;
import com.example.apple.splashguidedemo.help.BaseTimeTaskListener;

import java.util.Timer;

/**
 * @author crazyZhangxl on 2018-11-16 11:27:08.
 * Describe: 闪屏页1  采用TextView + Timer定时器实现
 */

public class SplashFirstActivity extends AppCompatActivity implements BaseTimeTaskListener {
    private Timer mTimer;
    private AppCompatTextView mTvTime;
    private int mCount = 6;

    private void  initTimer(){
        mTimer = new Timer();
        mTimer.schedule(new BaseTimeTask(this),0,1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mTvTime = findViewById(R.id.tvTime);
        mTvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimer != null){
                    mTimer.cancel();
                    mTimer = null;
                    doJump();
                }
            }
        });
        initTimer();
    }


    private void  doJump(){
        startActivity(new Intent(SplashFirstActivity.this,HomeActivity.class));
        finish();
    }

    @Override
    public void doTimeTask() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mCount > 0){
                    mTvTime.setText(String.format("跳过\n%d秒",mCount));
                    mCount --;
                }else {
                    mTimer.cancel();
                    mTimer = null;
                    doJump();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // 如果定时器还存在 那么直接返回
        if (mTimer != null){
            return;
        }
        super.onBackPressed();
    }

}
