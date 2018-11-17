package com.jasonzou.retrofitdemo;

import android.app.Application;

import com.jasonzou.retrofitdemo.eventbus.EventBusMaster;
import com.jasonzou.retrofitdemo.greendao.DaoSession;
import com.jasonzou.retrofitdemo.greendao.DbMaster;
import com.jasonzou.retrofitdemo.logger.LoggerMaster;
import com.jasonzou.retrofitdemo.network.APIMaster;


/**
 * 项目:  RetrofitDemo <br>
 * 类名:  com.jasonzou.retrofitdemo.BaseApplication<br>
 * 描述:  略<br>
 * 创建人: jasonzou<br>
 * 创建时间: 2018/9/14 17:29<br>
 */
public class BaseApplication extends Application {
    private DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        LoggerMaster.initLogger();
        DbMaster.init(this);
        APIMaster.getInstance().init(this);
        EventBusMaster.init();
    }
}
