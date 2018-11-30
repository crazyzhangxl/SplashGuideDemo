package com.example.apple.splashguidedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.apple.splashguidedemo.count.BaseCountTimer;
import com.example.apple.splashguidedemo.count.ICountDownTimerListener;

/**
 * @author crazyZhangxl on 2018-11-27 8:35:32.
 * Describe:
 */

public class CountDownActivity extends AppCompatActivity implements ICountDownTimerListener {
    private TextView mTvText;
    private BaseCountTimer mBaseCountTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);
        mTvText = findViewById(R.id.tvText);

        // 创建对象 ———————— 并且执行 ————————————
        mBaseCountTimer = new BaseCountTimer(6000, 1000, this);
        mBaseCountTimer.start();
    }

    @Override
    public void doOnTick(long seconds) {
        mTvText.setText("进行时间: "+seconds+"秒");
    }

    @Override
    public void doFinish() {
        mTvText.setText("结束了");
        finish();
    }

    @Override
    public void onBackPressed() {
        // 按返回键的时候 进行回收,防止造成内存泄漏
        if (mBaseCountTimer != null){
            mBaseCountTimer.onFinish();
            mBaseCountTimer.cancel();
            mBaseCountTimer = null;
        }
        super.onBackPressed();
    }
}
