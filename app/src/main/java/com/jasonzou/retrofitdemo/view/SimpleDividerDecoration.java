package com.jasonzou.retrofitdemo.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecycleView的Divider设置
 */
public class SimpleDividerDecoration extends RecyclerView.ItemDecoration {

    public static final int BOTH = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    private final int padding;
    private int paddingSide = BOTH;
    private int dividerHeight;
    private Paint dividerPaint;

    public SimpleDividerDecoration(int color, int height, int padding) {
        dividerPaint = new Paint();
        dividerPaint.setColor(color);
        dividerHeight = height;
        this.padding = padding;
    }

    public SimpleDividerDecoration justOneSidePadding(@IntRange(from = BOTH, to = RIGHT) int side) {
        paddingSide = side;
        return this;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = dividerHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();

        int left = parent.getPaddingLeft() + (paddingSide == BOTH || paddingSide == LEFT ? padding : 0);
        int right = parent.getWidth() - parent.getPaddingRight() - (paddingSide == BOTH || paddingSide == RIGHT ? padding : 0);

        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + dividerHeight;
            c.drawRect(left, top, right, bottom, dividerPaint);
        }
    }
}