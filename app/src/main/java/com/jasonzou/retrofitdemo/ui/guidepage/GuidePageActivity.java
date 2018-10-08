package com.jasonzou.retrofitdemo.ui.guidepage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jasonzou.retrofitdemo.R;
import com.jasonzou.retrofitdemo.ui.main.MainActivity;
import com.jasonzou.retrofitdemo.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 */
public class GuidePageActivity extends AppCompatActivity {

    private List<ImageView> views = new ArrayList<>();
    private Button launch;
    private Context mContext;
    private ViewPager vpContent;
    private LinearLayout pageIndicator;
    private TextView desc;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_page);
        mContext = getApplicationContext();

        initView();
    }

    private void initView() {
        ImageView pageA = new ImageView(this);
        Glide.with(this).load(R.drawable.guide_one).into(pageA);
        pageA.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ImageView pageB = new ImageView(this);
        Glide.with(this).load(R.drawable.guide_two).into(pageB);
        pageB.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ImageView pageC = new ImageView(this);
        Glide.with(this).load(R.drawable.guide_three).into(pageC);
        pageC.setScaleType(ImageView.ScaleType.CENTER_CROP);

        views.add(pageA);
        views.add(pageB);
        views.add(pageC);

        launch = findViewById(R.id.launcher);
        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.RECTANGLE);
        bg.setColor(Color.WHITE);
        bg.setStroke(Utils.dip2px(mContext, 1), Color.parseColor("#2d9de8"));
        bg.setCornerRadius(Utils.dip2px(mContext, 15));
        launch.setBackground(bg);
        launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuidePageActivity.this, MainActivity.class));
                finish();
            }
        });

        desc = findViewById(R.id.desc);

        pageIndicator = findViewById(R.id.pageIndicator);

        vpContent = findViewById(R.id.vpContent);
        vpContent.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(views.get(position));
                return views.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public int getCount() {
                return views.size();
            }
        });

        GradientDrawable bgGray = new GradientDrawable();//页标背景图 灰色
        bgGray.setShape(GradientDrawable.RECTANGLE);
        bgGray.setColor(Color.parseColor("#e6e6e6"));
        bgGray.setCornerRadius(Utils.dip2px(mContext, 5));
        final GradientDrawable finalBgGray = bgGray;

        GradientDrawable bgYellow = new GradientDrawable();//页标背景图 其他
        bgYellow.setShape(GradientDrawable.RECTANGLE);
        bgYellow.setColor(Color.parseColor("#ffd4a5"));
        bgYellow.setCornerRadius(Utils.dip2px(mContext, 2));
        final GradientDrawable finalBgYellow = bgYellow;

        GradientDrawable bgGreen = new GradientDrawable();//页标背景图 其他
        bgGreen.setShape(GradientDrawable.RECTANGLE);
        bgGreen.setColor(Color.parseColor("#21e4b2"));
        bgGreen.setCornerRadius(Utils.dip2px(mContext, 2));
        final GradientDrawable finalBgGreen = bgGreen;


        GradientDrawable bgBlue = new GradientDrawable();//页标背景图 其他
        bgBlue.setShape(GradientDrawable.RECTANGLE);
        bgBlue.setColor(Color.parseColor("#1b94e6"));
        bgBlue.setCornerRadius(Utils.dip2px(mContext, 2));
        final GradientDrawable finalBgBlue = bgBlue;

        LinearLayout.LayoutParams lpGray = new LinearLayout.LayoutParams(Utils.dip2px(mContext, 20), Utils.dip2px(mContext, 5));
        LinearLayout.LayoutParams lpOther = new LinearLayout.LayoutParams(Utils.dip2px(mContext, 30), Utils.dip2px(mContext, 5));
        lpGray.leftMargin = Utils.dip2px(mContext, 5);
        lpOther.leftMargin = Utils.dip2px(mContext, 5);
        final LinearLayout.LayoutParams finalLpGray = lpGray;
        final LinearLayout.LayoutParams finalLpOther = lpOther;
        pageIndicator.removeAllViews();
        for (int i = 0, len = views.size(); i < len; i++) {
            View child = new View(mContext);
            child.setLayoutParams(finalLpGray);
            pageIndicator.addView(child);
        }

        vpContent.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        desc.setText("随时随地咨询专业律师");

                        launch.setVisibility(View.GONE);
                        pageIndicator.setVisibility(View.VISIBLE);

                        pageIndicator.getChildAt(0).setLayoutParams(finalLpOther);
                        pageIndicator.getChildAt(1).setLayoutParams(finalLpGray);
                        pageIndicator.getChildAt(2).setLayoutParams(finalLpGray);

                        pageIndicator.getChildAt(0).setBackground(finalBgYellow);
                        pageIndicator.getChildAt(1).setBackground(finalBgGray);
                        pageIndicator.getChildAt(2).setBackground(finalBgGray);
                        break;
                    case 1:
                        desc.setText("案件进度一目了然");

                        launch.setVisibility(View.GONE);
                        pageIndicator.setVisibility(View.VISIBLE);

                        pageIndicator.getChildAt(0).setLayoutParams(finalLpGray);
                        pageIndicator.getChildAt(1).setLayoutParams(finalLpOther);
                        pageIndicator.getChildAt(2).setLayoutParams(finalLpGray);

                        pageIndicator.getChildAt(0).setBackground(finalBgGray);
                        pageIndicator.getChildAt(1).setBackground(finalBgGreen);
                        pageIndicator.getChildAt(2).setBackground(finalBgGray);
                        break;
                    case 2:
                        desc.setText("你的私人法律顾问");

                        launch.setVisibility(View.VISIBLE);
                        pageIndicator.setVisibility(View.GONE);

                        pageIndicator.getChildAt(0).setLayoutParams(finalLpGray);
                        pageIndicator.getChildAt(1).setLayoutParams(finalLpGray);
                        pageIndicator.getChildAt(2).setLayoutParams(finalLpOther);

                        pageIndicator.getChildAt(0).setBackground(finalBgGray);
                        pageIndicator.getChildAt(1).setBackground(finalBgGray);
                        pageIndicator.getChildAt(2).setBackground(finalBgBlue);
                        break;
                }

            }
        });
        vpContent.setCurrentItem(0);
        pageIndicator.setVisibility(View.VISIBLE);
        pageIndicator.getChildAt(0).setLayoutParams(finalLpOther);
        pageIndicator.getChildAt(1).setLayoutParams(finalLpGray);
        pageIndicator.getChildAt(2).setLayoutParams(finalLpGray);

        pageIndicator.getChildAt(0).setBackground(finalBgYellow);
        pageIndicator.getChildAt(1).setBackground(finalBgGray);
        pageIndicator.getChildAt(2).setBackground(finalBgGray);
    }
}
