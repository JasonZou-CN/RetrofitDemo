package com.jasonzou.retrofitdemo.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.jasonzou.retrofitdemo.util.MPermissions;
import com.jasonzou.retrofitdemo.util.StatusBarUtil;
import com.jasonzou.retrofitdemo.util.SystemUtil;

import java.lang.reflect.Field;


/**
 * Activity基类，实现共性的处理
 * Created by jasonzou on 2017/9/22.
 */

public class BaseActivity extends AppCompatActivity implements OnFragmentUIChangeListener {
    /*允许子类获取到该实例，拿到重要权限的请求状态*/ MPermissions initPermissions;
    private int statusBarHeight = 0;
    private int BASE_PERMISSION_REQ_CODE = 0x1000;//其他情况下的权限请求码应当低于这个
    private OnActivityUIChangeListener mListener;
    private View contView;
    private View nodataPage;
    private View loadingPage;
    private BroadcastReceiver broadcastReceiver;

    /**
     * px -> dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public void setOnActivityUIChangeListener(OnActivityUIChangeListener mListener) {
        this.mListener = mListener;
    }

    /**
     * 请求竖屏
     */
    private void requestPortraitScreen() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
    }

    private void requestSoftinputMode(int mode) {
        //        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        getWindow().setSoftInputMode(mode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPortraitScreen();
        requestSoftinputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        /*状态栏*/
        initPermissions = MPermissions.newBuilder(this, new String[]{Manifest.permission.READ_PHONE_STATE}).iCalllback(new MPermissions.ICalllback() {
            @Override
            public void onAllGranted() {
                if (SystemUtil.getDeviceBrand().toLowerCase().contains("oppo")) {

                } else {
                    //透明状态栏
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//Android KITKAT
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);//Android L

                    /*MIUI,FLYME,6.0系统状态栏处理*/
                    StatusBarUtil.StatusBarLightMode(BaseActivity.this);//MIUI,FLYME,6.0系统状态栏处理

                    /*状态栏高度*/
                    statusBarHeight = getStatusBarHeight(BaseActivity.this);
                }
            }

            @Override
            public void onSomeDenied(String[] avoids) {

            }
        }).requestCode(BASE_PERMISSION_REQ_CODE).build().request();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.broadcastReceiver != null) {
            unregisterReceiver(this.broadcastReceiver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (initPermissions != null) {
            initPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    /**
     * 状态栏高度，支持沉浸式返回实际高度，不支持返回0
     *
     * @return
     */
    public int getStatusBarHeight() {
        return statusBarHeight;
    }

    /**
     * 设置状态栏高度
     *
     * @param view
     */
    public View bindStatusHeightToView(View view) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.height = statusBarHeight;
        view.setLayoutParams(lp);
        return view;
    }

    /**
     * 设置状态栏高度,
     *
     * @param viewId Activity的View ID
     */
    public View bindStatusHeightToView(int viewId) {
        View view = findViewById(viewId);
        if (view == null)
            return null;
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.height = statusBarHeight;
        view.setLayoutParams(lp);
        return view;
    }

    /**
     * 通过反射的方式获取状态栏高度
     *
     * @return
     */
    int getStatusBarHeight(Context ctx) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return ctx.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取根布局
     *
     * @return
     */
    public ViewGroup getContentView() {
        ViewGroup root = ((ViewGroup) findViewById(android.R.id.content));
        return (ViewGroup) root.getChildAt(0);
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

    public int getSuitableHeight(int widthRatio, int heightRatio) {
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        double ratio = widthPixels * 1.0 / widthRatio;
        return (int) (heightRatio * ratio);
    }

    /**
     * 【业务逻辑相关 - 耦合代码】
     * 响应Fragment的状态，改变Activity的UI,由Fragment调用
     *
     * @param data
     */
    @Override
    public void onFragmentUIChange(Object data) {

    }

    /**
     * 更新Activity界面
     *
     * @param data
     */
    public void updateFragment(Object data) {
        if (mListener != null) {
            mListener.onActivityUIChange(data);
        }
    }

    /**
     * 设置三个交互页面
     *
     * @param contView
     * @param nodataPage
     * @param loadingPage
     */
    public void setPagesView(View contView, View nodataPage, View loadingPage) {
        this.contView = contView;
        this.nodataPage = nodataPage;
        this.loadingPage = loadingPage;
    }

    /**
     * 显示内容页
     */
    public void showContView() {
        if (contView == null || nodataPage == null || loadingPage == null) {
            return;
        }
        contView.setVisibility(View.VISIBLE);
        nodataPage.setVisibility(View.GONE);
        loadingPage.setVisibility(View.GONE);
    }

    /**
     * 显示无数据页
     */
    public void showNodataPage() {
        if (contView == null || nodataPage == null || loadingPage == null) {
            return;
        }
        contView.setVisibility(View.GONE);
        nodataPage.setVisibility(View.VISIBLE);
        loadingPage.setVisibility(View.GONE);
    }

    /**
     * 显示加载页
     */
    public void showLoadingPage() {
        if (contView == null || nodataPage == null || loadingPage == null) {
            return;
        }
        contView.setVisibility(View.GONE);
        nodataPage.setVisibility(View.GONE);
        loadingPage.setVisibility(View.VISIBLE);
    }


    /**
     * 注册数据变化的广播接收器
     *
     * @param dataChangedBroadcastReceiver
     * @param actions
     */
    public void registerBrocastReceiver(BroadcastReceiver dataChangedBroadcastReceiver, @NonNull String... actions) {
        if (actions == null || actions.length == 0) {
            Log.e("BaseFragment", "广播接收者的过滤器不能为空");
            //  throw new IllegalArgumentException("\"广播接收者的过滤器不能为空\"");
            return;
        }

        // 1. 实例化BroadcastReceiver子类 &  IntentFilter
        this.broadcastReceiver = dataChangedBroadcastReceiver;
        IntentFilter intentFilter = new IntentFilter();

        // 2. 设置接收广播的类型
        for (String action : actions) {
            intentFilter.addAction(action);
        }
        // 3. 动态注册：调用Context的registerReceiver（）方法
        registerReceiver(this.broadcastReceiver, intentFilter);
    }

    /**
     * 发送数据改变的广播
     *
     * @param action
     */
    public void sendDataChangedBroadcast(String action, Bundle dataBundle) {
        Intent intent = new Intent();
        if (dataBundle != null) {
            intent.putExtras(dataBundle);
        }
        //对应BroadcastReceiver中intentFilter的action
        intent.setAction(action);
        //发送广播
        sendBroadcast(intent);
    }
}

