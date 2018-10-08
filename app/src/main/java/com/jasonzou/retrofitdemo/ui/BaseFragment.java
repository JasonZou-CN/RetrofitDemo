package com.jasonzou.retrofitdemo.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BaseFragment extends Fragment implements OnActivityUIChangeListener {

    private View contView, loadingPage, nodataPage;

    private OnFragmentUIChangeListener mListener;
    private BroadcastReceiver broadcastReceiver;

    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText("");
        return textView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentUIChangeListener) {
            mListener = (OnFragmentUIChangeListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentUIChangeListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * 更新Activity界面
     *
     * @param data
     */
    public void updateActivity(Object data) {
        if (mListener != null) {
            mListener.onFragmentUIChange(data);
        }
    }

    public void bindStatusHeightToView(BaseActivity baseActivity, View view) {
        baseActivity.bindStatusHeightToView(view);
    }

    /**
     * todo
     * 不要在此处调用View，会得到NullPoint异常
     * 响应Activity的状态，改变Fragment
     * 【业务逻辑相关 - 耦合代码】
     *
     * @param data
     */
    @Override
    public void onActivityUIChange(Object data) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.broadcastReceiver!=null) {
            getContext().unregisterReceiver(this.broadcastReceiver);
        }
    }

    /**
     * 显示内容页
     */
    public boolean showContView() {
        if (contView == null || nodataPage == null || loadingPage == null) {
            Log.e("BaseFragment", "当前页面,存在null的contView,nodataPage或者loadingPage页面");
            return false;
        } else {
            contView.setVisibility(View.VISIBLE);
            nodataPage.setVisibility(View.GONE);
            loadingPage.setVisibility(View.GONE);
            return true;
        }
    }

    /**
     * 显示无数据页
     */
    public boolean showNodataPage() {
        if (contView == null || nodataPage == null || loadingPage == null) {
            Log.e("BaseFragment", "当前页面,存在null的contView,nodataPage或者loadingPage页面");
            return false;
        } else {
            contView.setVisibility(View.GONE);
            nodataPage.setVisibility(View.VISIBLE);
            loadingPage.setVisibility(View.GONE);
            return true;
        }
    }

    /**
     * 显示加载页
     */
    public boolean showLoadingPage() {
        if (contView == null || nodataPage == null || loadingPage == null) {
            Log.e("BaseFragment", "当前页面,存在null的contView,nodataPage或者loadingPage页面");
            return false;
        } else {
            contView.setVisibility(View.GONE);
            nodataPage.setVisibility(View.GONE);
            loadingPage.setVisibility(View.VISIBLE);
            return true;
        }
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
        // 2. 设置接收广播的类型
        IntentFilter intentFilter = new IntentFilter();
        for (String action : actions) {
            intentFilter.addAction(action);
        }
        // 3. 动态注册：调用Context的registerReceiver（）方法
        getContext().registerReceiver(this.broadcastReceiver, intentFilter);
    }

    /**
     * 发送数据改变的广播
     *
     * @param action
     * @param dataBundle
     */
    public void sendDataChangedBroadcast(String action, Bundle dataBundle) {
        Intent intent = new Intent();
        if (dataBundle != null) {
            intent.putExtras(dataBundle);
        }
        //对应BroadcastReceiver中intentFilter的action
        intent.setAction(action);
        //发送广播
        getContext().sendBroadcast(intent);
    }

}
