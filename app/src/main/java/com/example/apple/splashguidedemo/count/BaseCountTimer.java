package com.example.apple.splashguidedemo.count;

import android.os.CountDownTimer;

/**
 * @author crazyZhangxl on 2018/11/27.
 * Describe:
 */
public class BaseCountTimer extends CountDownTimer {
    // 接口
    private ICountDownTimerListener mICountDownTimerListener;

    public BaseCountTimer(long millisInFuture, long countDownInterval,ICountDownTimerListener iCountDownTimerListener) {
        super(millisInFuture, countDownInterval);
        this.mICountDownTimerListener = iCountDownTimerListener;
    }


    @Override
    public void onTick(long millisUntilFinished) {
        // 进行间隔回掉
        if (mICountDownTimerListener != null){
            mICountDownTimerListener.doOnTick(millisUntilFinished/1000);
        }
    }

    @Override
    public void onFinish() {
        // 结束回掉
        if (mICountDownTimerListener != null){
            mICountDownTimerListener.doFinish();
        }
    }
}
