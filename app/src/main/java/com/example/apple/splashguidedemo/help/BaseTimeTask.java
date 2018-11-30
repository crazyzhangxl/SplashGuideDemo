package com.example.apple.splashguidedemo.help;

import java.util.TimerTask;

/**
 * @author crazyZhangxl on 2018/11/16.
 * Describe:
 */
public class BaseTimeTask extends TimerTask {
    private BaseTimeTaskListener mBaseTimeTaskListener;

    public BaseTimeTask(BaseTimeTaskListener baseTimeTaskListener) {
        mBaseTimeTaskListener = baseTimeTaskListener;
    }

    @Override
    public void run() {
        if (mBaseTimeTaskListener != null){
            mBaseTimeTaskListener.doTimeTask();
        }
    }
}
