package com.jasonzou.retrofitdemo.ui.refreshandloadmore;

import android.os.Bundle;

import com.jasonzou.retrofitdemo.R;
import com.jasonzou.retrofitdemo.ui.BaseActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

public class RefreshLoadmoreActivity extends BaseActivity {

    @BindView(R.id.gifView)
    GifImageView gifView;
    @BindView(R.id.refLayout)
    SmartRefreshLayout refLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_loadmore);
        ButterKnife.bind(this);
    }
}
