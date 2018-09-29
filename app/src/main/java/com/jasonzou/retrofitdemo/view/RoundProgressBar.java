package com.jasonzou.retrofitdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.jasonzou.retrofitdemo.R;


/**
 * DESC:
 *      -圆形的进度条;
 *      -带操作按钮的环形进度条(Todo)
 * 作者:邹旭.
 */
public class RoundProgressBar extends View {

    /**
     * 环形
     */
    private static final int STROKE = 0;

    /**
     * 环形带字
     */
    private static final int STROKE_TEXT = 1;

    /**
     * 填充
     */
    private static final int STROKE_FILL = 2;

    /**
     * 带进度的按钮 - IOS
     */
    private static final int STROKE_IMG = 3;

    private Paint mPaint;
    private int mBackColor;
    private int mFrontColor;
    private int mTextColor;
    private float mTextSize;
    private float mBorderWidth;
    private int mMode;

    private float mHalfBorder;
    private int max;
    private int value;
    private int startPos = -90;
    private Paint.Style style;
    private boolean isFill;
    private boolean isClockwise;
    private int textHalfSize;

    private Drawable mCurrentImg;

    public RoundProgressBar(Context context) {
        super(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);

        mBackColor = mTypedArray.getColor(R.styleable.RoundProgressBar_backColor, Color.WHITE);
        mFrontColor = mTypedArray.getColor(R.styleable.RoundProgressBar_frontColor, Color.GRAY);
        mTextColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.GRAY);
        mTextSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, sp2px(context, 15));
        mCurrentImg = mTypedArray.getDrawable(R.styleable.RoundProgressBar_mCurrentImg);
        value = mTypedArray.getInt(R.styleable.RoundProgressBar_value, 50);
        max = mTypedArray.getInt(R.styleable.RoundProgressBar_max, 100);
        isClockwise = mTypedArray.getBoolean(R.styleable.RoundProgressBar_isClockwise, true);
        textHalfSize = (int) (mTextSize * 0.4f);
        mBorderWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_barBorderWidth, dp2px(context, 5));
        mHalfBorder = mBorderWidth * 0.5f;
        mMode = mTypedArray.getInteger(R.styleable.RoundProgressBar_mode, STROKE_TEXT);
        isFill = mMode == STROKE_FILL;
        style = isFill ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE;
        mTypedArray.recycle();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getValue() {
        return value;
    }

    public synchronized void setValue(int newValue) {
        newValue = Math.max(0, newValue);
        newValue = Math.min(max, newValue);

        this.value = newValue;
        postInvalidate();
    }

    public int getmFrontColor() {
        return mFrontColor;
    }

    public void setmFrontColor(int mFrontColor) {
        this.mFrontColor = mFrontColor;
        postInvalidate();
    }

    public void setCenterImg(Drawable mImg) {
        if (mImg == null)
            return;
        else
            mCurrentImg = mImg;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //刻度背景
        int center = (int) (getWidth() * 0.5f);
        int radius = (int) (center - mHalfBorder); //圆环的半径
        mPaint.setColor(mBackColor); //设置圆环的颜色
        mPaint.setStyle(style); //设置空心
        mPaint.setStrokeWidth(mBorderWidth); //设置圆环的宽度
        mPaint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(center, center, radius, mPaint); //画出圆环

        int percent = (int) (value * 100f / max);
        //文本
        if (mMode == STROKE_TEXT) {
            mPaint.setStrokeWidth(0);
            mPaint.setColor(mTextColor);
            mPaint.setTextSize(mTextSize);
            //            mPaint.setTypeface(Typeface.DEFAULT_BOLD); //设置粗体字体
            float textHalfWidth = mPaint.measureText(percent + "%") * 0.5f;
            canvas.drawText(percent + "%", center - textHalfWidth, center + textHalfSize, mPaint);
        } else if (mCurrentImg != null && mMode == STROKE) {//带进度的按钮
            /*BitmapDrawable bmpDraw = (BitmapDrawable) mCurrentImg;
            Bitmap bmp = bmpDraw.getBitmap();
            int girlBitWidth = bmp.getWidth();
            int girlBitHeight = bmp.getHeight();
            Rect cenSrcRect = new Rect(girlBitWidth / 2 - dp2px(getContext(), 5), girlBitHeight / 2 + dp2px(getContext(), 5), girlBitWidth / 2 + dp2px(getContext(), 5), girlBitWidth / 2 - dp2px(getContext(), 5));
            Rect girlDesRect = new Rect(center - 10, center + 10, center + 10, center - 10);
            canvas.drawBitmap(bmp, cenSrcRect, girlDesRect, null);*/

        }

        //进度
        if (value > 0) {
            mPaint.setStrokeWidth(mBorderWidth);
            mPaint.setColor(mFrontColor);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);
            int angle = -(int) (360 * percent / 100f); //逆时针
            if (isClockwise) {
                angle *= -1;
            }
            mPaint.setStyle(style);
            canvas.drawArc(oval, startPos, angle, isFill, mPaint);
        }
    }

    /**
     * convert dp to its equivalent px
     * <p>
     * 将dp转换为与之相等的px
     */
    private int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
