package com.jasonzou.retrofitdemo.ui;

/**
 * Activity实现，在Fragment中的改变，外部的Activity需要响应并更新UI
 */
public interface OnFragmentUIChangeListener {
    void onFragmentUIChange(Object data);
}
