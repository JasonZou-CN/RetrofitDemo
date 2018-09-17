package com.jasonzou.retrofitdemo;

import android.app.Application;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jasonzou.retrofitdemo.network.APIMaster;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;


/**
 * 项目:  RetrofitDemo <br>
 * 类名:  com.jasonzou.retrofitdemo.BaseApplication<br>
 * 描述:  略<br>
 * 创建人: jasonzou<br>
 * 创建时间: 2018/9/14 17:29<br>
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        initLogger();

        APIMaster.getInstance().init(this);
    }

    private void initLogger() {

        /*AS升级到3.1后，会出现打印的日志格式异常问题  LOGGER issuess #173*/
        LogStrategy strategy = new LogStrategy() {
            @Override public void log(int priority, String tag, String message) {
                Log.println(priority, randomKey() + tag, message);
            }
            private int last;
            private String randomKey() {
                int random = (int) (10 * Math.random());
                if (random == last) {
                    random = (random + 1) % 10;
                }
                last = random;
                return String.valueOf(random);
            }
        };
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
//                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
//                .methodCount(2)         // (Optional) How many method line to show. Default 2
//                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
              .logStrategy(strategy) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("LOGGER")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return BuildConfig.DEBUG;
            }
        });

        //以下是保存自定义日志
        FormatStrategy formatStrateg = CsvFormatStrategy.newBuilder()
                .tag("LOGGER")
                .build();
        Logger.addLogAdapter(new DiskLogAdapter(formatStrateg));
    }
}
