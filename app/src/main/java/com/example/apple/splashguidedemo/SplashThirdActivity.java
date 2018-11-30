package com.example.apple.splashguidedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.apple.splashguidedemo.views.CountdownView;

/**
 * @author crazyZhangxl on 2018-11-29 16:03:44.
 * Describe: 另外一种闪频效果
 */

public class SplashThirdActivity extends AppCompatActivity {
    private CountdownView mCountdownView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_third);
        mCountdownView = findViewById(R.id.countDownView);
        mCountdownView.setDuration(5)
                .doViewInitEnd();
        mCountdownView.setStateListener(new CountdownView.CountDownListener() {
            @Override
            public void onFinished() {
                startActivity(new Intent(SplashThirdActivity.this,HomeActivity.class));
                finish();
            }

            @Override
            public void onJumpOnclick() {
                startActivity(new Intent(SplashThirdActivity.this,HomeActivity.class));
                finish();
        }});

    }

    @Override
    public void onBackPressed() {
        if (mCountdownView.isProgressRunning()){
            return;
        }
        super.onBackPressed();
    }
}
