package com.example.apple.splashguidedemo.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author crazyZhangxl on 2018/11/16.
 * Describe: 一个自定义用于跳过的自定义View
 *
 * 再次明确下代码执行的顺序:
 *
 * 构造方法(init) -------->  onFinishInflate() 加载布局完成 ---------> Java代码中设置的参数
 *
 * --------> onMeasure() ------------> onSizeChanged() ---------------> onDraw()
 */
public class JumpTextView extends View{
    private MyViewClickListener mMyViewClickListener;

    private boolean mIsFirstMeasureOK = true;
    /**
     * 默认的宽和高
     */
    private int mDfWidthHeight = 60;

    /**
     * 倒计时的时间
     */
    private int mTimeSecond = 4;
    /**
     * 进度条画笔
     */
    private Paint mProgressPaint;

    /**
     * 字体画笔
     */
    private Paint mTextPaint;

    /**
     * 背景颜色画笔
     */
    private Paint mBgPaint;

    /**
     * 中点
     */
    private Point mCenterPoint;
    /**
     * 半径长度
     */
    private int mRadius;

    private int mPsPaintWidth = 4;

    private ValueAnimator mValueAnimator;

    /**
     * 标示进度是否正在继续
     */
    public boolean isProgressRunning = false;

    private Path mAllPath;
    private Path mMDistPath;
    private PathMeasure mPathMeasure;
    private float mAnimatedValue;
    private String mText = "跳过";
    private int ANIMATOR_DURATION;
    private int mTextSize = 15;

    private int mInnerCircleColor = Color.WHITE;
    private int mProgressColor = Color.GREEN;
    private int mTextColor = Color.RED;

    private boolean mIsCanDrawing = false;

    public interface  MyViewClickListener{
        void  onJumpOnclick();
        void  onJumpEnd();
    }

    public void setMyViewClickListener(MyViewClickListener myViewClickListener){
        this.mMyViewClickListener = myViewClickListener;
    }


    /**
     *  -------------------------  使用开始  -------------------------
     * */

    public JumpTextView setCenterTextSize(int textSpSize){
        this.mTextSize = textSpSize;
        float density = getResources().getDisplayMetrics().density;
        mTextSize = (int)(mTextSize * density);
        mTextPaint.setTextSize(mTextSize);
        return this;
    }

    public JumpTextView setTimeDuration(int mTimeDuration){
        this.mTimeSecond = mTimeDuration;
        return this;
    }

    public JumpTextView setTextString(String text){
        this.mText = text;
        return this;
    }

    public JumpTextView setTextColor(int color){
        this.mTextColor = color;
        mTextPaint.setColor(mTextColor);
        return this;
    }

    public JumpTextView setProgressWidth(int circleWidth){
        this.mPsPaintWidth = circleWidth;
        mProgressPaint.setStrokeWidth(mPsPaintWidth);
        return this;
    }

    public JumpTextView setProgressColor(int color){
        this.mProgressColor = color;
        mProgressPaint.setColor(mProgressColor);
        return this;
    }



    /**
     * 属性初始化完成 —————————————— **** 必须要调用,开始即显示动画效果
     */
    public void doJumpTextInitEnd(){
        mIsCanDrawing = true;
        ANIMATOR_DURATION = mTimeSecond * 1000;
        doAnimatorStart();
    }


    /**
     * 该API用于解决用于触发响应
     * @param isCanDraw
     */
    public JumpTextView setDrawingState(boolean isCanDraw){
        mIsCanDrawing = isCanDraw;
        if (mIsCanDrawing){
            doAnimatorStart();
        }
        return this;
    }

    /**
     *  -------------------------  使用结束  -------------------------
     * */


    /**
     * -------------------------- 构造方法开始--------------------------
     * */

    public JumpTextView(Context context) {
        this(context,null);
    }

    public JumpTextView(Context context, @Nullable AttributeSet attrs) {
       this(context,attrs,0);
    }

    public JumpTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 禁止硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE,null);
        initAttrs();

    }

    /**
     * -------------------------- 构造方法结束--------------------------
     * */


    private void initAttrs() {
        float density = getResources().getDisplayMetrics().density;
        mDfWidthHeight = (int)(mDfWidthHeight * density);
        mTextSize = (int)(mTextSize * density);
        ANIMATOR_DURATION = mTimeSecond * 1000;
        mCenterPoint = new Point();

        // 初始化画笔
        mProgressPaint = new Paint();
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setStrokeWidth(mPsPaintWidth);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setDither(true);

        mBgPaint = new Paint();
        mBgPaint.setColor(mInnerCircleColor);
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setAntiAlias(true);
        mBgPaint.setDither(true);

        mTextPaint = new Paint();
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        initPath();
        Log.e("splash","-------init-------");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.e("splash","-------onFinishInflate -------");

    }

    /**
     *  设置动画
     */
    private void initAnimator() {
        mValueAnimator = ObjectAnimator.ofFloat(0, 1f);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatedValue = (float) animation.getAnimatedValue();
                // 重新绘制界面
                invalidate();
            }
        });

        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mMyViewClickListener != null){
                    doAnimatorStop();
                    mMyViewClickListener.onJumpEnd();
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isProgressRunning = true;
            }
        });
        mValueAnimator.setDuration(ANIMATOR_DURATION);
    }


    /**
     * 关闭路径动画
     */
    private void doAnimatorStop(){
        if (mValueAnimator != null) {
            isProgressRunning = false;
            mValueAnimator.removeAllListeners();
            mValueAnimator.removeAllUpdateListeners();
            mValueAnimator.cancel();
            mValueAnimator = null;
        }
    }

    private void initPath() {
        mAllPath = new Path();
        mMDistPath = new Path();
        mPathMeasure = new PathMeasure();
    }

    /**
     * 开启路径动画
     */
    private void  doAnimatorStart(){
        if (mValueAnimator == null){
            initAnimator();
        }
        // 动画开始前重置路线
        mAllPath.reset();
        mValueAnimator.start();
    }

    /**
     * 长宽取最小即可
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("splash", "onMeasure: ");
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int resultWidth = widthSize;
        int resultHeight = heightSize;
        if (MeasureSpec.AT_MOST == widthMode){
            resultWidth = mDfWidthHeight;
        }

        if (MeasureSpec.AT_MOST == heightMode){
            resultHeight = mDfWidthHeight;
        }

        int minSize = Math.min(resultWidth,resultHeight);
        setMeasuredDimension(minSize,minSize);
    }

    /**
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     *
     * 所有和长宽有关的属性至少是位于onSizeChanged之后的
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterPoint.x = w/2;
        mCenterPoint.y = h/2;
        mRadius = w/2;

        if (mIsFirstMeasureOK && mIsCanDrawing){
            mAllPath.addCircle(mCenterPoint.x,mCenterPoint.y,mRadius-mPsPaintWidth/2, Path.Direction.CW);
            mPathMeasure.setPath(mAllPath,false);
            mIsFirstMeasureOK = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 判断是否需要进行绘制  如果需要绘制那么立马就绘制出来
        if (mIsCanDrawing) {
            // 绘制内圆背景
            drawBackGround(canvas);
            // 绘制文本
            drawText(canvas);
            // 绘制进度条
            drawProgress(canvas);
        }
    }


    private void drawBackGround(Canvas canvas) {
        canvas.drawCircle(mCenterPoint.x,mCenterPoint.y,mRadius-mPsPaintWidth,mBgPaint);
    }

    private void drawText(Canvas canvas) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float baseLine = mCenterPoint.y-(fontMetrics.bottom + fontMetrics.top)/2;
        canvas.drawText(mText,mCenterPoint.x,baseLine,mTextPaint);
    }

    private void drawProgress(Canvas canvas) {
        canvas.save();
        // 由于需要从Y轴负半轴开始加载进度，所以旋转-90度
        canvas.rotate(-90,mCenterPoint.x,mCenterPoint.y);
        mMDistPath.reset();
        mPathMeasure.getSegment(0,mPathMeasure.getLength()*mAnimatedValue,mMDistPath,true);
        canvas.drawPath(mMDistPath,mProgressPaint);
        canvas.restore();
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
            if (mMyViewClickListener != null){
                mMyViewClickListener.onJumpOnclick();
                doAnimatorStop();
            }
        }
        return true;
    }
}
