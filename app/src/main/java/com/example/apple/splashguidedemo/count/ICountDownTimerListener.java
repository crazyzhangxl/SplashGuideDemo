package com.example.apple.splashguidedemo.count;

/**
 * @author crazyZhangxl on 2018/11/27.
 * Describe: 定义的定时回掉接口
 */
public interface ICountDownTimerListener {
    /**
     * 间隔一定的时间调用
     * @param seconds 代表当前进行到第几秒了
     */
    void doOnTick(long seconds);

    /**
     * 定时结束时进行回掉
     */
    void doFinish();
}
