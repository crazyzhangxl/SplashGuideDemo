package com.example.apple.splashguidedemo.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class CountdownView extends View {
    /**
     * 默认宽高
     */
    private int width = 60, height = 60;

    private int measureWidth,measureHeight;

    private Point mCenterPoint;

    private int radius;
    /**
     * 圆环的宽度
     */
    private int strokeWidth = 4;
    /**
     * 中间文字的尺寸
     */
    private int textSize = 18;
    private RectF rectF;

    ValueAnimator valueAnimator;

    /**
     * 倒计时持续时间，秒
     */
    private int duration = 4;
    /**
     * 剩余时间，秒。用于展示
     */
    private String timeRemians;
    private int currentDegree;

    private Paint backgroundPaint, forgroundPaint, textPaint ,centerPaint;
    private int centerColor = Color.YELLOW;
    private int backgroundColor = Color.GREEN;
    private int forgroundColor = Color.RED;
    private int textColor = Color.BLACK;
    private Rect textBounds = new Rect();
    private boolean isProgressRunning = false;
    private CountDownListener listener;
    private float mDensity;


    //-------------------------------   使用开始  --------------------------------

    public CountdownView setTextSize(int textSize) {
        this.textSize = textSize;
        textPaint.setTextSize(textSize * mDensity);
        return this;
    }

    /**
     * 设置进度条的宽度
     * @param strokeWidth
     * @return
     */
    public CountdownView setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        backgroundPaint.setStrokeWidth(strokeWidth);
        forgroundPaint.setStrokeWidth(strokeWidth);
        return this;
    }

    /**
     *
     * 设置颜色
     * @param centerColor     中心圆的颜色
     * @param backgroundColor 背景圆环颜色
     * @param forgroundColor  移动的圆环颜色
     * @param textColor       中间的文字颜色
     * @return
     */
    public CountdownView setColor(int centerColor,int backgroundColor, int forgroundColor, int textColor) {
        this.centerColor = centerColor;
        this.backgroundColor = backgroundColor;
        this.forgroundColor = forgroundColor;
        this.textColor = textColor;
        centerPaint.setColor(centerColor);
        backgroundPaint.setColor(backgroundColor);
        forgroundPaint.setColor(forgroundColor);
        textPaint.setColor(textColor);
        return this;
    }

    /**
     * 动画持续时间
     * @param duration 秒为单位
     * @return
     */
    public CountdownView setDuration(int duration){
        this.duration = duration;
        return this;
    }

    /**
     * 初始化结束 ---- 动画开始的入口
     */
    public void doViewInitEnd(){
        // 初始化时间
        initTime();
        // 结束动画
        startAnimator();
    }

    //-------------------------------   使用结束  --------------------------------


    /**
     * 设置状态监听
     * @param listener
     */
    public void setStateListener(CountDownListener listener){
        this.listener = listener;
    }

    public boolean isProgressRunning(){
        return isProgressRunning;
    }


    // ------------  动画执行相关  -------------

    private void initAnimator(){
        valueAnimator = ValueAnimator.ofInt(0, 360);
        //圆弧角度从0到360度
        valueAnimator.setDuration(duration * 1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int degree = (int) valueAnimator.getAnimatedValue();
                currentDegree = degree;
                timeRemians = String.valueOf(duration - valueAnimator.getCurrentPlayTime() / 1000);
                textPaint.getTextBounds(timeRemians, 0, timeRemians.length(), textBounds);
                postInvalidate();
                if (degree == 360 && listener != null) {
                    listener.onFinished();
                }
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isProgressRunning = false;
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                isProgressRunning = true;
                super.onAnimationStart(animation);
            }
        });
    }

    /**
     * 当手指抬起的时候响应点击事件
     *
     * 啊这里是无奈之举,为啥不响应点击事件呢,后面我得探究下
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ( event.getAction() == MotionEvent.ACTION_UP){
            if (listener != null){
                listener.onJumpOnclick();
                stopAnimator();
            }
        }
        return true;
    }

    /**
     * 开启动画
     */
    private void startAnimator(){
        if (valueAnimator == null){
            initAnimator();
        }
        valueAnimator.start();
    }

    /**
     * 结束动画
     */
    public void stopAnimator(){
        if (valueAnimator != null){
            valueAnimator.cancel();
            valueAnimator.removeAllUpdateListeners();
            valueAnimator.removeAllListeners();
            valueAnimator = null;
        }
    }



    public interface CountDownListener {
        void onFinished();
        void onJumpOnclick();

    }

    public CountdownView(Context context) {
        this(context,null);
    }

    public CountdownView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public CountdownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始化画笔
        mCenterPoint = new Point();
        initAttrs();
        initPaint();
    }

    private void initAttrs() {
        mDensity = getResources().getDisplayMetrics().density;
        width = (int) (mDensity * width);
        height = (int) (mDensity * height);
        textSize = (int)(mDensity * textSize);
    }


    private void initPaint() {
        centerPaint = new Paint();
        centerPaint.setAntiAlias(true);
        centerPaint.setStyle(Paint.Style.FILL);
        centerPaint.setColor(centerColor);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(strokeWidth);
        backgroundPaint.setAntiAlias(true);

        forgroundPaint = new Paint();
        forgroundPaint.setColor(forgroundColor);
        forgroundPaint.setStyle(Paint.Style.STROKE);
        forgroundPaint.setStrokeWidth(strokeWidth);
        forgroundPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
        textPaint.setAntiAlias(true);
    }

    private void initTime() {
        timeRemians = String.valueOf(duration);
        textPaint.getTextBounds(timeRemians, 0, timeRemians.length(), textBounds);
    }

    /**
     * 进行测量
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthResult = widthSize;
        int heightResult = heightSize;
        if (MeasureSpec.AT_MOST == widthMode){
            widthResult = width;
        }

        if (MeasureSpec.AT_MOST == heightMode){
            heightResult = height;
        }
        setMeasuredDimension(widthResult, heightResult);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        measureWidth = w;
        measureHeight = h;
        mCenterPoint.x = w/2;
        mCenterPoint.y = h/2;
        radius = Math.min(w,h)/2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (rectF == null) {
            rectF = new RectF(strokeWidth / 2, strokeWidth / 2, measureWidth - strokeWidth / 2, measureHeight - strokeWidth / 2);
        }
        // 绘制中心圆
        canvas.drawCircle(mCenterPoint.x,mCenterPoint.y,radius - strokeWidth,centerPaint);
        // 绘制背景圆弧
        canvas.drawArc(rectF, 270, 360, false, backgroundPaint);
        // 绘制移动的进度条圆弧
        canvas.drawArc(rectF, 270, currentDegree, false, forgroundPaint);
        // 绘制中心文本
        canvas.drawText(timeRemians, measureWidth / 2, measureHeight / 2 + textBounds.height() / 2, textPaint);
    }
}