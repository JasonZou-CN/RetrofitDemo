package com.jasonzou.retrofitdemo.util;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.orhanobut.logger.Logger;

/**
 * @项目 LawyerOA
 * @类名 com.lcoce.lawyeroa.utils
 * @描述 功能：<br/>
 * 1.Popwindow外部区域点击，popwindow消失<br/>
 * 2.监听弹窗消失动作<br/>
 * @创建人 jasonzou
 * @创建时间 2018/1/25 15:49
 */
public class MPopwindow extends PopupWindow {
    private final Activity activity;
    private final View contenView;

    private final int mWidth;
    private final int mHeight;
    private final int mAnim;
    private final boolean isTranslucent;
    private final IOnDissmiss iOnDissmiss;
    private final boolean intercetpOutsideTouch;//是否拦截PopWindow外部的触摸事件，默认情况下，点击窗体外部会隐藏PopWindow
    private final float mAlpha;//透明度-遮罩层

    private MPopwindow(Builder builder) {
        activity = builder.activity;
        contenView = builder.contenView;

        mWidth = builder.mWidth;
        mHeight = builder.mHeight;
        mAnim = builder.mAnim;
        iOnDissmiss = builder.iOnDissmiss;
        isTranslucent = builder.isTranslucent;
        intercetpOutsideTouch = builder.intercetpOutsideTouch;
        mAlpha = builder.mAlpha;
        build();
    }

    public static Builder newBuilder(@NonNull Activity activity, @NonNull View contenView) {
        return new Builder(activity, contenView);
    }

    public static Builder newBuilder(@NonNull Activity activity, @LayoutRes int idRes) {
        return new Builder(activity, idRes);
    }

    /**
     * 默认配置项
     */
    private void build() {
        if (contenView == null) {
            Logger.e("contentView must not null");
        }
        setContentView(contenView);
        setAnimationStyle(mAnim);//设置popwindow的动画样式
        setWidth(mWidth);
        setHeight(mHeight);


        setFocusable(true);//获取焦点
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
        //设置启用窗体外部事件监听
        setOutsideTouchable(true);
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (intercetpOutsideTouch && isShowing()) {
                    //不做任何响应,不 dismiss popupWindow
                    float vW = v.getWidth();
                    float vH = v.getHeight();
                    float yOff = event.getY();
                    float xOff = event.getX();
                    if (yOff < 0 || xOff < 0 || xOff > vW || yOff > vH)
                        return true;
                }
                //否则default，往下dispatch事件:关掉popupWindow
                return false;
            }
        });
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (isTranslucent)
                    setMaskLayer(1f);
                if (iOnDissmiss != null)
                    iOnDissmiss.onIntercept();
            }
        });
    }


    /**
     * 显示PopWindow,判断显示遮罩层,并返回实例
     *
     * @param parent
     * @param gravity
     * @param x
     * @param y
     */
    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (isTranslucent)//是否显示遮罩层
            setMaskLayer(mAlpha);
        super.showAtLocation(parent, gravity, x, y);
    }

    /**
     * 显示PopWindow,判断显示遮罩层,并返回实例
     *
     * @param anchor
     * @param gravity
     * @param x
     * @param y
     */
    @Override
    public void showAsDropDown(View anchor, int x, int y, int gravity) {
        if (isTranslucent)//是否显示遮罩层
            setMaskLayer(mAlpha);
        super.showAsDropDown(anchor, x, y, gravity);
    }

    /**
     * 显示PopWindow,判断显示遮罩层,并返回实例
     *
     * @param anchor
     * @param x
     * @param y
     */
    @Override
    public void showAsDropDown(View anchor, int x, int y) {
        if (isTranslucent)//是否显示遮罩层
            setMaskLayer(mAlpha);
        super.showAsDropDown(anchor, x, y);
    }

    /**
     * 显示PopWindow,判断显示遮罩层,并返回实例,叠加-100dp X偏移量
     *
     * @param anchor
     * @param x
     * @param y
     * @param gravity 相对anchor的位置 bottom|right:右下角
     */
    public void showAsDropDownWith100DPXDX(View anchor, int x, int y, int gravity) {
        if (isTranslucent)//是否显示遮罩层
            setMaskLayer(mAlpha);
        super.showAsDropDown(anchor, x - dip2px(108), y, gravity);
    }


    /**
     * 透明度-遮罩层
     *
     * @param alpha
     */
    private void setMaskLayer(float alpha) {
        if (activity != null) {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.alpha = alpha;
            activity.getWindow().setAttributes(lp);
        }
    }

    /**
     * 设置水平居中的Window，左右间距
     *
     * @param px
     * @return
     */
    public MPopwindow setMarginHorizontal(int px) {
        if (activity != null) {
            int mWidth = activity.getWindowManager().getDefaultDisplay().getWidth() - px * 2;
            setWidth(mWidth);
        }
        return this;
    }

    /**
     * DP值转像素值
     *
     * @param dpValue
     * @return
     */
    public int dip2px(int dpValue) {
        final float scale = activity.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public interface IOnDissmiss {
        boolean onIntercept();
    }

    public static final class Builder {
        private final Activity activity;
        private final View contenView;

        private IOnDissmiss iOnDissmiss = null;
        private int mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
        private int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        private int mAnim = 0;
        private boolean isTranslucent = true;
        private float mAlpha = .7f;
        private boolean intercetpOutsideTouch = false;

        private Builder(@NonNull Activity activity, @NonNull View contenView) {
            this.activity = activity;
            this.contenView = contenView;
        }

        private Builder(@NonNull Activity activity, @LayoutRes int idRes) {
            this.activity = activity;
            this.contenView = activity.getLayoutInflater().inflate(idRes, null);
        }

        public Builder mWidth(int mWidth) {
            this.mWidth = mWidth;
            return this;
        }

        public Builder mHeight(int mHeight) {
            this.mHeight = mHeight;
            return this;
        }

        /**窗体动画样式Style
         * @param mAnim
         * @return
         */
        public Builder mAnim(@StyleRes int mAnim) {
            this.mAnim = mAnim;
            return this;
        }

        public Builder iOnDissmiss(@NonNull IOnDissmiss iOnDissmiss) {
            this.iOnDissmiss = iOnDissmiss;
            return this;
        }

        public Builder intercetpOutsideTouch(boolean intercetpOutsideTouch) {
            this.intercetpOutsideTouch = intercetpOutsideTouch;
            return this;
        }

        public MPopwindow build() {
            return new MPopwindow(this);
        }
    }
}
