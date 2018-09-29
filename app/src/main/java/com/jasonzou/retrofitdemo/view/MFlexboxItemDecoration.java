package com.jasonzou.retrofitdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.flexbox.FlexboxItemDecoration;

/**
 * 项目:  LawyerOA <br>
 * 类名:  com.lcoce.lawyeroa.view.MFlexboxItemDecoration<br>
 * 描述:  略<br>
 * 创建人: jasonzou<br>
 * 创建时间: 2018/8/6 11:05<br>
 */
public class MFlexboxItemDecoration extends FlexboxItemDecoration {
    private int distance = 0;
    private Context context;

    public MFlexboxItemDecoration(Context context, int distance) {
        super(context);
        this.context = context;
        this.distance = distance;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.right = dip2px(distance);
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        /*默认会绘制一根线*/
        //super.onDraw(canvas, parent, state);
    }


    public int dip2px(int dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
