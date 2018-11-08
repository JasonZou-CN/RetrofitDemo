package com.jasonzou.retrofitdemo.ui.gifloading;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jasonzou.retrofitdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GifLoadingActivity extends Activity {

    @BindView(R.id.loading2)
    ImageView loading2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_loading);
        ButterKnife.bind(this);
        Glide.with(this).asGif().load(R.drawable.big_loading).into(loading2);
    }
}
