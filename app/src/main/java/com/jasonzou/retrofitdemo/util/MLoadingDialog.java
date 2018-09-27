package com.jasonzou.retrofitdemo.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jasonzou.retrofitdemo.R;

/**
 * Created with Android Studio
 * User:邹旭
 * Date:2017/7/28
 * Time:15:26
 * Desc:略
 */

public class MLoadingDialog {
    public static int dip2px(Context context, int pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue * scale + 0.5f);
    }

    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @param msg
     * @return
     */
    public static Dialog createLoadingDialog(Context context, String msg) {

        // 首先得到整个View
        View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_page, null);

        GradientDrawable bg = new GradientDrawable();
        float radius = dip2px(context, 10);
        bg.setShape(GradientDrawable.RECTANGLE);
        bg.setCornerRadii(new float[]{radius, radius, radius, radius, radius, radius, radius, radius});
        bg.setColor(Color.parseColor("#fcfbfb"));
        view.setBackground(bg);

        // 获取整个布局
        //        LinearLayout layout = (LinearLayout) view.findViewById(R.id.loadingPage);
        // 页面中的Img
        ImageView img = (ImageView) view.findViewById(R.id.loadingImg);
        // 页面中显示文本
        TextView tipText = (TextView) view.findViewById(R.id.loadingTxt);

        // 加载动画，动画用户使img图片不停的旋转
        img.setImageResource(R.drawable.loading_animation);
        AnimationDrawable animationDrawable = (AnimationDrawable) img.getDrawable();
        animationDrawable.start();
        // 显示文本
        tipText.setText(msg);

        // 创建自定义样式的Dialog
        Dialog loadingDialog = new Dialog(context, R.style.Loading_Dialog_Style);
        // 设置返回键无效
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        return loadingDialog;
    }

    /**
     * 显示Dialog
     */
    public static Dialog showAndCreateDialog(Context ctx) {
        Dialog mDialog = MLoadingDialog.createLoadingDialog(ctx, "正在加载中...");
        mDialog.show();
        return mDialog;

    }

    public static Dialog showAndCreateDialogs(Context ctx, String desc) {
        Dialog mDialog = MLoadingDialog.createLoadingDialog(ctx, desc);
        mDialog.show();
        return mDialog;
    }

    /**
     * 关闭Dialog
     */
    public static void closeDialog(Dialog dialog) {
        if (dialog != null) {
            dialog.dismiss();
            //            dialog.hide();
            dialog = null;
        }
    }
}
