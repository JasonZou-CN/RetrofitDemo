package com.jasonzou.retrofitdemo.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by MacBook on 2017/6/16.
 */

public class NoScrollViewPager extends ViewPager {
    private boolean scrollAble = false;

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollable(boolean scrollable) {
        scrollAble = scrollable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return scrollAble && super.onInterceptTouchEvent(arg0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return scrollAble && super.onTouchEvent(arg0);
    }
}
