package com.jasonzou.retrofitdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.jasonzou.retrofitdemo.R;

/**
 * 带渐变色横向的进度条
 * author:邹旭
 */
public class HorizonalProgressBar extends View {

    private Paint mFrontPaint;

    public void setmIsGradientBar(boolean mIsGradientBar) {
        this.mIsGradientBar = mIsGradientBar;
    }

    public void setmStartColor(int mStartColor) {
        this.mStartColor = mStartColor;
    }

    public void setmEndColor(int mEndColor) {
        this.mEndColor = mEndColor;
    }

    public void setmBackColor(int mBackColor) {
        this.mBackColor = mBackColor;
    }

    private boolean mIsGradientBar;
    private int mStartColor;
    private int mEndColor;
    private Paint mBackPaint;
    private int mBackColor;
    private int mFrontColor;

    private int max;
    private int progress;


    public HorizonalProgressBar(Context context) {
        super(context, null);
    }

    public HorizonalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBackPaint = new Paint();

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.HorizonalProgressBar);

        mBackColor = mTypedArray.getColor(R.styleable.HorizonalProgressBar_backColor, Color.WHITE);
        mStartColor = mTypedArray.getColor(R.styleable.HorizonalProgressBar_startColor, Color.parseColor("#4C6AF6"));
        mEndColor = mTypedArray.getColor(R.styleable.HorizonalProgressBar_endColor, Color.parseColor("#3464ad"));
        mIsGradientBar = mTypedArray.getBoolean(R.styleable.HorizonalProgressBar_isGradientBar, true);
        mFrontColor = mTypedArray.getColor(R.styleable.HorizonalProgressBar_frontColor, Color.RED);
        progress = mTypedArray.getInt(R.styleable.HorizonalProgressBar_progress, 50);
        max = mTypedArray.getInt(R.styleable.HorizonalProgressBar_max, 100);
        mTypedArray.recycle();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getProgress() {
        return progress;
    }

    public synchronized void setProgress(int newValue) {
        // progress >=0 && progress <= max
        newValue = Math.max(0, newValue);
        newValue = Math.min(max, newValue);

        this.progress = newValue;
        postInvalidate();
    }

    public int getmFrontColor() {
        return mFrontColor;
    }

    public void setmFrontColor(int mFrontColor) {
        this.mFrontColor = mFrontColor;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //刻度背景
        mBackPaint.setColor(mBackColor); //设置圆环的颜色
        mBackPaint.setStyle(Paint.Style.FILL);
        mBackPaint.setAntiAlias(true);  //消除锯齿
        RectF rect = new RectF(0, 0, getWidth(), getHeight());
        canvas.drawRoundRect(rect, getHeight() / 2, getHeight() / 2, mBackPaint);

        float percent = progress * 1f / max;

        //进度
        if (progress > 0) {
            mFrontPaint = new Paint();
            mFrontPaint.setStyle(Paint.Style.FILL);
            mFrontPaint.setAntiAlias(true);  //消除锯齿
            if (mIsGradientBar) {
                LinearGradient linearGradient = new LinearGradient(0, 0, getWidth() * percent, 02, new int[]{mStartColor, mEndColor}, null, Shader.TileMode.CLAMP);
                mFrontPaint.setShader(linearGradient);
            } else {
                mFrontPaint.setColor(mFrontColor);
            }
            RectF rectP = new RectF(0, 0, getWidth() * percent, getHeight());
            canvas.drawRoundRect(rectP, getHeight() / 2, getHeight() / 2, mFrontPaint);
        }
    }
}
