package com.example.apple.splashguidedemo;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.apple.splashguidedemo.views.JumpTextView;

/**
 * @author crazyZhangxl on 2018-11-16 11:27:44.
 * Describe: 闪屏页2 采用自定义View + ValueAnimator实现 效果好看一点
 */

public class SplashSecondActivity extends AppCompatActivity {
    private JumpTextView mJumpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_second);

        mJumpTextView =  findViewById(R.id.jumpView);
        mJumpTextView.setTimeDuration(6).setProgressColor(Color.GREEN)
                .setCenterTextSize(16)
                .setProgressWidth(4)
                .doJumpTextInitEnd();
        mJumpTextView.setMyViewClickListener(new JumpTextView.MyViewClickListener() {

            @Override
            public void onJumpOnclick() {
                finish();
                startActivity(new Intent(SplashSecondActivity.this,HomeActivity.class));
            }

            @Override
            public void onJumpEnd() {
                finish();
                startActivity(new Intent(SplashSecondActivity.this,HomeActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (mJumpTextView.isProgressRunning){
            return;
        }
        super.onBackPressed();
    }
}
