package com.jasonzou.retrofitdemo.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.jasonzou.retrofitdemo.R;

/**
 * 使用Canvas绘制一个无底边的三角形
 */
public class TriAngle extends View {

    private int bottomEdgeColor;//底边颜色
    private int mBorderColor;//边线颜色
    private int mFillColor;//三角形填充色
    private Paint paint;

    public TriAngle(Context context) {
        super(context);
    }

    public TriAngle(Context context, AttributeSet set) {
        super(context, set);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray mTypedArray = context.obtainStyledAttributes(set, R.styleable.TriAngle);
        mBorderColor = mTypedArray.getColor(R.styleable.TriAngle_borderColor, Color.WHITE);
        bottomEdgeColor = mTypedArray.getColor(R.styleable.TriAngle_bottomEdgeColor, Color.TRANSPARENT);
        mFillColor = mTypedArray.getColor(R.styleable.TriAngle_fillColor, Color.GRAY);
        mTypedArray.recycle();

    }

    public void onDraw(Canvas canvas) {


        Path path = new Path();
        path.moveTo(getWidth(), 0);
        path.lineTo(getWidth() / 2, getHeight());
        path.lineTo(0, 0);
        paint.setColor(mFillColor);
        canvas.drawPath(path, paint);

        paint.setColor(mBorderColor);
        paint.setStrokeWidth(dip2px(1));
        canvas.drawLine(0, 0, getWidth() / 2, getHeight(), paint);
        canvas.drawLine(getWidth() / 2, getHeight(), getWidth(), 0, paint);
        paint.setColor(bottomEdgeColor);
        canvas.drawLine(0, 0, getWidth(), 0, paint);


    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * DP值转像素值
     *
     * @param dpValue
     * @return
     */
    public int dip2px(int dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
