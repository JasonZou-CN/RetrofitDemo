package com.jasonzou.retrofitdemo.ui;

/**
 * Fragment实现，在Activity中的改变，内部的Fragment需要响应并更新UI
 */
public interface OnActivityUIChangeListener {
   void onActivityUIChange(Object data);
}